package com.app.quiz.base

import android.view.View
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.app.quiz.R
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_sign_in.*

abstract class BaseFragment : Fragment()  {

    fun showSnackBar(msg:String, @ColorRes colorId:Int=R.color.colorRed){
        var snackBar= Snackbar.make(getRootView(), msg, Snackbar.LENGTH_LONG)
        snackBar.view.setBackgroundColor(ContextCompat.getColor(requireActivity(),colorId))
        snackBar.show()
    }

     abstract fun getRootView(): View


}