package com.app.armygyan.quizz.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.app.armygyan.output.OutputExtension
import com.app.armygyan.output.OutputTarget
import com.app.armygyan.output.OutputTargetGenerator
import com.app.armygyan.quizz.model.QuestionSet
import kotlinx.coroutines.CoroutineExceptionHandler
import java.io.File
import java.util.*

class ScoreCardViewModel : ViewModel() {
    private var quizDetail: MutableLiveData<List<QuestionSet>> = MutableLiveData()
    private var toastInfo: MutableLiveData<String> = MutableLiveData()
    private var allPermGranted:Boolean = false
    val isLoading:MutableLiveData<Boolean> = MutableLiveData()
    fun getLoading()=isLoading
    fun getToastInfo()=toastInfo
    fun getQuizDetail()=quizDetail
    fun setQuizDetail(lstOfQuestion: ArrayList<QuestionSet>){
        quizDetail.value=lstOfQuestion
    }




    fun getOutputFile(): File  {
        return OutputTargetGenerator.fromAppDirectory()
            .setDefaultOutputDestination(
                _childFolderName = OutputTarget.FOLDER_OUTPUT,
                _fileExtension = OutputExtension.PNG,
                useExtension = true,
                createFile = true
            ).getOutputFile()

    }


    fun setAllPermGranted(status: Boolean) {
        allPermGranted=status
    }

    fun isAllPermGranted(): Boolean {
        return allPermGranted
    }


    private val exceptionHandler =
        CoroutineExceptionHandler { _, throwable ->

            var error = "Unable to save poster."
            if (!throwable.message.isNullOrBlank()) {
                error += "\n\n RootCause: ${throwable.message}"
            }
            toastInfo.value=(error)
        }

}
