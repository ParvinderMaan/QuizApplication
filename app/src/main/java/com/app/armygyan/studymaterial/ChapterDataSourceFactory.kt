package com.app.armygyan.studymaterial

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.app.armygyan.network.WebService
import com.app.armygyan.studymaterial.model.StudyMaterialChapter
import java.util.HashMap

class ChapterDataSourceFactory (val webService: WebService, var headers: HashMap<String, String>, private var categoryId: Long) : DataSource.Factory<Long, StudyMaterialChapter>() {
    val userLiveDataSource = MutableLiveData<ChapterDataSource>()

    override fun create(): DataSource<Long, StudyMaterialChapter> {
        var userDataSource = ChapterDataSource(webService,headers,categoryId)
        userLiveDataSource.postValue(userDataSource)
        return userDataSource
    }




}