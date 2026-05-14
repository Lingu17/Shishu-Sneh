package com.example.shishusneh.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.shishusneh.data.dao.*
import com.example.shishusneh.data.entities.*

@Database(entities = [BabyProfile::class, GrowthRecord::class, Vaccination::class, Milestone::class, User::class, ActivityRecord::class], version = 6, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun babyDao(): BabyDao
    abstract fun growthDao(): GrowthDao
    abstract fun vaccinationDao(): VaccinationDao
    abstract fun milestoneDao(): MilestoneDao
    abstract fun userDao(): UserDao
    abstract fun activityDao(): ActivityDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "shishu_sneh_database",
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }

    fun clearAllData() {
        clearAllTables()
    }

    suspend fun nukeBabyData() {
        // Run specific deletes to keep User account intact
        babyDao().deleteAll()
        growthDao().deleteAll()
        vaccinationDao().deleteAll()
        milestoneDao().deleteAll()
        activityDao().deleteAll()
    }
}
