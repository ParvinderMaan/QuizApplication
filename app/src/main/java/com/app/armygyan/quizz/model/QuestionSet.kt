package com.app.armygyan.quizz.model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class QuestionSet(@SerializedName("id") var quesId:Int,
                       @SerializedName("question") var quesName:String,
                       @SerializedName("a") var optOne:String,
                       @SerializedName("b") var optTwo:String,
                       @SerializedName("c") var optThree:String,
                       @SerializedName("d") var optFour:String,
                       @SerializedName("answer") var ansOption:String,
                       @SerializedName("explanation") var explanation:String,
                       @Expose(serialize = false,deserialize = false) var isQuesActive:Boolean=false,
                       @Expose(serialize = false,deserialize = false) var isQuesAttempted:Boolean=false,
                       @Expose(serialize = false,deserialize = false) var isAnsSelected:Boolean=false,
                       @Expose(serialize = false,deserialize = false) var ansOptSelected:Int=0) : Parcelable {

    override fun toString(): String {
        return "QuestionSet(quesId=$quesId, quesName='$quesName', optOne='$optOne', optTwo='$optTwo', optThree='$optThree', optFour='$optFour', ansOption='$ansOption', isQuesAttempted=$isQuesAttempted, isAnsSelected=$isAnsSelected, ansOptSelected=$ansOptSelected)"
    }

}