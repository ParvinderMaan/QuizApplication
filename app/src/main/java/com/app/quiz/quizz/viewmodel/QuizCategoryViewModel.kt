package com.app.quiz.quizz.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.app.quiz.QuizApplication
import com.app.quiz.network.WebService
import com.app.quiz.quizz.QuizCategoryDataSource
import com.app.quiz.quizz.QuizCategoryDataSourceFactory
import com.app.quiz.quizz.model.QuizDetail


class QuizCategoryViewModel  (application: Application) : AndroidViewModel(application) {
    var isLoading: MutableLiveData<Boolean>
    private var webService: WebService
    var isListEmpty: MutableLiveData<Boolean>
    private var itemDataSourceFactory: QuizCategoryDataSourceFactory
    private var liveDataSource: LiveData<QuizCategoryDataSource>
    var userPagedList: LiveData<PagedList<QuizDetail>>


    init {
        webService = (application as QuizApplication).getWebServiceInstance()
        isLoading = MutableLiveData()
        isListEmpty = MutableLiveData()
        itemDataSourceFactory = QuizCategoryDataSourceFactory(webService)
        liveDataSource = itemDataSourceFactory.userLiveDataSource

        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(true)
            .setInitialLoadSizeHint(10)
            .setPageSize(10)
            .build()

        userPagedList = LivePagedListBuilder(itemDataSourceFactory, config)
            .setBoundaryCallback(object : PagedList.BoundaryCallback<QuizDetail>() {
                override fun onZeroItemsLoaded() {
                    super.onZeroItemsLoaded()
                    // Handle empty initial load here
                    isListEmpty.value=true
                    isLoading.value=false
                }

                override fun onItemAtEndLoaded(itemAtEnd: QuizDetail) {
                    super.onItemAtEndLoaded(itemAtEnd)
                    // Here you can listen to last item on list

                }

                override fun onItemAtFrontLoaded(itemAtFront: QuizDetail) {
                    super.onItemAtFrontLoaded(itemAtFront)
                    // Here you can listen to first item on list
                    isLoading.value=false
                }
            })
            .build()

    }

}
