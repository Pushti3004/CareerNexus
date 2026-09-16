package com.example.careernexus

import android.app.DatePickerDialog
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

    private var selectedDateMillis: Long = 0L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_opportunity)
        val etTitle = findViewById<EditText>(R.id.etTitle)
        val spinnerType = findViewById<Spinner>(R.id.spinnerType)
        val btnPickDate = findViewById<Button>(R.id.btnPickDate)
        val tvSelectedDate = findViewById<TextView>(R.id.tvSelectedDate)
        val etLink = findViewById<EditText>(R.id.etLink)
        val etNotes = findViewById<EditText>(R.id.etNotes)
        val btnSave = findViewById<Button>(R.id.btnSave)

        // Fill dropdown with the 3 opportunity types
        val types = arrayOf("Internship", "Hackathon", "Workshop", "Seminar")
        spinnerType.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            types
        )

        // Show date picker when button is tapped
        btnPickDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            DatePickerDialog(
                this,
                { _, year, month, day ->
                    calendar.set(year, month, day, 23, 59) // deadline defaults to end of day
                    selectedDateMillis = calendar.timeInMillis
                    tvSelectedDate.text = "Deadline: $day/${month + 1}/$year"
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        // Save button click
        btnSave.setOnClickListener {
            val title = etTitle.text.toString().trim()
            val type = spinnerType.selectedItem.toString()
            val link = etLink.text.toString().trim()
            val notes = etNotes.text.toString().trim()

            // Basic validation
            if (title.isEmpty()) {
                Toast.makeText(this, "Please enter a title", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (selectedDateMillis == 0L) {
                Toast.makeText(this, "Please pick a deadline date", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (link.isEmpty()) {
                Toast.makeText(this, "Please enter a link", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Create the Opportunity object
            val opportunity = Opportunity(
                title = title,
                type = type,
                deadline = selectedDateMillis,
                link = link,
                notes = notes.ifEmpty { null }
            )

            // Save it into the shared in-memory repository
            OpportunityRepository.addOpportunity(opportunity)
            Toast.makeText(this, "Opportunity saved!", Toast.LENGTH_SHORT).show()

            // Close this screen and return to Dashboard
            finish()
        }
    }
}