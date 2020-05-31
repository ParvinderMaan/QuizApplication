package com.app.quiz.miscellaneous.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.app.quiz.QuizApplication
import com.app.quiz.annotation.Status
import com.app.quiz.extra.WebResponse
import com.app.quiz.helper.ErrorHandler
import com.app.quiz.miscellaneous.model.GeneralResponse
import com.app.quiz.miscellaneous.model.NotificationStatusRequest
import com.app.quiz.network.WebService
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.HeaderMap
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class SettingViewModel (application: Application) : AndroidViewModel(application) {
     private var webService: WebService
     var isLoading: MutableLiveData<Boolean>
     var input:MutableLiveData<NotificationStatusRequest>
     var resultNotificationStatus: MutableLiveData<WebResponse<GeneralResponse>>
     var resultSignOut: MutableLiveData<WebResponse<GeneralResponse>>
     var headerMap:Map<String, String>
    init {
        webService = (application as QuizApplication).getWebServiceInstance()
        isLoading = MutableLiveData()
        input = MutableLiveData()
        resultNotificationStatus = MutableLiveData()
        resultSignOut = MutableLiveData()
        headerMap=HashMap()
    }

    fun alterNotificationStatus() {
        isLoading.setValue(true)
        webService.alterNotificationStatus(input.value).enqueue(object : Callback<GeneralResponse> {
            override fun onResponse(call: Call<GeneralResponse>, response: Response<GeneralResponse>) {
                isLoading.value=false
                if (response.isSuccessful && response.body() != null) {
                    val result = response.body()
                    if (result!!.status) {
                        resultNotificationStatus.value= WebResponse(Status.SUCCESS, result, null)
                    } else {
                        resultNotificationStatus.value= WebResponse(Status.FAILURE, null, result.message)
                    }
                } else {
                    val errorMsg: String = ErrorHandler.reportError(response.code())
                    resultNotificationStatus.value= WebResponse(Status.FAILURE, null, errorMsg)
                }
            }

            override fun onFailure(call: Call<GeneralResponse>, error: Throwable) {
                isLoading.value=false
                val errorMsg: String? = ErrorHandler.reportError(error)
                resultNotificationStatus.value= WebResponse(Status.FAILURE, null, errorMsg)
            }
        })
    }
     fun attemptSignOut() {
         isLoading.value=true
        webService.attemptSignOut(headerMap).enqueue(object : Callback<GeneralResponse> {
            override fun onResponse(call: Call<GeneralResponse>, response: Response<GeneralResponse>) {
                isLoading.value=false
                if (response.isSuccessful && response.body() != null) {
                    val result = response.body()
                    if (result!!.status) {
                       resultSignOut.value= WebResponse(Status.SUCCESS, result, null)
                    } else {
                       resultSignOut.value= WebResponse(Status.FAILURE, null, result.message)
                    }
                } else {
                    val errorMsg: String = ErrorHandler.reportError(response.code())
                    resultSignOut.value= WebResponse(Status.FAILURE, null, errorMsg)

                }
            }

            override fun onFailure(call: Call<GeneralResponse>, error: Throwable) {
                isLoading.value=false
                Log.e("onFailure", error.printStackTrace().toString())
                val errorMsg: String? = ErrorHandler.reportError(error)
                resultSignOut.value= WebResponse(Status.FAILURE, null, errorMsg)
            }
        })
    }


}
