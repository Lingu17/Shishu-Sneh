package com.example.shishusneh.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.shishusneh.R
import com.example.shishusneh.databinding.ActivityMainBinding
import com.example.shishusneh.ui.fragments.*
import com.example.shishusneh.viewmodel.MainViewModel
import com.example.shishusneh.workers.BootReceiver
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkProfile()

        // Schedule WorkManager Reminders
        BootReceiver.scheduleVaccinationReminders(this)

        setupBottomNavigation()
        
        // Default fragment
        if (savedInstanceState == null) {
            loadFragment(HomeFragment())
        }
    }

    private fun checkProfile() {
        lifecycleScope.launch {
            try {
                // Using a one-shot check from the database
                kotlinx.coroutines.delay(500)
                val profile = viewModel.getBabyProfileSync()
                if (profile == null) {
                    val intent = Intent(this@MainActivity, AddBabyActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    @Suppress("DEPRECATION")
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                    finish()
                }
            } catch (e: Exception) {
                android.util.Log.e("PROFILE_CHECK", "Error checking profile", e)
            }
        }
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            val fragment = when (item.itemId) {
                R.id.navigation_home -> HomeFragment()
                R.id.navigation_growth -> GrowthFragment()
                R.id.navigation_vaccine -> VaccinationFragment()
                R.id.navigation_feeding -> FeedingFragment()
                R.id.navigation_milestones -> MilestonesFragment()
                else -> HomeFragment()
            }
            loadFragment(fragment)
            true
        }
    }

    private fun loadFragment(fragment: Fragment) {
        if (isFinishing || isDestroyed) return
        supportFragmentManager.beginTransaction()
            .replace(R.id.nav_host_fragment, fragment)
            .commitAllowingStateLoss()
    }

    fun navigateToTab(itemId: Int) {
        binding.bottomNavigation.selectedItemId = itemId
    }
}