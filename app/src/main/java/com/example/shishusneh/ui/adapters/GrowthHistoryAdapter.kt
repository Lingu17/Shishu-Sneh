package com.example.shishusneh.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.shishusneh.data.entities.GrowthRecord
import com.example.shishusneh.databinding.ItemGrowthHistoryBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class GrowthHistoryAdapter(private val onDelete: (GrowthRecord) -> Unit) :
    ListAdapter<GrowthRecord, GrowthHistoryAdapter.GrowthViewHolder>(GrowthDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GrowthViewHolder {
        val binding = ItemGrowthHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GrowthViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GrowthViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class GrowthViewHolder(private val binding: ItemGrowthHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())

        fun bind(record: GrowthRecord) {
            binding.tvDate.text = dateFormat.format(Date(record.dateMillis))
            binding.tvHeight.text = "Height: ${record.heightCm} cm"
            binding.tvWeight.text = "${record.weightKg} kg"
            binding.ivDelete.setOnClickListener { onDelete(record) }
        }
    }

    class GrowthDiffCallback : DiffUtil.ItemCallback<GrowthRecord>() {
        override fun areItemsTheSame(oldItem: GrowthRecord, newItem: GrowthRecord): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: GrowthRecord, newItem: GrowthRecord): Boolean =
            oldItem == newItem
    }
}