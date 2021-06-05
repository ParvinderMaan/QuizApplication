package com.app.armygyan.miscellaneous.view


import android.content.Context
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.app.armygyan.QuizApplication
import com.app.armygyan.annotation.Status
import com.app.armygyan.base.BaseFragment
import com.app.armygyan.databinding.FragmentTermAndConditionBinding
import com.app.armygyan.helper.SharedPrefHelper
import com.app.armygyan.interfacor.HomeFragmentSelectedListener
import com.app.armygyan.miscellaneous.model.TermAndConditionResponse
import com.app.armygyan.miscellaneous.viewmodel.TermAndConditionViewModel
import com.app.armygyan.network.WebHeader
import java.util.*

class TermAndConditionFragment : BaseFragment() {
    private var sharedPrefHelper: SharedPrefHelper?=null
    private lateinit var accessToken: String
    private var headerMap: HashMap<String, String>? = null
    private var mFragmentListener: HomeFragmentSelectedListener? = null
    private lateinit var viewModel: TermAndConditionViewModel
    private var _binding: FragmentTermAndConditionBinding? = null
    private val binder get() = _binding!!


    companion object {
        fun newInstance() =
            TermAndConditionFragment()
    }

    override fun getRootView(): View {
        return binder.clRoot
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
         mFragmentListener = context as HomeFragmentSelectedListener
         sharedPrefHelper = (context.applicationContext as QuizApplication).getSharedPrefInstance()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        accessToken= sharedPrefHelper?.read(SharedPrefHelper.KEY_ACCESS_TOKEN,"").toString()
        headerMap = HashMap()
        headerMap?.put(WebHeader.KEY_CONTENT_TYPE, WebHeader.VAL_CONTENT_TYPE)
        headerMap?.put(WebHeader.KEY_ACCEPT, WebHeader.VAL_ACCEPT)
        headerMap?.put(WebHeader.KEY_AUTHORIZATION,accessToken)
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentTermAndConditionBinding.inflate(inflater, container, false)
        return binder.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = ViewModelProviders.of(this).get(TermAndConditionViewModel::class.java)
        super.onViewCreated(view, savedInstanceState)
        headerMap?.let { viewModel.fetchTermAndCondition(it) }
        initObserver()
        binder.textView.movementMethod = ScrollingMovementMethod()
        binder.ibtnClose.setOnClickListener {
            mFragmentListener?.popTopMostFragment()
        }

    }

    private fun initObserver() {
        viewModel.isLoading.observe(viewLifecycleOwner,
            {
                if (it) binder.shimmerFrameLayout.visibility = View.VISIBLE
                else binder.shimmerFrameLayout.visibility = View.GONE

            })

        viewModel.resultantTermAndCondition.observe(viewLifecycleOwner, {
            when (it.status) {
                Status.SUCCESS -> loadTermAndConditions(it.data?.data?.get(0))
                Status.FAILURE -> it.errorMsg?.let { showSnackBar(it) }
            }
        })

    }

    private fun loadTermAndConditions(termsAndCondition: TermAndConditionResponse.TermAndConditionInfo?) {
        if (Build.VERSION.SDK_INT >=Build.VERSION_CODES.N) {
            binder.textView.text = Html.fromHtml(termsAndCondition?.termsAndCondition, Html.FROM_HTML_MODE_LEGACY)
        } else {
            @Suppress("DEPRECATION")
            binder.textView.text = Html.fromHtml(termsAndCondition?.termsAndCondition)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
