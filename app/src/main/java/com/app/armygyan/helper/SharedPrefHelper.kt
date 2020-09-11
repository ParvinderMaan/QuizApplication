package com.app.armygyan.helper

import android.app.Activity
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import com.app.armygyan.QuizApplication
import com.app.armygyan.base.SingletonHolder

class SharedPrefHelper private constructor(var application: QuizApplication) {

    private var sharedPref: SharedPreferences

    init {
        sharedPref = application.getSharedPreferences(application.getPackageName(), Activity.MODE_PRIVATE);
    }


    companion object : SingletonHolder<SharedPrefHelper, QuizApplication>(::SharedPrefHelper) {
        const val KEY_FULL_NAME = "key_full_name"
        const val KEY_ID = "key_id"
        const val KEY_EMAIL = "key_email"
        const val KEY_PROFILE_PIC = "key_pro_pic"
        const val KEY_LANGUAGE = "key_language"
        const val KEY_ACCESS_TOKEN = "key_access_token"
        const val KEY_DEVICE_ID = "key_device_id"
        const val KEY_NOTIFICATION_STATUS = "key_notification"
        const val KEY_IS_SIGN_IN = "key_sign_in"

    }

    fun read(key: String?, defValue: String?): String? {
        return sharedPref.getString(key, defValue)
    }

    fun write(key: String, value: String) {
        val prefsEditor: Editor = sharedPref.edit()
        prefsEditor.putString(key, value)
        prefsEditor.apply()
    }

    fun read(key: String, defValue: Boolean): Boolean {
        return sharedPref.getBoolean(key, defValue)
    }

    fun write(key: String, value: Boolean) {
        val prefsEditor: Editor = sharedPref.edit()
        prefsEditor.putBoolean(key, value)
        prefsEditor.apply()
    }

    fun read(key: String?, defValue: Long): Long? {
        return sharedPref.getLong(key, defValue)
    }

    fun write(key: String?, value: Long) {
        val prefsEditor: Editor = sharedPref.edit()
        prefsEditor.putLong(key, value)
        prefsEditor.apply()
    }

    fun clear() {
        val prefsEditor: Editor = sharedPref.edit()
        prefsEditor.clear()
        prefsEditor.apply()
    }




}