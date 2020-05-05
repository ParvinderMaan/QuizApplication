package com.app.quiz.miscellaneous.model

import com.google.gson.annotations.SerializedName


data class GeneralResponse(@SerializedName("status") var status: Boolean, @SerializedName("message") var message: String)