package com.app.quiz.studymaterial.view

import android.content.Context
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.app.quiz.QuizApplication
import com.app.quiz.R
import com.app.quiz.studymaterial.viewmodel.StudyMaterialViewModel
import com.app.quiz.annotation.FragmentType
import com.app.quiz.annotation.Status
import com.app.quiz.base.BaseFragment
import com.app.quiz.helper.SharedPrefHelper
import com.app.quiz.interfacor.HomeFragmentSelectedListener
import com.app.quiz.studymaterial.model.StudyMaterialCategory
import kotlinx.android.synthetic.main.fragment_study_material.*
import kotlinx.android.synthetic.main.fragment_study_material.cl_root
import kotlinx.android.synthetic.main.fragment_study_material.ibtn_close
import kotlinx.android.synthetic.main.fragment_study_material.shimmer_frame_layout


class StudyMaterialFragment : BaseFragment() {
    private lateinit var viewModel: StudyMaterialViewModel
    private var mFragmentListener: HomeFragmentSelectedListener? = null
    private var sharedPrefs: SharedPrefHelper? = null
    private var categoryAdapter: CategoryAdapter? = null

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
        viewModel.fetchCategories()
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


    }

    private fun initRecyclerView() {
        rv_category?.apply {
            layoutManager = GridLayoutManager(requireActivity(), 2)
            adapter = categoryAdapter
            categoryAdapter?.setOnItemClickListener(object :
                CategoryAdapter.OnItemClickListener {
                override fun onItemClick(item: StudyMaterialCategory?) {
                    mFragmentListener?.showFragment(FragmentType.CHAPTER_FRAGMENT, item?.id)
                }

                override fun onItemFavouriteClick(item: StudyMaterialCategory?) {
                    viewModel.categoryId=item?.id
                    viewModel.isFavouriteCategory()

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

        viewModel.resultFavouriteCategory.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Status.SUCCESS -> it.data?.message?.let { showSnackBar(it,R.color.colorGreen) }
                Status.FAILURE -> it.errorMsg?.let { showSnackBar(it)}
            }
        })
        viewModel.userPagedList.observe(viewLifecycleOwner, Observer {
            categoryAdapter?.submitList(it)

        })

    }

}
