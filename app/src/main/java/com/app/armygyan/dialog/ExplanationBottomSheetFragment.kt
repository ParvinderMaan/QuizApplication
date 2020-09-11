package com.app.armygyan.dialog

import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.app.armygyan.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.fragment_explaination.*
import java.util.ArrayList

class ExplanationBottomSheetFragment() : BottomSheetDialogFragment() {

    private var explaination: String?=null

    companion object {
        @Suppress("UNCHECKED_CAST")
        fun newInstance(payload: Any?): ExplanationBottomSheetFragment {
            var fragment = ExplanationBottomSheetFragment()
            val bundle = Bundle()
            var explain = payload as String
            bundle.putString("KEY", explain)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.AppBottomSheetDialogTheme);

        // collect our intent
        if (arguments != null) {
            explaination = arguments?.getString("KEY")
            if(explaination.isNullOrEmpty()) explaination=getString(R.string.alert_no_explanation)

        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_explaination, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
       // tv_explain?.text=explaination

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tv_explain?.text = Html.fromHtml(explaination, Html.FROM_HTML_MODE_LEGACY)
        } else {
            tv_explain?.text = Html.fromHtml(explaination)
        }
    }


}