package com.app.quiz.quizz.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Score(var totQues:Int,var totQuesAttempted: Int,var totAnsCorrect:Int,var totQuesWrong:Int):Parcelable


