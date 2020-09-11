package com.app.armygyan.studymaterial.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.app.armygyan.QuizApplication
import com.app.armygyan.annotation.Status
import com.app.armygyan.annotation.Status.FAILURE
import com.app.armygyan.annotation.Status.SUCCESS
import com.app.armygyan.extra.WebResponse
import com.app.armygyan.helper.ErrorHandler
import com.app.armygyan.miscellaneous.model.GeneralResponse
import com.app.armygyan.network.WebService
import com.app.armygyan.studymaterial.model.StudyMaterialCategory
import com.app.armygyan.studymaterial.model.StudyMaterialCategoryResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.HashMap

class StudyMaterialViewModel(application: Application) : AndroidViewModel(application) {
    private var webService: WebService
    var userId: MutableLiveData<Int>
    var categoryId: Long? = 0
    var resultFavouriteCategory: MutableLiveData<WebResponse<GeneralResponse>>
    var isLoading: MutableLiveData<Boolean>
    var isListEmpty: MutableLiveData<Boolean>
    var resultantStudyMaterialCategory: MutableLiveData<WebResponse<StudyMaterialCategoryResponse>>
    var lstOfCategories:MutableLiveData<List<StudyMaterialCategory>>

    init {
        webService = (application as QuizApplication).getWebServiceInstance()
        userId = MutableLiveData()
        resultFavouriteCategory = MutableLiveData()
        isLoading = MutableLiveData()
        isListEmpty = MutableLiveData()
        resultantStudyMaterialCategory= MutableLiveData()
        lstOfCategories= MutableLiveData()
    }

    fun isFavouriteCategory(headers: HashMap<String, String>) {
        webService.isFavouriteCategory(headers,categoryId).enqueue(object : Callback<GeneralResponse> {
            override fun onResponse(
                call: Call<GeneralResponse>,
                response: Response<GeneralResponse>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    val result = response.body()
                    if (result!!.status) {
                            resultFavouriteCategory.value = WebResponse(SUCCESS, result, null)
                        } else {
                            resultFavouriteCategory.value = WebResponse(FAILURE, null, result.message)
                    }
                } else {
                    val errorMsg: String = ErrorHandler.reportError(response.code())
                    resultFavouriteCategory.value = WebResponse(FAILURE, null, errorMsg)
                }
            }

            override fun onFailure(call: Call<GeneralResponse>, error: Throwable) {
                val errorMsg: String? = ErrorHandler.reportError(error)
                Log.e("onFailure", "" + error.printStackTrace())
                resultFavouriteCategory.value = WebResponse(FAILURE, null, errorMsg)
            }
        })
    }

    fun fetchCategories(headerMap: HashMap<String, String>) {
        isLoading.value=true
        webService.fetchStudyMaterialCategories(headerMap).enqueue(object :
            Callback<StudyMaterialCategoryResponse> {
            override fun onResponse(
                call: Call<StudyMaterialCategoryResponse>,
                response: Response<StudyMaterialCategoryResponse>
            ) {
                isLoading.value = false

                if (response.isSuccessful && response.body() != null) {
                    val result = response.body()
                    if (result!!.status) {
                        resultantStudyMaterialCategory.value = WebResponse(Status.SUCCESS, result, null)
                    } else {
                        resultantStudyMaterialCategory.value = WebResponse(Status.FAILURE, null, result.message)
                    }
                } else {
                    val errorMsg: String = ErrorHandler.reportError(response.code())
                    resultantStudyMaterialCategory.value = WebResponse(Status.FAILURE, null, errorMsg)
                }
            }

            override fun onFailure(call: Call<StudyMaterialCategoryResponse>, error: Throwable) {
                isLoading.value = false
             //   Log.e("onFailure ....", "" + error.printStackTrace())
                val errorMsg: String? = ErrorHandler.reportError(error)
                resultantStudyMaterialCategory.value = WebResponse(Status.FAILURE, null, errorMsg)
                //...................missing !!!
            }
        });


    }

}