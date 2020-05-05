package com.app.quiz.studymaterial.model

import com.google.gson.annotations.SerializedName

data class StudyMaterialCategory(@SerializedName("id") var id:Long,
                                 @SerializedName("category_name") var catName:String,
                                 @SerializedName("description") var catDesc:String,
                                 @SerializedName("status") var status :String,
                                 @SerializedName("created_at") var createdAt :String,
                                 @SerializedName("chapters_count") var chapterCount:String,
                                 @SerializedName("isFavourite") var isFavourite:String)

/*

  "id":1,
                "category_name":"कंप्यूटर विज्ञान",
                "description":"<p style=\"text-align: left;\"><a href=\"https://www.nayaseekhon.com/web-designing-kya-hai-hindi/\" target=\"_blank\" rel=\"noreferrer noopener\" aria-label=\"वेब डिजाइनिंग क्या है  (opens in a new tab)\">वेब डिजाइनिंग क्या है |</a></p>\r\n<p style=\"text-align: left;\"><a href=\"https://www.nayaseekhon.com/ip-address-kya-hai-hindi/\" target=\"_blank\" rel=\"noreferrer noopener\" aria-label=\"आईपी एड्रेस कैसे पता करे (opens in a new tab)\">आईपी एड्रेस कैसे पता करे |</a></p>\r\n<p style=\"text-align: left;\"><a href=\"https://www.nayaseekhon.com/output-device-kya-hai-hindi/\">कंप्यूटर के आउटपुट डिवाइस |</a></p>",
                "status":"1",
                "created_at":"2020-04-26 10:11:59",
                "updated_at":"2020-04-26 10:11:59",
                "chapters_count":"1"

 */