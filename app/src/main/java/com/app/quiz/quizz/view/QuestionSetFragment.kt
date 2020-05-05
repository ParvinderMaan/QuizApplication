package com.app.quiz.quizz.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.app.quiz.quizz.viewmodel.QuestionSetViewModel
import com.app.quiz.R
import com.app.quiz.annotation.FragmentType
import com.app.quiz.annotation.Status
import com.app.quiz.base.BaseFragment
import com.app.quiz.interfacor.HomeFragmentSelectedListener
import com.app.quiz.quizz.model.QuestionSet
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_about_us.*
import kotlinx.android.synthetic.main.fragment_question_set.*
import kotlinx.android.synthetic.main.fragment_study_material.ibtn_close
import kotlinx.android.synthetic.main.layout_quiz_discription_i.*
import kotlinx.android.synthetic.main.layout_quiz_discription_ii.*


class QuestionSetFragment : Fragment() {
    private lateinit var quesSetAdapter: QuestionSetAdapter
    private lateinit var quesNoAdapter: QuestionNoAdapter
    private lateinit var viewModel: QuestionSetViewModel
    private var mFragmentListener: HomeFragmentSelectedListener? = null
    private lateinit var  linearLayoutManager:LinearLayoutManager
    companion object {
        fun newInstance() = QuestionSetFragment()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mFragmentListener = context as HomeFragmentSelectedListener
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_question_set, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = ViewModelProviders.of(this).get(QuestionSetViewModel::class.java)
        super.onViewCreated(view, savedInstanceState)
        initObserver()
        ibtn_close?.setOnClickListener { showPopupMenu() }
        fbtn_finish?.setOnClickListener { showResult() }
        tv_show_more?.setOnClickListener {
            view_flipper?.setInAnimation(requireActivity(),
                R.anim.enter_from_right
            )
            view_flipper?.setOutAnimation(requireActivity(),
                R.anim.exit_to_left
            )
            view_flipper?.showNext()
        }
        tv_hide_more?.setOnClickListener {
            view_flipper?.setInAnimation(requireActivity(),
                R.anim.enter_from_left
            )
            view_flipper?.setOutAnimation(requireActivity(),
                R.anim.exit_to_right
            )
            view_flipper?.showPrevious()
        }
        initQuesSetRecyclerView()
        initQuesNoRecyclerView()

        view_flipper?.setFlipInterval(3000)


    }

    private fun initQuesNoRecyclerView() {
        rv_question_no?.apply {
            layoutManager=LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
            quesNoAdapter = QuestionNoAdapter()
            adapter = quesNoAdapter
            quesNoAdapter.addAll(getDummyQuestionSet())
            rv_question_no.setHasFixedSize(true)
            quesNoAdapter.setOnItemClickListener(object:
                QuestionNoAdapter.OnItemClickListener{
                override fun onItemClick(pos: Int) {
                    rv_question_set?.scrollToPosition(pos);
                }
            })
        }
    }

    private fun showResult() {
        mFragmentListener?.showFragment(FragmentType.SCORECARD_FRAGMENT,null);

    }

