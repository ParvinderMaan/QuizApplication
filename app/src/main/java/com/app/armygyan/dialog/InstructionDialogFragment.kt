package com.app.armygyan.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.armygyan.R
import com.app.armygyan.databinding.DialogInstructionsBinding
import com.app.armygyan.quizz.model.QuizDetail
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class InstructionDialogFragment : BottomSheetDialogFragment() {
    private var  dialogListener:InstructionDialogFragmentListener?=null
    var quizDetail:QuizDetail?=null
    var noOfMinutes:Int = 0
    var noOfQuestions:Int = 0
    private var _binding: DialogInstructionsBinding? = null
    private val  binder get() = _binding!!
    companion object {
        fun newInstance(quizDetail: QuizDetail?): InstructionDialogFragment {
            val fragment = InstructionDialogFragment()
            val bundle=Bundle()
            quizDetail?.let { bundle.putParcelable(KEY_ARGS, it) }
            fragment.arguments=bundle
            return fragment
        }
        const val KEY_ARGS="KEY"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.AppBottomSheetDialogTheme);

        arguments?.let {
            quizDetail=it.getParcelable(KEY_ARGS)
        }
        quizDetail?.quesCount?.toInt()?.let { noOfQuestions=it }
        quizDetail?.duration?.toInt()?.let { noOfMinutes=it }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = DialogInstructionsBinding.inflate(inflater, container, false)
        return binder.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isCancelable=false
        binder.btnStartQuiz.setOnClickListener {
            dialogListener?.onStartClick()
        }

        binder.tvDetails.text=resources.getString(R.string.title_i)+" "+noOfQuestions+" "+resources.getString(R.string.title_questions)
        binder.tvDetails.append("\n")
        binder.tvDetails.append(resources.getString(R.string.title_ii))
        binder.tvDetails.append("\n")
        binder.tvDetails.append(resources.getString(R.string.title_iii))
        binder.tvDetails.append("\n")
        binder.tvDetails.append(resources.getString(R.string.title_iv) +" "+ noOfMinutes +" "+resources.getString(R.string.title_minutes))
        binder.tvDetails.append("\n")
        binder.tvDetails.append(resources.getString(R.string.title_v))

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    interface InstructionDialogFragmentListener {
        fun onStartClick()
    }

    fun setOnInstructionDialogFragmentListener(listener:InstructionDialogFragmentListener ) {
        dialogListener = listener
    }
}