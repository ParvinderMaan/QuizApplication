package com.app.quiz.quizz.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.quiz.QuizApplication
import com.app.quiz.annotation.Status
import com.app.quiz.extra.WebResponse
import com.app.quiz.helper.ErrorHandler
import com.app.quiz.network.WebService
import com.app.quiz.quizz.model.QuestionSet
import com.app.quiz.quizz.model.QuizDetailResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class QuestionSetViewModel  (application: Application) : AndroidViewModel(application) {
    private var webService: WebService
     var isLoading: MutableLiveData<Boolean>
      var quizId: MutableLiveData<Int>
      var resultQuizDetail: MutableLiveData<WebResponse<QuizDetailResponse>>
      var lstOfQuestions:  MutableLiveData<List<QuestionSet>>

    init {
        webService = (application as QuizApplication).getWebServiceInstance()
        isLoading = MutableLiveData()
        quizId = MutableLiveData()
        resultQuizDetail = MutableLiveData()
        lstOfQuestions= MutableLiveData()
    }

    fun fetchQuizDetail() {
        isLoading.setValue(true)
        webService.fetchQuizDetail(quizId.value).enqueue(object : Callback<QuizDetailResponse> {
            override fun onResponse(call: Call<QuizDetailResponse>, response: Response<QuizDetailResponse>) {
                isLoading.value=true
                if (response.isSuccessful && response.body() != null) {
                    val result = response.body()
                    if (result!!.status) {
                        resultQuizDetail.value= WebResponse(Status.SUCCESS, result, null)
                    } else {
                        resultQuizDetail.value= WebResponse(Status.FAILURE, null, result.message)
                    }
                } else {
                    val errorMsg: String = ErrorHandler.reportError(response.code())
                    resultQuizDetail.value= WebResponse(Status.FAILURE, null, errorMsg)
                }
            }

            override fun onFailure(call: Call<QuizDetailResponse>, error: Throwable) {
                isLoading.value=false
                val errorMsg: String? = ErrorHandler.reportError(error)
                resultQuizDetail.value= WebResponse(Status.FAILURE, null, errorMsg)
            }
        })
    }




}
