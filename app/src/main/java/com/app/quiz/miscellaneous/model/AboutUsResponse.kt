package com.app.quiz.miscellaneous.model

import com.google.gson.annotations.SerializedName

data class AboutUsResponse(@SerializedName("status") var status: Boolean,
                           @SerializedName("message") var message: String,
                           @SerializedName("data") var data: List<AboutUsInfo>){

    data class AboutUsInfo(@SerializedName("description") var aboutUs: String )

}