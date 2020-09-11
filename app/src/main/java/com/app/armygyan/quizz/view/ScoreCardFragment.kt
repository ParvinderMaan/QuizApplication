package com.app.armygyan.quizz.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.app.armygyan.R
import com.app.armygyan.annotation.FragmentType
import com.app.armygyan.interfacor.HomeFragmentSelectedListener
import com.app.armygyan.quizz.model.QuestionSet
import com.app.armygyan.quizz.viewmodel.ScoreCardViewModel
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import kotlinx.android.synthetic.main.fragment_score_card.*
import kotlinx.coroutines.*
import java.util.*
import kotlin.math.abs
import kotlin.math.round


class ScoreCardFragment :Fragment() {
    private var quizDetail: ArrayList<QuestionSet>?=null
    private var mFragmentListener: HomeFragmentSelectedListener? = null
    private lateinit var viewModel: ScoreCardViewModel
    private lateinit var mInterstitialAd: InterstitialAd

    companion object {
        @Suppress("UNCHECKED_CAST")
        fun newInstance(payload: Any?):ScoreCardFragment{
            var fragment =ScoreCardFragment()
            val bundle = Bundle()
            var quizDetail = payload as ArrayList<QuestionSet>
            bundle.putParcelableArrayList("KEY", quizDetail)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mFragmentListener = context as HomeFragmentSelectedListener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ScoreCardViewModel::class.java)

        // collect our intent
        if (arguments != null) {
            quizDetail = arguments?.getParcelableArrayList("KEY")
            quizDetail?.let { viewModel.quizDetail.value=it
               }
            quizDetail?.forEach { println(it.toString()) }
        }

    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_score_card, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
  super.onViewCreated(view, savedInstanceState)
        fbtn_ok?.setOnClickListener {
            mFragmentListener?.popTillFragment(FragmentType.QUIZ_CATEGORY_FRAGMENT, 0)
        }
        fbtn_show_answers?.setOnClickListener {

            mFragmentListener?.showFragment(FragmentType.SHOW_ANSWER, quizDetail)
        }
        viewModel.quizDetail.observe(viewLifecycleOwner, Observer {
            val totQues:Int = it.size
            val totAttemptedQues:Int = it.filter { it.ansOptSelected!=0 }.size
            var correctAnsCount=0
            it.forEach{
                when(it.ansOptSelected){
                    1 -> if("a".equals(it.ansOption))  ++correctAnsCount
                    2 -> if("b".equals(it.ansOption))  ++correctAnsCount
                    3 -> if("c".equals(it.ansOption))  ++correctAnsCount
                    4 -> if("d".equals(it.ansOption))  ++correctAnsCount
                }
            }

            val totWrongQues=totAttemptedQues-correctAnsCount
            val totScore= ((correctAnsCount*2) - (totWrongQues*0.5))

            tv_tot_ques?.text= String.format(Locale.getDefault(), "%02d", totQues)
            tv_tot_attempted_ques?.text= String.format(Locale.getDefault(), "%02d", totAttemptedQues)
            tv_tot_correct_ques?.text= String.format(Locale.getDefault(), "%02d", correctAnsCount)
            tv_tot_wrong_ques?.text=String.format(Locale.getDefault(), "%02d", totWrongQues)
            tv_your_score?.text= String.format(Locale.getDefault(), "%.01f", totScore)

            var percentage = (totScore * 100) / (totQues*2); //12
           // Log.e("percentage",""+percentage.toInt())
            val performance = when(percentage.toInt()){
                in 90..100 -> resources.getString(R.string.title_excellent)
                in 80..90 -> resources.getString(R.string.title_good)
                in 60..79 -> resources.getString(R.string.title_average)
                else -> resources.getString(R.string.title_poor) }
            tv_over_all_performance?.text=performance
        })

        mInterstitialAd = InterstitialAd(activity)
        mInterstitialAd.adUnitId = "ca-app-pub-3940256099942544/103317371" // test ca-app-pub-3940256099942544/103317371
        mInterstitialAd.loadAd(AdRequest.Builder().build())
        mInterstitialAd.adListener= object: AdListener() {
            override fun onAdLoaded() { if (mInterstitialAd.isLoaded) mInterstitialAd.show() }
        }

    }
}
//ca-app-pub-7872107105590310/4646905684