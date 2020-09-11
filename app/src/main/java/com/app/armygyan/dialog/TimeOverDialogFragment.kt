package com.app.armygyan.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.app.armygyan.R
import kotlinx.android.synthetic.main.dialog_time_over.*

class TimeOverDialogFragment : DialogFragment() {
    private var  dialogListener:TimeOverDialogFragmentListener?=null


    companion object {
        fun newInstance(): TimeOverDialogFragment {
            return TimeOverDialogFragment()
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.PauseDialog);
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_time_over, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isCancelable=false
        btn_submit_quiz?.setOnClickListener {
            dialogListener?.onSubmitClick()
        }



    }

    interface TimeOverDialogFragmentListener {
        fun onSubmitClick()
    }

    fun setOnTimeOverDialogFragmentListener(listener:TimeOverDialogFragmentListener ) {
        dialogListener = listener
    }
}