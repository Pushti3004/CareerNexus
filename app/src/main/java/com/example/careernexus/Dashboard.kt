package com.example.careernexus

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.PopupMenu
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class Dashboard : AppCompatActivity() {

    private lateinit var adapter: OpportunityAdapter
    private lateinit var rvOpportunities: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_dashboard)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val btnMenu = findViewById<ImageButton>(R.id.btnMenu)
        btnMenu.setOnClickListener {
            val popup = PopupMenu(this, btnMenu)
            popup.menuInflater.inflate(R.menu.dashboard_menu, popup.menu)

            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.menu_logout -> {
                        logoutUser()
                        true
                    }
                    else -> false
                }
            }
            popup.show()
        }
        rvOpportunities = findViewById(R.id.rvOpportunities)
        val btnPlus = findViewById<FloatingActionButton>(R.id.btn_plus)

        // 1. Create adapter with an empty list initially
        adapter = OpportunityAdapter(emptyList()) { opportunity ->
            // handle card click later (e.g. open detail screen)
        }

        // 2. Attach LayoutManager + Adapter to RecyclerView
        rvOpportunities.layoutManager = LinearLayoutManager(this)
        rvOpportunities.adapter = adapter

        btnPlus.setOnClickListener {
            startActivity(Intent(this, AddOpportunity::class.java))
        }
    }

    // 3. Refresh data every time Dashboard becomes visible again
    override fun onResume() {
        super.onResume()
        val list = OpportunityRepository.getAll()
        adapter.updateList(list)
    }

    private fun logoutUser() {
        val prefs = getSharedPreferences("CareerNexusPrefs", Context.MODE_PRIVATE)
        prefs.edit().clear().apply()

        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}