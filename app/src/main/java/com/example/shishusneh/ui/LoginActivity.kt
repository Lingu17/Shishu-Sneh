package com.example.shishusneh.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.shishusneh.R
import com.example.shishusneh.data.AppDatabase
import com.example.shishusneh.databinding.ActivityLoginBinding
import com.example.shishusneh.viewmodel.MainViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        auth = FirebaseAuth.getInstance()

        val sharedPrefs = getSharedPreferences("shishu_sneh", MODE_PRIVATE)
        if (sharedPrefs.getBoolean("is_logged_in", false) && auth.currentUser != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            return
        }

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim().lowercase()
            val password = binding.etPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Success
                        lifecycleScope.launch {
                            val db = AppDatabase.getDatabase(this@LoginActivity)
                            val user = db.userDao().getUserByEmail(email)
                            
                            sharedPrefs.edit().apply {
                                putBoolean("is_logged_in", true)
                                putString("user_email", email)
                                putString("user_name", user?.fullName ?: "Mom")
                                apply()
                            }
                            
                            val intent = Intent(this@LoginActivity, MainActivity::class.java)
                            startActivity(intent)
                            @Suppress("DEPRECATION")
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                            finish()
                        }
                    } else {
                        Toast.makeText(this@LoginActivity, "Login failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        binding.tvSignup.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
            @Suppress("DEPRECATION")
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
    }
}
