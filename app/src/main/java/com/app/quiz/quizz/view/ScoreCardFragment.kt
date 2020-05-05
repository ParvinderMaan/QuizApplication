package com.app.quiz.quizz.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.app.quiz.R
import com.app.quiz.annotation.FragmentType
import com.app.quiz.base.BaseFragment
import com.app.quiz.interfacor.HomeFragmentSelectedListener
import com.app.quiz.quizz.model.Score
import com.app.quiz.quizz.viewmodel.ScoreCardViewModel
import kotlinx.android.synthetic.main.fragment_score_card.*


class ScoreCardFragment :Fragment() {
    private var score: Score?=null
    private var mFragmentListener: HomeFragmentSelectedListener? = null
    private lateinit var viewModel: ScoreCardViewModel

    companion object {
        fun newInstance(payload:Any):ScoreCardFragment{
            var fragment =ScoreCardFragment()
//            val bundle = Bundle()
//            bundle.putParcelable("KEY_", payload as Score)
//            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mFragmentListener = context as HomeFragmentSelectedListener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // collect our intent
//        if (arguments != null) {
//            score = arguments?.getParcelable("KEY_")
//        }

    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_score_card, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = ViewModelProviders.of(this).get(ScoreCardViewModel::class.java)
        super.onViewCreated(view, savedInstanceState)
        fbtn_ok?.setOnClickListener {
            mFragmentListener?.popTillFragment(FragmentType.QUIZ_CATEGORY_FRAGMENT,0)
        }
        fbtn_show_answers?.setOnClickListener {

        }

//        tv_over_all_performance?.text=""
//        tv_tot_ques?.text= score?.totQues.toString()
//        tv_tot_attempted_ques?.text= score?.totQuesAttempted.toString()
//        tv_tot_correct_ques?.text= score?.totAnsCorrect.toString()
//        tv_tot_wrong_ques?.text= score?.totQuesWrong.toString()
//        var percentage=Math.abs(score!!.totQues/score!!.totAnsCorrect *100)
//        tv_your_score?.text=percentage.toString()
    }

}
