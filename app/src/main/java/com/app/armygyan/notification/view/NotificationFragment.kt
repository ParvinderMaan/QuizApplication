package com.app.armygyan.notification.view


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.armygyan.QuizApplication
import com.app.armygyan.R
import com.app.armygyan.annotation.FragmentType
import com.app.armygyan.annotation.NotificationType
import com.app.armygyan.annotation.Status
import com.app.armygyan.base.BaseFragment
import com.app.armygyan.helper.SharedPrefHelper
import com.app.armygyan.interfacor.HomeFragmentSelectedListener
import com.app.armygyan.network.WebHeader
import com.app.armygyan.notification.model.NotificationResponse
import com.app.armygyan.notification.viewmodel.NotificationViewModel
import com.google.android.gms.ads.AdRequest
import kotlinx.android.synthetic.main.fragment_notification.*
import kotlinx.android.synthetic.main.fragment_notification.cl_root
import kotlinx.android.synthetic.main.fragment_notification.ibtn_close
import kotlinx.android.synthetic.main.fragment_notification.shimmer_frame_layout
import kotlinx.android.synthetic.main.fragment_notification.tv_empty_view
import java.util.*

class NotificationFragment : BaseFragment() {
    private  var notificationAdapter: NotificationAdapter?=null
    private lateinit var viewModel: NotificationViewModel
    private  var mFragmentListener: HomeFragmentSelectedListener?=null
    private var sharedPrefHelper: SharedPrefHelper?=null
    private lateinit var accessToken: String
    private var headerMap: HashMap<String, String>? = null
    companion object {
        fun newInstance() = NotificationFragment()
    }
    override fun getRootView(): View {
        return cl_root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        sharedPrefHelper = (context.applicationContext as QuizApplication).getSharedPrefInstance()
        mFragmentListener = context as HomeFragmentSelectedListener
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        accessToken= sharedPrefHelper?.read(SharedPrefHelper.KEY_ACCESS_TOKEN,"").toString()
        headerMap = HashMap<String, String>()
        headerMap?.put(WebHeader.KEY_CONTENT_TYPE, WebHeader.VAL_CONTENT_TYPE)
        headerMap?.put(WebHeader.KEY_ACCEPT, WebHeader.VAL_ACCEPT)
        headerMap?.put(WebHeader.KEY_AUTHORIZATION,accessToken)
        notificationAdapter = NotificationAdapter(Date())
      //  Log.e("accessToken",accessToken);
        viewModel = ViewModelProviders.of(this).get(NotificationViewModel::class.java)
        headerMap?.let { viewModel.fetchNotifications(it) }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_notification, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserver()
        ibtn_close?.setOnClickListener {
            mFragmentListener?.popTopMostFragment()
        }

        rv_notification?.apply {
            layoutManager = LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false)
            adapter = notificationAdapter
            notificationAdapter?.clear()
            notificationAdapter?.setOnItemClickListener(object:NotificationAdapter.OnItemClickListener{
                override fun onItemClick(item: NotificationResponse.Notification) {
                           when(item.notification_type){
                                   NotificationType.QUIZ -> mFragmentListener?.showFragment(
                                   FragmentType.QUIZ_CATEGORY_FRAGMENT,null)
                                   NotificationType.CHAPTER ->  mFragmentListener?.showFragment(
                                   FragmentType.STUDY_MATERIAL_FRAGMENT,null)
                           }
                }

            })
        }


        val adRequest = AdRequest.Builder().build()
        adView?.loadAd(adRequest)
    }


    private fun initObserver() {

        viewModel.isLoading.observe(viewLifecycleOwner,
            Observer {
                if (it) shimmer_frame_layout?.visibility = View.VISIBLE
                else shimmer_frame_layout?.visibility = View.GONE
            })

        viewModel.resultAllNotification.observe(viewLifecycleOwner, Observer {

            when(it.status){
                Status.SUCCESS -> viewModel.lstOfNotifications.value=it.data?.notifications
                Status.FAILURE -> if (it.errorMsg != null) showSnackBar(it.errorMsg.toString())
            }
        })

        viewModel.lstOfNotifications.observe(viewLifecycleOwner, Observer {
            if(it.isNotEmpty())  notificationAdapter?.addAll(it)
            else tv_empty_view?.visibility=View.VISIBLE
        })

    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

}
