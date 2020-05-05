package com.app.quiz.studymaterial.model

import com.google.gson.annotations.SerializedName

data class StudyMaterialCategoryResponse(
    @SerializedName("status") var status: Boolean, @SerializedName("message") var message: String,
    @SerializedName("data") var data: StudyMaterialCategoryData){

    data class StudyMaterialCategoryData(@SerializedName("data") var categories: List<StudyMaterialCategory>)

}


