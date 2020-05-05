package com.app.quiz.studymaterial

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.app.quiz.QuizApplication
import com.app.quiz.network.WebService
import com.app.quiz.studymaterial.model.StudyMaterialCategory
import com.app.quiz.studymaterial.model.StudyMaterialCategoryResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoryDataSource(private var webService: WebService)
    : PageKeyedDataSource<Long, StudyMaterialCategory>() {
    override fun loadInitial(params: LoadInitialParams<Long>, callback: LoadInitialCallback<Long, StudyMaterialCategory>) {
        webService.fetchStudyMaterialCategories(1).enqueue(object :
            Callback<StudyMaterialCategoryResponse> {
            override fun onResponse(call: Call<StudyMaterialCategoryResponse>, response: Response<StudyMaterialCategoryResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val result = response.body()
                    if (result?.data?.categories!=null)
                        callback.onResult(result.data.categories,null,2)
                }
            }

            override fun onFailure(call: Call<StudyMaterialCategoryResponse>, error: Throwable) {

                //...................missing !!!
        }
        })

    }

    override fun loadAfter(params: LoadParams<Long>, callback: LoadCallback<Long, StudyMaterialCategory>) {
        webService.fetchStudyMaterialCategories(params.key).enqueue(object :
            Callback<StudyMaterialCategoryResponse> {
            override fun onResponse(call: Call<StudyMaterialCategoryResponse>, response: Response<StudyMaterialCategoryResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val result = response.body()
                    if (result?.data?.categories!=null)
                        callback.onResult(result.data.categories,params.key+1)
                }
            }

            override fun onFailure(call: Call<StudyMaterialCategoryResponse>, error: Throwable) {
                //...................missing !!!
            }
        })

    }
    override fun loadBefore(params: LoadParams<Long>, callback: LoadCallback<Long, StudyMaterialCategory>) {}

}