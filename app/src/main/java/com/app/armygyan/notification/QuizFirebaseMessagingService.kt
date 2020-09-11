package com.app.armygyan.notification

import android.util.Log
import com.app.armygyan.annotation.FragmentType
import com.app.armygyan.annotation.NotificationType
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class QuizFirebaseMessagingService : FirebaseMessagingService() {
    override fun onMessageReceived(p0: RemoteMessage) {
        Log.e("QuizService",""+p0.from)
        Log.e("QuizService",""+p0.data)
        sendNotification(p0.data)
    }

    private fun sendNotification(data: Map<String, String>) {
        // val title: String= resources.getString(R.string.app_name)
        val title = data["title"]
         val type = data["notification_type"]
         val message = data["message"]
        if (type == null && message ==null && title ==null) return
        when(type){
            NotificationType.QUIZ ->{
                if (title != null && message != null) {
                    NotificationBuilder(this).setTitle(title).setBody(message).show()
                }
            }
           NotificationType.CHAPTER ->{
                if (title != null && message != null) {
                    NotificationBuilder(this).setTitle(title).setBody(message).show()
                }
            }
            NotificationType.GENERAL ->{
                if (title != null && message != null) {
                    NotificationBuilder(this).setTitle(title).setBody(message).show()
                }
            }
        }
    }

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
    }

}