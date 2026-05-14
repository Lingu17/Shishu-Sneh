package com.example.shishusneh.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.example.shishusneh.R
import com.example.shishusneh.databinding.ActivitySplashBinding
import com.example.shishusneh.ui.utils.VoiceFeedbackManager

class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private lateinit var voiceManager: VoiceFeedbackManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        voiceManager = VoiceFeedbackManager(this)

        // 1. Logo Animation (Scale + Fade In)
        binding.ivSplashLogo.alpha = 0f
        binding.ivSplashLogo.scaleX = 0.8f
        binding.ivSplashLogo.scaleY = 0.8f

        binding.ivSplashLogo.animate()
            .alpha(1f)
            .scaleX(1f)
            .scaleY(1f)
            .setDuration(1000)
            .setInterpolator(android.view.animation.OvershootInterpolator())
            .start()

        // 2. Text Animation (Delayed Fade In)
        binding.tvSplashName.alpha = 0f
        binding.tvSplashName.animate()
            .alpha(1f)
            .setDuration(800)
            .setStartDelay(400)
            .start()

        binding.tvSplashSubtitle.alpha = 0f
        binding.tvSplashSubtitle.animate()
            .alpha(0.7f)
            .setDuration(800)
            .setStartDelay(700)
            .start()

        // 3. Subtle Vibration
        vibrate()

        // 4. Voice Welcome
        Handler(Looper.getMainLooper()).postDelayed({
            voiceManager.speak("Welcome to Shishu-Sneh")
        }, 500)

        // 5. Logic to check login and navigate
        Handler(Looper.getMainLooper()).postDelayed({
            checkLoginAndNavigate()
        }, 2200)
    }

    private fun checkLoginAndNavigate() {
        val sharedPref = getSharedPreferences("shishu_sneh", Context.MODE_PRIVATE)
        val isLoggedIn = sharedPref.getBoolean("is_logged_in", false)

        val intent = if (isLoggedIn) {
            Intent(this, MainActivity::class.java)
        } else {
            Intent(this, LoginActivity::class.java)
        }

        startActivity(intent)
        @Suppress("DEPRECATION")
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        finish()
    }

    private fun vibrate() {
        try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
                val vibratorManager = getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
                vibratorManager.defaultVibrator.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE))
            } else {
                @Suppress("DEPRECATION")
                val vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                vibrator.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE))
            }
        } catch (e: Exception) {
            // Ignore if vibration fails
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        voiceManager.shutdown()
    }
}
