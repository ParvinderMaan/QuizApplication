package com.app.quiz.quizz

import android.util.Log
import androidx.paging.PageKeyedDataSource
import com.app.quiz.network.WebService
import com.app.quiz.quizz.model.QuizDetail
import com.app.quiz.quizz.model.QuizzesResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class QuizCategoryDataSource (private var webService: WebService) : PageKeyedDataSource<Long, QuizDetail>() {
    override fun loadInitial(params: LoadInitialParams<Long>, callback: LoadInitialCallback<Long, QuizDetail>) {
        webService.fetchQuizzes(1).enqueue(object :
            Callback<QuizzesResponse> {
            override fun onResponse(call: Call<QuizzesResponse>, response: Response<QuizzesResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val result = response.body()
                    if (result?.data?.quizDetail!=null)
                        callback.onResult(result.data.quizDetail,null,2)
                }
            }

            override fun onFailure(call: Call<QuizzesResponse>, error: Throwable) {
                //...................missing !!!
                Log.e("onFailure",""+error.message)
            }
        })

    }

    override fun loadAfter(params: LoadParams<Long>, callback: LoadCallback<Long, QuizDetail>) {
        webService.fetchQuizzes(params.key).enqueue(object :
            Callback<QuizzesResponse> {
            override fun onResponse(call: Call<QuizzesResponse>, response: Response<QuizzesResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val result = response.body()
                    if (result?.data?.quizDetail!=null)
                        callback.onResult(result.data.quizDetail,params.key+1)
                }
            }
            override fun onFailure(call: Call<QuizzesResponse>, error: Throwable) {
                //...................missing !!!
            }
        })

    }
    override fun loadBefore(params: LoadParams<Long>, callback: LoadCallback<Long, QuizDetail>) {}

}