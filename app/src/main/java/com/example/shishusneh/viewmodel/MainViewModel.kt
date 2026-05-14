package com.example.shishusneh.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.shishusneh.data.AppDatabase
import com.example.shishusneh.data.entities.BabyProfile
import com.example.shishusneh.data.entities.GrowthRecord
import com.example.shishusneh.data.entities.Milestone
import com.example.shishusneh.data.entities.Vaccination
import com.example.shishusneh.data.entities.ActivityRecord
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.Dispatchers
import java.util.Calendar

import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit
import com.example.shishusneh.workers.FeedingReminderWorker

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AppDatabase.getDatabase(application)
    private val workManager = WorkManager.getInstance(application)

    val babyProfile = database.babyDao().getBabyProfile().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null,
    )

    val growthRecords = database.growthDao().getAllGrowthRecords().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val vaccinations = database.vaccinationDao().getAllVaccinations().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    suspend fun getBabyProfileSync(): BabyProfile? {
        return database.babyDao().getBabyProfile().first()
    }

    val milestones = database.milestoneDao().getAllMilestones().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    private fun getTodayStart(): Long {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.timeInMillis
    }

    val todayActivities = database.activityDao().getTodayActivities(getTodayStart()).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun scheduleFeedingReminder(hours: Int) {
        if (hours <= 0) {
            workManager.cancelUniqueWork("FeedingReminder")
            return
        }

        val request = PeriodicWorkRequestBuilder<FeedingReminderWorker>(hours.toLong(), TimeUnit.HOURS)
            .addTag("feeding_reminder")
            .build()

        workManager.enqueueUniquePeriodicWork(
            "FeedingReminder",
            ExistingPeriodicWorkPolicy.REPLACE,
            request
        )
    }

    fun updateProfileImage(uri: String) {
        viewModelScope.launch {
            val current = babyProfile.value
            current?.let {
                database.babyDao().insertBabyProfile(it.copy(profileImageUri = uri))
            }
        }
    }

    fun addActivity(type: String, title: String, subtitle: String, iconRes: Int, colorRes: Int, notes: String = "", durationMinutes: Int = 0) {
        viewModelScope.launch {
            database.activityDao().insertActivity(
                ActivityRecord(
                    type = type,
                    title = title,
                    subtitle = subtitle,
                    timestamp = System.currentTimeMillis(),
                    iconResId = iconRes,
                    colorResId = colorRes,
                    notes = notes,
                    durationMinutes = durationMinutes
                )
            )
        }
    }

    fun addGrowthRecord(weight: Float, height: Float) {
        viewModelScope.launch {
            database.growthDao().insertGrowthRecord(
                GrowthRecord(dateMillis = System.currentTimeMillis(), weightKg = weight, heightCm = height)
            )
        }
    }

    fun deleteGrowthRecord(record: GrowthRecord) {
        viewModelScope.launch {
            database.growthDao().deleteGrowthRecord(record)
        }
    }

    fun updateVaccination(vaccination: Vaccination) {
        viewModelScope.launch {
            database.vaccinationDao().updateVaccination(vaccination)
        }
    }

    fun updateMilestone(milestone: Milestone) {
        viewModelScope.launch {
            database.milestoneDao().updateMilestone(milestone)
        }
    }

    fun insertBabyProfile(profile: BabyProfile) = viewModelScope.launch {
        database.babyDao().insertBabyProfile(profile)
        generateVaccinationSchedule(profile.dateOfBirthMillis)
        generateMilestones()
    }

    private suspend fun generateVaccinationSchedule(dob: Long) {
        val dayMillis = 86400000L
        val vaccines = listOf(
            Vaccination(name = "BCG", diseasePrevented = "Tuberculosis", dueDateMillis = dob),
            Vaccination(name = "HepB-0", diseasePrevented = "Hepatitis B", dueDateMillis = dob),
            Vaccination(name = "OPV-0", diseasePrevented = "Polio", dueDateMillis = dob),
            Vaccination(name = "OPV-1", diseasePrevented = "Polio Drops", dueDateMillis = dob + (dayMillis * 42)), // 6 weeks
            Vaccination(name = "Pentavalent-1", diseasePrevented = "Diphtheria, Tetanus, Hep B, Hib", dueDateMillis = dob + (dayMillis * 42)),
            Vaccination(name = "OPV-2", diseasePrevented = "Polio Drops", dueDateMillis = dob + (dayMillis * 70)), // 10 weeks
            Vaccination(name = "Pentavalent-2", diseasePrevented = "Diphtheria, Tetanus, etc.", dueDateMillis = dob + (dayMillis * 70)),
            Vaccination(name = "OPV-3", diseasePrevented = "Polio Drops", dueDateMillis = dob + (dayMillis * 98)), // 14 weeks
            Vaccination(name = "Pentavalent-3", diseasePrevented = "Diphtheria, Tetanus, etc.", dueDateMillis = dob + (dayMillis * 98)),
            Vaccination(name = "Measles/MR-1", diseasePrevented = "Measles & Rubella", dueDateMillis = dob + (dayMillis * 270)), // 9 months
            Vaccination(name = "Vitamin A", diseasePrevented = "Eye health & Immunity", dueDateMillis = dob + (dayMillis * 270))
        )
        database.vaccinationDao().insertVaccinations(vaccines)
    }

    private suspend fun generateMilestones() {
        val milestones = listOf(
            Milestone(monthNumber = 2, description = "Is the baby holding their head up?"),
            Milestone(monthNumber = 2, description = "Does the baby smile at you?"),
            Milestone(monthNumber = 4, description = "Can the baby track objects with eyes?"),
            Milestone(monthNumber = 4, description = "Is the baby making cooing sounds?"),
            Milestone(monthNumber = 6, description = "Does the baby recognize familiar faces?"),
            Milestone(monthNumber = 9, description = "Can the baby sit without support?"),
            Milestone(monthNumber = 12, description = "Can the baby stand alone?")
        )
        database.milestoneDao().insertMilestones(milestones)
    }

    fun updateBabyProfile(profile: BabyProfile) {
        viewModelScope.launch {
            database.babyDao().insertBabyProfile(profile)
        }
    }

    suspend fun deleteBabyProfile() {
        withContext(Dispatchers.IO) {
            // Targeted delete to keep User account/session intact
            database.babyDao().deleteAll()
            database.growthDao().deleteAll()
            database.vaccinationDao().deleteAll()
            database.milestoneDao().deleteAll()
            database.activityDao().deleteAll()
        }
    }

    fun updateActivity(activity: ActivityRecord) {
        viewModelScope.launch {
            database.activityDao().updateActivity(activity)
        }
    }

    fun deleteActivity(activity: ActivityRecord) {
        viewModelScope.launch {
            database.activityDao().deleteActivity(activity)
        }
    }
}
