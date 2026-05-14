package com.example.shishusneh.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shishusneh.R
import com.example.shishusneh.data.entities.ActivityRecord
import com.example.shishusneh.databinding.FragmentHomeBinding
import com.example.shishusneh.databinding.ItemTimelineBinding
import com.example.shishusneh.viewmodel.MainViewModel
import com.example.shishusneh.ui.utils.VoiceFeedbackManager
import com.example.shishusneh.ui.utils.ReportManager
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by viewModels({ requireActivity() })
    
    private lateinit var voiceManager: VoiceFeedbackManager
    private lateinit var reportManager: ReportManager

    inner class TimelineAdapter(private var items: List<ActivityRecord>) : RecyclerView.Adapter<TimelineAdapter.ViewHolder>() {
        inner class ViewHolder(val binding: ItemTimelineBinding) : RecyclerView.ViewHolder(binding.root)
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = 
            ViewHolder(ItemTimelineBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = items[position]
            holder.binding.tvTitle.text = item.title
            holder.binding.tvSubtitle.text = item.subtitle
            holder.binding.tvTime.text = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date(item.timestamp)).uppercase()
            holder.binding.ivIcon.setImageResource(item.iconResId)
            
            val colorRes = when(item.type) {
                "Sleep" -> R.color.accent_sleep
                "Feeding" -> R.color.accent_feeding
                "Growth" -> R.color.accent_growth
                "Diaper" -> R.color.accent_diaper
                else -> R.color.primaryColor
            }
            holder.binding.ivIcon.imageTintList = ContextCompat.getColorStateList(requireContext(), colorRes)
            
            holder.binding.ivEdit.setOnClickListener { showEditActivityDialog(item) }
            holder.binding.ivDelete.setOnClickListener { deleteActivityWithUndo(item) }
        }
        override fun getItemCount() = items.size
        fun getItemAt(pos: Int) = items[pos]
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        voiceManager = VoiceFeedbackManager(requireContext())
        reportManager = ReportManager(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvTimeline.layoutManager = LinearLayoutManager(context)
        setupSwipeToDelete()
        setupClickListeners()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.babyProfile.collect { profile ->
                        profile?.let {
                            binding.tvBabyName.text = it.name
                            binding.tvBabyAge.text = calculateAge(it.dateOfBirthMillis)
                        }
                    }
                }
                launch {
                    viewModel.growthRecords.collect { records ->
                        val latest = records.lastOrNull()
                        binding.tvBabyWeight.text = if (latest != null) "${latest.weightKg} kg" else "No data"
                        binding.tvBabyHeight.text = if (latest != null) "${latest.heightCm} cm" else "No data"
                        binding.tvGrowthInsight.text = if (latest != null) "+${latest.weightKg}kg" else "0g"
                    }
                }
                launch {
                    viewModel.todayActivities.collect { activities ->
                        updateTimeline(activities)
                        calculateInsights(activities)
                    }
                }
            }
        }
    }

    private fun updateTimeline(activities: List<ActivityRecord>) {
        if (activities.isEmpty()) {
            binding.rvTimeline.visibility = View.GONE
            binding.layoutEmptyTimeline.visibility = View.VISIBLE
        } else {
            binding.rvTimeline.visibility = View.VISIBLE
            binding.layoutEmptyTimeline.visibility = View.GONE
            binding.rvTimeline.adapter = TimelineAdapter(activities)
        }
    }

    private fun calculateInsights(activities: List<ActivityRecord>) {
        val totalSleepMinutes = activities.filter { it.type == "Sleep" }.sumOf { it.durationMinutes }
        val feedCount = activities.count { it.type == "Feeding" }
        
        val h = totalSleepMinutes / 60
        val m = totalSleepMinutes % 60
        
        binding.tvSleepInsight.text = when {
            h > 0 && m > 0 -> "${h}h ${m}m"
            h > 0 -> "${h}h"
            m > 0 -> "${m}m"
            else -> "0h"
        }
        
        binding.tvFeedInsight.text = feedCount.toString()
    }

    private fun setupSwipeToDelete() {
        val swipeHandler = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(r: RecyclerView, vh: RecyclerView.ViewHolder, t: RecyclerView.ViewHolder) = false
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val adapter = binding.rvTimeline.adapter as? TimelineAdapter
                adapter?.let { deleteActivityWithUndo(it.getItemAt(position)) }
            }
        }
        ItemTouchHelper(swipeHandler).attachToRecyclerView(binding.rvTimeline)
    }

    private fun deleteActivityWithUndo(activity: ActivityRecord) {
        viewModel.deleteActivity(activity)
        voiceManager.speak("Entry deleted")
        Snackbar.make(binding.root, "Deleted successfully", Snackbar.LENGTH_LONG)
            .setAction("Undo") {
                viewModel.addActivity(activity.type, activity.title, activity.subtitle, activity.iconResId, activity.colorResId, activity.notes, activity.durationMinutes)
                voiceManager.speak("Action undone")
            }.show()
    }

    private fun showEditActivityDialog(activity: ActivityRecord) {
        when(activity.type) {
            "Feeding" -> showFeedingDialog(activity)
            "Sleep" -> showSleepDialog(activity)
            "Diaper" -> showDiaperDialog(activity)
            else -> android.widget.Toast.makeText(context, "Cannot edit this type", android.widget.Toast.LENGTH_SHORT).show()
        }
    }

    private fun showEditProfileDialog() {
        val currentProfile = viewModel.babyProfile.value ?: return
        val dialogBinding = com.example.shishusneh.databinding.DialogEditProfileBinding.inflate(layoutInflater)
        val dialog = com.google.android.material.dialog.MaterialAlertDialogBuilder(requireContext()).setView(dialogBinding.root).create()
        dialogBinding.etEditName.setText(currentProfile.name)
        val sdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        dialogBinding.etEditDob.setText(sdf.format(Date(currentProfile.dateOfBirthMillis)))
        var selectedDob = currentProfile.dateOfBirthMillis
        dialogBinding.etEditDob.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker().setSelection(selectedDob).build()
            datePicker.addOnPositiveButtonClickListener { selection ->
                selectedDob = selection
                dialogBinding.etEditDob.setText(sdf.format(Date(selection)))
            }
            datePicker.show(parentFragmentManager, "DATE_PICKER")
        }
        val genders = arrayOf("Boy", "Girl", "Other")
        dialogBinding.etEditGender.setAdapter(ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, genders))
        dialogBinding.etEditGender.setText(currentProfile.gender, false)
        dialogBinding.btnUpdateProfile.setOnClickListener {
            val name = dialogBinding.etEditName.text.toString()
            if (name.isNotEmpty()) {
                viewModel.updateBabyProfile(currentProfile.copy(name = name, dateOfBirthMillis = selectedDob, gender = dialogBinding.etEditGender.text.toString()))
                voiceManager.speak("Profile updated")
                dialog.dismiss()
            }
        }
        dialog.show()
    }

    private fun showFeedingDialog(existing: ActivityRecord? = null) {
        val dialogBinding = com.example.shishusneh.databinding.DialogAddFeedingBinding.inflate(layoutInflater)
        val dialog = com.google.android.material.dialog.MaterialAlertDialogBuilder(requireContext()).setView(dialogBinding.root).create()
        if (existing != null) {
            val parts = existing.subtitle.split(" ")
            if (parts.size >= 2) {
                if (parts[0] == "Bottle") dialogBinding.btnBottle.isChecked = true else dialogBinding.btnBreast.isChecked = true
                dialogBinding.etDetails.setText(parts.drop(1).joinToString(" "))
            }
            dialogBinding.etNotes.setText(existing.notes)
            dialogBinding.btnSave.text = "Update Feed"
        }
        dialogBinding.btnSave.setOnClickListener {
            val type = if (dialogBinding.btnBreast.isChecked) "Breast" else "Bottle"
            val details = dialogBinding.etDetails.text.toString()
            if (details.isNotEmpty()) {
                if (existing != null) viewModel.updateActivity(existing.copy(subtitle = "$type $details", notes = dialogBinding.etNotes.text.toString()))
                else viewModel.addActivity("Feeding", "Feeding", "$type $details", R.drawable.ic_feeding, R.color.accent_feeding, dialogBinding.etNotes.text.toString())
                voiceManager.speak(if (existing != null) "Feeding updated" else "Feeding added successfully")
                dialog.dismiss()
                Snackbar.make(binding.root, "Saved successfully", Snackbar.LENGTH_SHORT).show()
            } else dialogBinding.etDetails.error = "Required"
        }
        dialog.show()
    }

    private fun showDiaperDialog(existing: ActivityRecord? = null) {
        val dialogBinding = com.example.shishusneh.databinding.DialogAddDiaperBinding.inflate(layoutInflater)
        val dialog = com.google.android.material.dialog.MaterialAlertDialogBuilder(requireContext()).setView(dialogBinding.root).create()
        if (existing != null) {
            when(existing.subtitle) {
                "Wet" -> dialogBinding.btnWet.isChecked = true
                "Dirty" -> dialogBinding.btnDirty.isChecked = true
                else -> dialogBinding.btnBoth.isChecked = true
            }
            dialogBinding.etNotes.setText(existing.notes)
            dialogBinding.btnSave.text = "Update Log"
        }
        dialogBinding.btnSave.setOnClickListener {
            val type = if (dialogBinding.btnWet.isChecked) "Wet" else if (dialogBinding.btnDirty.isChecked) "Dirty" else "Both"
            if (existing != null) viewModel.updateActivity(existing.copy(subtitle = type, notes = dialogBinding.etNotes.text.toString()))
            else viewModel.addActivity("Diaper", "Diaper", type, R.drawable.ic_profile, R.color.accent_diaper, dialogBinding.etNotes.text.toString())
            voiceManager.speak(if (existing != null) "Diaper updated" else "Diaper logged successfully")
            dialog.dismiss()
            Snackbar.make(binding.root, "Saved successfully", Snackbar.LENGTH_SHORT).show()
        }
        dialog.show()
    }

    private fun showSleepDialog(existing: ActivityRecord? = null) {
        val dialogBinding = com.example.shishusneh.databinding.DialogAddSleepBinding.inflate(layoutInflater)
        val dialog = com.google.android.material.dialog.MaterialAlertDialogBuilder(requireContext()).setView(dialogBinding.root).create()
        if (existing != null) {
            val h = existing.durationMinutes / 60
            val m = existing.durationMinutes % 60
            dialogBinding.etHours.setText(h.toString())
            dialogBinding.etMinutes.setText(m.toString())
            dialogBinding.etNotes.setText(existing.notes)
            dialogBinding.btnSave.text = "Update Sleep"
        }
        dialogBinding.btnSave.setOnClickListener {
            val h = dialogBinding.etHours.text.toString().toIntOrNull() ?: 0
            val m = dialogBinding.etMinutes.text.toString().toIntOrNull() ?: 0
            val totalMinutes = (h * 60) + m
            
            val display = when {
                h > 0 && m > 0 -> "${h}h ${m}m"
                h > 0 -> "${h}h"
                m > 0 -> "${m}m"
                else -> "0h"
            }
            
            if (totalMinutes > 0) {
                if (existing != null) viewModel.updateActivity(existing.copy(subtitle = display, durationMinutes = totalMinutes, notes = dialogBinding.etNotes.text.toString()))
                else viewModel.addActivity("Sleep", "Sleeping", display, R.drawable.ic_sleep, R.color.accent_sleep, dialogBinding.etNotes.text.toString(), totalMinutes)
                voiceManager.speak(if (existing != null) "Sleep updated" else "Sleep added successfully")
                dialog.dismiss()
                Snackbar.make(binding.root, "Saved successfully", Snackbar.LENGTH_SHORT).show()
            } else {
                dialogBinding.etMinutes.error = "Enter duration"
            }
        }
        dialog.show()
    }

    private fun calculateAge(dobMillis: Long): String {
        val diff = System.currentTimeMillis() - dobMillis
        val days = diff / (1000 * 60 * 60 * 24)
        return if (days < 30) "$days d" else "${days / 30} m"
    }

    private fun setupClickListeners() {
        val mainActivity = requireActivity() as com.example.shishusneh.ui.MainActivity
        binding.cardSleep.setOnClickListener { showSleepDialog() }
        binding.cardFeeding.setOnClickListener { showFeedingDialog() }
        binding.cardGrowth.setOnClickListener { mainActivity.navigateToTab(R.id.navigation_growth) }
        binding.cardDiaper.setOnClickListener { showDiaperDialog() }
        
        binding.ivLogout.setOnClickListener {
            requireContext().getSharedPreferences("shishu_sneh", android.content.Context.MODE_PRIVATE).edit().putBoolean("is_logged_in", false).apply()
            startActivity(Intent(requireActivity(), com.example.shishusneh.ui.LoginActivity::class.java).apply { flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK })
        }
        binding.ivEditProfile.setOnClickListener { showEditProfileDialog() }
        binding.ivDeleteProfile.setOnClickListener {
            com.google.android.material.dialog.MaterialAlertDialogBuilder(requireContext())
                .setTitle("Delete Profile")
                .setMessage("This will permanently delete ALL baby data and reset the app. Are you sure you want to continue?")
                .setPositiveButton("Delete Everything") { _, _ ->
                    lifecycleScope.launch {
                        viewModel.deleteBabyProfile()
                        voiceManager.speak("All data has been wiped.")
                    }
                }
                .setNegativeButton("Keep Data", null)
                .show()
        }
        binding.ivDownloadProfile.setOnClickListener { showReportDatePicker() }
        binding.ivSettings.setOnClickListener {
            com.example.shishusneh.ui.fragments.SettingsBottomSheet().show(parentFragmentManager, "SETTINGS_SHEET")
        }
        binding.fabAdd.setOnClickListener {
            com.example.shishusneh.ui.fragments.LogActivityBottomSheet { type ->
                when(type) {
                    "Feeding" -> showFeedingDialog()
                    "Sleep" -> showSleepDialog()
                    "Growth" -> mainActivity.navigateToTab(R.id.navigation_growth)
                    "Diaper" -> showDiaperDialog()
                }
            }.show(parentFragmentManager, "LOG_SHEET")
        }
        binding.btnEmptyLog.setOnClickListener {
            binding.fabAdd.performClick()
        }
    }

    private fun showReportDatePicker() {
        val babyProfile = viewModel.babyProfile.value ?: return
        val datePicker = MaterialDatePicker.Builder.datePicker().setTitleText("Select Report Date").build()
        datePicker.addOnPositiveButtonClickListener { selection ->
            if (selection < babyProfile.dateOfBirthMillis) {
                Snackbar.make(binding.root, "Please enter a valid date (after baby's birth)", Snackbar.LENGTH_LONG).show()
            } else {
                reportManager.generatePdfReport(babyProfile, viewModel.todayActivities.value, Date(selection))
            }
        }
        datePicker.show(parentFragmentManager, "REPORT_DATE")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        voiceManager.shutdown()
        _binding = null
    }
}
