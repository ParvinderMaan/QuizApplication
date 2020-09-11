package com.app.armygyan.studymaterial.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.app.armygyan.QuizApplication
import com.app.armygyan.annotation.Status
import com.app.armygyan.extra.WebResponse
import com.app.armygyan.helper.ErrorHandler
import com.app.armygyan.network.WebService
import com.app.armygyan.studymaterial.model.StudyMaterialChapterDetailResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChapterDetailViewModel(application: Application) : AndroidViewModel(application) {
    private var webService: WebService
     var isLoading: MutableLiveData<Boolean>
     var chapterId: Long? = 0
    var resultantChapterDetail: MutableLiveData<WebResponse<StudyMaterialChapterDetailResponse>>
    init {
        webService = (application as QuizApplication).getWebServiceInstance()
        isLoading = MutableLiveData()
        resultantChapterDetail = MutableLiveData()
    }

    fun fetchStudyMaterialChapterDetail(headers:HashMap<String, String>) {
        isLoading.value = true
        webService.fetchStudyMaterialChapterDetail(headers,chapterId).enqueue(object :
            Callback<StudyMaterialChapterDetailResponse> {
            override fun onResponse(call: Call<StudyMaterialChapterDetailResponse>, response: Response<StudyMaterialChapterDetailResponse>) {
                isLoading.value = false
                if (response.isSuccessful && response.body() != null) {
                    val result = response.body()
                    if (result!!.status) {
                        resultantChapterDetail.value = WebResponse(Status.SUCCESS, result, null)
                    } else {
                        resultantChapterDetail.value = WebResponse(Status.FAILURE, null, result.message)
                    }
                } else {
                    val errorMsg: String = ErrorHandler.reportError(response.code())
                    resultantChapterDetail.value = WebResponse(Status.FAILURE, null, errorMsg)
                }
            }

            override fun onFailure(call: Call<StudyMaterialChapterDetailResponse?>, error: Throwable) {
                isLoading.value = false
                val errorMsg: String? = ErrorHandler.reportError(error)
                resultantChapterDetail.value = WebResponse(Status.FAILURE, null, errorMsg)
            }
        })
    }


}
