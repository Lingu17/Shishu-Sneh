package com.example.shishusneh.ui.adapters

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.shishusneh.R
import com.example.shishusneh.data.entities.Vaccination
import com.example.shishusneh.databinding.ItemVaccinationBinding
import com.example.shishusneh.ui.utils.VoiceFeedbackManager
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class VaccinationAdapter(private val onCheckChanged: (Vaccination) -> Unit) :
    ListAdapter<Vaccination, VaccinationAdapter.VaccinationViewHolder>(VaccinationDiffCallback()) {
    
    private var voiceManager: VoiceFeedbackManager? = null

    override fun onAttachedToRecyclerView(recyclerView: androidx.recyclerview.widget.RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        voiceManager = VoiceFeedbackManager(recyclerView.context)
    }

    override fun onDetachedFromRecyclerView(recyclerView: androidx.recyclerview.widget.RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        voiceManager?.shutdown()
        voiceManager = null
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VaccinationViewHolder {
        val binding = ItemVaccinationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VaccinationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VaccinationViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class VaccinationViewHolder(private val binding: ItemVaccinationBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())

        fun bind(vaccination: Vaccination) {
            binding.tvVaccineName.text = vaccination.name
            binding.tvDisease.text = vaccination.diseasePrevented
            binding.tvDueDate.text = "Due: ${dateFormat.format(Date(vaccination.dueDateMillis))}"
            
            val now = System.currentTimeMillis()
            val isOverdue = !vaccination.isCompleted && vaccination.dueDateMillis < now
            
            when {
                vaccination.isCompleted -> {
                    binding.chipStatus.text = "Completed"
                    binding.chipStatus.setTextColor(ContextCompat.getColor(binding.root.context, R.color.status_completed))
                    binding.viewStatusIndicator.setBackgroundColor(ContextCompat.getColor(binding.root.context, R.color.status_completed))
                    binding.cardVaccine.setCardBackgroundColor(ContextCompat.getColor(binding.root.context, R.color.status_completed_bg))
                }
                isOverdue -> {
                    binding.chipStatus.text = "URGENT: OVERDUE"
                    binding.chipStatus.setTextColor(ContextCompat.getColor(binding.root.context, R.color.status_overdue))
                    binding.viewStatusIndicator.setBackgroundColor(ContextCompat.getColor(binding.root.context, R.color.status_overdue))
                    binding.cardVaccine.setCardBackgroundColor(ContextCompat.getColor(binding.root.context, R.color.status_overdue_bg))
                }
                else -> {
                    binding.chipStatus.text = "Upcoming"
                    binding.chipStatus.setTextColor(ContextCompat.getColor(binding.root.context, R.color.status_upcoming))
                    binding.viewStatusIndicator.setBackgroundColor(ContextCompat.getColor(binding.root.context, R.color.status_upcoming))
                    binding.cardVaccine.setCardBackgroundColor(ContextCompat.getColor(binding.root.context, R.color.white))
                }
            }

            binding.cbCompleted.setOnCheckedChangeListener(null)
            binding.cbCompleted.isChecked = vaccination.isCompleted
            binding.cbCompleted.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    voiceManager?.speak("${vaccination.name} vaccine marked as completed successfully")
                }
                onCheckChanged(vaccination.copy(isCompleted = isChecked))
            }
        }
    }

    class VaccinationDiffCallback : DiffUtil.ItemCallback<Vaccination>() {
        override fun areItemsTheSame(oldItem: Vaccination, newItem: Vaccination): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Vaccination, newItem: Vaccination): Boolean =
            oldItem == newItem
    }
}