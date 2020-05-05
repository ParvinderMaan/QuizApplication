package com.app.quiz.profile.model

import com.google.gson.annotations.SerializedName

data class ProfileInfo(
    @SerializedName("userId") var userId: String,
    @SerializedName("name") var name: String,
    @SerializedName("email") var email: String,
    @SerializedName("profilePic") var profilePic: String,
    @SerializedName("userType") var userType: String,
    @SerializedName("gender") var gender: String
)


