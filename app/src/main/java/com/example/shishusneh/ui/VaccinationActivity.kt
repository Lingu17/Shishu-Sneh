package com.example.shishusneh.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.shishusneh.databinding.ActivityVaccinationBinding

class VaccinationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVaccinationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVaccinationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // TODO: Implement RecyclerView adapter for vaccinations list
    }
}
