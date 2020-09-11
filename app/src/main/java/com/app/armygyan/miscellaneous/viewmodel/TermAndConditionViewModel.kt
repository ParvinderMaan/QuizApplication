package com.app.armygyan.miscellaneous.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.app.armygyan.QuizApplication
import com.app.armygyan.annotation.Status
import com.app.armygyan.extra.WebResponse
import com.app.armygyan.helper.ErrorHandler
import com.app.armygyan.miscellaneous.model.TermAndConditionResponse
import com.app.armygyan.network.WebService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.HashMap

class TermAndConditionViewModel  (application: Application) : AndroidViewModel(application) {
    private var webService: WebService
     var isLoading: MutableLiveData<Boolean>
     var resultantTermAndCondition: MutableLiveData<WebResponse<TermAndConditionResponse>>

    init {
        webService = (application as QuizApplication).getWebServiceInstance()
        isLoading = MutableLiveData()
        resultantTermAndCondition = MutableLiveData()
    }

    fun fetchTermAndCondition(headerMap: HashMap<String, String>) {
        isLoading.value = true
        webService.fetchTermAndCondition(headerMap,2).enqueue(object : Callback<TermAndConditionResponse> {
            override fun onResponse(call: Call<TermAndConditionResponse>, response: Response<TermAndConditionResponse>) {
                isLoading.value=false
                if (response.isSuccessful && response.body() != null) {
                    val result = response.body()
                    if (result!!.status) {
                        resultantTermAndCondition.value= WebResponse(Status.SUCCESS, result, null)
                    } else {
                        resultantTermAndCondition.value= WebResponse(Status.FAILURE, null, result.message)
                    }
                } else {
                    val errorMsg: String = ErrorHandler.reportError(response.code())
                    resultantTermAndCondition.value= WebResponse(Status.FAILURE, null, errorMsg)
                }
            }

            override fun onFailure(call: Call<TermAndConditionResponse>, error: Throwable) {
                isLoading.value=false
                val errorMsg: String? = ErrorHandler.reportError(error)
                resultantTermAndCondition.value= WebResponse(Status.FAILURE, null, errorMsg)
            }
        })
    }


}
