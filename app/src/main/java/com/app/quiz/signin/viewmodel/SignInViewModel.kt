package com.app.quiz.signin.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.app.quiz.QuizApplication
import com.app.quiz.annotation.Status.FAILURE
import com.app.quiz.annotation.Status.SUCCESS
import com.app.quiz.extra.WebResponse
import com.app.quiz.helper.ErrorHandler
import com.app.quiz.network.WebService
import com.app.quiz.signin.model.SignInRequest
import com.app.quiz.signin.model.SignInResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class SignInViewModel(application: Application) : AndroidViewModel(application) {
    private var webService: WebService
      var isLoading: MutableLiveData<Boolean>
      var signInRequest:SignInRequest
      var resultantSignIn: MutableLiveData<WebResponse<SignInResponse>>
      var headerMap:Map<String, String>
    init {
        webService = (application as QuizApplication).getWebServiceInstance()
        isLoading = MutableLiveData()
        signInRequest = SignInRequest()
        headerMap=HashMap()
        resultantSignIn = MutableLiveData()
    }

    fun attemptSignIn() {
        isLoading.value = true
        webService.attemptSignIn(headerMap,signInRequest.name,signInRequest.email,signInRequest.deviceId).enqueue(object : Callback<SignInResponse> {
            override fun onResponse(call: Call<SignInResponse>, response: Response<SignInResponse>) {
                isLoading.value=false
                if (response.isSuccessful && response.body() != null) {
                    val result = response.body()
                    if (result!!.status) {
                        resultantSignIn.value=WebResponse(SUCCESS, result, null)
                    } else {
                        resultantSignIn.value=WebResponse(FAILURE, null, result.message)
                    }
                } else {
                    val errorMsg: String = ErrorHandler.reportError(response.code())
                    resultantSignIn.value=WebResponse(FAILURE, null, errorMsg)
                }
            }

            override fun onFailure(call: Call<SignInResponse?>, error: Throwable) {
                isLoading.value=false
                val errorMsg: String? = ErrorHandler.reportError(error)
                resultantSignIn.value=WebResponse(FAILURE, null, errorMsg)
            }
        })
    }


}
