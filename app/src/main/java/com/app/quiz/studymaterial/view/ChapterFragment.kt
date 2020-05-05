package com.app.quiz.studymaterial.view


import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.quiz.QuizApplication
import com.app.quiz.R
import com.app.quiz.annotation.FragmentType
import com.app.quiz.annotation.Status
import com.app.quiz.base.BaseFragment
import com.app.quiz.helper.SharedPrefHelper
import com.app.quiz.interfacor.HomeFragmentSelectedListener
import com.app.quiz.network.WebHeader
import com.app.quiz.studymaterial.viewmodel.ChapterViewModel
import com.app.quiz.studymaterial.viewmodel.StudyMaterialViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_chapter.*
import kotlinx.android.synthetic.main.fragment_study_material.ibtn_close
import kotlinx.android.synthetic.main.fragment_study_material.rv_category
import java.util.HashMap


class ChapterFragment : BaseFragment() {
    private var chapterAdapter: ChapterAdapter?=null
    private lateinit var viewModel: ChapterViewModel
    private  var mFragmentListener: HomeFragmentSelectedListener?=null

    companion object {
        fun newInstance() = ChapterFragment()
    }

    override fun getRootView(): View {
       return cl_root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mFragmentListener = context as HomeFragmentSelectedListener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ChapterViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_chapter, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserver()
        ibtn_close?.setOnClickListener {
            mFragmentListener?.popTopMostFragment()
        }
        rv_category?.apply {
            layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
             chapterAdapter = ChapterAdapter()
             adapter = chapterAdapter
            chapterAdapter?.setOnItemClickListener(object :
                ChapterAdapter.OnItemClickListener{
                override fun onItemClick() {
                    mFragmentListener?.showFragment(FragmentType.CHAPTER_DETAIL_FRAGMENT,null)
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



        viewModel.userPagedList.observe(viewLifecycleOwner, Observer { chapterAdapter?.submitList(it)})

    }

}
