package com.app.quiz.studymaterial.view


import android.content.Context
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.app.quiz.QuizApplication
import com.app.quiz.R
import com.app.quiz.annotation.Status.FAILURE
import com.app.quiz.annotation.Status.SUCCESS
import com.app.quiz.base.BaseFragment
import com.app.quiz.helper.SharedPrefHelper
import com.app.quiz.interfacor.HomeFragmentSelectedListener
import com.app.quiz.network.WebHeader
import com.app.quiz.studymaterial.model.StudyMaterialChapterData
import com.app.quiz.studymaterial.viewmodel.ChapterDetailViewModel
import kotlinx.android.synthetic.main.fragment_chapter_detail.*
import kotlinx.android.synthetic.main.fragment_chapter_detail.textView


class ChapterDetailFragment : BaseFragment() {
    private lateinit var viewModel: ChapterDetailViewModel
    private  var mFragmentListener: HomeFragmentSelectedListener?=null


    companion object {
        fun newInstance() = ChapterDetailFragment()
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
        viewModel = ViewModelProviders.of(this).get(ChapterDetailViewModel::class.java)
        viewModel.chapterId=1 // static !!!
        viewModel.fetchStudyMaterialChapterDetail()
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_chapter_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserver()
        textView?.movementMethod = ScrollingMovementMethod()
        ibtn_close?.setOnClickListener {
            mFragmentListener?.popTopMostFragment()
        }

       // tv_header_title?.text=""

    }

    private fun initObserver() {
        viewModel.isLoading.observe(viewLifecycleOwner,
            Observer {
                if (it) shimmer_frame_layout?.visibility = View.VISIBLE
                else shimmer_frame_layout?.visibility = View.GONE

            })

        viewModel.resultantChapterDetail.observe(viewLifecycleOwner, Observer {
            when(it.status){
                SUCCESS -> it.data?.data?.let { itt -> loadChapterDetails(itt.get(0))}
                FAILURE ->  it.errorMsg?.let { showSnackBar(it) }
            }
        })

    }

    private fun loadChapterDetails(chapterInfo: StudyMaterialChapterData) {
        if (Build.VERSION.SDK_INT >=Build.VERSION_CODES.N) {
            textView.text = Html.fromHtml(chapterInfo.desc, Html.FROM_HTML_MODE_LEGACY)
        } else {
            textView.text = Html.fromHtml(chapterInfo.desc)
        }

        tv_view_count?.text = "Created on : "+chapterInfo.createdAt
        tv_created_on?.text="Viewed by  :"+chapterInfo.viewsCount
    }

}
