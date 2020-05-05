package com.app.quiz.studymaterial.model

import com.google.gson.annotations.SerializedName

data class StudyMaterialChaptersResponse(@SerializedName("status") var status: Boolean,
                                         @SerializedName("message") var message: String,
                                         @SerializedName("data") var data: StudyMaterialChaptersData){
data class StudyMaterialChaptersData(@SerializedName("data") var chapters: List<StudyMaterialChapter>)

}