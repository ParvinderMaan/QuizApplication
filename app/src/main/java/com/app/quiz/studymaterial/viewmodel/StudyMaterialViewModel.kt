package com.app.quiz.studymaterial.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.app.quiz.QuizApplication
import com.app.quiz.annotation.Status.FAILURE
import com.app.quiz.annotation.Status.SUCCESS
import com.app.quiz.extra.WebResponse
import com.app.quiz.helper.ErrorHandler
import com.app.quiz.miscellaneous.model.GeneralResponse
import com.app.quiz.network.WebService
import com.app.quiz.studymaterial.FavouriteRequest
import com.app.quiz.studymaterial.CategoryDataSource
import com.app.quiz.studymaterial.CategoryDataSourceFactory
import com.app.quiz.studymaterial.model.StudyMaterialCategory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StudyMaterialViewModel(application: Application) : AndroidViewModel(application) {
    private var config: PagedList.Config
    private var itemDataSourceFactory: CategoryDataSourceFactory
    private var webService: WebService
    var userId: MutableLiveData<Int>
    var categoryId: Long? =0
    var resultFavouriteCategory: MutableLiveData<WebResponse<GeneralResponse>>
    var liveDataSource: LiveData<CategoryDataSource>
    lateinit var userPagedList: LiveData<PagedList<StudyMaterialCategory>>
    var isLoading: MutableLiveData<Boolean>
    var isListEmpty: MutableLiveData<Boolean>


    init {
        webService = (application as QuizApplication).getWebServiceInstance()
        userId = MutableLiveData()
        resultFavouriteCategory = MutableLiveData()
        isLoading = MutableLiveData()
        isLoading.value=true  /// ??
        isListEmpty = MutableLiveData()
        itemDataSourceFactory = CategoryDataSourceFactory(webService)
        liveDataSource = itemDataSourceFactory.userLiveDataSource

         config = PagedList.Config.Builder()
            .setEnablePlaceholders(true)
            .setInitialLoadSizeHint(10)
            .setPageSize(10)
            .build()


    }

    fun fetchCategories() {
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

    fun isFavouriteCategory() {
       // isLoading.value=true
        webService.isFavouriteCategory(categoryId).enqueue(object : Callback<GeneralResponse> {
                override fun onResponse(call: Call<GeneralResponse>, response: Response<GeneralResponse>) {
                  //  isLoading.value = false
                    if (response.isSuccessful && response.body() != null) {
                        val result = response.body()
                        if (result != null) {
                            if (result.status) {
                                resultFavouriteCategory.value = WebResponse(SUCCESS, result, null)
                            } else {
                                resultFavouriteCategory.value = WebResponse(FAILURE, null, result.message)
                            }
                        }
                    } else {
                        val errorMsg: String = ErrorHandler.reportError(response.code())
                        resultFavouriteCategory.value = WebResponse(FAILURE, null, errorMsg)
                    }
                }

                 override fun onFailure(call: Call<GeneralResponse>, error: Throwable) {
                 //   isLoading.value = false
                    val errorMsg: String? = ErrorHandler.reportError(error)
                    Log.e("onFailure",""+error.printStackTrace())
                    resultFavouriteCategory.value = WebResponse(FAILURE, null, errorMsg)
                }
            })
    }


}
