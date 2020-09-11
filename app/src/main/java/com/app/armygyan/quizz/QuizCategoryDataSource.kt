package com.app.armygyan.quizz

import android.util.Log
import androidx.paging.PageKeyedDataSource
import com.app.armygyan.network.WebService
import com.app.armygyan.quizz.model.QuizDetail
import com.app.armygyan.quizz.model.QuizzesResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.HashMap

class QuizCategoryDataSource (private var webService: WebService,private var headers: HashMap<String, String>) : PageKeyedDataSource<Long, QuizDetail>() {
    override fun loadInitial(params: LoadInitialParams<Long>, callback: LoadInitialCallback<Long, QuizDetail>) {
        webService.fetchQuizzes(headers,1).enqueue(object :
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
               // Log.e("onFailure",""+error.printStackTrace())
            }
        })

    }

    override fun loadAfter(params: LoadParams<Long>, callback: LoadCallback<Long, QuizDetail>) {
        webService.fetchQuizzes(headers,params.key).enqueue(object :
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