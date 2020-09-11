package com.app.armygyan.quizz.viewmodel;

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.armygyan.quizz.model.QuestionSet

public class AnswerViewModel: ViewModel() {
    var quizDetail: MutableLiveData<List<QuestionSet>>

    init {
        quizDetail = MutableLiveData()
    }
}