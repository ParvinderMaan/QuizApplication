package com.app.quiz.quizz.model

import com.google.gson.annotations.SerializedName

data class QuizDetailResponse(
    @SerializedName("status") var status: Boolean, @SerializedName("message") var message: String,
    @SerializedName("data") var data: QuizData
) {
    data class QuizData(
        @SerializedName("noOfQues") var noOfQues: String,
        @SerializedName("level") var level: String,
        @SerializedName("duration") var duration: String,
        @SerializedName("quizName") var quizName: String,
        @SerializedName("quizData") var quizDetail: List<QuestionSet>
    )
}


