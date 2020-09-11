package com.app.armygyan.miscellaneous.view


import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.app.armygyan.BuildConfig
import com.app.armygyan.QuizApplication
import com.app.armygyan.R
import com.app.armygyan.annotation.FragmentType
import com.app.armygyan.annotation.Language
import com.app.armygyan.annotation.NotificationStatus
import com.app.armygyan.annotation.Status
import com.app.armygyan.base.BaseFragment
import com.app.armygyan.dialog.AlterLanguageDialogFragment
import com.app.armygyan.dialog.SignOutDialogFragment
import com.app.armygyan.helper.SharedPrefHelper
import com.app.armygyan.helper.SharedPrefHelper.Companion.KEY_NOTIFICATION_STATUS
import com.app.armygyan.interfacor.HomeFragmentSelectedListener
import com.app.armygyan.miscellaneous.viewmodel.SettingViewModel
import com.app.armygyan.network.WebHeader
import kotlinx.android.synthetic.main.fragment_setting.*
import kotlinx.coroutines.Runnable
import java.util.*


class SettingFragment :  BaseFragment() {
    private lateinit var viewModel: SettingViewModel
    private var sharedPrefs: SharedPrefHelper? = null
    private var mFragmentListener: HomeFragmentSelectedListener? = null
    private lateinit var accessToken: String
    private var headerMap: HashMap<String, String>? = null
    companion object {
        fun newInstance() = SettingFragment()
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
        viewModel = ViewModelProviders.of(this).get(SettingViewModel::class.java)
        accessToken= sharedPrefs?.read(SharedPrefHelper.KEY_ACCESS_TOKEN, "").toString()
        headerMap = HashMap<String, String>()
        headerMap?.put(WebHeader.KEY_CONTENT_TYPE, WebHeader.VAL_CONTENT_TYPE)
        headerMap?.put(WebHeader.KEY_ACCEPT, WebHeader.VAL_ACCEPT)
        headerMap?.put(WebHeader.KEY_AUTHORIZATION, accessToken)

    }



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_setting, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserver()

        when (sharedPrefs?.read(SharedPrefHelper.KEY_LANGUAGE, Language.ENGLISH)) {
            Language.ENGLISH -> tv_language_sel?.text = getString(R.string.title_english)
            Language.HINDI -> tv_language_sel?.text = getString(R.string.title_hindi)
        }


        when (sharedPrefs?.read(SharedPrefHelper.KEY_NOTIFICATION_STATUS, NotificationStatus.ON)) {
            NotificationStatus.OFF -> switch_push_notification?.isChecked = false
            NotificationStatus.ON -> switch_push_notification?.isChecked = true
        }

        initListener()

        val appVersion: String? = BuildConfig.VERSION_NAME
        tv_app_version.text=resources.getString(R.string.title_version).plus(" ").plus(appVersion)
    }

    private fun initListener() {
        ibtn_close?.setOnClickListener {
            mFragmentListener?.popTopMostFragment()
        }
        tv_term_and_condition?.setOnClickListener {
            mFragmentListener?.showFragment(FragmentType.TERM_CONDITION_FRAGMENT, null)
        }
        tv_sign_out?.setOnClickListener {
            showSignOutDialog()
        }
        tv_language_sel?.setOnClickListener {
            showLanguagePopupMenu()
        }

        tv_invite?.setOnClickListener {
          mFragmentListener?.shareApp()
        }
        switch_push_notification?.setOnCheckedChangeListener { view, isChecked ->
            if (!currentTypingState) {
                    onIsSwitchModified(true);
                    currentTypingState = true;
                }

                handlerX.removeCallbacks(stoppedTypingNotifier);
                handlerX.postDelayed(stoppedTypingNotifier, 2000);

        }
    }

    private fun onIsSwitchModified(isTyping: Boolean) {
        if (!isTyping) {
            headerMap?.let { viewModel.alterNotificationStatus(it) }
        }

    }

    private fun showLanguagePopupMenu() {
        val popupMenu = PopupMenu(requireActivity(), tv_language_sel)
        val menu = popupMenu.menu
        menu.add(0, 1, 0, getString(R.string.title_english))
        menu.add(0, 2, 0, getString(R.string.title_hindi))
        popupMenu.setOnMenuItemClickListener { item: MenuItem ->
            tv_language_sel?.text = item.title
            when (item.itemId) {
                1 -> {
                    sharedPrefs?.write(SharedPrefHelper.KEY_LANGUAGE, Language.ENGLISH) // English
                    showAlterLanguageDialog(Language.ENGLISH)
                }
                2 -> {
                    sharedPrefs?.write(SharedPrefHelper.KEY_LANGUAGE, Language.HINDI) // Hindi
                    showAlterLanguageDialog(Language.HINDI)
                }
            }
            false
        }
        popupMenu.show()


    }

    private fun showSignOutDialog() {
        val alertFragment: SignOutDialogFragment = SignOutDialogFragment.newInstance()
        alertFragment.show(childFragmentManager, "TAG")
        alertFragment.setOnAlertDialogListener(object :
            SignOutDialogFragment.AlertDialogListener {
            override fun onClickYes() {
                headerMap?.let { viewModel.attemptSignOut(it) }
            }

            override fun onClickNo() {}
        })
    }



    private fun showAlterLanguageDialog(langCode: String) {
        val alertDialogFragment: AlterLanguageDialogFragment =
            AlterLanguageDialogFragment.newInstance()
        alertDialogFragment.show(childFragmentManager, "TAG")
        alertDialogFragment.setOnAlertDialogListener(object :
            AlterLanguageDialogFragment.AlertDialogListener {
            override fun onClickYes() {
                mFragmentListener?.resetLocale(langCode)
            }

            override fun onClickNo() {}

        })
    }

    private fun initObserver() {
        viewModel.isLoading.observe(viewLifecycleOwner,
            Observer {
                if (it) progress_bar?.visibility = View.VISIBLE
                else progress_bar?.visibility = View.INVISIBLE
            })
        viewModel.resultNotificationStatus.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.message?.let { showSnackBar(it, R.color.colorGreen) }
                    it.data?.notifications?.notificationStatus?.let { it1 ->
                        sharedPrefs?.write(
                            KEY_NOTIFICATION_STATUS,
                            it1
                        )
                    }
                }
                Status.FAILURE -> it.errorMsg?.let { it1 -> showSnackBar(it1) }
            }

        })

        viewModel.resultSignOut.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Status.SUCCESS -> {
                    mFragmentListener?.popTillFragment(FragmentType.SIGN_IN_FRAGMENT, 0)
                    sharedPrefs?.clear()
                }
                Status.FAILURE -> it.errorMsg?.let { it1 -> showSnackBar(it1) }
            }
        })

    }
    //members you would need for the small thread that declares that you have stopped typing
    private var currentTypingState = false
    private val stoppedTypingNotifier = Runnable { //part A of the magic...

        onIsSwitchModified(false)
        currentTypingState = false

    }

    private var handlerX: Handler = Handler()
}
