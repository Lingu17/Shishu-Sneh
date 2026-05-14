package com.example.shishusneh.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "baby_profile")
data class BabyProfile(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val dateOfBirthMillis: Long,
    val gender: String,
    val profileImageUri: String? = null
)
