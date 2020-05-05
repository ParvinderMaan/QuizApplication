package com.app.quiz.miscellaneous.model

import com.google.gson.annotations.SerializedName

data class TermAndConditionResponse (@SerializedName("status") var status: Boolean,
                                     @SerializedName("message") var message: String,
                                     @SerializedName("data") var data: List<TermAndConditionInfo>) {

    data class TermAndConditionInfo(@SerializedName("description") var termsAndCondition: String )

}