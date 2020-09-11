package com.app.armygyan.studymaterial.model

import com.google.gson.annotations.SerializedName

data class StudyMaterialCategoryResponse(
    @SerializedName("status") var status: Boolean,
    @SerializedName("message") var message: String,
    @SerializedName("data") var categories: List<StudyMaterialCategory> ){

  //  data class StudyMaterialCategoryData(@SerializedName("data") var categories: List<StudyMaterialCategory>)

}


