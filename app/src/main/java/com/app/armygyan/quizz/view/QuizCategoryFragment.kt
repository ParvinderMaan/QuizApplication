package com.app.armygyan.quizz.view


import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.armygyan.QuizApplication
import com.app.armygyan.annotation.FragmentType
import com.app.armygyan.databinding.FragmentQuizCategoryBinding
import com.app.armygyan.helper.SharedPrefHelper
import com.app.armygyan.interfacor.HomeFragmentSelectedListener
import com.app.armygyan.network.WebHeader
import com.app.armygyan.quizz.model.QuizDetail
import com.app.armygyan.quizz.viewmodel.QuizCategoryViewModel
import com.google.android.gms.ads.AdRequest
import java.util.HashMap


class QuizCategoryFragment : Fragment() {
    private var quizCategoryAdapter: QuizCategoryAdapter?=null
    private var mFragmentListener: HomeFragmentSelectedListener? = null
    private lateinit var viewModel: QuizCategoryViewModel
    private var sharedPrefs: SharedPrefHelper? = null
    private var _binding: FragmentQuizCategoryBinding? = null
    private val  binder get() = _binding!!

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
        val accessToken = sharedPrefs?.read(SharedPrefHelper.KEY_ACCESS_TOKEN, "").toString()
        val headerMap = HashMap<String, String>()
        headerMap[WebHeader.KEY_CONTENT_TYPE] = WebHeader.VAL_CONTENT_TYPE
        headerMap[WebHeader.KEY_ACCEPT] = WebHeader.VAL_ACCEPT
        headerMap[WebHeader.KEY_AUTHORIZATION] = accessToken
        viewModel.headers=headerMap
        viewModel.fetchAllQuiz()
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentQuizCategoryBinding.inflate(inflater, container, false)
        return binder.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserver()
        binder.ibtnClose.setOnClickListener {
            mFragmentListener?.popTopMostFragment()
        }

        binder.rvQuizCategory.apply {
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
        binder.adView.loadAd(adRequest)
    }


    private fun initObserver() {
        viewModel.isLoading.observe(viewLifecycleOwner, {
                if (it)  binder.shimmerFrameLayout.visibility = View.VISIBLE
                else  binder.shimmerFrameLayout.visibility = View.GONE
            })


        viewModel.isListEmpty.observe(viewLifecycleOwner, {
            if (it)  binder.tvEmptyView.visibility = View.VISIBLE })

        viewModel.userPagedList.observe(viewLifecycleOwner, { quizCategoryAdapter?.submitList(it)})



    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}


