package com.app.quiz.studymaterial.model

import com.google.gson.annotations.SerializedName

data class StudyMaterialChapterData(
    @SerializedName("id") var id: Long,
    @SerializedName("category_id") var catId: String,
    @SerializedName("chapter_name") var chapterName: String,
    @SerializedName("description") var desc: String,
    @SerializedName("views_count") var viewsCount: String,
    @SerializedName("created_at") var createdAt: String



)


