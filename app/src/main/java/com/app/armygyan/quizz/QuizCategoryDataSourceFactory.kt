package com.app.armygyan.quizz

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.app.armygyan.network.WebService
import com.app.armygyan.quizz.model.QuizDetail
import java.util.HashMap

class QuizCategoryDataSourceFactory (val webService: WebService,var headers: HashMap<String, String>) : DataSource.Factory<Long, QuizDetail>() {
    val userLiveDataSource = MutableLiveData<QuizCategoryDataSource>()

    override fun create(): DataSource<Long, QuizDetail> {
        var userDataSource = QuizCategoryDataSource(webService,headers)
        userLiveDataSource.postValue(userDataSource)
        return userDataSource
    }




}