package com.app.armygyan.studymaterial.view


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.armygyan.QuizApplication
import com.app.armygyan.R
import com.app.armygyan.annotation.FragmentType
import com.app.armygyan.base.BaseFragment
import com.app.armygyan.helper.SharedPrefHelper
import com.app.armygyan.interfacor.HomeFragmentSelectedListener
import com.app.armygyan.network.WebHeader
import com.app.armygyan.studymaterial.model.StudyMaterialChapter
import com.app.armygyan.studymaterial.viewmodel.ChapterViewModel
import com.google.android.gms.ads.AdRequest
import kotlinx.android.synthetic.main.fragment_chapter.*
import kotlinx.android.synthetic.main.fragment_chapter.adView
import kotlinx.android.synthetic.main.fragment_chapter.cl_root
import kotlinx.android.synthetic.main.fragment_chapter.shimmer_frame_layout
import kotlinx.android.synthetic.main.fragment_study_material.ibtn_close
import kotlinx.android.synthetic.main.fragment_study_material.rv_category
import java.util.*


class ChapterFragment : BaseFragment() {
    private var chapterAdapter: ChapterAdapter?=null
    private lateinit var viewModel: ChapterViewModel
    private  var mFragmentListener: HomeFragmentSelectedListener?=null
    private var sharedPrefs: SharedPrefHelper?=null
    private lateinit var accessToken: String

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
        sharedPrefs = (context.applicationContext as QuizApplication).getSharedPrefInstance()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        viewModel = ViewModelProviders.of(this).get(ChapterViewModel::class.java)
        super.onCreate(savedInstanceState)

        arguments?.let { viewModel.categoryId=it.getLong("KEY") }
        accessToken= sharedPrefs?.read(SharedPrefHelper.KEY_ACCESS_TOKEN,"").toString()
        val headerMap = HashMap<String, String>()
        headerMap.put(WebHeader.KEY_CONTENT_TYPE, WebHeader.VAL_CONTENT_TYPE)
        headerMap.put(WebHeader.KEY_ACCEPT, WebHeader.VAL_ACCEPT)
        headerMap.put(WebHeader.KEY_AUTHORIZATION,accessToken)
        viewModel.headers= headerMap
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
             chapterAdapter = ChapterAdapter(Date())
             adapter = chapterAdapter
            chapterAdapter?.setOnItemClickListener(object :
                ChapterAdapter.OnItemClickListener{
                override fun onItemClick(item: StudyMaterialChapter?) {
                    mFragmentListener?.showFragment(FragmentType.CHAPTER_DETAIL_FRAGMENT,item)
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



        viewModel.userPagedList.observe(viewLifecycleOwner, Observer { chapterAdapter?.submitList(it)})

    }

}
