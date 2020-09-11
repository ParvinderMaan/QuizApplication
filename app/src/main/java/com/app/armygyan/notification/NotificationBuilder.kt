package com.app.armygyan.notification

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.app.armygyan.MainActivity
import com.app.armygyan.R

class NotificationBuilder(private var context: Context) {
    private var notificationManager: NotificationManager
    private var intent: Intent
    private var title=""
    private var message=""

    init {
        intent = Intent(context, MainActivity::class.java)
   //  intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK  or Intent.FLAG_ACTIVITY_NEW_TASK
        intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP;
        notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    }

    fun setTitle(title: String): NotificationBuilder {
        this.title = title
        return this
    }

    fun setBody(message: String): NotificationBuilder {
        this.message = message
        return this
    }

    fun show() {
        val NOTIFICATION_CHANNEL_ID = context.getString(R.string.default_notification_channel_id)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val pendingIntent = PendingIntent.getActivity(context.applicationContext, 0, intent, PendingIntent.FLAG_ONE_SHOT)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                "My Notifications",
                NotificationManager.IMPORTANCE_HIGH
            )
            // Configure the notification channel.
            notificationChannel.description = "" + message
            notificationChannel.enableLights(true)
            notificationChannel.setShowBadge(true)
            notificationChannel.lightColor = Color.RED
            // Configure the notification channel.
            val att = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                .build()
            notificationChannel.setSound(defaultSoundUri, att)
            notificationChannel.vibrationPattern = longArrayOf(1000, 1000)
            notificationChannel.enableVibration(true)
            notificationManager.createNotificationChannel(notificationChannel)
        }
        val notificationBuilder =
            NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
        notificationBuilder.setAutoCancel(true)
            .setDefaults(Notification.DEFAULT_ALL)
            .setWhen(System.currentTimeMillis())
            .setSmallIcon(R.drawable.ic_notification)
            .setTicker(context.resources.getString(R.string.app_name))
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setContentTitle(title)
            .setColor(ContextCompat.getColor(context, R.color.colorWhite))
            .setContentInfo("") //   .setStyle(new NotificationCompat.BigTextStyle(""))
            .setStyle(NotificationCompat.BigTextStyle().bigText(message))
            .setContentText(message)
            .setVibrate(longArrayOf(1000, 1000))
            .setOnlyAlertOnce(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
        notificationManager.notify(0, notificationBuilder.build())
    }


}