package com.app.quiz.notification.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.app.quiz.QuizApplication
import com.app.quiz.annotation.Status
import com.app.quiz.extra.WebResponse
import com.app.quiz.helper.ErrorHandler
import com.app.quiz.miscellaneous.model.AboutUsResponse
import com.app.quiz.miscellaneous.model.GeneralResponse
import com.app.quiz.network.WebService
import com.app.quiz.notification.model.NotificationResponse
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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

    fun fetchNotifications() {
        isLoading.value=true
        webService.fetchNotifications().enqueue(object : Callback<NotificationResponse> {
            override fun onResponse(call: Call<NotificationResponse>, response: Response<NotificationResponse>) {
                isLoading.value=false
                if (response.isSuccessful && response.body() != null) {
                    val result = response.body()
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
