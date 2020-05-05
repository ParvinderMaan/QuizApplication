package com.app.quiz.miscellaneous.view


import android.content.Context
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.app.quiz.R
import com.app.quiz.annotation.Status.FAILURE
import com.app.quiz.annotation.Status.SUCCESS
import com.app.quiz.base.BaseFragment
import com.app.quiz.interfacor.HomeFragmentSelectedListener
import com.app.quiz.miscellaneous.model.AboutUsResponse
import com.app.quiz.miscellaneous.viewmodel.AboutUsViewModel
import kotlinx.android.synthetic.main.fragment_about_us.*
import kotlinx.android.synthetic.main.fragment_about_us.ibtn_close


class AboutUsFragment :  BaseFragment() {
    private  var mFragmentListener: HomeFragmentSelectedListener?=null
    private lateinit var viewModel: AboutUsViewModel

    companion object {
        fun newInstance() = AboutUsFragment()
    }

    override fun getRootView(): View {
        return cl_root
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        mFragmentListener = context as HomeFragmentSelectedListener
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_about_us, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = ViewModelProviders.of(this).get(AboutUsViewModel::class.java)
        super.onViewCreated(view, savedInstanceState)
        initObserver()
        textView?.movementMethod = ScrollingMovementMethod()
        ibtn_close?.setOnClickListener {
            mFragmentListener?.popTopMostFragment()
        }

        viewModel.fetchAboutUs()
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
            textView.text = Html.fromHtml(info?.aboutUs)
        }


    }
}
