package com.app.quiz.quizz.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.quiz.QuizApplication
import com.app.quiz.annotation.Status
import com.app.quiz.extra.WebResponse
import com.app.quiz.helper.ErrorHandler
import com.app.quiz.network.WebService
import com.app.quiz.quizz.model.QuestionSet
import com.app.quiz.quizz.model.QuizDetail
import com.app.quiz.quizz.model.QuizDetailResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class QuestionSetViewModel  (application: Application) : AndroidViewModel(application) {
      private var webService: WebService
      var isLoading: MutableLiveData<Boolean>
      var quizDetail: MutableLiveData<QuizDetail>
      var isInstructDialogVisibility: MutableLiveData<Boolean>
      var isTimeOverDialogVisibility: MutableLiveData<Boolean>
      var resultQuizDetail: MutableLiveData<WebResponse<QuizDetailResponse>>
      var lstOfQuestions:  MutableLiveData<List<QuestionSet>>

    init {
        webService = (application as QuizApplication).getWebServiceInstance()
        isLoading = MutableLiveData()
        resultQuizDetail = MutableLiveData()
        lstOfQuestions= MutableLiveData()
        quizDetail=MutableLiveData()
        isInstructDialogVisibility=MutableLiveData()
        isTimeOverDialogVisibility=MutableLiveData()
    }

    fun fetchQuizDetail() {
        isLoading.value = true
        webService.fetchQuizDetail(quizDetail.value?.id).enqueue(object : Callback<QuizDetailResponse> {
            override fun onResponse(call: Call<QuizDetailResponse>, response: Response<QuizDetailResponse>) {
                isLoading.value=false
                if (response.isSuccessful && response.body() != null) {
                    val result = response.body()
                    result?.status?.let {
                        if (it) {
                            resultQuizDetail.value= WebResponse(Status.SUCCESS, result, null)
                        } else {
                            resultQuizDetail.value= WebResponse(Status.FAILURE, null, result.message)
                        }
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
