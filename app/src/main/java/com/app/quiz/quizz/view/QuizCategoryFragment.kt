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
import com.app.quiz.R
import com.app.quiz.annotation.FragmentType
import com.app.quiz.interfacor.HomeFragmentSelectedListener
import com.app.quiz.quizz.model.QuizDetail
import com.app.quiz.quizz.viewmodel.QuizCategoryViewModel
import kotlinx.android.synthetic.main.fragment_quiz_category.*
import kotlinx.android.synthetic.main.fragment_quiz_category.ibtn_close


class QuizCategoryFragment : Fragment() {
    private var quizCategoryAdapter: QuizCategoryAdapter?=null
    private var mFragmentListener: HomeFragmentSelectedListener? = null
    private lateinit var viewModel: QuizCategoryViewModel

    companion object {
        fun newInstance() = QuizCategoryFragment()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mFragmentListener = context as HomeFragmentSelectedListener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(QuizCategoryViewModel::class.java)

    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_quiz_category, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserver()
        ibtn_close?.setOnClickListener {
            mFragmentListener?.popTopMostFragment()
        }

        rv_quiz_category?.apply {
            layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
             quizCategoryAdapter = QuizCategoryAdapter()
            adapter = quizCategoryAdapter

            quizCategoryAdapter?.setOnItemClickListener(object :
                QuizCategoryAdapter.OnItemClickListener{
                override fun onItemClick(quizDetail: QuizDetail?) {
                    mFragmentListener?.showFragment(FragmentType.QUESTION_SET_FRAGMENT,quizDetail)
                }
            })
        }

    }


    private fun initObserver() {
        viewModel.isLoading.observe(viewLifecycleOwner,
            Observer {
                if (it) shimmer_frame_layout?.visibility = View.VISIBLE
                else shimmer_frame_layout?.visibility = View.GONE
            })


        viewModel.isListEmpty.observe(viewLifecycleOwner,
            Observer { if (it) tv_empty_view?.visibility = View.VISIBLE })

        viewModel.userPagedList.observe(viewLifecycleOwner, Observer { quizCategoryAdapter?.submitList(it)})



    }

}


