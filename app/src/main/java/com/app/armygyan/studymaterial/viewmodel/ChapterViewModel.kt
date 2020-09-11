package com.app.armygyan.studymaterial.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.app.armygyan.QuizApplication
import com.app.armygyan.network.WebService
import com.app.armygyan.studymaterial.ChapterDataSource
import com.app.armygyan.studymaterial.ChapterDataSourceFactory
import com.app.armygyan.studymaterial.model.StudyMaterialChapter
import java.util.HashMap


class ChapterViewModel (application: Application) : AndroidViewModel(application) {
    var headers: HashMap<String, String>
    private var config: PagedList.Config
    private var webService: WebService
     var isLoading: MutableLiveData<Boolean>
     var userId: MutableLiveData<Int>
     var categoryId: Long = 0
     var isListEmpty: MutableLiveData<Boolean>
     private lateinit var itemDataSourceFactory: ChapterDataSourceFactory
     private lateinit var liveDataSource: LiveData<ChapterDataSource>
     lateinit var userPagedList: LiveData<PagedList<StudyMaterialChapter>>


    init {
        webService = (application as QuizApplication).getWebServiceInstance()
        isLoading = MutableLiveData()
        userId = MutableLiveData()
        isListEmpty = MutableLiveData()
        headers=HashMap<String, String>()
        config = PagedList.Config.Builder()
            .setEnablePlaceholders(true)
            .setInitialLoadSizeHint(10)
            .setPageSize(10)
            .build()

    }


    fun fetchChapters() {
        itemDataSourceFactory = ChapterDataSourceFactory(webService,headers,categoryId)
        liveDataSource = itemDataSourceFactory.userLiveDataSource
        userPagedList = LivePagedListBuilder(itemDataSourceFactory, config)
            .setBoundaryCallback(object : PagedList.BoundaryCallback<StudyMaterialChapter>() {
                override fun onZeroItemsLoaded() {
                    super.onZeroItemsLoaded()
                    // Handle empty initial load here
                    isListEmpty.value=true
                    isLoading.value=false
                }

                override fun onItemAtEndLoaded(itemAtEnd: StudyMaterialChapter) {
                    super.onItemAtEndLoaded(itemAtEnd)
                    // Here you can listen to last item on list

                }

                override fun onItemAtFrontLoaded(itemAtFront: StudyMaterialChapter) {
                    super.onItemAtFrontLoaded(itemAtFront)
                    // Here you can listen to first item on list
                    isLoading.value=false
                }
            })
            .build()
    }




}
