package com.app.quiz.studymaterial.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.app.quiz.QuizApplication
import com.app.quiz.network.WebService
import com.app.quiz.studymaterial.ChapterDataSource
import com.app.quiz.studymaterial.ChapterDataSourceFactory
import com.app.quiz.studymaterial.model.StudyMaterialChapter


class ChapterViewModel (application: Application) : AndroidViewModel(application) {
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

         config = PagedList.Config.Builder()
            .setEnablePlaceholders(true)
            .setInitialLoadSizeHint(10)
            .setPageSize(10)
            .build()

    }


    fun fetchChapters() {
        itemDataSourceFactory = ChapterDataSourceFactory(webService,categoryId)
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
