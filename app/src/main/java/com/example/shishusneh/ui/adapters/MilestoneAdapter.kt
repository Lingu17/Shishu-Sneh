package com.example.shishusneh.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.shishusneh.R
import com.example.shishusneh.data.entities.Milestone
import com.example.shishusneh.databinding.ItemMilestoneBinding

class MilestoneAdapter(
    private val onCheckChanged: (Milestone) -> Unit,
    private val onMilestoneClick: (Milestone) -> Unit
) :
    ListAdapter<Milestone, MilestoneAdapter.MilestoneViewHolder>(MilestoneDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MilestoneViewHolder {
        val binding = ItemMilestoneBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MilestoneViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MilestoneViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class MilestoneViewHolder(private val binding: ItemMilestoneBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(milestone: Milestone) {
            binding.tvDescription.text = milestone.description
            binding.tvMonth.text = "Typical: ${milestone.monthNumber} months"
            
            // Visual Feedback for completion
            if (milestone.isAchieved) {
                binding.cardMilestone.setCardBackgroundColor(ContextCompat.getColor(binding.root.context, R.color.status_completed_bg))
                binding.tvDescription.setTextColor(ContextCompat.getColor(binding.root.context, R.color.status_completed))
            } else {
                binding.cardMilestone.setCardBackgroundColor(ContextCompat.getColor(binding.root.context, R.color.white))
                binding.tvDescription.setTextColor(ContextCompat.getColor(binding.root.context, R.color.primaryTextColor))
            }

            binding.cbAchieved.setOnCheckedChangeListener(null)
            binding.cbAchieved.isChecked = milestone.isAchieved
            binding.cbAchieved.setOnCheckedChangeListener { _, isChecked ->
                onCheckChanged(milestone.copy(isAchieved = isChecked))
            }
            
            binding.root.setOnClickListener {
                onMilestoneClick(milestone)
            }
        }
    }

    class MilestoneDiffCallback : DiffUtil.ItemCallback<Milestone>() {
        override fun areItemsTheSame(oldItem: Milestone, newItem: Milestone): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Milestone, newItem: Milestone): Boolean =
            oldItem == newItem
    }
}