    private fun initQuesSetRecyclerView() {
        rv_question_set?.apply {
            linearLayoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
            layoutManager=linearLayoutManager
             quesSetAdapter = QuestionSetAdapter()
            adapter = quesSetAdapter
            quesSetAdapter.addAll(getDummyQuestionSet());
            rv_question_set.setHasFixedSize(true)
            var mPagerSnapHelper = PagerSnapHelper()
            mPagerSnapHelper.attachToRecyclerView(rv_question_set);
            rv_question_set.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                var mVisiblePos:Int=0
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    mVisiblePos= linearLayoutManager.findLastCompletelyVisibleItemPosition()
                    if((quesSetAdapter.items.size-1)==mVisiblePos){
                        if(!fbtn_finish.isShown)  fbtn_finish.show()
                    }else{
                        if(fbtn_finish.isShown)  fbtn_finish.hide()
                    }
                }
            })

        }
    }

    private fun showPopupMenu() {
        val popupMenu = PopupMenu(requireActivity(), ibtn_close)
        val menu = popupMenu.menu
        menu.add(0, 1, 0, getString(R.string.title_show_results))
        menu.add(0, 2, 0, getString(R.string.title_restart))
        menu.add(0, 3, 0, getString(R.string.title_quit_quiz))
        popupMenu.setOnMenuItemClickListener { item: MenuItem ->
            when (item.itemId) {
                1 -> mFragmentListener?.showFragment(FragmentType.SCORECARD_FRAGMENT,null)
                2 -> {
                   // restart timer !!!!!
                }
                3 -> mFragmentListener?.popTopMostFragment()
            }
            false
        }
        popupMenu.show()
    }


    fun getDummyQuestionSet(): MutableList<QuestionSet> {
        val lstOfQues = mutableListOf<QuestionSet>()
        lstOfQues.add(
            QuestionSet(
                1,
                "A connection from one HTML to another HTML document is called ",
                "Hyper Link",
                "Connecting Link",
                "Icon",
                "All of these",
                1,
                false,
                false,
                0
            )
        );

        lstOfQues.add(
            QuestionSet(
                2,
                "The ........ lists the location of the files on the disk.",
                "Boot sector",
                "FAT",
                "Data area",
                "Root folder",
                2,
                false,
                false,
                0
            )
        );

        lstOfQues.add(
            QuestionSet(
                3,
                "In context of Computers, FAT stands for",
                "File Allocation Table",
                "File Access Table",
                "Folder Allocation Table",
                "Folder Access Table",
                1,
                false,
                false,
                0
            )
        );

        lstOfQues.add(
            QuestionSet(
                4,
                "Which American Computer Company is also known by the nick name “Big Blue” ?",
                "Microsoft",
                "Apple",
                "Compaq Corporation",
                "IBM",
                4,
                false,
                false,
                0
            )
        );

        lstOfQues.add(
            QuestionSet(
                5, "Which protocol is uesd to send E-mail ?  ", "HTTP", "SSH", "SMTP",
                "POP3", 3,
                false, false, 0
            )
        );

        lstOfQues.add(
            QuestionSet(
                6,
                "The device used by banks to auto-matically read those special numbers on the bottom of cheques is ",
                "MICR",
                "OMR",
                "UDIC",
                "UPC",
                1,
                false,
                false,
                0
            )
        );

        lstOfQues.add(
            QuestionSet(
                7,
                "What is full form of MICR ? ",
                "Magnetic Ink Case Reader",
                "Magnetic Ink Character Recognition",
                "Magnetic Ink Card Recognition",
                "Magnetic Ink Character Recoder",
                2,
                false,
                false,
                0
            )
        );

        lstOfQues.add(
            QuestionSet(
                8,
                "Which company bought the popular video teleconferencing software ‘Skype’ ?",
                "Google",
                "Accenture",
                "Oracle",
                "Microsoft",
                4,
                false,
                false,
                0
            )
        );

        lstOfQues.add(
            QuestionSet(
                9, "Which protocol is used to send E-mail ?", "SMTP", "POP3", "HTTP",
                "FTP", 2,
                false, false, 0
            )
        );


        lstOfQues.add(
            QuestionSet(
                10,
                "BIOS is used for ",
                "Updating system information on network",
                "It helps in routing",
                "Loading operating system",
                "It takes input from keyboard and other devices",
                3,
                false,
                false,
                0
            )
        );
        return lstOfQues;
    }


    private fun initObserver() {

        viewModel.isLoading.observe(viewLifecycleOwner,
            Observer {

            })

        viewModel.resultQuizDetail.observe(viewLifecycleOwner, Observer {

//            when(it.status){
//                Status.SUCCESS -> viewModel.lstOfQuestions.value=it.data?.data?.quizDetail
//                Status.FAILURE -> //if (it.errorMsg != null)
//                   // Snackbar.make(cl_rootz,""+it.errorMsg , Snackbar.LENGTH_LONG).show()
//            }
        })

        viewModel.lstOfQuestions.observe(viewLifecycleOwner, Observer {
            //it

        })


    }
}

