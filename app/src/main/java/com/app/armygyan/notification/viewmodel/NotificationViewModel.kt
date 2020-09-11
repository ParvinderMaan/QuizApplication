package com.app.armygyan.notification.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.app.armygyan.QuizApplication
import com.app.armygyan.annotation.Status
import com.app.armygyan.extra.WebResponse
import com.app.armygyan.helper.ErrorHandler
import com.app.armygyan.miscellaneous.model.AboutUsResponse
import com.app.armygyan.miscellaneous.model.GeneralResponse
import com.app.armygyan.network.WebService
import com.app.armygyan.notification.model.NotificationResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.HashMap

class NotificationViewModel (application: Application) : AndroidViewModel(application) {
    private var webService: WebService
     var isLoading: MutableLiveData<Boolean>
     var resultAllNotification: MutableLiveData<WebResponse<NotificationResponse>>
     var lstOfNotifications: MutableLiveData<List<NotificationResponse.Notification>>

    init {
        webService = (application as QuizApplication).getWebServiceInstance()
        isLoading = MutableLiveData()
        lstOfNotifications= MutableLiveData()
        resultAllNotification = MutableLiveData()
    }

    fun fetchNotifications(headerMap: HashMap<String, String>) {
        isLoading.value=true
        webService.fetchNotifications(headerMap).enqueue(object : Callback<NotificationResponse> {
            override fun onResponse(call: Call<NotificationResponse>, response: Response<NotificationResponse>) {
                isLoading.value=false
                if (response.isSuccessful && response.body() != null) {
                    val result = response.body()
                    Log.e("pm",response.body().toString());

                    if (result!!.status) {
                        resultAllNotification.value= WebResponse(Status.SUCCESS, result, null)
                    } else {
                        resultAllNotification.value= WebResponse(Status.FAILURE, null, result.message)
                    }
                } else {
                    val errorMsg: String = ErrorHandler.reportError(response.code())
                    resultAllNotification.value= WebResponse(Status.FAILURE, null, errorMsg)
                }
            }

            override fun onFailure(call: Call<NotificationResponse>, error: Throwable) {
                isLoading.value=false
                val errorMsg: String? = ErrorHandler.reportError(error)
                resultAllNotification.value= WebResponse(Status.FAILURE, null, errorMsg)
            }
        })
    }



}
