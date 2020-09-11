package com.app.armygyan.helper

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class TimeUtil private constructor() {

    companion object {
        fun utcToLocal(datesToConvert: String): String {
            var ourDate:String
            try {
                val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.getDefault())
                formatter.timeZone = TimeZone.getTimeZone("UTC")
                val value = formatter.parse(datesToConvert)
                val dateFormatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.getDefault()) //this format changeable
                dateFormatter.timeZone = TimeZone.getDefault()
                 ourDate = dateFormatter.format(value)
                //Log.d("ourDate", ourDate);
            } catch (e: ParseException) {
                ourDate = "0000-00-00"
            }
        return ourDate
        }
    }












}