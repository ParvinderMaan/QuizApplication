package com.app.armygyan.base

import android.view.View
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.app.armygyan.R
import com.google.android.material.snackbar.Snackbar

abstract class BaseFragment : Fragment()  {

    fun showSnackBar(msg:String, @ColorRes colorId:Int=R.color.colorRed){
        val snackBar= Snackbar.make(getRootView(), msg, Snackbar.LENGTH_LONG)
        snackBar.view.setBackgroundColor(ContextCompat.getColor(requireActivity(),colorId))
        snackBar.show()
    }

     abstract fun getRootView(): View


}