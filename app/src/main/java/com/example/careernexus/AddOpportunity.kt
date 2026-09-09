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

class AddOpportunityActivity : AppCompatActivity() {

    private var selectedDateMillis: Long = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_opportunity)

        val etTitle = findViewById<EditText>(R.id.etTitle)
        val spinnerType = findViewById<Spinner>(R.id.spinnerType)
        val btnPickDate = findViewById<Button>(R.id.btnPickDate)
        val tvSelectedDate = findViewById<TextView>(R.id.tvSelectedDate)
        val etLink = findViewById<EditText>(R.id.etLink)
        val etNotes = findViewById<EditText>(R.id.etNotes)
        val btnSave = findViewById<Button>(R.id.btnSave)

        // Spinner options
        val types = arrayOf("Internship", "Hackathon", "Workshop")
        spinnerType.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, types)

        // Date picker
        btnPickDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            DatePickerDialog(
                this,
                { _, year, month, day ->
                    calendar.set(year, month, day, 23, 59) // default end of day
                    selectedDateMillis = calendar.timeInMillis
                    tvSelectedDate.text = "Deadline: ${day}/${month + 1}/${year}"
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        // Save button
        btnSave.setOnClickListener {
            val title = etTitle.text.toString().trim()
            val type = spinnerType.selectedItem.toString()
            val link = etLink.text.toString().trim()
            val notes = etNotes.text.toString().trim()

            if (title.isEmpty()) {
                Toast.makeText(this, "Please enter a title", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (selectedDateMillis == 0L) {
                Toast.makeText(this, "Please pick a deadline date", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val opportunity = Opportunity(
                title = title,
                type = type,
                deadline = selectedDateMillis,
                link = link.ifEmpty { null },
                notes = notes.ifEmpty { null }
            )

            OpportunityRepository.addOpportunity(opportunity)

            Toast.makeText(this, "Opportunity saved!", Toast.LENGTH_SHORT).show()
            finish() // go back to Dashboard
        }
    }
}