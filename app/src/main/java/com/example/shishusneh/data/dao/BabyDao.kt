package com.example.shishusneh.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.shishusneh.data.entities.BabyProfile
import kotlinx.coroutines.flow.Flow

@Dao
interface BabyDao {
    @Query("SELECT * FROM baby_profile LIMIT 1")
    fun getBabyProfile(): Flow<BabyProfile?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBabyProfile(profile: BabyProfile)

    @androidx.room.Delete
    suspend fun deleteBabyProfile(profile: BabyProfile)

    @Query("DELETE FROM baby_profile")
    suspend fun deleteAll()
}
