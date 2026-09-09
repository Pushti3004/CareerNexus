package com.example.careernexus

data class Opportunity(
    val id: Int = 0,
    val title: String,
    val type: String,       // "Internship" | "Hackathon" | "Workshop"
    val deadline: Long,     // store as epoch millis
    val link: String? = null,
    val notes: String? = null,
    val isCompleted: Boolean = false
)