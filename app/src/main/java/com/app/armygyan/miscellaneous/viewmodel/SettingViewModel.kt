package com.app.armygyan.miscellaneous.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.app.armygyan.QuizApplication
import com.app.armygyan.annotation.Status
import com.app.armygyan.extra.WebResponse
import com.app.armygyan.helper.ErrorHandler
import com.app.armygyan.miscellaneous.model.GeneralResponse
import com.app.armygyan.miscellaneous.model.NotificationStatusRequest
import com.app.armygyan.network.WebService
import com.app.armygyan.notification.model.NotificationControllerResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.net.ssl.HttpsURLConnection

class SettingViewModel (application: Application) : AndroidViewModel(application) {
     private var webService: WebService
     var isLoading: MutableLiveData<Boolean>
     var input:MutableLiveData<NotificationStatusRequest>
     var resultNotificationStatus: MutableLiveData<WebResponse<NotificationControllerResponse>>
     var resultSignOut: MutableLiveData<WebResponse<GeneralResponse>>
    init {
        webService = (application as QuizApplication).getWebServiceInstance()
        isLoading = MutableLiveData()
        input = MutableLiveData()
        resultNotificationStatus = MutableLiveData()
        resultSignOut = MutableLiveData()
    }

    fun alterNotificationStatus(headerMap: HashMap<String, String>) {
        isLoading.value = true
        webService.alterNotificationStatus(headerMap).enqueue(object : Callback<NotificationControllerResponse> {
            override fun onResponse(call: Call<NotificationControllerResponse>, response: Response<NotificationControllerResponse>) {
                isLoading.value=false
                Log.e("Response :",response.body().toString())
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

            override fun onFailure(call: Call<NotificationControllerResponse>, error: Throwable) {
                Log.e("onFailure Setting  --->"," "+error.printStackTrace())
                isLoading.value=false
                val errorMsg: String? = ErrorHandler.reportError(error)
                resultNotificationStatus.value= WebResponse(Status.FAILURE, null, errorMsg)
            }
        })
    }
     fun attemptSignOut(headerMap: HashMap<String, String>) {
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
                    if (response.code() ==  HttpsURLConnection.HTTP_UNAUTHORIZED) {
                        resultSignOut.value = WebResponse(Status.SUCCESS, GeneralResponse(true,"success"), null)
                    }
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
