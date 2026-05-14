package com.example.shishusneh.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.shishusneh.R
import com.example.shishusneh.data.AppDatabase
import com.example.shishusneh.data.entities.User
import com.example.shishusneh.databinding.ActivitySignupBinding
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.btnSignup.setOnClickListener {
            val fullName = binding.etFullName.text.toString().trim()
            val email = binding.etEmail.text.toString().trim().lowercase()
            val password = binding.etPassword.text.toString().trim()

            if (fullName.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password.length < 6) {
                Toast.makeText(this, "Password should be at least 6 characters", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Success
                        lifecycleScope.launch {
                            val db = AppDatabase.getDatabase(this@SignupActivity)
                            val newUser = User(email, password, fullName)
                            db.userDao().insertUser(newUser)
                            Toast.makeText(this@SignupActivity, "Account created! Please log in.", Toast.LENGTH_SHORT).show()
                            finish()
                            @Suppress("DEPRECATION")
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                        }
                    } else {
                        Toast.makeText(this, "Registration failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        binding.tvLogin.setOnClickListener {
            finish()
            @Suppress("DEPRECATION")
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
    }
}
