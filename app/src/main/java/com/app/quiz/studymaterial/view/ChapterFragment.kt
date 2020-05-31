package com.app.quiz.studymaterial.view


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.quiz.R
import com.app.quiz.annotation.FragmentType
import com.app.quiz.base.BaseFragment
import com.app.quiz.interfacor.HomeFragmentSelectedListener
import com.app.quiz.studymaterial.model.StudyMaterialChapter
import com.app.quiz.studymaterial.viewmodel.ChapterViewModel
import kotlinx.android.synthetic.main.fragment_chapter.*
import kotlinx.android.synthetic.main.fragment_study_material.ibtn_close
import kotlinx.android.synthetic.main.fragment_study_material.rv_category


class ChapterFragment : BaseFragment() {
    private var chapterAdapter: ChapterAdapter?=null
    private lateinit var viewModel: ChapterViewModel
    private  var mFragmentListener: HomeFragmentSelectedListener?=null

    companion object {
        fun newInstance(payload: Any?): ChapterFragment {
            var fragment = ChapterFragment()
            var bundle=Bundle()
            if (payload is Long) bundle.putLong("KEY", payload.toLong())
            fragment.arguments=bundle
            return fragment
        }
    }

    override fun getRootView(): View {
       return cl_root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mFragmentListener = context as HomeFragmentSelectedListener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        viewModel = ViewModelProviders.of(this).get(ChapterViewModel::class.java)
        super.onCreate(savedInstanceState)
        arguments?.let {
            viewModel.categoryId=it.getLong("KEY")
        }

        viewModel.fetchChapters()
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
                override fun onItemClick(item: StudyMaterialChapter?) {
                    mFragmentListener?.showFragment(FragmentType.CHAPTER_DETAIL_FRAGMENT,item)
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
