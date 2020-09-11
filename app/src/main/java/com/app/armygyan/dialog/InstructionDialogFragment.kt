package com.app.armygyan.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.app.armygyan.R
import com.app.armygyan.quizz.model.QuizDetail
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.dialog_instructions.*

class InstructionDialogFragment : BottomSheetDialogFragment() {
    private var  dialogListener:InstructionDialogFragmentListener?=null
    var quizDetail:QuizDetail?=null
    var noOfMinutes:Int = 0
    var noOfQuestions:Int = 0
    companion object {
        fun newInstance(quizDetail: QuizDetail?): InstructionDialogFragment {
            val fragment = InstructionDialogFragment()
            val bundle=Bundle()
            quizDetail?.let { bundle.putParcelable("KEY", it) }
            fragment.arguments=bundle
            return fragment
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.AppBottomSheetDialogTheme);

        arguments?.let {
            quizDetail=it.getParcelable("KEY")
           // noOfMinutes=(noOfQuestions*30)/60
        }
        quizDetail?.quesCount?.toInt()?.let { noOfQuestions=it }
        quizDetail?.duration?.toInt()?.let { noOfMinutes=it }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.dialog_instructions, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isCancelable=false
        btn_start_quiz?.setOnClickListener {
            dialogListener?.onStartClick()
        }

        tv_details?.text=resources.getString(R.string.title_i)+" "+noOfQuestions+" "+resources.getString(R.string.title_questions)
        tv_details?.append("\n")
        tv_details?.append(resources.getString(R.string.title_ii))
        tv_details?.append("\n")
        tv_details?.append(resources.getString(R.string.title_iii))
        tv_details?.append("\n")
        tv_details?.append(resources.getString(R.string.title_iv) +" "+ noOfMinutes +" "+resources.getString(R.string.title_minutes))
        tv_details?.append("\n")
        tv_details?.append(resources.getString(R.string.title_v))

    }

    interface InstructionDialogFragmentListener {
        fun onStartClick()
    }

    fun setOnInstructionDialogFragmentListener(listener:InstructionDialogFragmentListener ) {
        dialogListener = listener
    }
}