package com.example.shishusneh.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.shishusneh.data.entities.Milestone
import kotlinx.coroutines.flow.Flow

@Dao
interface MilestoneDao {
    @Query("SELECT * FROM milestones ORDER BY monthNumber ASC")
    fun getAllMilestones(): Flow<List<Milestone>>

    @Update
    suspend fun updateMilestone(milestone: Milestone)

    @Insert
    suspend fun insertMilestones(milestones: List<Milestone>)

    @Query("DELETE FROM milestones")
    suspend fun deleteAll()
}
