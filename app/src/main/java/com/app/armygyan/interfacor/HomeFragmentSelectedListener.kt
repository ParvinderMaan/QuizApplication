package com.app.armygyan.interfacor;

import com.app.armygyan.annotation.FragmentType

interface HomeFragmentSelectedListener {

    fun showFragment(@FragmentType tag:String, payload:Any?)
    fun popTillFragment( tag:String,  flag:Int)
    fun popTopMostFragment()
    fun resetLocale(locale: String)
    fun shareApp()

 }
