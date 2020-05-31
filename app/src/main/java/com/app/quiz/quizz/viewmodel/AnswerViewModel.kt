package com.app.quiz.quizz.viewmodel;

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.quiz.quizz.model.QuestionSet

public class AnswerViewModel: ViewModel() {
    var quizDetail: MutableLiveData<List<QuestionSet>>

    init {
        quizDetail = MutableLiveData()
    }
}
