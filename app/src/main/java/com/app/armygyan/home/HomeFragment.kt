package com.app.armygyan.home

import android.content.Context
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.app.armygyan.QuizApplication
import com.app.armygyan.annotation.FragmentType
import com.app.armygyan.databinding.FragmentHomeBinding
import com.app.armygyan.helper.SharedPrefHelper
import com.app.armygyan.interfacor.HomeFragmentSelectedListener


class HomeFragment :Fragment() {
    private var usrName: String?=null
    private lateinit var viewModel: HomeViewModel
    private  var mFragmentListener: HomeFragmentSelectedListener?=null
    private var sharedPrefs: SharedPrefHelper? = null
    private var _binding: FragmentHomeBinding? = null
    private val binder get() = _binding!!

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
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binder.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        super.onViewCreated(view, savedInstanceState)
        binder.tvUsrWelcome.append(" ")
        binder.tvUsrWelcome.append(usrName?.split(" ")?.get(0))
        initListener()

    }

    private fun initListener() {

        binder.tvSetting.setOnClickListener {
            mFragmentListener?.showFragment(FragmentType.SETTING_FRAGMENT,null)
        }

        binder.tvMyProfile.setOnClickListener {
            mFragmentListener?.showFragment(FragmentType.PROFILE_FRAGMENT,null)
        }
        binder.tvAboutUs.setOnClickListener {
            mFragmentListener?.showFragment(FragmentType.ABOUT_US_FRAGMENT,null)
        }

        binder.tvNotification.setOnClickListener {
            mFragmentListener?.showFragment(FragmentType.NOTIFICATION_FRAGMENT,null)
        }

        binder.tvStudyMaterial.setOnClickListener {
            mFragmentListener?.showFragment(FragmentType.STUDY_MATERIAL_FRAGMENT,null)
        }

        binder.btnPlayQuiz.setOnClickListener {
            mFragmentListener?.showFragment(FragmentType.QUIZ_CATEGORY_FRAGMENT,null)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
