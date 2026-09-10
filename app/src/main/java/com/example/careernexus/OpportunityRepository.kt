package com.example.careernexus

object OpportunityRepository {
    private val opportunities = mutableListOf<Opportunity>()

    fun addOpportunity(opportunity: Opportunity) {
        opportunities.add(opportunity)
    }

    fun getAll(): List<Opportunity> {
        return opportunities.sortedBy { it.deadline }
    }
}