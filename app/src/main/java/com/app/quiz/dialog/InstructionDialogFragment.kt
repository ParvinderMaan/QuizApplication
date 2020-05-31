package com.app.quiz.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.app.quiz.R
import kotlinx.android.synthetic.main.dialog_instructions.*

class InstructionDialogFragment : DialogFragment() {
    private var  dialogListener:InstructionDialogFragmentListener?=null
    var noOfQuestions:Int = 0
    var noOfMinutes:Int = 0

    companion object {
        fun newInstance(noOfQuestions:Int?): InstructionDialogFragment {
            val fragment = InstructionDialogFragment()
            val bundle=Bundle()
            noOfQuestions?.let { bundle.putInt("KEY", it) }
            fragment.arguments=bundle
            return fragment
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            noOfQuestions=it.getInt("KEY")
            noOfMinutes=(noOfQuestions*30)/60
        }
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

        tv_details?.text="\u2022 The quiz has $noOfQuestions questions"
        tv_details?.append("\n")
        tv_details?.append("\u2022 Each question carries 1 mark")
        tv_details?.append("\n")
        tv_details?.append("\u2022 No negative marking")
        tv_details?.append("\n")
        tv_details?.append("\u2022 Total duration is $noOfMinutes minutes")
        tv_details?.append("\n")
        tv_details?.append("\u2022 Select answer at once")

    }

    interface InstructionDialogFragmentListener {
        fun onStartClick()
    }

    fun setOnInstructionDialogFragmentListener(listener:InstructionDialogFragmentListener ) {
        dialogListener = listener
    }
}