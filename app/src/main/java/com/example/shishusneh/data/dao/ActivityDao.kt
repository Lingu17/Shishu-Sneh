package com.example.shishusneh.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.shishusneh.data.entities.ActivityRecord
import kotlinx.coroutines.flow.Flow

@Dao
interface ActivityDao {
    @Query("SELECT * FROM activity_records ORDER BY timestamp DESC")
    fun getAllActivities(): Flow<List<ActivityRecord>>

    @Query("SELECT * FROM activity_records WHERE timestamp >= :todayStart ORDER BY timestamp DESC")
    fun getTodayActivities(todayStart: Long): Flow<List<ActivityRecord>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertActivity(activity: ActivityRecord)

    @androidx.room.Update
    suspend fun updateActivity(activity: ActivityRecord)

    @androidx.room.Delete
    suspend fun deleteActivity(activity: ActivityRecord)

    @Query("DELETE FROM activity_records")
    suspend fun deleteAll()
}
