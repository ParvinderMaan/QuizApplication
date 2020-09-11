package com.app.armygyan.quizz.view

import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup

import androidx.appcompat.widget.PopupMenu
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.app.armygyan.QuizApplication
import com.app.armygyan.R
import com.app.armygyan.annotation.FragmentType
import com.app.armygyan.annotation.Status
import com.app.armygyan.base.BaseFragment
import com.app.armygyan.dialog.InstructionDialogFragment
import com.app.armygyan.dialog.TimeOverDialogFragment
import com.app.armygyan.helper.SharedPrefHelper
import com.app.armygyan.interfacor.HomeFragmentSelectedListener
import com.app.armygyan.network.WebHeader
import com.app.armygyan.quizz.model.QuestionSet
import com.app.armygyan.quizz.model.QuizDetail
import com.app.armygyan.quizz.viewmodel.QuestionSetViewModel
import kotlinx.android.synthetic.main.fragment_question_set.*
import kotlinx.android.synthetic.main.layout_quiz_discription_i.*
import kotlinx.coroutines.*
import java.util.*


class QuestionSetFragment : BaseFragment() {
    private lateinit var quesSetAdapter: QuestionSetAdapter
    private lateinit var quesNoAdapter: QuestionNoAdapter
    private lateinit var viewModel: QuestionSetViewModel
    private var mFragmentListener: HomeFragmentSelectedListener? = null
    private lateinit var llManagerQuesSet:LinearLayoutManager
    private lateinit var llManagerQuesNo:LinearLayoutManager
    private var alertDialogFragment:InstructionDialogFragment?=null
    private var timeOverDialogFragment:TimeOverDialogFragment?=null
    private var sharedPrefs: SharedPrefHelper? = null
    private lateinit var accessToken: String
    private var headerMap: HashMap<String, String>? = null
    // Timer objects
    private lateinit var timer: CountDownTimer
    private var secondsRemaining: Long = 0
    var mVisiblePos:Int=0
    companion object {
        fun newInstance(payload: Any?): QuestionSetFragment {
            val fragment = QuestionSetFragment()
            val bundle=Bundle()
            if (payload is QuizDetail) bundle.putParcelable("KEY", payload)
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
        viewModel = ViewModelProviders.of(this).get(QuestionSetViewModel::class.java)
        super.onCreate(savedInstanceState)
        arguments?.let { viewModel.quizDetail.value=it.getParcelable("KEY") }
        accessToken= sharedPrefs?.read(SharedPrefHelper.KEY_ACCESS_TOKEN,"").toString()
        headerMap = HashMap<String, String>()
        headerMap?.put(WebHeader.KEY_CONTENT_TYPE, WebHeader.VAL_CONTENT_TYPE)
        headerMap?.put(WebHeader.KEY_ACCEPT, WebHeader.VAL_ACCEPT)
        headerMap?.put(WebHeader.KEY_AUTHORIZATION,accessToken)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_question_set, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListener()
        initObserver()
        initQuesSetRecyclerView()
        initQuesNoRecyclerView()
        view_flipper?.flipInterval = 3000

        // IO,Main,Default
        CoroutineScope(Dispatchers.Default).launch {
            delay(1000)
            withContext(Dispatchers.Main) {
                viewModel.isInstructDialogVisibility.value=true
            }
        }

    }


    private fun initListener() {
        ibtn_close?.setOnClickListener { showPopupMenu() }
        fbtn_finish?.setOnClickListener { showResult() }
    }

    private fun initQuesNoRecyclerView() {
        rv_question_no?.apply {
            llManagerQuesNo=LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
            layoutManager=llManagerQuesNo
            quesNoAdapter = QuestionNoAdapter()
            adapter = quesNoAdapter
            setHasFixedSize(true)
             quesNoAdapter.setOnItemClickListener(object:
                QuestionNoAdapter.OnItemClickListener{
                override fun onItemClick(pos: Int) {
                    rv_question_set?.scrollToPosition(pos)
                }
            })

        }

    }

    private fun showResult() {
        timer.cancel()
        mFragmentListener?.showFragment(FragmentType.SCORECARD_FRAGMENT,quesSetAdapter.items)
    }

    private fun initQuesSetRecyclerView() {
        rv_question_set?.apply {
            llManagerQuesSet = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
            layoutManager=llManagerQuesSet
             quesSetAdapter = QuestionSetAdapter()
            adapter = quesSetAdapter
            quesSetAdapter.setOnItemClickListener(object:QuestionSetAdapter.OnItemClickListener{
                override fun onItemClick(model: QuestionSet, adapterPosition: Int) {
                 quesNoAdapter.refreshItemStatus(model,adapterPosition)

                }
            })
            rv_question_set.setHasFixedSize(true)
            val mPagerSnapHelper = PagerSnapHelper()
            mPagerSnapHelper.attachToRecyclerView(rv_question_set);
            rv_question_set.addOnScrollListener(object : RecyclerView.OnScrollListener() {

                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    mVisiblePos= llManagerQuesSet.findLastCompletelyVisibleItemPosition()

                    if(mVisiblePos!=RecyclerView.NO_POSITION){
                       // Log.e("QuestionSetFragment", mVisiblePos.toString())
                        val item = quesNoAdapter.getItem(mVisiblePos)
                        quesNoAdapter.refreshItemIndex(item,mVisiblePos)
                    }

                    val visiblePos = llManagerQuesNo.findLastVisibleItemPosition()
                    if(visiblePos==llManagerQuesSet.findFirstCompletelyVisibleItemPosition()){

                        // IO,Main,Default
                        CoroutineScope(Dispatchers.Default).launch {
                            delay(1000)
                            withContext(Dispatchers.Main) {
                                rv_question_no?.smoothScrollToPosition(visiblePos+5)
                            }
                        }
                    }
//                    val visiblePoss = llManagerQuesNo.findFirstVisibleItemPosition()
//                    if(visiblePoss==llManagerQuesSet.findFirstVisibleItemPosition()){
//                        rv_question_no?.smoothScrollToPosition(visiblePoss-1)
//                    }
                }
            })

        }
    }

