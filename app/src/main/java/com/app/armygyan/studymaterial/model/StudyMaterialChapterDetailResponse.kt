package com.app.armygyan.studymaterial.model

import com.google.gson.annotations.SerializedName

data class StudyMaterialChapterDetailResponse(@SerializedName("status") var status: Boolean,
                                              @SerializedName("message") var message: String,
                                              @SerializedName("data") var data: StudyMaterialChapterData
)