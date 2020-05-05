package com.app.quiz.extra

import android.content.Context
import android.net.ConnectivityManager
import com.app.quiz.QuizApplication

class NetworkUtil {


    companion object {
        @JvmStatic
        fun isNetAvail(context: QuizApplication?): Boolean {
            val connectivityManager =
                context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = connectivityManager.activeNetworkInfo
            return networkInfo != null && networkInfo.isConnected
        }


    }

}