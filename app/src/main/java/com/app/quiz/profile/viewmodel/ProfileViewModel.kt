package com.app.quiz.profile.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.app.quiz.QuizApplication
import com.app.quiz.annotation.Status
import com.app.quiz.extra.WebResponse
import com.app.quiz.helper.ErrorHandler
import com.app.quiz.miscellaneous.model.GeneralResponse
import com.app.quiz.network.WebService
import com.app.quiz.profile.ProfileInfoResponse
import com.app.quiz.profile.model.ProfileInfo
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileViewModel(application: Application) : AndroidViewModel(application) {
    private var webService: WebService
    var isLoading: MutableLiveData<Boolean>
    var userId: MutableLiveData<Int>
    var profileInfo: MutableLiveData<ProfileInfo>
    var resultUpdateProfile: MutableLiveData<WebResponse<GeneralResponse>>
    var resultGetProfile: MutableLiveData<WebResponse<ProfileInfoResponse>>

    init {
        webService = (application as QuizApplication).getWebServiceInstance()
        isLoading = MutableLiveData()
        userId = MutableLiveData()
        resultGetProfile = MutableLiveData()
        resultUpdateProfile = MutableLiveData()
        profileInfo = MutableLiveData()
    }

    fun fetchProfileInfo() {
        isLoading.setValue(true)
        webService.fetchProfileInfo(userId.value).enqueue(object : Callback<ProfileInfoResponse> {
            override fun onResponse(
                call: Call<ProfileInfoResponse>,
                response: Response<ProfileInfoResponse>
            ) {
                isLoading.value = true
                if (response.isSuccessful && response.body() != null) {
                    val result = response.body()
                    if (result!!.status) {
                        resultGetProfile.value = WebResponse(Status.SUCCESS, result, null)
                    } else {
                        resultGetProfile.value = WebResponse(Status.FAILURE, null, result.message)
                    }
                } else {
                    val errorMsg: String = ErrorHandler.reportError(response.code())
                    resultGetProfile.value = WebResponse(Status.FAILURE, null, errorMsg)
                }
            }

            override fun onFailure(call: Call<ProfileInfoResponse>, error: Throwable) {
                isLoading.value = false
                val errorMsg: String? = ErrorHandler.reportError(error)
                resultGetProfile.value = WebResponse(Status.FAILURE, null, errorMsg)
            }
        })
    }

    fun updateProfileInfo() {
        isLoading.setValue(true)
        webService.updateProfileInfo(profileInfo.value).enqueue(object : Callback<GeneralResponse> {
            override fun onResponse(
                call: Call<GeneralResponse>,
                response: Response<GeneralResponse>
            ) {
                isLoading.value = true
                if (response.isSuccessful && response.body() != null) {
                    val result = response.body()
                    if (result!!.status) {
                        resultUpdateProfile.value = WebResponse(Status.SUCCESS, result, null)
                    } else {
                        resultUpdateProfile.value =
                            WebResponse(Status.FAILURE, null, result.message)
                    }
                } else {
                    val errorMsg: String = ErrorHandler.reportError(response.code())
                    resultUpdateProfile.value = WebResponse(Status.FAILURE, null, errorMsg)
                }
            }

            override fun onFailure(call: Call<GeneralResponse>, error: Throwable) {
                isLoading.value = false
                val errorMsg: String? = ErrorHandler.reportError(error)
                resultUpdateProfile.value = WebResponse(Status.FAILURE, null, errorMsg)
            }
        })
    }


    fun alterProfilePic(imgPart: MultipartBody.Part?) {
        isLoading.setValue(true)
        webService.alterProfilePic(imgPart).enqueue(object : Callback<GeneralResponse> {
            override fun onResponse(
                call: Call<GeneralResponse>,
                response: Response<GeneralResponse>
            ) {
                isLoading.setValue(false)
                if (response.isSuccessful && response.body() != null) {
                    val result = response.body()
                    if (result!!.status) {
                        resultUpdateProfile.value = WebResponse(Status.SUCCESS, result, null)
                    } else {
                        resultUpdateProfile.value =
                            WebResponse(Status.FAILURE, null, result.message)
                    }
                } else {
                    val errorMsg: String = ErrorHandler.reportError(response.code())
                    resultUpdateProfile.value = WebResponse(Status.FAILURE, null, errorMsg)
                }
            }

            override fun onFailure(call: Call<GeneralResponse>, error: Throwable) {
                isLoading.setValue(false)
                val errorMsg = ErrorHandler.reportError(error)
                resultUpdateProfile.setValue(WebResponse(Status.FAILURE, null, errorMsg))
            }
        })
    }

}
