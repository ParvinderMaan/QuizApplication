package com.app.quiz.miscellaneous.view


import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.app.quiz.QuizApplication
import com.app.quiz.R
import com.app.quiz.annotation.FragmentType
import com.app.quiz.annotation.Language
import com.app.quiz.annotation.NotificationStatus
import com.app.quiz.annotation.Status
import com.app.quiz.base.BaseFragment
import com.app.quiz.dialog.AlterLanguageDialogFragment
import com.app.quiz.dialog.SignOutDialogFragment
import com.app.quiz.helper.SharedPrefHelper
import com.app.quiz.interfacor.HomeFragmentSelectedListener
import com.app.quiz.miscellaneous.viewmodel.SettingViewModel
import com.app.quiz.network.WebHeader
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import kotlinx.android.synthetic.main.fragment_setting.*
import kotlinx.android.synthetic.main.fragment_setting.progress_bar
import kotlinx.android.synthetic.main.fragment_sign_in.*
import kotlinx.coroutines.*
import java.util.*
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


class SettingFragment :  BaseFragment() {
    private lateinit var viewModel: SettingViewModel
    private var googleSignInClient: GoogleSignInClient? = null
    private var sharedPrefs: SharedPrefHelper? = null
    private var mFragmentListener: HomeFragmentSelectedListener? = null

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
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
        viewModel = ViewModelProviders.of(this).get(SettingViewModel::class.java)
        val accessToken=sharedPrefs?.read(SharedPrefHelper.KEY_ACCESS_TOKEN,"")
        val headerMap = HashMap<String, String>()
        headerMap[WebHeader.KEY_CONTENT_TYPE] = WebHeader.VAL_CONTENT_TYPE
        headerMap[WebHeader.KEY_ACCEPT] = WebHeader.VAL_ACCEPT
        accessToken?.let { headerMap[WebHeader.KEY_AUTHORIZATION]= it }
        viewModel.headerMap=headerMap
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_setting, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListener()
        initObserver()

        when (sharedPrefs?.read(SharedPrefHelper.KEY_LANGUAGE, Language.ENGLISH)) {
            Language.ENGLISH -> tv_language_sel?.setText(getString(R.string.title_english))
            Language.HINDI -> tv_language_sel?.setText(getString(R.string.title_hindi))
        }


        when (sharedPrefs?.read(SharedPrefHelper.KEY_NOTIFICATION_STATUS, NotificationStatus.OFF)) {
            NotificationStatus.OFF -> switch_push_notification?.isChecked = false
            NotificationStatus.ON -> switch_push_notification?.isChecked = true
        }


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

    }

    private fun showLanguagePopupMenu() {
        val popupMenu = PopupMenu(requireActivity(), tv_language_sel)
        val menu = popupMenu.menu
        menu.add(0, 1, 0, getString(R.string.title_english))
        menu.add(0, 2, 0, getString(R.string.title_hindi))
        popupMenu.setOnMenuItemClickListener { item: MenuItem ->
            tv_language_sel?.setText(item.title)
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
                // IO,Main,Default
                CoroutineScope(Dispatchers.Default).launch {
                    // async code can be written here ....
                    val isGoogleSignOut = async { attemptGoogleSignOut() }
                    val isAppSignOut = async {viewModel.attemptSignOut() }
                    if(isGoogleSignOut.await()==true && isAppSignOut.await()==true){
                        withContext(Dispatchers.Main) {
                        sharedPrefs?.clear()
                        mFragmentListener?.popTillFragment(FragmentType.SIGN_IN_FRAGMENT, 1)
                        }
                    }else{
                        withContext(Dispatchers.Main) {
                            showSnackBar("Please try again")
                        }
                    }
                }
            }

            override fun onClickNo() {}
        })
    }

//    private  fun attemptGoogleAccountSignOut() {
//        viewModel.isLoading.value=true
//        googleSignInClient?.signOut()
//            ?.addOnCompleteListener(requireActivity(), object : OnCompleteListener<Void?> {
//                override fun onComplete(p0: Task<Void?>) {
//                    viewModel.isLoading.value=false
//                    if(p0.isSuccessful){
//                        sharedPrefs?.clear()
//                        mFragmentListener?.popTillFragment(FragmentType.SIGN_IN_FRAGMENT, 1)
//                    }
//                }
//            })?.addOnFailureListener(requireActivity(), object : OnFailureListener {
//            override fun onFailure(p0: Exception) {
//                viewModel.isLoading.value=false
//                showSnackBar(p0.message.toString())
//            }
//
//        })
//    }


        private suspend fun attemptGoogleSignOut() : Boolean? = suspendCoroutine {
            googleSignInClient?.signOut()?.addOnFailureListener { p0 ->
                showSnackBar(p0.message.toString())
                it.resume(false)
            }?.addOnSuccessListener{p0 ->
                it.resume(true)
            }
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

        viewModel.resultSignOut.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Status.SUCCESS -> it.data?.let { it1 ->
                    sharedPrefs?.clear()
//                  showSnackBar(it1.message,R.color.colorGreen)
                    mFragmentListener?.popTillFragment(FragmentType.SIGN_IN_FRAGMENT, 1)
                }
                Status.FAILURE -> it.errorMsg?.let { it1 -> showSnackBar(it1) }
            }
        })

    }

}
