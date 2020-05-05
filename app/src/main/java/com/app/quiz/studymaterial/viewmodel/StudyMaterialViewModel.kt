package com.app.quiz.studymaterial.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.app.quiz.QuizApplication
import com.app.quiz.extra.WebResponse
import com.app.quiz.miscellaneous.model.GeneralResponse
import com.app.quiz.network.WebService
import com.app.quiz.studymaterial.FavouriteRequest
import com.app.quiz.studymaterial.CategoryDataSource
import com.app.quiz.studymaterial.CategoryDataSourceFactory
import com.app.quiz.studymaterial.model.StudyMaterialCategory

class StudyMaterialViewModel(application: Application) : AndroidViewModel(application) {
    private var itemDataSourceFactory: CategoryDataSourceFactory
    private var webService: WebService
    var userId: MutableLiveData<Int>
    var favouriteRequest: MutableLiveData<FavouriteRequest>
    var resultFavouriteCategory: MutableLiveData<WebResponse<GeneralResponse>>
    var liveDataSource: LiveData<CategoryDataSource>
    var userPagedList: LiveData<PagedList<StudyMaterialCategory>>
    var isLoading: MutableLiveData<Boolean>
    var isListEmpty: MutableLiveData<Boolean>


    init {
        webService = (application as QuizApplication).getWebServiceInstance()
        userId = MutableLiveData()
        resultFavouriteCategory = MutableLiveData()
        favouriteRequest = MutableLiveData()
        isLoading = MutableLiveData()
        isLoading.value=true
        isListEmpty = MutableLiveData()
        itemDataSourceFactory = CategoryDataSourceFactory(webService)
        liveDataSource = itemDataSourceFactory.userLiveDataSource

        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(true)
            .setInitialLoadSizeHint(10)
            .setPageSize(10)
            .build()

        userPagedList = LivePagedListBuilder(itemDataSourceFactory, config)
            .setBoundaryCallback(object : PagedList.BoundaryCallback<StudyMaterialCategory>() {
                override fun onZeroItemsLoaded() {
                    super.onZeroItemsLoaded()
                    // Handle empty initial load here
                    isListEmpty.value=true
                    isLoading.value=false
                }

                override fun onItemAtEndLoaded(itemAtEnd: StudyMaterialCategory) {
                    super.onItemAtEndLoaded(itemAtEnd)
                    // Here you can listen to last item on list

                }

                override fun onItemAtFrontLoaded(itemAtFront: StudyMaterialCategory) {
                    super.onItemAtFrontLoaded(itemAtFront)
                    // Here you can listen to first item on list
                    isLoading.value=false
                }
            })
            .build()
    }


/*
    fun isFavouriteCategory() {
        isLoading.setValue(true)
        webService.isFavouriteCategory(favouriteRequest.value)
            .enqueue(object : Callback<GeneralResponse> {
                override fun onResponse(
                    call: Call<GeneralResponse>,
                    response: Response<GeneralResponse>
                ) {
                    isLoading.value = true
                    if (response.isSuccessful && response.body() != null) {
                        val result = response.body()
                        if (result!!.status) {
                            resultFavouriteCategory.value = WebResponse(SUCCESS, result, null)
                        } else {
                            resultFavouriteCategory.value =
                                WebResponse(FAILURE, null, result.message)
                        }
                    } else {
                        val errorMsg: String = ErrorHandler.reportError(response.code())
                        resultFavouriteCategory.value = WebResponse(FAILURE, null, errorMsg)
                    }
                }

                override fun onFailure(call: Call<GeneralResponse>, error: Throwable) {
                    isLoading.value = false
                    val errorMsg: String? = ErrorHandler.reportError(error)
                    resultFavouriteCategory.value = WebResponse(FAILURE, null, errorMsg)
                }
            })
    }

 */
}
