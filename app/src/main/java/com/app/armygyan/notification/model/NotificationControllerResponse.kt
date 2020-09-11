package com.app.armygyan.notification.model

import com.google.gson.annotations.SerializedName

data class NotificationControllerResponse(
    @SerializedName("status") var status: Boolean,
    @SerializedName("message") var message: String,
    @SerializedName("data") var notifications: NotificationData) {
    data class NotificationData(@SerializedName("id") var notiId: Long,
                            @SerializedName("user_id") var user_id: String,
                            @SerializedName("notification_status") var notificationStatus: String,
                            @SerializedName("created_at") var createdAt: String)

}