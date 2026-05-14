package com.example.shishusneh.workers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.shishusneh.R
import com.example.shishusneh.ui.utils.VoiceFeedbackManager

class FeedingReminderWorker(
    private val context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        showNotification()
        return Result.success()
    }

    private fun showNotification() {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "feeding_reminders"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Feeding Reminders",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        val voiceManager = VoiceFeedbackManager(context)
        voiceManager.speak("It's time to feed your baby")

        val notification = NotificationCompat.Builder(context, channelId)
            .setContentTitle("Feeding Time! 🍼")
            .setContentText("It's time to feed your baby. Keep them healthy and happy!")
            .setSmallIcon(R.drawable.ic_feeding)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(2, notification)
    }
}
