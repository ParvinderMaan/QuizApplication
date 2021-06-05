package com.app.armygyan.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.app.armygyan.R
import com.app.armygyan.databinding.DialogTimeOverBinding

class TimeOverDialogFragment : DialogFragment() {
    private var  dialogListener:TimeOverDialogFragmentListener?=null
    private var _binding: DialogTimeOverBinding? = null
    private val  binder get() = _binding!!

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
        _binding = DialogTimeOverBinding.inflate(inflater, container, false)
        return binder.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isCancelable=false
        binder.btnSubmitQuiz.setOnClickListener {
            dialogListener?.onSubmitClick()
        }

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    interface TimeOverDialogFragmentListener {
        fun onSubmitClick()
    }

    fun setOnTimeOverDialogFragmentListener(listener:TimeOverDialogFragmentListener ) {
        dialogListener = listener
    }
}