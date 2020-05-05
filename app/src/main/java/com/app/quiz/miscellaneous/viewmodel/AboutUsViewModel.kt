package com.app.quiz.miscellaneous.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.app.quiz.QuizApplication
import com.app.quiz.annotation.Status
import com.app.quiz.extra.WebResponse
import com.app.quiz.helper.ErrorHandler
import com.app.quiz.miscellaneous.model.AboutUsResponse
import com.app.quiz.network.WebService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AboutUsViewModel (application: Application) : AndroidViewModel(application) {
    private var webService: WebService
     var isLoading: MutableLiveData<Boolean>
     var resultantAboutUs: MutableLiveData<WebResponse<AboutUsResponse>>

    init {
        webService = (application as QuizApplication).getWebServiceInstance()
        isLoading = MutableLiveData()
        resultantAboutUs = MutableLiveData()
    }

    fun fetchAboutUs() {
        isLoading.value = true
        webService.fetchAboutUs(1).enqueue(object : Callback<AboutUsResponse> {
            override fun onResponse(call: Call<AboutUsResponse>, response: Response<AboutUsResponse>) {
                isLoading.value=false
                if (response.isSuccessful && response.body() != null) {
                    val result = response.body()
                    if (result!!.status) {
                        resultantAboutUs.value= WebResponse(Status.SUCCESS, result, null)
                    } else {
                        resultantAboutUs.value= WebResponse(Status.FAILURE, null, result.message)
                    }
                } else {
                    val errorMsg: String = ErrorHandler.reportError(response.code())
                    resultantAboutUs.value= WebResponse(Status.FAILURE, null, errorMsg)
                }
            }

            override fun onFailure(call: Call<AboutUsResponse>, error: Throwable) {
                isLoading.value=false
                val errorMsg: String? = ErrorHandler.reportError(error)
                resultantAboutUs.value= WebResponse(Status.FAILURE, null, errorMsg)
            }
        })
    }


}
