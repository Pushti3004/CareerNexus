package com.example.careernexus

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(
        context,
        DATABASE_NAME,
        null,
        DATABASE_VERSION
    ) {

    companion object {

        private const val DATABASE_NAME = "CareerNexus.db"
        private const val DATABASE_VERSION = 3
        private const val TABLE_OPPORTUNITIES = "opportunities"
        private const val COL_ID = "id"
        private const val COL_TITLE = "title"
        private const val COL_TYPE = "type"
        private const val COL_DEADLINE = "deadline"
        private const val COL_SOURCE = "source"
        private const val COL_LINK = "link"
        private const val COL_NOTES = "notes"
        private const val COL_COMPLETED = "completed"
    }

    override fun onCreate(db: SQLiteDatabase) {

        val createTable = """
            CREATE TABLE $TABLE_OPPORTUNITIES (
                $COL_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_TITLE TEXT NOT NULL,
                $COL_TYPE TEXT NOT NULL,
                $COL_DEADLINE INTEGER NOT NULL,
                $COL_SOURCE TEXT,
                $COL_LINK TEXT,
                $COL_NOTES TEXT,
                $COL_COMPLETED INTEGER DEFAULT 0
            )
        """.trimIndent()

        db.execSQL(createTable)
    }

    override fun onUpgrade(
        db: SQLiteDatabase,
        oldVersion: Int,
        newVersion: Int
    ) {
        db.execSQL(
            "DROP TABLE IF EXISTS $TABLE_OPPORTUNITIES"
        )

        onCreate(db)
    }

    fun insertOpportunity(
        title: String,
        type: String,
        deadlineMillis: Long,
        source: String,
        link: String,
        notes: String
    ): Long {
        val db = writableDatabase
        val values = ContentValues()
        values.put(COL_TITLE, title)
        values.put(COL_TYPE, type)
        values.put(COL_DEADLINE, deadlineMillis)
        values.put(COL_SOURCE, source)
        values.put(COL_LINK, link)
        values.put(COL_NOTES, notes)
        values.put(COL_COMPLETED, 0)

        return db.insert(
            TABLE_OPPORTUNITIES,
            null,
            values
        )
    }

    fun getAllOpportunities(): MutableList<Opportunity> {

        val opportunities = mutableListOf<Opportunity>()

        val db = readableDatabase
        val cursor = db.query(
            TABLE_OPPORTUNITIES,
            null,
            null,
            null,
            null,
            null,
            "$COL_ID DESC"
        )

        cursor.use {

            while (it.moveToNext()) {

                val opportunity = Opportunity(

                    id = it.getInt(
                        it.getColumnIndexOrThrow(COL_ID)
                    ),

                    title = it.getString(
                        it.getColumnIndexOrThrow(COL_TITLE)
                    ),

                    type = it.getString(
                        it.getColumnIndexOrThrow(COL_TYPE)
                    ),

                    deadlineMillis = it.getLong(
                        it.getColumnIndexOrThrow(COL_DEADLINE)
                    ),

                    source = it.getString(
                        it.getColumnIndexOrThrow(COL_SOURCE)
                    ) ?: "",

                    link = it.getString(
                        it.getColumnIndexOrThrow(COL_LINK)
                    ) ?: "",

                    notes = it.getString(
                        it.getColumnIndexOrThrow(COL_NOTES)
                    ) ?: "",

                    completed = it.getInt( it.getColumnIndexOrThrow(COL_COMPLETED)
                    ) == 1
                )
                opportunities.add(opportunity)
            }
        }
        return opportunities
    }
    fun getActiveOpportunityCount(): Int {

        val db = readableDatabase

        val currentTime = System.currentTimeMillis()

        val cursor = db.rawQuery(
            """
        SELECT COUNT(*) 
        FROM $TABLE_OPPORTUNITIES
        WHERE $COL_DEADLINE > ?
        AND $COL_COMPLETED = 0
        """.trimIndent(),
            arrayOf(currentTime.toString())
        )

        var count = 0

        cursor.use {
            if (it.moveToFirst()) {
                count = it.getInt(0)
            }
        }

        return count
    }
    fun updateOpportunity(
        id: Int,
        title: String,
        type: String,
        deadlineMillis: Long,
        source: String,
        link: String,
        notes: String
    ): Int {
        val db = writableDatabase
        val values = ContentValues()
        values.put(COL_TITLE, title)
        values.put(COL_TYPE, type)
        values.put(COL_DEADLINE, deadlineMillis)
        values.put(COL_SOURCE, source)
        values.put(COL_LINK, link)
        values.put(COL_NOTES, notes)
        return db.update(
            TABLE_OPPORTUNITIES,
            values,
            "$COL_ID = ?",
            arrayOf(id.toString())
        )
    }
    fun markOpportunityCompleted(id: Int): Int {
        val db = writableDatabase
        val values = ContentValues()
        values.put(COL_COMPLETED, 1)
        return db.update(
            TABLE_OPPORTUNITIES,
            values,
            "$COL_ID = ?",
            arrayOf(id.toString())
        )
    }
    fun deleteOpportunity(id: Int): Int {
        val db = writableDatabase
        return db.delete(
            TABLE_OPPORTUNITIES,
            "$COL_ID = ?",
            arrayOf(id.toString())
        )
    }
}