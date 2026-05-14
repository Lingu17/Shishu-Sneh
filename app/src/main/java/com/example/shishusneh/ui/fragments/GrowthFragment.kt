package com.example.shishusneh.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shishusneh.R
import com.example.shishusneh.data.entities.GrowthRecord
import com.example.shishusneh.databinding.FragmentGrowthBinding
import com.example.shishusneh.ui.adapters.GrowthHistoryAdapter
import com.example.shishusneh.viewmodel.MainViewModel
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import kotlinx.coroutines.launch

class GrowthFragment : Fragment() {

    private var _binding: FragmentGrowthBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by viewModels({ requireActivity() })
    private var isWeightSelected = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGrowthBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupChart()
        
        val adapter = GrowthHistoryAdapter { record ->
            viewModel.deleteGrowthRecord(record)
        }
        binding.rvGrowthHistory.adapter = adapter
        binding.rvGrowthHistory.layoutManager = LinearLayoutManager(context)

        binding.fabAddGrowth.setOnClickListener { showAddGrowthDialog() }
        binding.btnEmptyAdd.setOnClickListener { showAddGrowthDialog() }

        binding.toggleGrowthType.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                isWeightSelected = checkedId == R.id.btnWeight
                viewLifecycleOwner.lifecycleScope.launch {
                    viewModel.growthRecords.collect { records ->
                        updateChart(records)
                    }
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.growthRecords.collect { records ->
                    adapter.submitList(records)
                    updateChart(records)
                    
                    // Premium Toggle Empty State UI
                    if (records.isEmpty()) {
                        binding.layoutEmptyChart.visibility = View.VISIBLE
                        binding.lineChart.visibility = View.GONE
                        binding.tvEmptyHistory.visibility = View.VISIBLE
                    } else {
                        binding.layoutEmptyChart.visibility = View.GONE
                        binding.lineChart.visibility = View.VISIBLE
                        binding.tvEmptyHistory.visibility = View.GONE
                    }
                }
            }
        }
    }

    private fun showAddGrowthDialog() {
        val builder = com.google.android.material.dialog.MaterialAlertDialogBuilder(requireContext())
        val dialogBinding = com.example.shishusneh.databinding.DialogAddGrowthBinding.inflate(layoutInflater)
        builder.setView(dialogBinding.root)

        val dialog = builder.create()
        dialogBinding.btnSave.setOnClickListener {
            val weight = dialogBinding.etWeight.text.toString().toFloatOrNull()
            val height = dialogBinding.etHeight.text.toString().toFloatOrNull()
            if (weight != null && height != null) {
                viewModel.addGrowthRecord(weight, height)
                dialog.dismiss()
            } else {
                android.widget.Toast.makeText(context, "Please enter valid numbers", android.widget.Toast.LENGTH_SHORT).show()
            }
        }
        dialog.show()
    }

    private fun setupChart() {
        binding.lineChart.apply {
            description.isEnabled = false
            setTouchEnabled(true)
            isDragEnabled = true
            setScaleEnabled(true)
            setPinchZoom(true)
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.setDrawGridLines(false)
            axisRight.isEnabled = false
            axisLeft.setDrawGridLines(true)
            axisLeft.gridColor = ContextCompat.getColor(requireContext(), R.color.dividerColor)
            animateX(1000)
        }
    }

    private fun updateChart(records: List<GrowthRecord>) {
        val entries = records.mapIndexed { index, record ->
            val value = if (isWeightSelected) record.weightKg else record.heightCm
            Entry(index.toFloat(), value)
        }
        
        binding.lineChart.axisLeft.removeAllLimitLines()
        
        if (isWeightSelected) {
            val whoStandard = LimitLine(3.5f, "WHO Avg (Newborn)").apply {
                lineWidth = 2f
                enableDashedLine(10f, 10f, 0f)
                lineColor = ContextCompat.getColor(requireContext(), R.color.accent_growth)
                textColor = ContextCompat.getColor(requireContext(), R.color.accent_growth)
                textSize = 10f
            }
            binding.lineChart.axisLeft.addLimitLine(whoStandard)
        }
        
        if (entries.isNotEmpty()) {
            val label = if (isWeightSelected) "Weight (kg)" else "Height (cm)"
            val colorRes = if (isWeightSelected) R.color.accent_growth else R.color.accent_feeding
            
            val dataSet = LineDataSet(entries, label).apply {
                color = ContextCompat.getColor(requireContext(), colorRes)
                valueTextColor = ContextCompat.getColor(requireContext(), R.color.primaryTextColor)
                lineWidth = 4f
                circleRadius = 6f
                setCircleColor(ContextCompat.getColor(requireContext(), colorRes))
                setDrawFilled(true)
                fillDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.gradient_chart_fill)
                mode = LineDataSet.Mode.CUBIC_BEZIER
            }
            binding.lineChart.data = LineData(dataSet)
            binding.lineChart.invalidate()
        } else {
            binding.lineChart.clear()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
