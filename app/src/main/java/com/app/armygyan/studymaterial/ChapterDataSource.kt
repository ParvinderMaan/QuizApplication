package com.app.armygyan.studymaterial

import android.util.Log
import androidx.paging.PageKeyedDataSource
import com.app.armygyan.network.WebService
import com.app.armygyan.studymaterial.model.StudyMaterialChapter
import com.app.armygyan.studymaterial.model.StudyMaterialChaptersResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.HashMap

class ChapterDataSource(private var webService: WebService,private var headers: HashMap<String, String>,private var categoryId: Long) : PageKeyedDataSource<Long, StudyMaterialChapter>() {
    override fun loadInitial(params: LoadInitialParams<Long>, callback: LoadInitialCallback<Long, StudyMaterialChapter>) {
        webService.fetchStudyMaterialChapters(headers,1,categoryId).enqueue(object :
            Callback<StudyMaterialChaptersResponse> {
            override fun onResponse(call: Call<StudyMaterialChaptersResponse>, response: Response<StudyMaterialChaptersResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val result = response.body()
                    if (result?.data?.chapters!=null)
                        callback.onResult(result.data.chapters,null,2)
                }
            }

            override fun onFailure(call: Call<StudyMaterialChaptersResponse>, error: Throwable) {
                //...................missing !!!
             //   Log.e("onFailure",""+error.message)
            }
        })

    }

    override fun loadAfter(params: LoadParams<Long>, callback: LoadCallback<Long, StudyMaterialChapter>) {
        webService.fetchStudyMaterialChapters(headers,params.key,categoryId).enqueue(object :
            Callback<StudyMaterialChaptersResponse> {
            override fun onResponse(call: Call<StudyMaterialChaptersResponse>, response: Response<StudyMaterialChaptersResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val result = response.body()
                    if (result?.data?.chapters!=null)
                        callback.onResult(result.data.chapters,params.key+1)
                }
            }

            override fun onFailure(call: Call<StudyMaterialChaptersResponse>, error: Throwable) {
                //...................missing !!!
            }
        })

    }
    override fun loadBefore(params: LoadParams<Long>, callback: LoadCallback<Long, StudyMaterialChapter>) {}

}