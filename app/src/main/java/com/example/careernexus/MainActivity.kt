package com.example.careernexus

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Looper
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.logging.Handler

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val logo = findViewById<ImageView>(R.id.CareerNexus)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val logoAnimation = AnimationUtils.loadAnimation(
            this,
            R.anim.logo_animation
        )
        logo.startAnimation(logoAnimation)
        android.os.Handler(Looper.getMainLooper()).postDelayed({
            val prefs = getSharedPreferences("CareerNexusPrefs", Context.MODE_PRIVATE)
            val isLoggedIn = prefs.getBoolean("isLoggedIn", false)

            val intent = if (isLoggedIn) {
                Intent(this, Dashboard::class.java)
            } else {
                Intent(this, LoginActivity::class.java)
            }
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }, 3000)
    }
}