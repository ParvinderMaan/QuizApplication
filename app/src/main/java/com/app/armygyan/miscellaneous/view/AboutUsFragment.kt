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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.app.armygyan.QuizApplication
import com.app.armygyan.R
import com.app.armygyan.annotation.Status.FAILURE
import com.app.armygyan.annotation.Status.SUCCESS
import com.app.armygyan.base.BaseFragment
import com.app.armygyan.helper.SharedPrefHelper
import com.app.armygyan.interfacor.HomeFragmentSelectedListener
import com.app.armygyan.miscellaneous.model.AboutUsResponse
import com.app.armygyan.miscellaneous.viewmodel.AboutUsViewModel
import com.app.armygyan.network.WebHeader
import com.app.armygyan.network.WebUrl
import kotlinx.android.synthetic.main.fragment_about_us.*
import java.util.*


class AboutUsFragment :  BaseFragment() {
    private  var mFragmentListener: HomeFragmentSelectedListener?=null
    private lateinit var viewModel: AboutUsViewModel
    private var sharedPrefHelper: SharedPrefHelper?=null
    private lateinit var accessToken: String
    private var headerMap: HashMap<String, String>? = null

    companion object {
        fun newInstance() = AboutUsFragment()
    }

    override fun getRootView(): View {
        return cl_root
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_about_us, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = ViewModelProviders.of(this).get(AboutUsViewModel::class.java)
        super.onViewCreated(view, savedInstanceState)
        headerMap?.let { viewModel.fetchAboutUs(it) }
        initObserver()
        textView?.movementMethod = ScrollingMovementMethod()
        ibtn_close?.setOnClickListener {
            mFragmentListener?.popTopMostFragment()
        }

//        iv_facebook?.setOnClickListener {
//            val url = "https://www.facebook.com/dashmeshchauke"
//            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
//            startActivity(Intent.createChooser(intent, "Open with"))
//        }

        iv_instagram?.setOnClickListener {
            val url = "https://www.instagram.com/army_gyan/?hl=en"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(Intent.createChooser(intent, "Open with"))
        }


    }

    private fun initObserver() {

        viewModel.isLoading.observe(viewLifecycleOwner,
            Observer {
                if (it) shimmer_frame_layout?.visibility = View.VISIBLE
                else shimmer_frame_layout?.visibility = View.GONE

            })

        viewModel.resultantAboutUs.observe(viewLifecycleOwner, Observer {

            when(it.status){
                SUCCESS -> loadAboutUs(it.data?.data?.get(0))
                FAILURE -> it.errorMsg?.let { showSnackBar(it)}
            }
        })

    }

    private fun loadAboutUs(info: AboutUsResponse.AboutUsInfo?) {
        if (Build.VERSION.SDK_INT >=Build.VERSION_CODES.N) {
            textView.text = Html.fromHtml(info?.aboutUs, Html.FROM_HTML_MODE_LEGACY)
        } else {
            @Suppress("DEPRECATION")
            textView.text = Html.fromHtml(info?.aboutUs)
        }


    }
}
