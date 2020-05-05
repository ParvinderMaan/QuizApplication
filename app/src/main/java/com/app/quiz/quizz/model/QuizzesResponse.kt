package com.app.quiz.quizz.model

import com.google.gson.annotations.SerializedName

data class QuizzesResponse(@SerializedName("status") var status: Boolean,
                           @SerializedName("message") var message: String,
                           @SerializedName("data") var data: QuizzesData){
    data class QuizzesData(@SerializedName("data") var quizDetail: List<QuizDetail>)
}