    private fun showPopupMenu() {
        val popupMenu = PopupMenu(requireActivity(), ibtn_close)
        val menu = popupMenu.menu
        menu.add(0, 1, 0, getString(R.string.title_show_results))
//        menu.add(0, 2, 0, getString(R.string.title_restart))
        menu.add(0, 2, 0, getString(R.string.title_quit_quiz))
        popupMenu.setOnMenuItemClickListener { item: MenuItem ->

            when (item.itemId) {
                1 -> {
                  showResult()}
                2 -> {
                    timer.cancel()
                    mFragmentListener?.popTopMostFragment()
                }
                3 ->{
                    // restart timer !!!!!
                }
            }
            false
        }
        popupMenu.show()
    }


    private fun initObserver() {
        viewModel.isLoading.observe(viewLifecycleOwner,
            Observer {
                if (it) shimmer_frame_layout?.visibility = View.VISIBLE
                else shimmer_frame_layout?.visibility = View.GONE
            })

        viewModel.isInstructDialogVisibility.observe(viewLifecycleOwner, Observer {
                it?.let {
                    if(it) {
                      //  var noOfQues = viewModel.lstOfQuestions.value?.size;
                        val quizDetail = viewModel.quizDetail.value
                        alertDialogFragment = InstructionDialogFragment.newInstance(quizDetail)
                        alertDialogFragment?.show(childFragmentManager, "TAG")
                        alertDialogFragment?.setOnInstructionDialogFragmentListener(object :
                            InstructionDialogFragment.InstructionDialogFragmentListener {
                            override fun onStartClick() {
                                headerMap?.let { it1 -> viewModel.fetchQuizDetail(it1) }
                                viewModel.isInstructDialogVisibility.value=false
                            }
                        })
                    }else{
                        alertDialogFragment?.dismiss()
                     //   val noOfQues = viewModel.quizDetail.value?.quesCount?.toInt()
                        val duration = viewModel.quizDetail.value?.duration?.toInt()
                        duration?.let {
//                            tv_timer?.text=it.toString().plus(":00");
                            startTimer(it.toLong() * 60000)
                        }
                    }
                }
            })

        viewModel.isTimeOverDialogVisibility.observe(viewLifecycleOwner, Observer {
            it?.let {
                if(it) {
                    timeOverDialogFragment = TimeOverDialogFragment.newInstance()
                    timeOverDialogFragment?.show(childFragmentManager, "TAG")
                    timeOverDialogFragment?.setOnTimeOverDialogFragmentListener(object :
                        TimeOverDialogFragment.TimeOverDialogFragmentListener {
                        override fun onSubmitClick() {
                            viewModel.isTimeOverDialogVisibility.value=false
                            showResult()
                        }
                    })
                }else{
                    timeOverDialogFragment?.dismiss()
                }
            }

        })


        viewModel.quizDetail.observe(viewLifecycleOwner,
            Observer {
                tv_header_title?.text=it.name
//                when (it.level) {
//                    "1" -> tv_level_count?.text=resources.getString(R.string.level_beginner)
//                    "2" -> tv_level_count?.text=resources.getString(R.string.level_medium)
//                    "3" -> tv_level_count?.text=resources.getString(R.string.level_expert)
//                }

            })

        viewModel.resultQuizDetail.observe(viewLifecycleOwner, Observer {
            when(it.status){
                Status.SUCCESS ->{
                    viewModel.lstOfQuestions.value=it.data?.data?.quizQuestion
                }
                Status.FAILURE -> it.errorMsg?.let { showSnackBar(it) }
            }
        })

        viewModel.lstOfQuestions.observe(viewLifecycleOwner, Observer {
           if(it.isNotEmpty()) {
              // tv_ques_no_count?.text= it.size.toString()
               tv_timer?.text=it.size.toString().plus(":").plus("00")
               quesSetAdapter.addAll(it)
               quesNoAdapter.addAll(it)
               var item = quesNoAdapter.getItem(0)
               quesNoAdapter.refreshItemIndex(item,mVisiblePos)
               group_content?.visibility=View.VISIBLE
           }else{
               tv_empty_view?.visibility = View.VISIBLE
           }
        })


    }

    private fun startTimer(duration:Long) {
        timer = object : CountDownTimer(duration, 1000) {
            override fun onFinish(){
                tv_timer?.text="00:00"
                //show Dialog !!!!!
                viewModel.isTimeOverDialogVisibility.value=true
            }

            override fun onTick(millisUntilFinished: Long) {
              //  Log.e("onTick","millisUntilFinished");
                secondsRemaining = millisUntilFinished / 1000
                val minutesUntilFinished = secondsRemaining / 60
                val secondsInMinuteUntilFinished = secondsRemaining - minutesUntilFinished * 60
                val secondsStr = secondsInMinuteUntilFinished.toString()
                var abc = "$minutesUntilFinished:${if (secondsStr.length == 2) secondsStr else "0" + secondsStr}"
                tv_timer?.text =abc
             //   if(minutesUntilFinished==1L) showSnackBar(getString(R.string.alert_one_minute),R.color.colorYellow)

            }
        }.start()
    }


}

