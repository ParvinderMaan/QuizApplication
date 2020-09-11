package com.app.armygyan.home

import android.content.Context
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.app.armygyan.QuizApplication
import com.app.armygyan.R
import com.app.armygyan.annotation.FragmentType

import com.app.armygyan.helper.SharedPrefHelper
import com.app.armygyan.interfacor.HomeFragmentSelectedListener
import kotlinx.android.synthetic.main.fragment_home.*


class HomeFragment :Fragment() {
    private var usrName: String?=null
    private lateinit var viewModel: HomeViewModel
    private  var mFragmentListener: HomeFragmentSelectedListener?=null
    private var sharedPrefs: SharedPrefHelper? = null

    companion object {
        fun newInstance() = HomeFragment()
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mFragmentListener = context as HomeFragmentSelectedListener
        sharedPrefs = (context.applicationContext as QuizApplication).getSharedPrefInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        usrName=sharedPrefs?.read(SharedPrefHelper.KEY_FULL_NAME, "")

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        super.onViewCreated(view, savedInstanceState)
        tv_usr_welcome?.append(" ")
        tv_usr_welcome?.append(usrName?.split(" ")?.get(0))
        initListener()

    }

    private fun initListener() {

        tv_setting?.setOnClickListener {
            mFragmentListener?.showFragment(FragmentType.SETTING_FRAGMENT,null)
        }

        tv_my_profile?.setOnClickListener {
            mFragmentListener?.showFragment(FragmentType.PROFILE_FRAGMENT,null)
        }
        tv_about_us?.setOnClickListener {
            mFragmentListener?.showFragment(FragmentType.ABOUT_US_FRAGMENT,null)
        }

        tv_notification?.setOnClickListener {
            mFragmentListener?.showFragment(FragmentType.NOTIFICATION_FRAGMENT,null)
        }

        tv_study_material?.setOnClickListener {
            mFragmentListener?.showFragment(FragmentType.STUDY_MATERIAL_FRAGMENT,null)
        }

        btn_play_quiz?.setOnClickListener {
            mFragmentListener?.showFragment(FragmentType.QUIZ_CATEGORY_FRAGMENT,null)
        }
    }


}
