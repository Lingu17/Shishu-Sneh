package com.example.shishusneh.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.shishusneh.databinding.ActivityFeedingGuideBinding

class FeedingGuideActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFeedingGuideBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFeedingGuideBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
