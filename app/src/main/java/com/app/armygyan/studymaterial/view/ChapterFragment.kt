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
import com.app.armygyan.databinding.FragmentChapterBinding
import com.app.armygyan.helper.SharedPrefHelper
import com.app.armygyan.interfacor.HomeFragmentSelectedListener
import com.app.armygyan.network.WebHeader
import com.app.armygyan.studymaterial.model.StudyMaterialChapter
import com.app.armygyan.studymaterial.viewmodel.ChapterViewModel
import com.google.android.gms.ads.AdRequest
import java.util.*


class ChapterFragment : BaseFragment() {
    private var chapterAdapter: ChapterAdapter?=null
    private lateinit var viewModel: ChapterViewModel
    private  var mFragmentListener: HomeFragmentSelectedListener?=null
    private var sharedPrefs: SharedPrefHelper?=null
    private lateinit var accessToken: String
    private var _binding: FragmentChapterBinding? = null
    private val binder get() = _binding!!

    companion object {
        fun newInstance(payload: Any?): ChapterFragment {
            val fragment = ChapterFragment()
            val bundle=Bundle()
            if (payload is Long) bundle.putLong(KEY_ARGS, payload.toLong())
            fragment.arguments=bundle
            return fragment
        }

        const val KEY_ARGS="KEY"
    }

    override fun getRootView(): View {
       return binder.clRoot
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mFragmentListener = context as HomeFragmentSelectedListener
        sharedPrefs = (context.applicationContext as QuizApplication).getSharedPrefInstance()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        viewModel = ViewModelProviders.of(this).get(ChapterViewModel::class.java)
        super.onCreate(savedInstanceState)

        arguments?.let { viewModel.categoryId=it.getLong(KEY_ARGS) }
        accessToken= sharedPrefs?.read(SharedPrefHelper.KEY_ACCESS_TOKEN,"").toString()
        val headerMap = HashMap<String, String>()
        headerMap[WebHeader.KEY_CONTENT_TYPE] = WebHeader.VAL_CONTENT_TYPE
        headerMap[WebHeader.KEY_ACCEPT] = WebHeader.VAL_ACCEPT
        headerMap[WebHeader.KEY_AUTHORIZATION] = accessToken
        viewModel.headers= headerMap
        viewModel.fetchChapters()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View{
        _binding = FragmentChapterBinding.inflate(inflater, container, false)
         return binder.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserver()
        binder.ibtnClose.setOnClickListener {
            mFragmentListener?.popTopMostFragment()
        }
        binder.rvCategory.apply {
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
        binder.adView.loadAd(adRequest)

    }

    private fun initObserver() {
        viewModel.isLoading.observe(viewLifecycleOwner, {
                if (it) binder.shimmerFrameLayout.visibility = View.VISIBLE
                else binder.shimmerFrameLayout.visibility = View.GONE
            })


        viewModel.isListEmpty.observe(viewLifecycleOwner, {
            if (it) binder.tvEmptyView.visibility = View.VISIBLE })



        viewModel.userPagedList.observe(viewLifecycleOwner, { chapterAdapter?.submitList(it)})

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}
