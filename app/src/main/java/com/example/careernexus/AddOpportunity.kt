package com.example.careernexus

import android.app.DatePickerDialog
import android.content.Intent
import android.icu.util.Calendar
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
class AddOpportunity : AppCompatActivity() {

    private lateinit var etTitle: EditText
    private lateinit var spinnerType: Spinner
    private lateinit var btnPickDate: Button
    private lateinit var tvSelectedDate: TextView
    private lateinit var tvSourceLabel: EditText
    private lateinit var etLink: EditText
    private lateinit var etNotes: EditText
    private lateinit var btnSave: Button
    private var selectedDate = ""
    private var deadlineMillis: Long = 0L
    private var editOpportunityId: Int = -1

    private lateinit var databaseHelper: DatabaseHelper
    val opportunityTypes = arrayOf(
        "Other",
        "Examination",
        "Internship",
        "Hackathon",
        "Scholarship",
        "Competition",
        "Workshop",
        "Seminar",
        "Job"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_opportunity)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        etTitle = findViewById(R.id.etTitle)
        spinnerType = findViewById(R.id.spinnerType)
        btnPickDate = findViewById(R.id.btnPickDate)
        tvSelectedDate = findViewById(R.id.tvSelectedDate)
        tvSourceLabel = findViewById(R.id.tvSourceLabel)
        etLink = findViewById(R.id.etLink)
        etNotes = findViewById(R.id.etNotes)
        btnSave = findViewById(R.id.btnSave)

        databaseHelper = DatabaseHelper(this)

        setupSpinner()
        setupDatePicker()
        editOpportunityId = intent.getIntExtra( "EDIT_OPPORTUNITY_ID", -1 )
        if (editOpportunityId != -1) {
            loadOpportunityForEdit()
            btnSave.text = "Update Opportunity"
        } else {
            btnSave.text = "Save Opportunity"
        }
        setupSaveButton()
    }
    private fun loadOpportunityForEdit() {
        val opportunity = databaseHelper
            .getAllOpportunities()
            .find {
                it.id == editOpportunityId
            }
        if (opportunity == null) {
            Toast.makeText( this, "Opportunity not found", Toast.LENGTH_SHORT ).show()
            finish()
            return
        }
        etTitle.setText( opportunity.title )
        val typePosition = opportunityTypes.indexOf( opportunity.type )
        if (typePosition >= 0) {
            spinnerType.setSelection( typePosition )
        }
        deadlineMillis = opportunity.deadlineMillis
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = deadlineMillis
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        val month = calendar.get(Calendar.MONTH) + 1
        val year = calendar.get(Calendar.YEAR)
        selectedDate = "$day/$month/$year"
        tvSelectedDate.text = selectedDate
        tvSourceLabel.setText( opportunity.source )
        etLink.setText( opportunity.link )
        etNotes.setText( opportunity.notes )
    }
    private fun setupSpinner() {

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            opportunityTypes
        )
        adapter.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )
        spinnerType.adapter = adapter
    }
    private fun setupDatePicker() {

        btnPickDate.setOnClickListener {

            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            val datePickerDialog = DatePickerDialog(
                this,
                { _, selectedYear, selectedMonth, selectedDay ->
                    val actualMonth = selectedMonth + 1
                    calendar.set(
                        selectedYear,
                        selectedMonth,
                        selectedDay,
                        23,
                        59,
                        59
                    )
                    deadlineMillis = calendar.timeInMillis
                    selectedDate = "$selectedDay/$actualMonth/$selectedYear"
                    tvSelectedDate.text = selectedDate
                },
                year,
                month,
                day
            )
            datePickerDialog.show()
        }
    }
    private fun setupSaveButton() {

        btnSave.setOnClickListener {

            val title = etTitle.text.toString().trim()
            val type = spinnerType.selectedItem.toString()
            val source = tvSourceLabel.text.toString().trim()
            val link = etLink.text.toString().trim()
            val notes = etNotes.text.toString().trim()

            if (title.isEmpty()) {
                etTitle.error = "Please enter opportunity title"
                etTitle.requestFocus()
                return@setOnClickListener
            }

            if (selectedDate.isEmpty()) {
                Toast.makeText(
                    this, "Please select a deadline date",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            if (link.isEmpty()){
                Toast.makeText(
                    this, "Please enter link",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            if (editOpportunityId == -1) {
                val result = databaseHelper.insertOpportunity(
                    title = title,
                    type = type,
                    deadlineMillis = deadlineMillis,
                    source = source,
                    link = link,
                    notes = notes
                )
                if (result != -1L) {

                    Toast.makeText(
                        this,
                        "Opportunity Saved!",
                        Toast.LENGTH_SHORT
                    ).show()
                    finish()

                } else {

                    Toast.makeText(
                        this,
                        "Failed to save opportunity",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                val result = databaseHelper.updateOpportunity(
                    id = editOpportunityId,
                    title = title,
                    type = type,
                    deadlineMillis = deadlineMillis,
                    source = source,
                    link = link,
                    notes = notes
                )
                if (result > 0) {
                    Toast.makeText( this, "Opportunity Updated!", Toast.LENGTH_SHORT ).show()
                    finish()
                } else {
                    Toast.makeText( this, "Failed to update opportunity", Toast.LENGTH_SHORT ).show() }
            }
        }
    }
}