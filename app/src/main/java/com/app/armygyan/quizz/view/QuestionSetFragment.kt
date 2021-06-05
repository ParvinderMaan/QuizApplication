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
import com.app.armygyan.databinding.FragmentQuestionSetBinding
import com.app.armygyan.dialog.InstructionDialogFragment
import com.app.armygyan.dialog.TimeOverDialogFragment
import com.app.armygyan.helper.SharedPrefHelper
import com.app.armygyan.interfacor.HomeFragmentSelectedListener
import com.app.armygyan.network.WebHeader
import com.app.armygyan.quizz.model.QuestionSet
import com.app.armygyan.quizz.model.QuizDetail
import com.app.armygyan.quizz.viewmodel.QuestionSetViewModel
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
    private var _binding: FragmentQuestionSetBinding? = null
    private val binder get() = _binding!!



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
        return binder.clRoot
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentQuestionSetBinding.inflate(inflater, container, false)
        return binder.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListener()
        initObserver()
        initQuesSetRecyclerView()
        initQuesNoRecyclerView()
        binder.viewFlipper.flipInterval = 3000

        CoroutineScope(Dispatchers.Default).launch {
            delay(1000)
            withContext(Dispatchers.Main) {
                viewModel.isInstructDialogVisibility.value=true
            }
        }

    }


    private fun initListener() {
        binder.ibtnClose.setOnClickListener { showPopupMenu() }
        binder.fbtnFinish.setOnClickListener { showResult() }
    }

    private fun initQuesNoRecyclerView() {
        binder.llQuizDiscriptionI.rvQuestionNo.apply {
            llManagerQuesNo=LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
            layoutManager=llManagerQuesNo
            quesNoAdapter = QuestionNoAdapter()
            adapter = quesNoAdapter
            setHasFixedSize(true)
             quesNoAdapter.setOnItemClickListener(object:
                QuestionNoAdapter.OnItemClickListener{
                override fun onItemClick(pos: Int) {
                    binder.rvQuestionSet.scrollToPosition(pos)
                }
            })

        }

    }

    private fun showResult() {
        timer.cancel()
        mFragmentListener?.showFragment(FragmentType.SCORECARD_FRAGMENT,quesSetAdapter.items)
    }

    private fun initQuesSetRecyclerView() {
        binder.rvQuestionSet.apply {
            llManagerQuesSet = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
            layoutManager=llManagerQuesSet
             quesSetAdapter = QuestionSetAdapter()
            adapter = quesSetAdapter
            quesSetAdapter.setOnItemClickListener(object:QuestionSetAdapter.OnItemClickListener{
                override fun onItemClick(model: QuestionSet, adapterPosition: Int) {
                 quesNoAdapter.refreshItemStatus(model,adapterPosition)
                }
            })
            binder.rvQuestionSet.setHasFixedSize(true)
            val mPagerSnapHelper = PagerSnapHelper()
            mPagerSnapHelper.attachToRecyclerView(binder.rvQuestionSet);
            binder.rvQuestionSet.addOnScrollListener(object : RecyclerView.OnScrollListener() {

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
                                binder.llQuizDiscriptionI.rvQuestionNo.smoothScrollToPosition(visiblePos+5)
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
        val popupMenu = PopupMenu(requireActivity(), binder.ibtnClose)
        val menu = popupMenu.menu
        menu.add(0, 1, 0, getString(R.string.title_show_results))
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
        viewModel.isLoading.observe(viewLifecycleOwner, {
                if (it) binder.shimmerFrameLayout.visibility = View.VISIBLE
                else binder.shimmerFrameLayout.visibility = View.GONE
            })

        viewModel.isInstructDialogVisibility.observe(viewLifecycleOwner, {
                it?.let {
                    if(it) {
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
                        val duration = viewModel.quizDetail.value?.duration?.toInt()
                        duration?.let { startTimer(it.toLong() * 60000) }
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


        viewModel.quizDetail.observe(viewLifecycleOwner, {
                binder.tvHeaderTitle.text=it.name
//                when (it.level) {
//                    "1" -> tv_level_count?.text=resources.getString(R.string.level_beginner)
//                    "2" -> tv_level_count?.text=resources.getString(R.string.level_medium)
//                    "3" -> tv_level_count?.text=resources.getString(R.string.level_expert)
//                }
            })

        viewModel.resultQuizDetail.observe(viewLifecycleOwner, {
            when(it.status){
                Status.SUCCESS ->{
                    viewModel.lstOfQuestions.value=it.data?.data?.quizQuestion
                }
                Status.FAILURE -> it.errorMsg?.let { showSnackBar(it) }
            }
        })

        viewModel.lstOfQuestions.observe(viewLifecycleOwner, {
           if(it.isNotEmpty()) {
               binder.tvTimer.text=it.size.toString().plus(":").plus("00")
               quesSetAdapter.addAll(it)
               quesNoAdapter.addAll(it)
               val item = quesNoAdapter.getItem(0)
               quesNoAdapter.refreshItemIndex(item,mVisiblePos)
               binder.groupContent.visibility=View.VISIBLE
           }else{
               binder.tvEmptyView.visibility = View.VISIBLE
           }
        })


    }

    private fun startTimer(duration:Long) {
        timer = object : CountDownTimer(duration, 1000) {
            override fun onFinish(){
                binder.tvTimer.text="00:00"
                //show Dialog !!!!!
                viewModel.isTimeOverDialogVisibility.value=true
            }

            override fun onTick(millisUntilFinished: Long) {
                secondsRemaining = millisUntilFinished / 1000
                val minutesUntilFinished = secondsRemaining / 60
                val secondsInMinuteUntilFinished = secondsRemaining - minutesUntilFinished * 60
                val secondsStr = secondsInMinuteUntilFinished.toString()
                val abc = "$minutesUntilFinished:${if (secondsStr.length == 2) secondsStr else "0" + secondsStr}"
                binder.tvTimer.text =abc
             //   if(minutesUntilFinished==1L) showSnackBar(getString(R.string.alert_one_minute),R.color.colorYellow)

            }
        }.start()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}

