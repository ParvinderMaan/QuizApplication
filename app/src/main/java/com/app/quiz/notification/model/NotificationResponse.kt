package com.app.quiz.notification.model

import com.google.gson.annotations.SerializedName

data class NotificationResponse(
    @SerializedName("status") var status: Boolean, @SerializedName("message") var message: String,
    @SerializedName("data") var data: NotificationData
) {
    data class NotificationData(@SerializedName("notifications") var notifications: List<Notification>)
    data class Notification(@SerializedName("notiId") var notiId: String,
                            @SerializedName("notiName") var notiName: String,
                            @SerializedName("createdOn") var createdOn: String)

}