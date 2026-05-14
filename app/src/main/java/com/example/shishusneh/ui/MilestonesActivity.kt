package com.example.shishusneh.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.shishusneh.databinding.ActivityMilestonesBinding

class MilestonesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMilestonesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMilestonesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // TODO: Implement RecyclerView adapter for milestones list
    }
}
