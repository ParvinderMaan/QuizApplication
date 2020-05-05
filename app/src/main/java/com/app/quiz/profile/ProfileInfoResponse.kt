package com.app.quiz.profile

import com.app.quiz.profile.model.ProfileInfo
import com.google.gson.annotations.SerializedName

data class ProfileInfoResponse(@SerializedName("status") var status: Boolean, @SerializedName("message") var message: String,
                               @SerializedName("data") var data: ProfileInfo)