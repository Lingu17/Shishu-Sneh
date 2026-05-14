package com.example.shishusneh.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.shishusneh.data.entities.GrowthRecord
import kotlinx.coroutines.flow.Flow

@Dao
interface GrowthDao {
    @Query("SELECT * FROM growth_records ORDER BY dateMillis ASC")
    fun getAllGrowthRecords(): Flow<List<GrowthRecord>>

    @Insert
    suspend fun insertGrowthRecord(record: GrowthRecord)

    @Delete
    suspend fun deleteGrowthRecord(record: GrowthRecord)

    @Query("DELETE FROM growth_records")
    suspend fun deleteAll()
}
