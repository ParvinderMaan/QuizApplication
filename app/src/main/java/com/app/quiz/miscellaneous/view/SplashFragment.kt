package com.app.quiz.miscellaneous.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.app.quiz.QuizApplication
import com.app.quiz.R
import com.app.quiz.miscellaneous.viewmodel.SplashViewModel
import com.app.quiz.annotation.FragmentType
import com.app.quiz.base.BaseFragment
import com.app.quiz.helper.SharedPrefHelper
import com.app.quiz.interfacor.HomeFragmentSelectedListener
import kotlinx.coroutines.*


class SplashFragment :  Fragment() {
    private var isUserSignIn: Boolean? = null
    private var DELAY_INTERVAL=3000L
    private  var mFragmentListener: HomeFragmentSelectedListener?=null
    private lateinit var viewModel: SplashViewModel
    private var sharedPrefs: SharedPrefHelper? =null

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
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = ViewModelProviders.of(this).get(SplashViewModel::class.java)
        super.onViewCreated(view, savedInstanceState)
        viewModel.action.observe(viewLifecycleOwner,
            Observer<Int> {
                when(isUserSignIn) {
                    true ->  mFragmentListener?.showFragment(FragmentType.HOME_FRAGMENT,null)
                    false ->   mFragmentListener?.showFragment(FragmentType.SIGN_IN_FRAGMENT,null)
                }
            })
        // IO,Main,Default
        CoroutineScope(Dispatchers.Default).launch {
            delay(DELAY_INTERVAL)
            withContext(Dispatchers.Main) {
               viewModel.showFragment()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        //Toast.makeText(requireActivity(),"SplashFragment onDestroyView.....",Toast.LENGTH_LONG).show();
    }

}

