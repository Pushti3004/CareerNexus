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

    private lateinit var databaseHelper: DatabaseHelper

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
        setupSaveButton()
    }
    private fun setupSpinner() {

        val opportunityTypes = arrayOf(
            "Internship",
            "Hackathon",
            "Scholarship",
            "Competition",
            "Workshop",
            "Seminar",
            "Job",
            "Other"
        )

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

                val intent =
                    Intent(this, Dashboard::class.java)

                startActivity(intent)

                finish()

            } else {

                Toast.makeText(
                    this,
                    "Failed to save opportunity",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}