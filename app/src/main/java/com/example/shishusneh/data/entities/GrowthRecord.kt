package com.example.shishusneh.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "growth_records")
data class GrowthRecord(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val dateMillis: Long,
    val weightKg: Float,
    val heightCm: Float
)
