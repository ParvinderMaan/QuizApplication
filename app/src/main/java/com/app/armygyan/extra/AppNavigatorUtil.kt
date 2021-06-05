package com.app.armygyan.extra

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.app.armygyan.R
import java.lang.NullPointerException

class AppNavigatorUtil(private var context: Context) {
    var packageName:String?=null

    fun openLink(url:String){
        if(packageName==null) throw NullPointerException("packageName is null")

        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        intent.setPackage(packageName)
        context.startActivity(intent)
    }

    fun openStore(){
        if(packageName==null) throw NullPointerException("packageName is null")
        try {
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(context.getString(R.string.uri_google_market)+packageName)))
        } catch (e: ActivityNotFoundException) {
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(context.getString(R.string.uri_google_store)+packageName)))
        }
    }

    fun isPackageExist(target: String): Boolean {
        return context.packageManager.getInstalledApplications(0).find { info -> info.packageName == target } != null
    }

}