package com.app.quiz.quizz.model

import com.google.gson.annotations.SerializedName

data class QuizDetail(
    @SerializedName("id") var id: Long,
    @SerializedName("quiz_name") var name: String,
    @SerializedName("topics") var topics: String,
    @SerializedName("level") var level: String,
    @SerializedName("view_count") var viewCount: String,
    @SerializedName("created_at") var createdOn: String,
    @SerializedName("quizStatus") var quizStatus: String
)


