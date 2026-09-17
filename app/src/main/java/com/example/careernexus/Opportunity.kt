package com.example.careernexus

data class Opportunity(
    val id: Int,
    val title: String,
    val type: String,
    val deadlineMillis: Long,
    val source: String,
    val link: String,
    val notes: String
)