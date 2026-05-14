package com.example.shishusneh.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "activity_records")
data class ActivityRecord(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val type: String, // "Diaper", "Feeding", "Sleep", "Growth"
    val title: String,
    val subtitle: String,
    val timestamp: Long,
    val iconResId: Int,
    val colorResId: Int,
    val notes: String = "",
    val durationMinutes: Int = 0
)
