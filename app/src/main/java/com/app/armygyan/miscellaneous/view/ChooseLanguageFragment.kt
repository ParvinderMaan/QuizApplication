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
import com.app.armygyan.databinding.FragmentChooseLanguageBinding
import com.app.armygyan.helper.SharedPrefHelper
import com.app.armygyan.interfacor.HomeFragmentSelectedListener
import kotlinx.coroutines.*


class ChooseLanguageFragment :  BaseFragment() {
    private var sharedPrefs: SharedPrefHelper? = null
    private var mFragmentListener: HomeFragmentSelectedListener? = null
    private var _binding: FragmentChooseLanguageBinding? = null
    private val binder get() = _binding!!

    companion object {
        fun newInstance() = ChooseLanguageFragment()
    }

    override fun getRootView(): View {
        return binder.flMain
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mFragmentListener = context as HomeFragmentSelectedListener
        sharedPrefs = (context.applicationContext as QuizApplication).getSharedPrefInstance()
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View{
        _binding = FragmentChooseLanguageBinding.inflate(inflater, container, false)
        return binder.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val constraintSetStart= ConstraintSet()
        val constraintSetEnd= ConstraintSet()
        constraintSetStart.clone(activity,R.layout.fragment_choose_language)
        constraintSetEnd.clone(activity,R.layout.fragment_choose_language_alt)

        binder.btnHindi.setOnClickListener {
            sharedPrefs?.write(SharedPrefHelper.KEY_LANGUAGE, Language.HINDI) // English
            mFragmentListener?.resetLocale(Language.HINDI)
            Toast.makeText(activity,getString(R.string.title_change_language),Toast.LENGTH_LONG).show()
        }

        binder.btnEnglish.setOnClickListener {
            sharedPrefs?.write(SharedPrefHelper.KEY_LANGUAGE, Language.ENGLISH) // English
            mFragmentListener?.resetLocale(Language.ENGLISH)
            Toast.makeText(activity,getString(R.string.title_change_language),Toast.LENGTH_LONG).show()
        }

        CoroutineScope(Dispatchers.Default).launch {
            delay(800)
            withContext(Dispatchers.Main) {
                val transition= ChangeBounds()
                transition.duration=1200
                TransitionManager.beginDelayedTransition(binder.flMain,transition)
                constraintSetEnd.applyTo(binder.flMain)

            }
        }

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}