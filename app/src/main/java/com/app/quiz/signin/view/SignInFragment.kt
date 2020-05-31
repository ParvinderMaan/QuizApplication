package com.app.quiz.signin.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.app.quiz.QuizApplication
import com.app.quiz.R
import com.app.quiz.annotation.FragmentType
import com.app.quiz.annotation.Status.FAILURE
import com.app.quiz.annotation.Status.SUCCESS
import com.app.quiz.base.BaseFragment
import com.app.quiz.helper.SharedPrefHelper
import com.app.quiz.interfacor.HomeFragmentSelectedListener
import com.app.quiz.network.WebHeader
import com.app.quiz.signin.model.SignInRequest
import com.app.quiz.signin.model.SignInResponse
import com.app.quiz.signin.viewmodel.SignInViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_sign_in.*
import kotlinx.coroutines.*
import java.util.HashMap
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


class SignInFragment : BaseFragment() {
    private val REQEST_CODE_SIGN_IN = 210
    private var googleSignInClient: GoogleSignInClient? = null
    private var sharedPrefs: SharedPrefHelper? = null
    private var mFragmentListener: HomeFragmentSelectedListener? = null
    private lateinit var viewModel: SignInViewModel

    companion object {
        fun newInstance() = SignInFragment()
    }

    override fun getRootView(): View {
       return cl_parent
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
        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso);
        viewModel = ViewModelProviders.of(this).get(SignInViewModel::class.java)
        val headerMap = HashMap<String, String>()
        headerMap[WebHeader.KEY_CONTENT_TYPE] = WebHeader.VAL_CONTENT_TYPE
        headerMap[WebHeader.KEY_ACCEPT] = WebHeader.VAL_ACCEPT
        viewModel.headerMap=headerMap
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_sign_in, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserver()
        fbtn_sign_in?.setOnClickListener {
          viewModel.isLoading.value=true
          val signInIntent: Intent? = googleSignInClient?.getSignInIntent()
          startActivityForResult(signInIntent, REQEST_CODE_SIGN_IN)
        }
    }

    private fun initObserver() {
        viewModel.isLoading.observe(viewLifecycleOwner,
            Observer {
                if (it) progress_bar?.visibility = View.VISIBLE
                else progress_bar?.visibility = View.INVISIBLE
            })

        viewModel.resultantSignIn.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                SUCCESS -> handleSigInResult(it.data?.data, it.data?.token)
                FAILURE -> showSnackBar(it.errorMsg.toString())
            }
        })

    }

    private fun handleSigInResult(data: SignInResponse.Data?, token: String?) {
        data?.userId?.let { sharedPrefs?.write(SharedPrefHelper.KEY_ID, it) }
        token?.let { sharedPrefs?.write(SharedPrefHelper.KEY_ACCESS_TOKEN, it) }
        data?.notificationStatus?.let { sharedPrefs?.write(SharedPrefHelper.KEY_NOTIFICATION_STATUS, it) } // "0" "1"
        viewModel.signInRequest.name?.let { sharedPrefs?.write(SharedPrefHelper.KEY_FULL_NAME, it) }
        viewModel.signInRequest.email?.let { sharedPrefs?.write(SharedPrefHelper.KEY_EMAIL, it) }
        viewModel.signInRequest.photoUrl?.let { sharedPrefs?.write(SharedPrefHelper.KEY_PROFILE_PIC, it) }
        sharedPrefs?.write(SharedPrefHelper.KEY_IS_SIGN_IN, true)
        mFragmentListener?.showFragment(FragmentType.HOME_FRAGMENT,null)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        viewModel.isLoading.value=false
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == REQEST_CODE_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach a listener.
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val acc = completedTask.getResult(ApiException::class.java)
            viewModel.signInRequest = SignInRequest(acc?.displayName, acc?.email, acc?.photoUrl.toString(), "12345")
          //  viewModel.attemptSignIn()
          // new
            // IO,Main,Default
            CoroutineScope(Dispatchers.Default).launch {
                // async code can be written here ....
                val isGoogleSignOut = async { attemptGoogleSignOut() }
                val isAppSignIn = async {viewModel.attemptSignIn() }
                val signInResponse=isAppSignIn.await()
                var isGooglySignOut = isGoogleSignOut.await()
                if(isGooglySignOut==true && signInResponse!=null){
                    withContext(Dispatchers.Main) {
                        handleSigInResult(signInResponse.data, signInResponse.token)
                    }
                }else{
                    withContext(Dispatchers.Main) {
                        showSnackBar("Something went wrong ! Please try again")
                    }
                }
            }







        } catch (e: ApiException) {
          //  Log.e("SignInFragment", "signInResult:failed code=" + e.statusCode)
             showSnackBar("Please try  again")
        }
    }

    private suspend fun attemptGoogleSignOut() : Boolean? = suspendCoroutine {
        googleSignInClient?.signOut()?.addOnFailureListener { p0 ->
            println("addOnFailureListener...")
            showSnackBar(p0.message.toString())
            it.resume(false)
        }?.addOnSuccessListener{p0 ->
            println("addOnSuccessListener...")
            it.resume(true)
        }
    }
}
