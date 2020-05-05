package com.app.quiz

import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.app.quiz.annotation.Language
import com.app.quiz.helper.LocaleHelper
import com.app.quiz.helper.SharedPrefHelper
import com.app.quiz.network.ServiceGenerator
import com.app.quiz.network.WebService

class QuizApplication  : Application() {

    // @use DI
    fun getWebServiceInstance(): WebService {
        return getRetrofitInstance().createService(WebService::class.java);
    }

    // @use DI
    fun getRetrofitInstance(): ServiceGenerator{
        return ServiceGenerator.getInstance(this);
    }

    // @use DI
    fun getSharedPrefInstance(): SharedPrefHelper {
        return SharedPrefHelper.getInstance(this);
    }


}

