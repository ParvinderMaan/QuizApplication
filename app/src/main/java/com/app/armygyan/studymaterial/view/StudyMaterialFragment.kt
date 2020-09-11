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
import com.app.armygyan.R
import com.app.armygyan.studymaterial.viewmodel.StudyMaterialViewModel
import com.app.armygyan.annotation.FragmentType
import com.app.armygyan.annotation.Status
import com.app.armygyan.base.BaseFragment
import com.app.armygyan.helper.SharedPrefHelper
import com.app.armygyan.interfacor.HomeFragmentSelectedListener
import com.app.armygyan.network.WebHeader
import com.app.armygyan.studymaterial.model.StudyMaterialCategory
import com.google.android.gms.ads.AdRequest
import kotlinx.android.synthetic.main.fragment_study_material.adView
import kotlinx.android.synthetic.main.fragment_study_material.cl_root
import kotlinx.android.synthetic.main.fragment_study_material.ibtn_close
import kotlinx.android.synthetic.main.fragment_study_material.rv_category
import kotlinx.android.synthetic.main.fragment_study_material.shimmer_frame_layout
import kotlinx.android.synthetic.main.fragment_study_material.tv_empty_view
import java.util.HashMap


class StudyMaterialFragment : BaseFragment() {
    private lateinit var viewModel: StudyMaterialViewModel
    private var mFragmentListener: HomeFragmentSelectedListener? = null
    private var sharedPrefs: SharedPrefHelper? = null
    private var categoryAdapter: CategoryAdapter? = null
    private lateinit var accessToken: String
    private var headerMap: HashMap<String, String>? = null
    companion object {
        fun newInstance() = StudyMaterialFragment()
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


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_study_material, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        initObserver()
        ibtn_close?.setOnClickListener {
            mFragmentListener?.popTopMostFragment()
        }

        val adRequest = AdRequest.Builder().build()
        adView?.loadAd(adRequest)
    }

    private fun initRecyclerView() {
        rv_category?.apply {
            layoutManager=LinearLayoutManager(requireActivity())
            adapter = categoryAdapter
            categoryAdapter?.setOnItemClickListener(itemClickListener)

        }
    }

    private fun initObserver() {
        viewModel.isLoading.observe(viewLifecycleOwner,
            Observer {
                if (it) shimmer_frame_layout?.visibility = View.VISIBLE
                else shimmer_frame_layout?.visibility = View.GONE

            })

        viewModel.isListEmpty.observe(viewLifecycleOwner, Observer { if (it) tv_empty_view?.visibility = View.VISIBLE })

//        viewModel.resultFavouriteCategory.observe(viewLifecycleOwner, Observer {
//            when (it.status) {
//                Status.SUCCESS -> it.data?.message?.let { showSnackBar(it,R.color.colorGreen) }
//                Status.FAILURE -> it.errorMsg?.let { showSnackBar(it)}
//            }
//        })

        viewModel.lstOfCategories.observe(viewLifecycleOwner, Observer {
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




}
