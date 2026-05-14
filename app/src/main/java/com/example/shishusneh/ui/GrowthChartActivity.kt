package com.example.shishusneh.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.core.graphics.toColorInt
import com.example.shishusneh.databinding.ActivityGrowthChartBinding
import com.example.shishusneh.viewmodel.MainViewModel
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import kotlinx.coroutines.launch
import java.util.Date
import java.util.Locale

class GrowthChartActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGrowthChartBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGrowthChartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupChart()

        binding.btnSaveGrowth.setOnClickListener {
            val weight = binding.etWeight.text.toString().toFloatOrNull()
            val height = binding.etHeight.text.toString().toFloatOrNull()

            if ((weight != null) && (height != null)) {
                viewModel.addGrowthRecord(weight, height)
                binding.etWeight.text?.clear()
                binding.etHeight.text?.clear()
                Toast.makeText(this, "Record Saved", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Please enter valid numbers", Toast.LENGTH_SHORT).show()
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.growthRecords.collect { records ->
                    val entries = records.mapIndexed { index, record ->
                        Entry(index.toFloat(), record.weightKg)
                    }
                    
                    if (entries.isNotEmpty()) {
                        val dataSet = LineDataSet(entries, "Weight (kg)").apply {
                            color = "#F48FB1".toColorInt()
                            valueTextColor = android.graphics.Color.BLACK
                            lineWidth = 3f
                            circleRadius = 5f
                            setCircleColor("#BF5F82".toColorInt())
                        }
                        binding.lineChart.data = LineData(dataSet)
                        binding.lineChart.invalidate()
                    } else {
                        binding.lineChart.clear()
                    }
                }
            }
        }
    }

    private fun setupChart() {
        binding.lineChart.apply {
            description.isEnabled = false
            setTouchEnabled(true)
            isDragEnabled = true
            setScaleEnabled(true)
            setPinchZoom(true)
            
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            axisRight.isEnabled = false
        }
    }
}
