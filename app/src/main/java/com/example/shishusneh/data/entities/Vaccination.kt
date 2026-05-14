package com.example.shishusneh.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "vaccinations")
data class Vaccination(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val diseasePrevented: String,
    val dueDateMillis: Long,
    val isCompleted: Boolean = false
)
