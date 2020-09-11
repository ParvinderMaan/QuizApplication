package com.app.armygyan.studymaterial.view


import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.text.Html
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.app.armygyan.QuizApplication
import com.app.armygyan.R
import com.app.armygyan.annotation.Language
import com.app.armygyan.annotation.Status.FAILURE
import com.app.armygyan.annotation.Status.SUCCESS
import com.app.armygyan.base.BaseFragment
import com.app.armygyan.helper.SharedPrefHelper
import com.app.armygyan.interfacor.HomeFragmentSelectedListener
import com.app.armygyan.network.WebHeader
import com.app.armygyan.studymaterial.model.StudyMaterialChapter
import com.app.armygyan.studymaterial.model.StudyMaterialChapterData
import com.app.armygyan.studymaterial.viewmodel.ChapterDetailViewModel
import com.google.android.gms.ads.AdRequest
import kotlinx.android.synthetic.main.fragment_chapter_detail.*
import kotlinx.android.synthetic.main.fragment_chapter_detail.cl_root
import kotlinx.android.synthetic.main.fragment_chapter_detail.ibtn_close
import kotlinx.android.synthetic.main.fragment_chapter_detail.tv_header_title
import kotlinx.android.synthetic.main.fragment_setting.*
import java.util.HashMap


class ChapterDetailFragment : BaseFragment() {
    private var chapterInfo: StudyMaterialChapter?=null
    private lateinit var viewModel: ChapterDetailViewModel
    private  var mFragmentListener: HomeFragmentSelectedListener?=null
    private var sharedPrefs: SharedPrefHelper?=null
    private lateinit var accessToken: String
    private var headerMap: HashMap<String, String>? = null

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
        sharedPrefs = (context.applicationContext as QuizApplication).getSharedPrefInstance()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ChapterDetailViewModel::class.java)
        arguments?.let { chapterInfo = it.getParcelable("KEY")
            viewModel.chapterId= chapterInfo?.chapterId?.toLong() }
        accessToken= sharedPrefs?.read(SharedPrefHelper.KEY_ACCESS_TOKEN,"").toString()
        headerMap = HashMap<String, String>()
        headerMap?.put(WebHeader.KEY_CONTENT_TYPE, WebHeader.VAL_CONTENT_TYPE)
        headerMap?.put(WebHeader.KEY_ACCEPT, WebHeader.VAL_ACCEPT)
        headerMap?.put(WebHeader.KEY_AUTHORIZATION,accessToken)
        headerMap?.let {viewModel.fetchStudyMaterialChapterDetail(it) }

    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_chapter_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserver()
      //  textView?.movementMethod = ScrollingMovementMethod()
        ibtn_close?.setOnClickListener {
            mFragmentListener?.popTopMostFragment()
        }
        chapterInfo?.chapterName?.let { tv_header_title?.text=it[0].toUpperCase().plus(it.substring(1)) }

        ibtn_more?.setOnClickListener { showPopupMenu() }

        val adRequest = AdRequest.Builder().build()
        adView?.setBackgroundColor(ContextCompat.getColor(activity as Context,R.color.colorWhite))
        adView?.loadAd(adRequest)

    }

    private fun initObserver() {
        viewModel.isLoading.observe(viewLifecycleOwner,
            Observer {

//                if (it) shimmer_frame_layout?.visibility = View.VISIBLE
//                else shimmer_frame_layout?.visibility = View.GONE

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
            @Suppress("DEPRECATION")
            textView?.text = Html.fromHtml(chapterInfo.desc)
        }

    }

    private fun showPopupMenu() {
        val popupMenu = PopupMenu(requireActivity(), ibtn_more)
        val menu = popupMenu.menu
        menu.add(0, 1, 0, getString(R.string.title_small))
        menu.add(0, 2, 0, getString(R.string.title_medium))
        menu.add(0, 3, 0, getString(R.string.title_large))
        popupMenu.setOnMenuItemClickListener { item: MenuItem ->
            when (item.itemId) {
                1 -> {
                    textView?.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f);
                }
                2 -> {
                    textView?.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f);
                }
                3 -> {
                    textView?.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f);
                }
            }
            false
        }
        popupMenu.show()
    }
}
