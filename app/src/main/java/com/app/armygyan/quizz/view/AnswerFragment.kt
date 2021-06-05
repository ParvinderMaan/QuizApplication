package com.app.armygyan.quizz.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView

import com.app.armygyan.R
import com.app.armygyan.base.BaseFragment
import com.app.armygyan.databinding.FragmentAnswerBinding
import com.app.armygyan.dialog.ExplanationBottomSheetFragment
import com.app.armygyan.interfacor.HomeFragmentSelectedListener
import com.app.armygyan.quizz.model.QuestionSet
import com.app.armygyan.quizz.viewmodel.AnswerViewModel
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import java.util.ArrayList


class AnswerFragment : BaseFragment() {
    private var mFragmentListener: HomeFragmentSelectedListener? = null
    private var quizDetail: ArrayList<QuestionSet>?=null
    private lateinit var linearLayoutManager:LinearLayoutManager
    private lateinit var answerAdapter: AnswerAdapter
    private lateinit var viewModel: AnswerViewModel
    private var mVisiblePos:Int=0
    private lateinit var mInterstitialAd: InterstitialAd
    private var _binding: FragmentAnswerBinding? = null
    private val binder get() = _binding!!


    companion object {
        @Suppress("UNCHECKED_CAST")
        fun newInstance(payload: Any?):AnswerFragment{
            val fragment =AnswerFragment()
            val bundle = Bundle()
            val quizDetail = payload as ArrayList<QuestionSet>
            bundle.putParcelableArrayList("KEY", quizDetail)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun getRootView(): View {
        TODO("Not yet implemented")
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        mFragmentListener = context as HomeFragmentSelectedListener
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(AnswerViewModel::class.java)

        if (arguments != null) {
            quizDetail = arguments?.getParcelableArrayList("KEY")
            quizDetail?.let { viewModel.quizDetail.value=it
            }
        }


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View{
        _binding = FragmentAnswerBinding.inflate(inflater, container, false)
        return binder.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binder.tvHeaderTitle.text=resources.getString(R.string.title_answers)
        binder.tvPrevious.setOnClickListener {
            if(mVisiblePos>=1) binder.rvAnswerSet.smoothScrollToPosition(mVisiblePos-1)


        }
        binder.tvNext.setOnClickListener {
            if(mVisiblePos<answerAdapter.itemCount)  binder.rvAnswerSet.smoothScrollToPosition(mVisiblePos+1)
        }

        binder.ibtnClose.setOnClickListener { mFragmentListener?.popTopMostFragment() }

        initAnsSetRecyclerView()
        viewModel.quizDetail.observe(viewLifecycleOwner, Observer {
            if(it.isNotEmpty()) {
                answerAdapter.addAll(it)
            }
        })

        mInterstitialAd = InterstitialAd(activity)
        mInterstitialAd.adUnitId = "ca-app-pub-3940256099942544/103317371" // test ca-app-pub-3940256099942544/103317371
        mInterstitialAd.loadAd(AdRequest.Builder().build())
        mInterstitialAd.adListener= object: AdListener() {
            override fun onAdLoaded() {
                if (mInterstitialAd.isLoaded) mInterstitialAd.show()
            }
        }
    }


    private fun initAnsSetRecyclerView() {
        binder.rvAnswerSet.apply {
            linearLayoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
            layoutManager=linearLayoutManager
            answerAdapter = AnswerAdapter()
            answerAdapter.setOnItemClickListener(itemClickListener)
            adapter = answerAdapter
            binder.rvAnswerSet.setHasFixedSize(true)
            val mPagerSnapHelper = PagerSnapHelper()
            mPagerSnapHelper.attachToRecyclerView(binder.rvAnswerSet)
            binder.rvAnswerSet.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    mVisiblePos= linearLayoutManager.findLastCompletelyVisibleItemPosition()
                    if((answerAdapter.items.size-1)==mVisiblePos){
                        if(binder.tvNext.isEnabled)  binder.tvNext.isEnabled=false
                    }else{
                        if(!binder.tvNext.isEnabled)  binder.tvNext.isEnabled=true
                    }
                    if(0==mVisiblePos){
                        if(binder.tvPrevious.isEnabled)  binder.tvPrevious.isEnabled=false
                    }else{
                        if(!binder.tvPrevious.isEnabled)  binder.tvPrevious.isEnabled=true
                    }

                }
            })

        }
    }

    var itemClickListener:AnswerAdapter.OnItemClickListener= object :AnswerAdapter.OnItemClickListener{
        override fun onItemClick(model: QuestionSet,pos:Int) {
            model.explanation.let {
                val alertDialogFragment: ExplanationBottomSheetFragment = ExplanationBottomSheetFragment.newInstance(model.explanation)
                alertDialogFragment.show(childFragmentManager, "TAG")
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
