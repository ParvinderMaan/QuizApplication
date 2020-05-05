package com.app.quiz.quizz

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.app.quiz.network.WebService
import com.app.quiz.quizz.model.QuizDetail

class QuizCategoryDataSourceFactory (val webService: WebService) : DataSource.Factory<Long, QuizDetail>() {
    val userLiveDataSource = MutableLiveData<QuizCategoryDataSource>()

    override fun create(): DataSource<Long, QuizDetail> {
        var userDataSource = QuizCategoryDataSource(webService)
        userLiveDataSource.postValue(userDataSource)
        return userDataSource
    }




}