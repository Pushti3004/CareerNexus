package com.example.careernexus

import kotlin.concurrent.atomics.AtomicReference

data class Opportunity(
    val title: String,
    val type: String,       // "Internship" | "Hackathon" | "Workshop" | "Seminar" | "Placement Activtiy"
    val deadline: Long,     // store as epoch millis
    val reference: String,  // From where you heard about this
    val link: String,
    val notes: String? = null,
    val isCompleted: Boolean = false
)