package com.app.armygyan.quizz.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class QuizDetailResponse(
    @SerializedName("status") var status: Boolean,
    @SerializedName("message") var message: String,
    @SerializedName("data") var data: QuizData) {
    data class QuizData(
        @SerializedName("id") var id: Long,
        @SerializedName("noOfQues") var noOfQues: String,
        @SerializedName("level") var level: String,
        @SerializedName("duration") var duration: String,
        @SerializedName("quiz_name") var quizName: String,
        @SerializedName("topics") var topics: String,
        @SerializedName("description") var description: String,
        @SerializedName("view_count") var viewCount: String,
        @SerializedName("created_at") var createdOn: String,
        @SerializedName("questions") var quizQuestion: List<QuestionSet>)
}


