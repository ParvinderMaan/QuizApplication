package com.app.armygyan.miscellaneous.view

import android.content.Context
import android.os.Bundle
import android.transition.ChangeBounds
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintSet
import com.app.armygyan.QuizApplication
import com.app.armygyan.R
import com.app.armygyan.annotation.Language
import com.app.armygyan.base.BaseFragment
import com.app.armygyan.helper.SharedPrefHelper
import com.app.armygyan.interfacor.HomeFragmentSelectedListener
import kotlinx.android.synthetic.main.fragment_choose_language.*
import kotlinx.coroutines.*


class ChooseLanguageFragment :  BaseFragment() {
    private var sharedPrefs: SharedPrefHelper? = null
    private var mFragmentListener: HomeFragmentSelectedListener? = null


    companion object {
        fun newInstance() = ChooseLanguageFragment()
    }

    override fun getRootView(): View {
        return fl_main
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mFragmentListener = context as HomeFragmentSelectedListener
        sharedPrefs = (context.applicationContext as QuizApplication).getSharedPrefInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_choose_language, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val constraintSetStart= ConstraintSet()
        val constraintSetEnd= ConstraintSet()
        constraintSetStart.clone(activity,R.layout.fragment_choose_language)
        constraintSetEnd.clone(activity,R.layout.fragment_choose_language_alt)

        btn_hindi?.setOnClickListener {
            sharedPrefs?.write(SharedPrefHelper.KEY_LANGUAGE, Language.HINDI) // English
            mFragmentListener?.resetLocale(Language.HINDI)
            Toast.makeText(activity,getString(R.string.title_change_language),Toast.LENGTH_LONG).show()

//            val transition= ChangeBounds()
//            transition.duration=1200
//            TransitionManager.beginDelayedTransition(fl_main,transition) //,transition
//            constraintSetStart.applyTo(fl_main)
        }

        btn_english?.setOnClickListener {
            sharedPrefs?.write(SharedPrefHelper.KEY_LANGUAGE, Language.ENGLISH) // English
            mFragmentListener?.resetLocale(Language.ENGLISH)
            Toast.makeText(activity,getString(R.string.title_change_language),Toast.LENGTH_LONG).show()

//            val transition= ChangeBounds()
//            transition.duration=1200
//            TransitionManager.beginDelayedTransition(fl_main,transition) //,transition
//            constraintSetStart.applyTo(fl_main)
        }




        // IO,Main,Default
        CoroutineScope(Dispatchers.Default).launch {
            delay(800)
            withContext(Dispatchers.Main) {
                val transition= ChangeBounds()
                transition.duration=1200
                if(fl_main!=null){
                    TransitionManager.beginDelayedTransition(fl_main,transition) //,transition
                    constraintSetEnd.applyTo(fl_main)
                }

            }
        }

    }
}