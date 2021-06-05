package com.app.armygyan.notification.view


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.armygyan.QuizApplication
import com.app.armygyan.annotation.FragmentType
import com.app.armygyan.annotation.NotificationType
import com.app.armygyan.annotation.Status
import com.app.armygyan.base.BaseFragment
import com.app.armygyan.databinding.FragmentNotificationBinding
import com.app.armygyan.helper.SharedPrefHelper
import com.app.armygyan.interfacor.HomeFragmentSelectedListener
import com.app.armygyan.network.WebHeader
import com.app.armygyan.notification.model.NotificationResponse
import com.app.armygyan.notification.viewmodel.NotificationViewModel
import com.google.android.gms.ads.AdRequest
import java.util.*

class NotificationFragment : BaseFragment() {
    private  var notificationAdapter: NotificationAdapter?=null
    private lateinit var viewModel: NotificationViewModel
    private  var mFragmentListener: HomeFragmentSelectedListener?=null
    private var sharedPrefHelper: SharedPrefHelper?=null
    private lateinit var accessToken: String
    private var headerMap: HashMap<String, String>? = null
    private var _binding: FragmentNotificationBinding? = null
    private val binder get() = _binding!!


    companion object {
        fun newInstance() = NotificationFragment()
    }
    override fun getRootView(): View {
        return binder.clRoot
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentNotificationBinding.inflate(inflater, container, false)
        return binder.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserver()
        binder.ibtnClose.setOnClickListener {
            mFragmentListener?.popTopMostFragment()
        }

        binder.rvNotification.apply {
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
        binder.adView.loadAd(adRequest)
    }


    private fun initObserver() {

        viewModel.isLoading.observe(viewLifecycleOwner, {
                if (it) binder.shimmerFrameLayout.visibility = View.VISIBLE
                else binder.shimmerFrameLayout.visibility = View.GONE
            })

        viewModel.resultAllNotification.observe(viewLifecycleOwner, {

            when(it.status){
                Status.SUCCESS -> viewModel.lstOfNotifications.value=it.data?.notifications
                Status.FAILURE -> if (it.errorMsg != null) showSnackBar(it.errorMsg.toString())
            }
        })

        viewModel.lstOfNotifications.observe(viewLifecycleOwner, {
            if(it.isNotEmpty())  notificationAdapter?.addAll(it)
            else binder.tvEmptyView.visibility=View.VISIBLE
        })

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}
