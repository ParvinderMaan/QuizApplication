package com.app.armygyan.miscellaneous.view

import android.content.Context
import android.os.Bundle
import android.transition.ChangeBounds
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.app.armygyan.QuizApplication
import com.app.armygyan.R
import com.app.armygyan.annotation.FragmentType
import com.app.armygyan.databinding.FragmentSplashBinding
import com.app.armygyan.helper.SharedPrefHelper
import com.app.armygyan.interfacor.HomeFragmentSelectedListener
import com.app.armygyan.miscellaneous.viewmodel.SplashViewModel
import kotlinx.coroutines.*


class SplashFragment :  Fragment() {
    private var isLanguageSelected: String?=null
    private var isUserSignIn: Boolean? = null
    private var DELAY_INTERVAL=2500L
    private  var mFragmentListener: HomeFragmentSelectedListener?=null
    private lateinit var viewModel: SplashViewModel
    private var sharedPrefs: SharedPrefHelper? =null
    private var _binding: FragmentSplashBinding? = null
    private val binder get() = _binding!!

    companion object {
        fun newInstance() = SplashFragment()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mFragmentListener = context as HomeFragmentSelectedListener
        sharedPrefs = (context.applicationContext as QuizApplication).getSharedPrefInstance()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isUserSignIn=sharedPrefs?.read(SharedPrefHelper.KEY_IS_SIGN_IN,false)
        isLanguageSelected=sharedPrefs?.read(SharedPrefHelper.KEY_LANGUAGE, null)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binder.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = ViewModelProviders.of(this).get(SplashViewModel::class.java)
        super.onViewCreated(view, savedInstanceState)

        val constraintSetStart= ConstraintSet()
        val constraintSetEnd= ConstraintSet()
        constraintSetStart.clone(activity,R.layout.fragment_splash)
        constraintSetEnd.clone(activity,R.layout.fragment_splash_alt)

        // IO,Main,Default
        CoroutineScope(Dispatchers.Default).launch {
            delay(100)
            withContext(Dispatchers.Main) {
                val transition= ChangeBounds()
                transition.duration=800
                TransitionManager.beginDelayedTransition(binder.clRoot,transition) //,transition
                constraintSetEnd.applyTo(binder.clRoot)
            }
        }


        viewModel.action.observe(viewLifecycleOwner, {
                when(isUserSignIn) {
                    true ->   mFragmentListener?.showFragment(FragmentType.HOME_FRAGMENT,null)
                    false ->   mFragmentListener?.showFragment(FragmentType.SIGN_IN_FRAGMENT,null)
                }
            })

        CoroutineScope(Dispatchers.Default).launch {
            delay(DELAY_INTERVAL)
            viewModel.showFragment()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



}

