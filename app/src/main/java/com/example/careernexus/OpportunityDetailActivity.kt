package com.example.careernexus

import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.icu.util.TimeUnit
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.util.Date
import java.util.Locale

class OpportunityDetailActivity : AppCompatActivity() {

    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var btnBack: ImageButton
    private lateinit var btnEdit: ImageButton
    private lateinit var tvDetailType: TextView
    private lateinit var tvDetailTitle: TextView
    private lateinit var tvDetailDeadline: TextView
    private lateinit var tvDaysRemaining: TextView
    private lateinit var tvDetailNotes: TextView
    private lateinit var tvDetailSource: TextView
    private lateinit var tvDetailLink: TextView
    private lateinit var btnMarkCompleted: Button
    private lateinit var btnDeleteOpportunity: Button
    private var opportunityId: Int = -1
    private var opportunity: Opportunity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_opportunity_detail)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        databaseHelper = DatabaseHelper(this)
        initializeViews()
        opportunityId = intent.getIntExtra( "OPPORTUNITY_ID", -1 )
        if (opportunityId == -1) {
            Toast.makeText( this, "Opportunity not found", Toast.LENGTH_SHORT ).show()
            finish()
            return
        }
        loadOpportunity()
        setupBackButton()
        setupEditButton()
        setupDeleteButton()
        setupCompletedButton()
        setupLinkClick()
    }
    private fun initializeViews() {
        btnBack = findViewById(R.id.btnBack)
        btnEdit = findViewById(R.id.btnEdit)
        tvDetailType = findViewById(R.id.tvDetailType)
        tvDetailTitle = findViewById(R.id.tvDetailTitle)
        tvDetailDeadline = findViewById(R.id.tvDetailDeadline)
        tvDaysRemaining = findViewById(R.id.tvDaysRemaining)
        tvDetailNotes = findViewById(R.id.tvDetailNotes)
        tvDetailSource = findViewById(R.id.tvDetailSource)
        tvDetailLink = findViewById(R.id.tvDetailLink)
        btnMarkCompleted = findViewById(R.id.btnMarkCompleted)
        btnDeleteOpportunity = findViewById(R.id.btnDeleteOpportunity)
    }
    private fun loadOpportunity() {
        val allOpportunities = databaseHelper.getAllOpportunities()
        opportunity = allOpportunities.find { it.id == opportunityId }
        if (opportunity == null) {
            Toast.makeText( this, "Opportunity not found", Toast.LENGTH_SHORT ).show()
            finish()
            return
        }
        displayOpportunity(opportunity!!)
    }
    private fun displayOpportunity( opportunity: Opportunity ) {
        tvDetailType.text = opportunity.type
        tvDetailTitle.text = opportunity.title
        val formatter = SimpleDateFormat( "dd MMM yyyy, hh:mm a", Locale.getDefault() )
        tvDetailDeadline.text = "Deadline: ${ formatter.format(Date(opportunity.deadlineMillis)) }"
        tvDaysRemaining.text = getRemainingTime( opportunity.deadlineMillis )
        tvDetailNotes.text = if (opportunity.notes.isNotEmpty()) { opportunity.notes } else { "No notes added." }
        tvDetailSource.text = if (opportunity.source.isNotEmpty()) { opportunity.source } else { "Not specified" }
        tvDetailLink.text = if (opportunity.link.isNotEmpty()) { opportunity.link } else { "No reference link" }
    }
    private fun getRemainingTime(deadlineMillis: Long ): String {
        var difference = deadlineMillis - System.currentTimeMillis()
        if (difference <= 0) { return "Expired" }
        val days = java.util.concurrent.TimeUnit.MILLISECONDS.toDays(difference)
        difference -= java.util.concurrent.TimeUnit.DAYS.toMillis(days)

        val hours = java.util.concurrent.TimeUnit.MILLISECONDS.toHours(difference)
        difference -= java.util.concurrent.TimeUnit.HOURS.toMillis(hours)

        val minutes = java.util.concurrent.TimeUnit.MILLISECONDS.toMinutes(difference)
        return when {
            days > 0 -> "$days days left"
            hours > 0 -> "$hours hours left"
            minutes > 0 -> "$minutes minutes left"
            else -> "Less than a minute"
        }
    }
    private fun setupBackButton() {
        btnBack.setOnClickListener {
            val intent = Intent(this, Dashboard::class.java)
            startActivity(intent)
            finish()
        }
    }
    private fun setupEditButton() {
        btnEdit.setOnClickListener {
            val currentOpportunity = opportunity ?: return@setOnClickListener
            val intent = Intent( this, AddOpportunity::class.java )
            intent.putExtra( "EDIT_OPPORTUNITY_ID", currentOpportunity.id )
            startActivity(intent)
        }
    }
    private fun setupDeleteButton() {
        btnDeleteOpportunity.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Delete Opportunity")
                .setMessage("Are you sure you want to delete this opportunity?")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Delete") { _, _ ->
                    deleteOpportunity()
                }
                .show()
        }
    }
    private fun deleteOpportunity() {
        val result = databaseHelper.deleteOpportunity( opportunityId )
        if (result > 0) {
            Toast.makeText( this, "Opportunity deleted", Toast.LENGTH_SHORT ).show()
            finish()
        } else {
            Toast.makeText( this, "Failed to delete opportunity", Toast.LENGTH_SHORT ).show()
        }
    }
    private fun setupCompletedButton() {
        btnMarkCompleted.setOnClickListener {
            val result = databaseHelper.markOpportunityCompleted(opportunityId)
            if (result > 0) {
                Toast.makeText(this,"Opportunity marked as completed",Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this,"Failed to update opportunity",Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun setupLinkClick() {
        tvDetailLink.setOnClickListener {
            val link = opportunity?.link?.trim()
            if (link.isNullOrEmpty()) { return@setOnClickListener }
            try { val url = if ( link.startsWith("http://") || link.startsWith("https://") ) { link
            } else { "https://$link"
            }
                val intent = Intent( Intent.ACTION_VIEW, Uri.parse(url) )
                startActivity(intent)
            } catch (e: Exception) { Toast.makeText( this, "Unable to open link", Toast.LENGTH_SHORT ).show()
            }
        }
    }
    override fun onResume() {
        super.onResume()
        if (::databaseHelper.isInitialized && opportunityId != -1 ) {
            loadOpportunity()
        }
    }
}
