package com.example.shishusneh.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "milestones")
data class Milestone(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val monthNumber: Int,
    val description: String,
    val isAchieved: Boolean = false
)
