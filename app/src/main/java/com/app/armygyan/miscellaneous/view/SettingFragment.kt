package com.app.armygyan.miscellaneous.view


import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.lifecycle.ViewModelProviders
import com.app.armygyan.BuildConfig
import com.app.armygyan.QuizApplication
import com.app.armygyan.R
import com.app.armygyan.annotation.FragmentType
import com.app.armygyan.annotation.Language
import com.app.armygyan.annotation.NotificationStatus
import com.app.armygyan.annotation.Status
import com.app.armygyan.base.BaseFragment
import com.app.armygyan.databinding.FragmentSettingBinding
import com.app.armygyan.dialog.AlterLanguageDialogFragment
import com.app.armygyan.dialog.SignOutDialogFragment
import com.app.armygyan.helper.SharedPrefHelper
import com.app.armygyan.helper.SharedPrefHelper.Companion.KEY_NOTIFICATION_STATUS
import com.app.armygyan.interfacor.HomeFragmentSelectedListener
import com.app.armygyan.miscellaneous.viewmodel.SettingViewModel
import com.app.armygyan.network.WebHeader
import kotlinx.coroutines.Runnable
import java.util.*


class SettingFragment :  BaseFragment() {
    private lateinit var viewModel: SettingViewModel
    private var sharedPrefs: SharedPrefHelper? = null
    private var mFragmentListener: HomeFragmentSelectedListener? = null
    private lateinit var accessToken: String
    private var headerMap: HashMap<String, String>? = null
    private var _binding: FragmentSettingBinding? = null
    private val binder get() = _binding!!


    companion object {
        fun newInstance() = SettingFragment()
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
    ): View {
        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        return binder.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserver()

        when (sharedPrefs?.read(SharedPrefHelper.KEY_LANGUAGE, Language.ENGLISH)) {
            Language.ENGLISH -> binder.tvLanguageSel.text = getString(R.string.title_english)
            Language.HINDI -> binder.tvLanguageSel?.text = getString(R.string.title_hindi)
        }


        when (sharedPrefs?.read(KEY_NOTIFICATION_STATUS, NotificationStatus.ON)) {
            NotificationStatus.OFF -> binder.switchPushNotification.isChecked = false
            NotificationStatus.ON -> binder.switchPushNotification.isChecked = true
        }

        initListener()

        val appVersion: String = BuildConfig.VERSION_NAME
        binder.tvAppVersion.text=resources.getString(R.string.title_version).plus(" ").plus(appVersion)
    }

    private fun initListener() {
        binder.ibtnClose.setOnClickListener {
            mFragmentListener?.popTopMostFragment()
        }
        binder.tvTermAndCondition.setOnClickListener {
            mFragmentListener?.showFragment(FragmentType.TERM_CONDITION_FRAGMENT, null)
        }
        binder.tvSignOut.setOnClickListener {
            showSignOutDialog()
        }
        binder.tvLanguageSel.setOnClickListener {
            showLanguagePopupMenu()
        }

        binder.tvInvite.setOnClickListener {
          mFragmentListener?.shareApp()
        }
        binder.switchPushNotification.setOnCheckedChangeListener { view, isChecked ->
            if (!currentTypingState) {
                    onIsSwitchModified(true);
                    currentTypingState = true;
                }

                handlerX.removeCallbacks(stoppedTypingNotifier);
                handlerX.postDelayed(stoppedTypingNotifier, 2000)

        }
    }

    private fun onIsSwitchModified(isTyping: Boolean) {
        if (!isTyping) {
            headerMap?.let { viewModel.alterNotificationStatus(it) }
        }

    }

    private fun showLanguagePopupMenu() {
        val popupMenu = PopupMenu(requireActivity(), binder.tvLanguageSel)
        val menu = popupMenu.menu
        menu.add(0, 1, 0, getString(R.string.title_english))
        menu.add(0, 2, 0, getString(R.string.title_hindi))
        popupMenu.setOnMenuItemClickListener { item: MenuItem ->
            binder.tvLanguageSel.text = item.title
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
        viewModel.isLoading.observe(viewLifecycleOwner, {
                if (it) binder.progressBar.visibility = View.VISIBLE
                else binder.progressBar.visibility = View.INVISIBLE
            })
        viewModel.resultNotificationStatus.observe(viewLifecycleOwner, {
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

        viewModel.resultSignOut.observe(viewLifecycleOwner, {
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
