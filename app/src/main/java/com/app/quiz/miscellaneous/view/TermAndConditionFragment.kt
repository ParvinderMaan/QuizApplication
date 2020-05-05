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
import com.app.quiz.annotation.Status
import com.app.quiz.base.BaseFragment
import com.app.quiz.interfacor.HomeFragmentSelectedListener
import com.app.quiz.miscellaneous.model.TermAndConditionResponse
import com.app.quiz.miscellaneous.viewmodel.TermAndConditionViewModel
import kotlinx.android.synthetic.main.fragment_term_and_condition.*
import kotlinx.android.synthetic.main.fragment_term_and_condition.ibtn_close
import kotlinx.android.synthetic.main.fragment_term_and_condition.textView

class TermAndConditionFragment : BaseFragment() {
    private var mFragmentListener: HomeFragmentSelectedListener? = null
    private lateinit var viewModel: TermAndConditionViewModel

    companion object {
        fun newInstance() =
            TermAndConditionFragment()
    }

    override fun getRootView(): View {
        return cl_root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mFragmentListener = context as HomeFragmentSelectedListener
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_term_and_condition, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = ViewModelProviders.of(this).get(TermAndConditionViewModel::class.java)
        super.onViewCreated(view, savedInstanceState)
        initObserver()
        textView?.setMovementMethod(ScrollingMovementMethod())
        ibtn_close?.setOnClickListener {
            mFragmentListener?.popTopMostFragment()
        }
        viewModel.fetchTermAndCondition()
    }

    private fun initObserver() {

        viewModel.isLoading.observe(viewLifecycleOwner,
            Observer {
                if (it) shimmer_frame_layout?.visibility = View.VISIBLE
                else shimmer_frame_layout?.visibility = View.GONE

            })

        viewModel.resultantTermAndCondition.observe(viewLifecycleOwner, Observer {

            when (it.status) {
                Status.SUCCESS -> loadTermAndConditions(it.data?.data?.get(0))
                Status.FAILURE -> it.errorMsg?.let { showSnackBar(it) }
            }
        })

    }

    private fun loadTermAndConditions(termsAndCondition: TermAndConditionResponse.TermAndConditionInfo?) {
        if (Build.VERSION.SDK_INT >=Build.VERSION_CODES.N) {
            textView.text = Html.fromHtml(termsAndCondition?.termsAndCondition, Html.FROM_HTML_MODE_LEGACY)
        } else {
            textView.text = Html.fromHtml(termsAndCondition?.termsAndCondition)
        }
    }
}
