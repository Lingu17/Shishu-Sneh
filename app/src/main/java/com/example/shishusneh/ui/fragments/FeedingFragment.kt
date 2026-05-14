package com.example.shishusneh.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shishusneh.R
import com.example.shishusneh.databinding.FragmentFeedingBinding
import com.example.shishusneh.databinding.ItemNutritionTipBinding
import com.example.shishusneh.ui.utils.VoiceFeedbackManager
import com.example.shishusneh.viewmodel.MainViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class FeedingFragment : Fragment() {

    private var _binding: FragmentFeedingBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by viewModels({ requireActivity() })
    private lateinit var voiceManager: VoiceFeedbackManager

    data class NutritionTip(val title: String, val description: String)

    inner class NutritionAdapter(private val tips: List<NutritionTip>) : RecyclerView.Adapter<NutritionAdapter.ViewHolder>() {
        inner class ViewHolder(val binding: ItemNutritionTipBinding) : RecyclerView.ViewHolder(binding.root)
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
            ItemNutritionTipBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val tip = tips[position]
            holder.binding.tvTipTitle.text = tip.title
            holder.binding.tvTipDescription.text = tip.description
            holder.binding.root.setOnClickListener {
                voiceManager.speak("${tip.title}: ${tip.description}")
            }
        }
        override fun getItemCount() = tips.size
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentFeedingBinding.inflate(inflater, container, false)
        voiceManager = VoiceFeedbackManager(requireContext())
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvNutritionTips.layoutManager = LinearLayoutManager(context)
        setupDataCollection()
        setupClickListeners()
    }

    private fun setupDataCollection() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.babyProfile.collect { profile ->
                        profile?.let {
                            val diff = System.currentTimeMillis() - it.dateOfBirthMillis
                            val days = diff / (1000 * 60 * 60 * 24)
                            val ageStr = if (days < 30) "$days-day-old" else "${days / 30}-month-old"
                            binding.tvBabyAgeContext.text = "Tips for your $ageStr newborn"
                            updateNutritionTips(it.dateOfBirthMillis)
                        }
                    }
                }
                launch {
                    viewModel.todayActivities.collect { activities ->
                        val feeds = activities.filter { it.type == "Feeding" }
                        
                        // Professional Placeholder handling
                        if (feeds.isEmpty()) {
                            binding.tvTotalFeeds.text = "---"
                            binding.tvLastFeedTime.text = "---"
                        } else {
                            binding.tvTotalFeeds.text = feeds.size.toString()
                            val lastFeed = feeds.maxByOrNull { it.timestamp }
                            binding.tvLastFeedTime.text = lastFeed?.let {
                                SimpleDateFormat("h:mm a", Locale.getDefault()).format(Date(it.timestamp))
                            } ?: "---"
                        }
                    }
                }
            }
        }
    }

    private fun updateNutritionTips(dobMillis: Long) {
        val diff = System.currentTimeMillis() - dobMillis
        val months = diff / (1000L * 60 * 60 * 24 * 30)
        val tips = if (months < 6) {
            listOf(
                NutritionTip("Exclusive Breastfeeding", "For the first 6 months, only breast milk is sufficient for your baby."),
                NutritionTip("Feeding Frequency", "Newborns typically feed every 2-3 hours. Watch for hunger cues like rooting."),
                NutritionTip("Vitamin D", "Breastfed babies often need Vitamin D supplements. Consult your pediatrician."),
                NutritionTip("Hydration", "Babies do not need extra water; breast milk contains over 80% water."),
                NutritionTip("Burping", "Always burp your baby after each feed to reduce gas and spit-up."),
                NutritionTip("Mother's Diet", "Consume a balanced diet rich in iron, calcium, and proteins for healthy milk."),
                NutritionTip("Skin-to-Skin", "Holding baby close helps stimulate milk production and bonding."),
                NutritionTip("Hunger Cues", "Rooting, sucking on hands, and smacking lips are early signs of hunger."),
                NutritionTip("Comfort Nursing", "Nursing provides emotional security and stress relief for your newborn."),
                NutritionTip("No Solids Yet", "Wait until 6 months to introduce any solid foods or juices."),
                NutritionTip("Responsive Feeding", "Feed based on baby's cues rather than a strict clock schedule."),
                NutritionTip("Positioning", "Ensure a deep latch to prevent nipple soreness and ensure efficient feeding."),
                NutritionTip("Night Feedings", "Night feeds are vital for newborn growth and maintaining milk supply."),
                NutritionTip("Avoid Pacifiers Early", "Try to wait until breastfeeding is well-established (around 4 weeks)."),
                NutritionTip("Relaxation", "Try to stay relaxed during feeds; stress can sometimes slow milk flow."),
                NutritionTip("Cluster Feeding", "It's normal for babies to feed very frequently at certain times of day."),
                NutritionTip("Pumping Tips", "If pumping, store milk in clean, BPA-free containers or storage bags.")
            )
        } else {
            listOf(
                NutritionTip("Introduction to Solids", "Start with single-ingredient purees to easily identify any allergies."),
                NutritionTip("Iron-Rich Foods", "Babies need iron after 6 months. Try mashed lentils or iron-fortified cereals."),
                NutritionTip("Consistency", "Start with thin purees and gradually move to thicker, textured foods."),
                NutritionTip("Continued Breastfeeding", "Breast milk remains an important source of nutrition alongside solids."),
                NutritionTip("Finger Foods", "Offer soft-cooked vegetable sticks to develop self-feeding skills."),
                NutritionTip("Hydration", "Introduce small sips of water from a cup during meal times."),
                NutritionTip("Avoid Honey", "Do not give honey to babies under 1 year due to botulism risk."),
                NutritionTip("No Added Salt/Sugar", "Avoid seasonings to protect baby's developing kidneys and taste buds."),
                NutritionTip("Fruit Variety", "Mashed banana, avocado, and pear are excellent first fruits."),
                NutritionTip("Vegetable Mix", "Introduce carrots, sweet potatoes, and peas for essential vitamins."),
                NutritionTip("Egg Introduction", "Thoroughly cooked eggs can be introduced after 6 months."),
                NutritionTip("Yogurt and Cheese", "Full-fat dairy can be introduced if there are no known allergies."),
                NutritionTip("Meal Routine", "Establish 2-3 small meal times to help baby adapt to a schedule."),
                NutritionTip("Self-Feeding", "Let baby explore textures with their hands to improve motor skills."),
                NutritionTip("Safe Textures", "Ensure all food is soft enough to be mashed by baby's gums."),
                NutritionTip("Allergy Awareness", "Watch for rashes or tummy upset when trying new foods."),
                NutritionTip("Be Patient", "It may take several attempts for a baby to accept a new flavor.")
            )
        }
        binding.rvNutritionTips.adapter = NutritionAdapter(tips)
    }

    private fun setupClickListeners() {
        binding.btnQuickLog.setOnClickListener { showFeedingDialog() }
        binding.btnSetReminder.setOnClickListener {
            SetAlertBottomSheet { interval ->
                viewModel.scheduleFeedingReminder(interval)
                val msg = if (interval > 0) "Feeding reminder set every $interval hours" else "Alerts disabled"
                Snackbar.make(binding.root, msg, Snackbar.LENGTH_SHORT).show()
                voiceManager.speak(if (interval > 0) "Reminder set successfully" else "Alerts disabled")
            }.show(parentFragmentManager, "ALERT_SHEET")
        }
    }

    private fun showFeedingDialog() {
        val dialogBinding = com.example.shishusneh.databinding.DialogAddFeedingBinding.inflate(layoutInflater)
        val dialog = com.google.android.material.dialog.MaterialAlertDialogBuilder(requireContext()).setView(dialogBinding.root).create()
        dialogBinding.btnSave.setOnClickListener {
            val type = if (dialogBinding.btnBreast.isChecked) "Breast" else "Bottle"
            val details = dialogBinding.etDetails.text.toString()
            if (details.isNotEmpty()) {
                viewModel.addActivity("Feeding", "Feeding", "$type $details", R.drawable.ic_feeding, R.color.accent_feeding, dialogBinding.etNotes.text.toString())
                voiceManager.speak("Feeding logged successfully")
                dialog.dismiss()
                Snackbar.make(binding.root, "Feeding logged!", Snackbar.LENGTH_SHORT).show()
            } else {
                dialogBinding.etDetails.error = "Required"
            }
        }
        dialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        voiceManager.shutdown()
        _binding = null
    }
}
