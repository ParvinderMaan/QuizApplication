package com.app.quiz.quizz.model

import com.google.gson.annotations.SerializedName


data class QuestionSet(@SerializedName("quesId") var quesId:Int,
                       @SerializedName("quesName") var quesName:String,
                       @SerializedName("optOne") var optOne:String,
                       @SerializedName("optTwo") var optTwo:String,
                       @SerializedName("optThree") var optThree:String,
                       @SerializedName("optFour") var optFour:String,
                       @SerializedName("ansOption") var ansOption:Int,
                       @SerializedName("isQuesAttempted") var isQuesAttempted:Boolean,
                       @SerializedName("isAnsSelected") var isAnsSelected:Boolean,
                       @SerializedName("ansOptSelected") var ansOptSelected:Int=0)