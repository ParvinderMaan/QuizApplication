package com.app.armygyan.signin.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.app.armygyan.QuizApplication
import com.app.armygyan.annotation.Status.FAILURE
import com.app.armygyan.annotation.Status.SUCCESS
import com.app.armygyan.extra.WebResponse
import com.app.armygyan.helper.ErrorHandler
import com.app.armygyan.network.WebService
import com.app.armygyan.signin.model.SignInRequest
import com.app.armygyan.signin.model.SignInResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class SignInViewModel(application: Application) : AndroidViewModel(application) {
    private var webService: WebService
      var isLoading: MutableLiveData<Boolean>
      var isViewEnable: MutableLiveData<Boolean>
      var signInRequest:SignInRequest
      var resultantSignIn: MutableLiveData<WebResponse<SignInResponse>>
      var headerMap:Map<String, String>
    init {
        webService = (application as QuizApplication).getWebServiceInstance()
        isLoading = MutableLiveData()
        isViewEnable= MutableLiveData()
        signInRequest = SignInRequest()
        headerMap=HashMap()
        resultantSignIn = MutableLiveData()
    }

    suspend fun attemptSignIn() : SignInResponse? = suspendCoroutine {
        isLoading.postValue(true)
        isViewEnable.postValue(false)
        webService.attemptSignIn(headerMap,signInRequest.name,signInRequest.email,signInRequest.deviceId).enqueue(object : Callback<SignInResponse> {
            override fun onResponse(call: Call<SignInResponse>, response: Response<SignInResponse>) {
                isLoading.postValue(false)
                isViewEnable.postValue(true)
                if (response.isSuccessful && response.body() != null) {
                    val result = response.body()
                    if (result!!.status) {
                       // resultantSignIn.value=WebResponse(SUCCESS, result, null)
                        it.resume(result)
                    } else {
                      //  resultantSignIn.value=WebResponse(FAILURE, null, result.message)
                        it.resume(null)
                    }
                }
            }

            override fun onFailure(call: Call<SignInResponse?>, error: Throwable) {
                isLoading.postValue(false)
                isViewEnable.postValue(true)
               // val errorMsg: String? = ErrorHandler.reportError(error)
               // Log.e("onFailure", error.printStackTrace().toString())
              //  resultantSignIn.value=WebResponse(FAILURE, null, errorMsg)
                it.resume(null)
            }
        })
    }


}
