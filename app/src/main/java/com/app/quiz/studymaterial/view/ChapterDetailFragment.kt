package com.app.quiz.studymaterial.view


import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.text.Html
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.app.quiz.R
import com.app.quiz.annotation.Status.FAILURE
import com.app.quiz.annotation.Status.SUCCESS
import com.app.quiz.base.BaseFragment
import com.app.quiz.interfacor.HomeFragmentSelectedListener
import com.app.quiz.studymaterial.model.StudyMaterialChapter
import com.app.quiz.studymaterial.model.StudyMaterialChapterData
import com.app.quiz.studymaterial.viewmodel.ChapterDetailViewModel
import kotlinx.android.synthetic.main.fragment_chapter_detail.*


class ChapterDetailFragment : BaseFragment() {
    private var chapterInfo: StudyMaterialChapter?=null
    private lateinit var viewModel: ChapterDetailViewModel
    private  var mFragmentListener: HomeFragmentSelectedListener?=null


    companion object {
        fun newInstance(payload: Any?): ChapterDetailFragment {
            var fragment = ChapterDetailFragment()
            var bundle=Bundle()
            if (payload is StudyMaterialChapter) bundle.putParcelable("KEY", payload)
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
        viewModel = ViewModelProviders.of(this).get(ChapterDetailViewModel::class.java)
        super.onCreate(savedInstanceState)
        arguments?.let {
             chapterInfo = it.getParcelable("KEY")
             viewModel.chapterId= chapterInfo?.chapterId?.toLong()
           //  viewModel.chapterId=1                                // static remove !!!!!!!
        }


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

        tv_header_title?.text=chapterInfo?.chapterName
        tv_chapter_created_on?.text=chapterInfo?.createdOn?.let{it.split(" ")[0]}

    }

    private fun initObserver() {
        viewModel.isLoading.observe(viewLifecycleOwner,
            Observer {
                if (it) shimmer_frame_layout?.visibility = View.VISIBLE
                else shimmer_frame_layout?.visibility = View.GONE

            })

        viewModel.resultantChapterDetail.observe(viewLifecycleOwner, Observer {
            when(it.status){
                SUCCESS -> it.data?.data?.let { itt -> loadChapterDetails(itt)}
                FAILURE ->  it.errorMsg?.let { showSnackBar(it) }
            }
        })

    }

    private fun loadChapterDetails(chapterInfo: StudyMaterialChapterData) {
        if (Build.VERSION.SDK_INT >=Build.VERSION_CODES.N) {
            textView?.text = Html.fromHtml(chapterInfo.desc, Html.FROM_HTML_MODE_LEGACY)
        } else {
            textView?.text = Html.fromHtml(chapterInfo.desc)
        }

//        tv_view_count?.text="Created on : ".plus(chapterInfo?.createdAt.split(" ")[0])
//        tv_created_on?.text="Viewed by  :".plus(chapterInfo?.viewsCount)

    }

}
