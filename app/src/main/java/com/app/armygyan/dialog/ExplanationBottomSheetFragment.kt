package com.app.armygyan.dialog

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.app.armygyan.R
import com.app.armygyan.databinding.FragmentExplainationBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class ExplanationBottomSheetFragment() : BottomSheetDialogFragment() {
    private var explaination: String?=null
    private var _binding: FragmentExplainationBinding? = null
    private val  binder get() = _binding!!

    companion object {
        fun newInstance(payload: Any?): ExplanationBottomSheetFragment {
            val fragment = ExplanationBottomSheetFragment()
            val bundle = Bundle()
            val explain = payload as String
            bundle.putString(KEY_ARGS, explain)
            fragment.arguments = bundle
            return fragment
        }
        const val KEY_ARGS="KEY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.AppBottomSheetDialogTheme);
        if (arguments != null) {
            explaination = arguments?.getString(KEY_ARGS)
            if(explaination.isNullOrEmpty()) explaination=getString(R.string.alert_no_explanation)
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentExplainationBinding.inflate(inflater, container, false)
        return binder.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            binder.tvExplain.text = Html.fromHtml(explaination, Html.FROM_HTML_MODE_LEGACY)
        } else {
            binder.tvExplain.text = Html.fromHtml(explaination)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}