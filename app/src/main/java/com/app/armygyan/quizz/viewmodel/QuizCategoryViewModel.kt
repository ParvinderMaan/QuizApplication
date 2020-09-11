package com.app.armygyan.quizz.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.app.armygyan.QuizApplication
import com.app.armygyan.network.WebService
import com.app.armygyan.quizz.QuizCategoryDataSource
import com.app.armygyan.quizz.QuizCategoryDataSourceFactory
import com.app.armygyan.quizz.model.QuizDetail
import java.util.HashMap


class QuizCategoryViewModel  (application: Application) : AndroidViewModel(application) {
    private var config: PagedList.Config
    var headers: HashMap<String, String>
    var isLoading: MutableLiveData<Boolean>
    private var webService: WebService
    var isListEmpty: MutableLiveData<Boolean>
    private lateinit var itemDataSourceFactory: QuizCategoryDataSourceFactory
    private lateinit var liveDataSource: LiveData<QuizCategoryDataSource>
    lateinit var userPagedList: LiveData<PagedList<QuizDetail>>



    init {
        webService = (application as QuizApplication).getWebServiceInstance()
        isLoading = MutableLiveData()
        isListEmpty = MutableLiveData()
        headers=HashMap<String, String>()


         config = PagedList.Config.Builder()
            .setEnablePlaceholders(true)
            .setInitialLoadSizeHint(10)
            .setPageSize(10)
            .build()



    }

    fun fetchAllQuiz() {
        itemDataSourceFactory = QuizCategoryDataSourceFactory(webService,headers)
        liveDataSource = itemDataSourceFactory.userLiveDataSource
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
