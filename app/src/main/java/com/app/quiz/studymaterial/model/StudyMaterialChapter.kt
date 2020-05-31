package com.app.quiz.studymaterial.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class StudyMaterialChapter (
    @SerializedName("id") var chapterId: String,
    @SerializedName("chapter_name") var chapterName: String,
    @SerializedName("topics") var topics: String,
    @SerializedName("category_id") var catId: String,
  //  @SerializedName("description") var description: String,
    @SerializedName("views_count") var viewCount: String,
    @SerializedName("created_at") var createdOn: String,
    @SerializedName("status") var status: String): Parcelable

/*
"id":1,
            "category_id":"1",
            "chapter_name":"कंप्यूटर इंजीनियरिंग की विभिन्न फ़ील्ड्स",
            "topics":"कंप्यूटर साइंस में विभिन्न कोर्सेज | डिप्लोमा कोर्सेज",
            "description":"<p>टिं</p>"
            "views_count":"0",
            "created_at":"2020-04-26 10:18:10",
            "updated_at":"2020-04-26 10:18:10"
 */