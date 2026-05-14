package com.example.shishusneh.workers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.shishusneh.R
import com.example.shishusneh.data.AppDatabase
import kotlinx.coroutines.flow.firstOrNull

class VaccinationReminderWorker(
    private val context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val database = AppDatabase.getDatabase(context)
        val vaccinationsFlow = database.vaccinationDao().getAllVaccinations()
        
        val vaccinations = vaccinationsFlow.firstOrNull() ?: return Result.success()
        
        val now = System.currentTimeMillis()
        val upcomingVaccinations = vaccinations.filter { 
            !it.isCompleted && it.dueDateMillis > now && (it.dueDateMillis - now) < (7 * 24 * 60 * 60 * 1000L) // Due in next 7 days
        }

        if (upcomingVaccinations.isNotEmpty()) {
            showNotification(upcomingVaccinations.size)
        }

        return Result.success()
    }

    private fun showNotification(count: Int) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "vaccination_reminders"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Vaccination Alerts",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(context, channelId)
            .setContentTitle("Upcoming Vaccinations")
            .setContentText("You have $count upcoming vaccination(s) for your baby.")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(1, notification)
    }
}
