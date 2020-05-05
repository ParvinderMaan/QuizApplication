package com.app.quiz.interfacor;

import com.app.quiz.annotation.FragmentType

interface HomeFragmentSelectedListener {

    fun showFragment(@FragmentType tag:String,  payload:Any?)
    fun popTillFragment( tag:String,  flag:Int)
    fun popTopMostFragment()
    fun resetLocale(locale: String)


 }
