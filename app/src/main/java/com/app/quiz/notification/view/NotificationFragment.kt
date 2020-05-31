package com.app.quiz.notification.view


import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.quiz.R
import com.app.quiz.annotation.Status
import com.app.quiz.base.BaseFragment
import com.app.quiz.interfacor.HomeFragmentSelectedListener
import com.app.quiz.notification.viewmodel.NotificationViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_notification.*

class NotificationFragment : BaseFragment() {
    private  var notificationAdapter: NotificationAdapter?=null
    private lateinit var viewModel: NotificationViewModel
    private  var mFragmentListener: HomeFragmentSelectedListener?=null

    companion object {
        fun newInstance() = NotificationFragment()
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
         notificationAdapter = NotificationAdapter()

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_notification, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = ViewModelProviders.of(this).get(NotificationViewModel::class.java)
        super.onViewCreated(view, savedInstanceState)
        initObserver()
        ibtn_close?.setOnClickListener {
            mFragmentListener?.popTopMostFragment()
        }
       // ibtn_more?.setOnClickListener { showPopupMenu() }

        rv_notification?.apply {
            layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
            adapter = notificationAdapter
        }

        viewModel.fetchNotifications()

    }

    private fun showPopupMenu() {
        val popupMenu = PopupMenu(requireActivity(), ibtn_more)
        val menu = popupMenu.menu
        menu.add(0, 1, 0, "Clear All")
        popupMenu.setOnMenuItemClickListener { item: MenuItem ->

            false
        }
        popupMenu.show()
    }

    private fun initObserver() {

        viewModel.isLoading.observe(viewLifecycleOwner,
            Observer {
                if (it) progress_bar?.visibility = View.VISIBLE
                else progress_bar?.visibility = View.INVISIBLE
            })

        viewModel.resultAllNotification.observe(viewLifecycleOwner, Observer {

            when(it.status){
                Status.SUCCESS -> viewModel.lstOfNotifications.value=it.data?.notifications
                Status.FAILURE -> if (it.errorMsg != null) showSnackBar(it.errorMsg.toString())
            }
        })

        viewModel.lstOfNotifications.observe(viewLifecycleOwner, Observer {
            if(it.isNotEmpty()) notificationAdapter?.addAll(it)
            else tv_empty_view?.visibility=View.VISIBLE

        })


    }

}
