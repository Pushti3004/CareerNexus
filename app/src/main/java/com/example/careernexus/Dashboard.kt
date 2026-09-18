package com.example.careernexus

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class Dashboard : AppCompatActivity() {

    private lateinit var recyclerOpportunities: RecyclerView

    private lateinit var databaseHelper: DatabaseHelper

    private lateinit var adapter: OpportunityAdapter
    private lateinit var tvOpportunityCount: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_dashboard)

        databaseHelper = DatabaseHelper(this)
        tvOpportunityCount = findViewById(R.id.tvOpportunityCount)
        recyclerOpportunities = findViewById(R.id.rvOpportunities)
        recyclerOpportunities.layoutManager = LinearLayoutManager(this)
        loadOpportunities()

        val btnPlus = findViewById<FloatingActionButton>(R.id.btn_plus)
        btnPlus.setOnClickListener {
            startActivity(Intent(this, AddOpportunity::class.java))
        }

        val btnMenu = findViewById<ImageButton>(R.id.btnMenu)
        btnMenu.setOnClickListener {
            val popup = PopupMenu(this, btnMenu)
            popup.menuInflater.inflate(R.menu.dashboard_menu, popup.menu)
            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.logout -> {
                        logoutUser()
                        true
                    }
                    else -> false
                }
            }
            popup.show()
        }
    }
    override fun onResume() {
        super.onResume()
        if (::databaseHelper.isInitialized) {
            loadOpportunities()
        }
    }
    private fun loadOpportunities() {

        val opportunityList = databaseHelper.getAllOpportunities()

        adapter = OpportunityAdapter(opportunityList)
        recyclerOpportunities.adapter = adapter

        val activeCount = databaseHelper.getActiveOpportunityCount()
        tvOpportunityCount.text = activeCount.toString()
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
