package com.app.quiz.quizz.view

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView

import com.app.quiz.R
import com.app.quiz.annotation.FragmentType
import com.app.quiz.interfacor.HomeFragmentSelectedListener
import com.app.quiz.quizz.model.QuestionSet
import com.app.quiz.quizz.viewmodel.AnswerViewModel
import kotlinx.android.synthetic.main.fragment_answer.*
import kotlinx.android.synthetic.main.fragment_answer.tv_header_title
import java.util.ArrayList


class AnswerFragment : Fragment() {
    private var mFragmentListener: HomeFragmentSelectedListener? = null
    private var quizDetail: ArrayList<QuestionSet>?=null
    private lateinit var linearLayoutManager:LinearLayoutManager
    private lateinit var answerAdapter: AnswerAdapter
    private lateinit var viewModel: AnswerViewModel
    private var mVisiblePos:Int=0


    companion object {
        @Suppress("UNCHECKED_CAST")
        fun newInstance(payload: Any?):AnswerFragment{
            var fragment =AnswerFragment()
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
        viewModel = ViewModelProviders.of(this).get(AnswerViewModel::class.java)

        // collect our intent
        if (arguments != null) {
            quizDetail = arguments?.getParcelableArrayList("KEY")
            quizDetail?.let { viewModel.quizDetail.value=it
            }
//            quizDetail?.forEach { println(it.toString()) }
        }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_answer, container, false)
    }
        // mFragmentListener?.popTopMostFragment()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tv_header_title?.text=resources.getString(R.string.title_answers)
        fbtn_previous?.setOnClickListener {
            rv_answer_set?.smoothScrollToPosition(mVisiblePos-1)


        }
        fbtn_next?.setOnClickListener {
            rv_answer_set?.smoothScrollToPosition(mVisiblePos+1)
        }

       // fbtn_previous.show()
        initAnsSetRecyclerView()
        viewModel.quizDetail.observe(viewLifecycleOwner, Observer {
            if(it.isNotEmpty()) {
                answerAdapter.addAll(it)
            }
        })

    }


    private fun initAnsSetRecyclerView() {
        rv_answer_set?.apply {
            linearLayoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
            layoutManager=linearLayoutManager
            answerAdapter = AnswerAdapter()
            adapter = answerAdapter
            rv_answer_set.setHasFixedSize(true)
            val mPagerSnapHelper = PagerSnapHelper()
            mPagerSnapHelper.attachToRecyclerView(rv_answer_set);
            rv_answer_set.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    mVisiblePos= linearLayoutManager.findLastCompletelyVisibleItemPosition()
                    if((answerAdapter.items.size-1)==mVisiblePos){
                        if(fbtn_next.isShown)  fbtn_next.hide()
                    }else{
                        if(!fbtn_next.isShown)  fbtn_next.show()
                    }
                    if(0==mVisiblePos){
                        if(fbtn_previous.isShown)  fbtn_previous.hide()
                    }else{
                        if(!fbtn_previous.isShown)  fbtn_previous.show()
                    }

                }
            })

        }
    }

}
