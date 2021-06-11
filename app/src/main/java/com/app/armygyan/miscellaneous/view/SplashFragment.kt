package com.app.armygyan.miscellaneous.view

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.transition.ChangeBounds
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import com.app.armygyan.QuizApplication
import com.app.armygyan.R
import com.app.armygyan.annotation.FragmentType
import com.app.armygyan.databinding.FragmentSplashBinding
import com.app.armygyan.helper.SharedPrefHelper
import com.app.armygyan.interfacor.HomeFragmentSelectedListener
import com.app.armygyan.miscellaneous.viewmodel.SplashViewModel
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.android.play.core.tasks.Task
import com.squareup.picasso.Picasso
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO


class SplashFragment :  Fragment() {
    private var isLanguageSelected: String?=null
    private var isUserSignIn: Boolean? = null
    private var DELAY_INTERVAL=2000L
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

        viewModel.action.observe(viewLifecycleOwner, {
                when(isUserSignIn) {
                    true ->   mFragmentListener?.showFragment(FragmentType.HOME_FRAGMENT,null)
                    false ->   mFragmentListener?.showFragment(FragmentType.SIGN_IN_FRAGMENT,null)
                }
            })

        viewLifecycleOwner.lifecycleScope.launch {
            delay(DELAY_INTERVAL)
          viewModel.showFragment()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }





}

