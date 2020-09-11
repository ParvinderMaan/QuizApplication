package com.app.armygyan.quizz.view


import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.armygyan.QuizApplication
import com.app.armygyan.R
import com.app.armygyan.annotation.FragmentType
import com.app.armygyan.helper.SharedPrefHelper
import com.app.armygyan.interfacor.HomeFragmentSelectedListener
import com.app.armygyan.network.WebHeader
import com.app.armygyan.quizz.model.QuizDetail
import com.app.armygyan.quizz.viewmodel.QuizCategoryViewModel
import com.google.android.gms.ads.AdRequest
import kotlinx.android.synthetic.main.fragment_quiz_category.*
import kotlinx.android.synthetic.main.fragment_quiz_category.adView
import kotlinx.android.synthetic.main.fragment_quiz_category.ibtn_close
import kotlinx.android.synthetic.main.fragment_quiz_category.shimmer_frame_layout
import kotlinx.android.synthetic.main.fragment_quiz_category.tv_empty_view
import kotlinx.android.synthetic.main.fragment_study_material.*
import java.util.HashMap


class QuizCategoryFragment : Fragment() {
    private var quizCategoryAdapter: QuizCategoryAdapter?=null
    private var mFragmentListener: HomeFragmentSelectedListener? = null
    private lateinit var viewModel: QuizCategoryViewModel
    private var sharedPrefs: SharedPrefHelper? = null

    companion object {
        fun newInstance() = QuizCategoryFragment()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mFragmentListener = context as HomeFragmentSelectedListener
        sharedPrefs = (context.applicationContext as QuizApplication).getSharedPrefInstance()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(QuizCategoryViewModel::class.java)
        var accessToken = sharedPrefs?.read(SharedPrefHelper.KEY_ACCESS_TOKEN, "").toString()
        var headerMap = HashMap<String, String>()
        headerMap.put(WebHeader.KEY_CONTENT_TYPE, WebHeader.VAL_CONTENT_TYPE)
        headerMap.put(WebHeader.KEY_ACCEPT, WebHeader.VAL_ACCEPT)
        headerMap.put(WebHeader.KEY_AUTHORIZATION,accessToken)
        viewModel.headers=headerMap
        viewModel.fetchAllQuiz()
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
                override fun onItemClick(quiz: QuizDetail?) {
                    mFragmentListener?.showFragment(FragmentType.QUESTION_SET_FRAGMENT,quiz)
                }
            })
        }

        val adRequest = AdRequest.Builder().build()
        adView?.loadAd(adRequest)
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


