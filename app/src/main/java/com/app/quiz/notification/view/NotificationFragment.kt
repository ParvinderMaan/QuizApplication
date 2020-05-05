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
import kotlinx.android.synthetic.main.fragment_setting.ibtn_close
import kotlinx.android.synthetic.main.fragment_sign_in.*

class NotificationFragment : Fragment() {
    private lateinit var viewModel: NotificationViewModel
    private  var mFragmentListener: HomeFragmentSelectedListener?=null

    companion object {
        fun newInstance() = NotificationFragment()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mFragmentListener = context as HomeFragmentSelectedListener
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        ibtn_more?.setOnClickListener { showPopupMenu() }

        rv_notification?.apply {
            layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
            val notificationAdapter =
                NotificationAdapter()
            adapter = notificationAdapter

        }

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

            })

        viewModel.resultAllNotification.observe(viewLifecycleOwner, Observer {

            when(it.status){
                Status.SUCCESS -> viewModel.lstOfNotifications.value=it.data?.data?.notifications
                Status.FAILURE -> if (it.errorMsg != null)
                    Snackbar.make(clRoot,""+it.errorMsg , Snackbar.LENGTH_LONG).show()
            }
        })
        viewModel.resultDeleteNotification.observe(viewLifecycleOwner, Observer {

            when(it.status){
                Status.SUCCESS ->it.data?.message
                Status.FAILURE -> if (it.errorMsg != null)
                    Snackbar.make(clRoot,""+it.errorMsg , Snackbar.LENGTH_LONG).show()
            }
        })

        viewModel.lstOfNotifications.observe(viewLifecycleOwner, Observer {


        })


    }

}
