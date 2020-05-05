package com.app.quiz.studymaterial

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.app.quiz.network.WebService
import com.app.quiz.studymaterial.model.StudyMaterialCategory


class CategoryDataSourceFactory(val webService: WebService) : DataSource.Factory<Long, StudyMaterialCategory>() {
       val userLiveDataSource = MutableLiveData<CategoryDataSource>()

    override fun create(): DataSource<Long, StudyMaterialCategory> {
        var userDataSource = CategoryDataSource(webService)
        userLiveDataSource.postValue(userDataSource)
         return userDataSource
    }




}