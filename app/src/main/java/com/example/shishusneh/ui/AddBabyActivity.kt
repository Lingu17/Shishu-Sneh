package com.example.shishusneh.ui

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.shishusneh.R
import kotlinx.coroutines.launch
import com.example.shishusneh.data.entities.BabyProfile
import com.example.shishusneh.databinding.ActivityAddBabyBinding
import com.example.shishusneh.viewmodel.MainViewModel
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AddBabyActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddBabyBinding
    private val viewModel: MainViewModel by viewModels()
    private var selectedDob: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBabyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        resetUIState()
        setupGenderDropdown()
        setupDatePicker()

        binding.btnSave.setOnClickListener {
            saveProfile()
        }
    }

    private fun resetUIState() {
        // Ensure fields are fresh and fully visible (fixing the "dim" issue)
        binding.etName.isEnabled = true
        binding.etName.alpha = 1.0f
        binding.etName.setText("")

        binding.etDob.isEnabled = true
        binding.etDob.alpha = 1.0f
        binding.etDob.setText("")

        binding.etGender.isEnabled = true
        binding.etGender.alpha = 1.0f
        binding.etGender.setText("", false)
        
        selectedDob = 0L
    }

    private fun setupGenderDropdown() {
        val genders = arrayOf("Boy", "Girl", "Other")
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, genders)
        binding.etGender.setAdapter(adapter)
        
        binding.etGender.setOnClickListener {
            binding.etGender.showDropDown()
        }
    }

    private fun setupDatePicker() {
        binding.etDob.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select Baby\'s Birthday")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()

            datePicker.addOnPositiveButtonClickListener { selection ->
                selectedDob = selection
                val sdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
                binding.etDob.setText(sdf.format(Date(selection)))
            }
            datePicker.show(supportFragmentManager, "DATE_PICKER")
        }
    }

    private fun saveProfile() {
        val name = binding.etName.text.toString().trim()
        val gender = binding.etGender.text.toString().trim()

        if (name.isEmpty()) {
            binding.etName.error = "Name is required"
            return
        }
        if (selectedDob == 0L) {
            Toast.makeText(this, "Please select Date of Birth", Toast.LENGTH_SHORT).show()
            return
        }
        if (gender.isEmpty()) {
            Toast.makeText(this, "Please select Gender", Toast.LENGTH_SHORT).show()
            return
        }

        val profile = BabyProfile(
            name = name,
            dateOfBirthMillis = selectedDob,
            gender = gender
        )
        
        // Use lifecycleScope to wait for the insertion before navigating
        lifecycleScope.launch {
            viewModel.insertBabyProfile(profile).join()
            
            startActivity(Intent(this@AddBabyActivity, MainActivity::class.java))
            @Suppress("DEPRECATION")
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            finish()
        }
    }
}