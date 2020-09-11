package com.app.armygyan.miscellaneous.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.app.armygyan.QuizApplication
import com.app.armygyan.annotation.Status
import com.app.armygyan.extra.WebResponse
import com.app.armygyan.helper.ErrorHandler
import com.app.armygyan.miscellaneous.model.AboutUsResponse
import com.app.armygyan.network.WebService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.HashMap

class AboutUsViewModel (application: Application) : AndroidViewModel(application) {
    private var webService: WebService
     var isLoading: MutableLiveData<Boolean>
     var resultantAboutUs: MutableLiveData<WebResponse<AboutUsResponse>>

    init {
        webService = (application as QuizApplication).getWebServiceInstance()
        isLoading = MutableLiveData()
        resultantAboutUs = MutableLiveData()
    }

    fun fetchAboutUs(headerMap: HashMap<String, String>) {
        isLoading.value = true
        webService.fetchAboutUs(headerMap,1).enqueue(object : Callback<AboutUsResponse> {
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
                Log.e("onFailure About us --->"," "+error.printStackTrace())
                isLoading.value=false
                val errorMsg: String? = ErrorHandler.reportError(error)
                resultantAboutUs.value= WebResponse(Status.FAILURE, null, errorMsg)
            }
        })
    }


}
