package com.app.armygyan.signin.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.transition.ChangeBounds
import android.transition.TransitionManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.app.armygyan.QuizApplication
import com.app.armygyan.R
import com.app.armygyan.annotation.FragmentType
import com.app.armygyan.annotation.Status.FAILURE
import com.app.armygyan.annotation.Status.SUCCESS
import com.app.armygyan.base.BaseFragment
import com.app.armygyan.helper.SharedPrefHelper
import com.app.armygyan.interfacor.HomeFragmentSelectedListener
import com.app.armygyan.network.WebHeader
import com.app.armygyan.signin.model.SignInRequest
import com.app.armygyan.signin.model.SignInResponse
import com.app.armygyan.signin.viewmodel.SignInViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.InstanceIdResult
import kotlinx.android.synthetic.main.fragment_sign_in.*
import kotlinx.coroutines.*
import java.util.HashMap
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


class SignInFragment : BaseFragment() {
    private var deviceId: String?=null
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
        initializeDeviceToken()
        initObserver()
        fbtn_sign_in?.setOnClickListener {
          viewModel.isLoading.value=true
          val signInIntent: Intent? = googleSignInClient?.signInIntent
          startActivityForResult(signInIntent, REQEST_CODE_SIGN_IN)
        }

        val constraintSetStart= ConstraintSet()
        val constraintSetEnd= ConstraintSet()
        constraintSetStart.clone(activity,R.layout.fragment_sign_in)
        constraintSetEnd.clone(activity,R.layout.fragment_sign_in_alt)

        // IO,Main,Default
        CoroutineScope(Dispatchers.Default).launch {
            delay(100)
            withContext(Dispatchers.Main) {
                val transition= ChangeBounds()
                transition.duration=800
                TransitionManager.beginDelayedTransition(cl_parent,transition) //,transition
                constraintSetEnd.applyTo(cl_parent)
            }
        }

    }

    private fun initObserver() {
        viewModel.isLoading.observe(viewLifecycleOwner,
            Observer {
                if (it) progress_bar?.visibility = View.VISIBLE
                else progress_bar?.visibility = View.INVISIBLE
            })

        viewModel.isViewEnable.observe(viewLifecycleOwner,
            Observer { fbtn_sign_in?.isClickable = it })

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
        mFragmentListener?.showFragment(FragmentType.CHOOSE_LANGUAGE_FRAGMENT,null)
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
            deviceId=sharedPrefs?.read(SharedPrefHelper.KEY_DEVICE_ID,"");
            viewModel.signInRequest = SignInRequest(acc?.displayName, acc?.email, acc?.photoUrl.toString(), deviceId)
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

    private fun initializeDeviceToken() {
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener { task: Task<InstanceIdResult?> ->
                if (task.isSuccessful && task.result != null) {
                    sharedPrefs?.write(SharedPrefHelper.KEY_DEVICE_ID,task.result!!.token)
                    Log.e("SplashFragment", "token is : "+task.result!!.token)
                } else {
                    Log.e("SplashFragment", "getInstanceId failed", task.exception)
                    sharedPrefs?.write(SharedPrefHelper.KEY_DEVICE_ID,"12345")
                }
            }
    }
}
