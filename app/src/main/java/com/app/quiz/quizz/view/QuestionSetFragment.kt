package com.app.quiz.quizz.view

import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
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
import com.app.quiz.R
import com.app.quiz.annotation.FragmentType
import com.app.quiz.annotation.Status
import com.app.quiz.base.BaseFragment
import com.app.quiz.dialog.InstructionDialogFragment
import com.app.quiz.dialog.TimeOverDialogFragment
import com.app.quiz.interfacor.HomeFragmentSelectedListener
import com.app.quiz.quizz.model.QuizDetail
import com.app.quiz.quizz.viewmodel.QuestionSetViewModel
import kotlinx.android.synthetic.main.fragment_question_set.*
import kotlinx.android.synthetic.main.layout_quiz_discription_i.*
import kotlinx.android.synthetic.main.layout_quiz_discription_ii.*
import kotlinx.coroutines.*


class QuestionSetFragment : BaseFragment() {
    private lateinit var quesSetAdapter: QuestionSetAdapter
    private lateinit var quesNoAdapter: QuestionNoAdapter
    private lateinit var viewModel: QuestionSetViewModel
    private var mFragmentListener: HomeFragmentSelectedListener? = null
    private lateinit var linearLayoutManager:LinearLayoutManager
    private var alertDialogFragment:InstructionDialogFragment?=null
    private var timeOverDialogFragment:TimeOverDialogFragment?=null
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
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        viewModel = ViewModelProviders.of(this).get(QuestionSetViewModel::class.java)
        super.onCreate(savedInstanceState)
        arguments?.let {
            viewModel.quizDetail.value=it.getParcelable("KEY")

        }


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


        viewModel.fetchQuizDetail()

        // IO,Main,Default
//        CoroutineScope(Dispatchers.Default).launch {
//            delay(1000)
//            withContext(Dispatchers.Main) {
//
//            }
//        }



    }



    private fun initListener() {
        ibtn_close?.setOnClickListener { showPopupMenu() }
        tv_show_more?.setOnClickListener {
            view_flipper?.setInAnimation(requireActivity(), R.anim.enter_from_right)
            view_flipper?.setOutAnimation(requireActivity(), R.anim.exit_to_left)
            view_flipper?.showNext()
        }
        tv_hide_more?.setOnClickListener {
            view_flipper?.setInAnimation(requireActivity(), R.anim.enter_from_left)
            view_flipper?.setOutAnimation(requireActivity(), R.anim.exit_to_right)
            view_flipper?.showPrevious()
        }
        fbtn_previous?.setOnClickListener {
            rv_question_set?.smoothScrollToPosition(mVisiblePos-1)


        }
        fbtn_next?.setOnClickListener {
            rv_question_set?.smoothScrollToPosition(mVisiblePos+1)
        }

    }

    private fun initQuesNoRecyclerView() {
        rv_question_no?.apply {
            layoutManager=LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
            quesNoAdapter = QuestionNoAdapter()
            adapter = quesNoAdapter
            rv_question_no.setHasFixedSize(true)
            quesNoAdapter.setOnItemClickListener(object:
                QuestionNoAdapter.OnItemClickListener{
                override fun onItemClick(pos: Int) {
                    rv_question_set?.scrollToPosition(pos)
                }
            })
        }
    }

    private fun showResult() {
//        quesSetAdapter.items.forEach {
//            println(it.toString())
//        }

        timer.cancel()
        mFragmentListener?.showFragment(FragmentType.SCORECARD_FRAGMENT,quesSetAdapter.items)

    }

    private fun initQuesSetRecyclerView() {
        rv_question_set?.apply {
            linearLayoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
            layoutManager=linearLayoutManager
             quesSetAdapter = QuestionSetAdapter()
            adapter = quesSetAdapter
            rv_question_set.setHasFixedSize(true)
            val mPagerSnapHelper = PagerSnapHelper()
            mPagerSnapHelper.attachToRecyclerView(rv_question_set);
            rv_question_set.addOnScrollListener(object : RecyclerView.OnScrollListener() {

                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    mVisiblePos= linearLayoutManager.findLastCompletelyVisibleItemPosition()

                    if(mVisiblePos!=RecyclerView.NO_POSITION){
                       // Log.e("QuestionSetFragment", mVisiblePos.toString())
                        var item = quesNoAdapter.getItem(mVisiblePos)
                        quesNoAdapter.setItemIndex(item,mVisiblePos)
                    }
                    if((quesNoAdapter.items.size-1)==mVisiblePos){
                        if(fbtn_next.isShown)  fbtn_next.hide()
                    }else{
                        if(!fbtn_next.isShown)  fbtn_next.show()
                    }
                    if(0==mVisiblePos){
                        if(fbtn_previous.isShown)  fbtn_previous.hide()
                    }else{
                        if(!fbtn_previous.isShown)  fbtn_previous.show()
                    }



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

            })

        viewModel.isInstructDialogVisibility.observe(viewLifecycleOwner, Observer {
                it?.let {
                    if(it) {
                        var noOfQues = viewModel.lstOfQuestions.value?.size;
                        alertDialogFragment = InstructionDialogFragment.newInstance(noOfQues)
                        alertDialogFragment?.show(childFragmentManager, "TAG")
                        alertDialogFragment?.setOnInstructionDialogFragmentListener(object :
                            InstructionDialogFragment.InstructionDialogFragmentListener {
                            override fun onStartClick() {
                                viewModel.isInstructDialogVisibility.value=false
                            }
                        })
                    }else{
                        alertDialogFragment?.dismiss()
                        viewModel.lstOfQuestions.value?.let {
                            startTimer(it.size.toLong() * 60000)
                            tv_timer?.text=it.size.toString().plus(":00");
                            startTimer(it.size.toLong() * 60000)
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
                when (it.level) {
                    "1" -> tv_level_count?.text=resources.getString(R.string.level_beginner)
                    "2" -> tv_level_count?.text=resources.getString(R.string.level_medium)
                    "3" -> tv_level_count?.text=resources.getString(R.string.level_expert)
                }

            })

        viewModel.resultQuizDetail.observe(viewLifecycleOwner, Observer {
            when(it.status){
                Status.SUCCESS ->{
                    viewModel.lstOfQuestions.value=it.data?.data?.quizQuestion
                    viewModel.isInstructDialogVisibility.value=true
                }
                Status.FAILURE -> it.errorMsg?.let { showSnackBar(it) }
            }
        })

        viewModel.lstOfQuestions.observe(viewLifecycleOwner, Observer {
           if(it.isNotEmpty()) {
               tv_ques_no_count?.text= it.size.toString()
               tv_timer?.text=it.size.toString().plus(":").plus("00")
               quesSetAdapter.addAll(it)
               quesNoAdapter.addAll(it)
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
                Log.e("onTick","millisUntilFinished");
                secondsRemaining = millisUntilFinished / 1000
                val minutesUntilFinished = secondsRemaining / 60
                val secondsInMinuteUntilFinished = secondsRemaining - minutesUntilFinished * 60
                val secondsStr = secondsInMinuteUntilFinished.toString()
                var abc = "$minutesUntilFinished:${if (secondsStr.length == 2) secondsStr else "0" + secondsStr}"
                tv_timer?.text =abc
            }
        }.start()
    }


}

