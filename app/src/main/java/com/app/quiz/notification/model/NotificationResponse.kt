package com.app.quiz.notification.model

import com.google.gson.annotations.SerializedName

data class NotificationResponse(
    @SerializedName("status") var status: Boolean, @SerializedName("message") var message: String,
    @SerializedName("data") var notifications: List<Notification>
) {
    data class Notification(@SerializedName("id") var notiId: Long,
                            @SerializedName("notification_type") var notification_type: String,
                            @SerializedName("content") var content: String,
                            @SerializedName("created_at") var createdOn: String)

}