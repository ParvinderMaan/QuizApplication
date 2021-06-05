package com.app.armygyan.studymaterial.view

import android.content.Context
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.armygyan.QuizApplication
import com.app.armygyan.studymaterial.viewmodel.StudyMaterialViewModel
import com.app.armygyan.annotation.FragmentType
import com.app.armygyan.annotation.Status
import com.app.armygyan.base.BaseFragment
import com.app.armygyan.databinding.FragmentStudyMaterialBinding
import com.app.armygyan.helper.SharedPrefHelper
import com.app.armygyan.interfacor.HomeFragmentSelectedListener
import com.app.armygyan.network.WebHeader
import com.app.armygyan.studymaterial.model.StudyMaterialCategory
import com.google.android.gms.ads.AdRequest
import java.util.HashMap


class StudyMaterialFragment : BaseFragment() {
    private lateinit var viewModel: StudyMaterialViewModel
    private var mFragmentListener: HomeFragmentSelectedListener? = null
    private var sharedPrefs: SharedPrefHelper? = null
    private var categoryAdapter: CategoryAdapter? = null
    private lateinit var accessToken: String
    private var headerMap: HashMap<String, String>? = null
    private var _binding: FragmentStudyMaterialBinding? = null
    private val binder get() = _binding!!

    companion object {
        fun newInstance() = StudyMaterialFragment()
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
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(StudyMaterialViewModel::class.java)
        categoryAdapter = CategoryAdapter()
        accessToken= sharedPrefs?.read(SharedPrefHelper.KEY_ACCESS_TOKEN,"").toString()
        headerMap = HashMap<String, String>()
        headerMap?.put(WebHeader.KEY_CONTENT_TYPE, WebHeader.VAL_CONTENT_TYPE)
        headerMap?.put(WebHeader.KEY_ACCEPT, WebHeader.VAL_ACCEPT)
        headerMap?.put(WebHeader.KEY_AUTHORIZATION,accessToken)
        headerMap?.let { viewModel.fetchCategories(it)}
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentStudyMaterialBinding.inflate(inflater, container, false)
         return binder.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        initObserver()
        binder.ibtnClose.setOnClickListener {
            mFragmentListener?.popTopMostFragment()
        }

        val adRequest = AdRequest.Builder().build()
        binder.adView.loadAd(adRequest)
    }

    private fun initRecyclerView() {
        binder.rvCategory.apply {
            layoutManager=LinearLayoutManager(requireActivity())
            adapter = categoryAdapter
            categoryAdapter?.setOnItemClickListener(itemClickListener)

        }
    }

    private fun initObserver() {
        viewModel.isLoading.observe(viewLifecycleOwner, {
                if (it) binder.shimmerFrameLayout.visibility = View.VISIBLE
                else binder.shimmerFrameLayout.visibility = View.GONE
            })

        viewModel.isListEmpty.observe(viewLifecycleOwner,
            { if (it) binder.tvEmptyView.visibility = View.VISIBLE })


        viewModel.lstOfCategories.observe(viewLifecycleOwner, {
           if(it.isNotEmpty()) {
               categoryAdapter?.clearAll()
               val topList: List<StudyMaterialCategory> = it.filter { it.isFavourite }
               val bottomList: List<StudyMaterialCategory> = it.filter { !it.isFavourite }
               categoryAdapter?.addAll(topList)
               categoryAdapter?.addAll(bottomList)
           }
           else viewModel.isListEmpty.value=true
        })

        viewModel.resultantStudyMaterialCategory.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Status.SUCCESS -> it.data?.categories.let { viewModel.lstOfCategories.value=it }
                Status.FAILURE -> it.errorMsg?.let { showSnackBar(it)}
            }
        })

    }


    val itemClickListener: CategoryAdapter.OnItemClickListener  = object : CategoryAdapter.OnItemClickListener {
        override fun onItemClick(item: StudyMaterialCategory?) {
            mFragmentListener?.showFragment(FragmentType.CHAPTER_FRAGMENT, item?.id)
            return
        }

        override fun onItemFavouriteClick(item: StudyMaterialCategory?, adapterPosition: Int) {
            viewModel.categoryId=item?.id
            item?.let { categoryAdapter?.refreshUi(it,adapterPosition) }
            headerMap?.let { viewModel.isFavouriteCategory(it) }
            return
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}
