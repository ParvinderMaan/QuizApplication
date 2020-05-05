package com.app.quiz.miscellaneous.model

import com.google.gson.annotations.SerializedName

data class NotificationStatusRequest(@SerializedName("userId") var userId:String,
                                     @SerializedName("notificationStatus") var notificationStatus:String)


