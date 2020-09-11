package com.app.armygyan.miscellaneous.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SplashViewModel : ViewModel() {
     val action: MutableLiveData<Int> = MutableLiveData();
    init {
//        action.value=0
    }

    fun showFragment() {
        action.value=1;
    }

}
