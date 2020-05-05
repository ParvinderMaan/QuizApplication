package com.app.quiz.signin.model

import com.google.gson.annotations.SerializedName

data class SignInResponse(@SerializedName("status") var status: Boolean,
                          @SerializedName("message") var message: String,
                          @SerializedName("data") var  data: Data,
                          @SerializedName("token") var token: String){

    data class Data(@SerializedName("id") var userId: Long,
                          @SerializedName("name") var name: String,
                          @SerializedName("email") var email: String,
                          @SerializedName("notificationStatus") var notificationStatus: String,
                          @SerializedName("status") var status: String)
}

