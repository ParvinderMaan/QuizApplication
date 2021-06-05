package com.app.armygyan.miscellaneous.view


import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.app.armygyan.QuizApplication
import com.app.armygyan.annotation.Status.FAILURE
import com.app.armygyan.annotation.Status.SUCCESS
import com.app.armygyan.base.BaseFragment
import com.app.armygyan.databinding.FragmentAboutUsBinding
import com.app.armygyan.extra.AppNavigatorUtil
import com.app.armygyan.helper.SharedPrefHelper
import com.app.armygyan.interfacor.HomeFragmentSelectedListener
import com.app.armygyan.miscellaneous.model.AboutUsResponse
import com.app.armygyan.miscellaneous.viewmodel.AboutUsViewModel
import com.app.armygyan.network.WebHeader
import com.app.armygyan.network.WebUrl
import java.util.*


class AboutUsFragment :  BaseFragment() {
    private  var mFragmentListener: HomeFragmentSelectedListener?=null
    private lateinit var viewModel: AboutUsViewModel
    private var sharedPrefHelper: SharedPrefHelper?=null
    private lateinit var accessToken: String
    private var headerMap: HashMap<String, String>? = null
    private var _binding: FragmentAboutUsBinding? = null
    private val binder get() = _binding!!

    companion object {
        fun newInstance() = AboutUsFragment()
        const val PACKAGE_INSTAGRAM="com.instagram.android"
    }

    override fun getRootView(): View {
        return binder.clRoot
    }
    private val navigationUtil by lazy {
        AppNavigatorUtil(requireActivity())
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mFragmentListener = context as HomeFragmentSelectedListener
        sharedPrefHelper = (context.applicationContext as QuizApplication).getSharedPrefInstance()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        accessToken= sharedPrefHelper?.read(SharedPrefHelper.KEY_ACCESS_TOKEN,"").toString()
        headerMap = HashMap<String, String>()
        headerMap?.put(WebHeader.KEY_CONTENT_TYPE, WebHeader.VAL_CONTENT_TYPE)
        headerMap?.put(WebHeader.KEY_ACCEPT, WebHeader.VAL_ACCEPT)
        headerMap?.put(WebHeader.KEY_AUTHORIZATION,accessToken)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAboutUsBinding.inflate(inflater, container, false)
        return binder.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = ViewModelProviders.of(this).get(AboutUsViewModel::class.java)
        super.onViewCreated(view, savedInstanceState)
        headerMap?.let { viewModel.fetchAboutUs(it) }
        initObserver()
        binder.textView.movementMethod = ScrollingMovementMethod()
        binder.ibtnClose.setOnClickListener {
            mFragmentListener?.popTopMostFragment()
        }

        binder.ivInstagram.setOnClickListener {
            val packageName=PACKAGE_INSTAGRAM
            if (navigationUtil.isPackageExist(packageName)) {
                navigationUtil.packageName=packageName
                navigationUtil.openLink(WebUrl.INSTAGRAM_URL)
            } else {
                navigationUtil.packageName=packageName
                navigationUtil.openStore()
            }
        }


    }

    private fun initObserver() {

        viewModel.isLoading.observe(viewLifecycleOwner, {
                if (it) binder.shimmerFrameLayout.visibility = View.VISIBLE
                else binder.shimmerFrameLayout.visibility = View.GONE

            })

        viewModel.resultantAboutUs.observe(viewLifecycleOwner, {
            when(it.status){
                SUCCESS -> loadAboutUs(it.data?.data?.get(0))
                FAILURE -> it.errorMsg?.let { showSnackBar(it)}
            }
        })

    }

    private fun loadAboutUs(info: AboutUsResponse.AboutUsInfo?) {
        if (Build.VERSION.SDK_INT >=Build.VERSION_CODES.N) {
            binder.textView.text = Html.fromHtml(info?.aboutUs, Html.FROM_HTML_MODE_LEGACY)
        } else {
            @Suppress("DEPRECATION")
            binder.textView.text = Html.fromHtml(info?.aboutUs)
        }


    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
