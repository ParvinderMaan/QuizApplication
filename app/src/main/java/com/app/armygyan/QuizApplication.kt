package com.app.armygyan

import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.app.armygyan.annotation.Language
import com.app.armygyan.helper.LocaleHelper
import com.app.armygyan.helper.SharedPrefHelper
import com.app.armygyan.network.ServiceGenerator
import com.app.armygyan.network.WebService

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

