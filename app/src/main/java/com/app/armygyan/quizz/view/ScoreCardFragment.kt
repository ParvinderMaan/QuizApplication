package com.app.armygyan.quizz.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.app.armygyan.R
import com.app.armygyan.annotation.FragmentType
import com.app.armygyan.databinding.FragmentScoreCardBinding
import com.app.armygyan.interfacor.HomeFragmentSelectedListener
import com.app.armygyan.quizz.model.QuestionSet
import com.app.armygyan.quizz.viewmodel.ScoreCardViewModel
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import java.util.*



class ScoreCardFragment :Fragment() {
    private var quizDetail: ArrayList<QuestionSet>?=null
    private var mFragmentListener: HomeFragmentSelectedListener? = null
    private lateinit var viewModel: ScoreCardViewModel
    private lateinit var mInterstitialAd: InterstitialAd
    private var _binding: FragmentScoreCardBinding? = null
    private val binder get() = _binding!!

    companion object {
        @Suppress("UNCHECKED_CAST")
        fun newInstance(payload: Any?):ScoreCardFragment{
            val fragment =ScoreCardFragment()
            val bundle = Bundle()
            val quizDetail = payload as ArrayList<QuestionSet>
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


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentScoreCardBinding.inflate(inflater, container, false)
        return binder.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
  super.onViewCreated(view, savedInstanceState)
        binder.fbtnOk.setOnClickListener {
            mFragmentListener?.popTillFragment(FragmentType.QUIZ_CATEGORY_FRAGMENT, 0)
        }
        binder.fbtnShowAnswers.setOnClickListener {

            mFragmentListener?.showFragment(FragmentType.SHOW_ANSWER, quizDetail)
        }
        viewModel.quizDetail.observe(viewLifecycleOwner, {
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

            binder.tvTotQues.text = String.format(Locale.getDefault(), "%02d", totQues)
            binder.tvTotAttemptedQues.text = String.format(Locale.getDefault(), "%02d", totAttemptedQues)
            binder.tvTotCorrectQues.text = String.format(Locale.getDefault(), "%02d", correctAnsCount)
            binder.tvTotWrongQues.text =String.format(Locale.getDefault(), "%02d", totWrongQues)
            binder.tvYourScore.text = String.format(Locale.getDefault(), "%.01f", totScore)

            val percentage = (totScore * 100) / (totQues*2)
            val performance = when(percentage.toInt()){
                in 90..100 -> resources.getString(R.string.title_excellent)
                in 80..90 -> resources.getString(R.string.title_good)
                in 60..79 -> resources.getString(R.string.title_average)
                else -> resources.getString(R.string.title_poor) }
            binder.tvOverAllPerformance.text=performance
        })

        mInterstitialAd = InterstitialAd(activity)
        mInterstitialAd.adUnitId = "ca-app-pub-3940256099942544/103317371" // test ca-app-pub-3940256099942544/103317371
        mInterstitialAd.loadAd(AdRequest.Builder().build())
        mInterstitialAd.adListener= object: AdListener() {
            override fun onAdLoaded() { if (mInterstitialAd.isLoaded) mInterstitialAd.show() }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
