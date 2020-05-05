package com.app.quiz.studymaterial

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.app.quiz.network.WebService
import com.app.quiz.studymaterial.model.StudyMaterialChapter

class ChapterDataSourceFactory (val webService: WebService) : DataSource.Factory<Long, StudyMaterialChapter>() {
    val userLiveDataSource = MutableLiveData<ChapterDataSource>()

    override fun create(): DataSource<Long, StudyMaterialChapter> {
        var userDataSource = ChapterDataSource(webService)
        userLiveDataSource.postValue(userDataSource)
        return userDataSource
    }




}