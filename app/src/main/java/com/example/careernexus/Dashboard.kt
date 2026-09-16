package com.example.careernexus

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.PopupMenu
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
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


        adapter = OpportunityAdapter(
            emptyList(),
            onItemClick = { opportunity ->
                val intent = Intent(this, AddOpportunity::class.java)
                startActivity(intent)
            },
            onMenuClick = { opportunity, anchorView ->
                val popup = PopupMenu(this, anchorView)
                popup.menuInflater.inflate(R.menu.opportunity_menu, popup.menu)
                popup.setOnMenuItemClickListener { item ->
                    when (item.itemId) {
                        R.id.menu_edit -> {
                            val intent = Intent(this, AddOpportunity::class.java)
                            startActivity(intent)
                            true
                        }

                        R.id.menu_delete -> {
                            AlertDialog.Builder(this).setTitle("Delete Opportunity")
                                .setMessage("Are you sure you want to delete this opportunity?")
                                .setPositiveButton("Delete") { _, _ ->
                                    OpportunityRepository.deleteOpportunity(opportunity.id)
                                    adapter.updateList(OpportunityRepository.getAll())
                                }
                                .setNegativeButton("Cancel", null)
                                .show()
                            true
                        }

                        else -> false
                    }
                }
                popup.show()
            }
        )

        private fun logoutUser() {
            val prefs = getSharedPreferences("CareerNexusPrefs", Context.MODE_PRIVATE)
            prefs.edit().clear().apply()

            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
}
