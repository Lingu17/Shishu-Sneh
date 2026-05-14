package com.example.shishusneh.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.shishusneh.data.entities.Vaccination
import kotlinx.coroutines.flow.Flow

@Dao
interface VaccinationDao {
    @Query("SELECT * FROM vaccinations ORDER BY dueDateMillis ASC")
    fun getAllVaccinations(): Flow<List<Vaccination>>

    @Update
    suspend fun updateVaccination(vaccination: Vaccination)

    @Insert
    suspend fun insertVaccinations(vaccinations: List<Vaccination>)

    @Query("DELETE FROM vaccinations")
    suspend fun deleteAll()
}
