package com.app.quiz.profile.view


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
import com.app.quiz.QuizApplication
import com.app.quiz.R
import com.app.quiz.annotation.Language
import com.app.quiz.annotation.Status
import com.app.quiz.base.BaseFragment
import com.app.quiz.helper.SharedPrefHelper
import com.app.quiz.signin.viewmodel.SignInViewModel
import com.app.quiz.interfacor.HomeFragmentSelectedListener
import com.app.quiz.profile.viewmodel.ProfileViewModel
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_notification.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_setting.ibtn_close


class ProfileFragment : Fragment() {
    private var usrId: Long?=null
    private var usrName:String?=null
    private var usrEmail:String?=null
    private var usrProfileUrl:String?=null
    private var mFragmentListener: HomeFragmentSelectedListener? = null
    private lateinit var viewModel: ProfileViewModel
    private var sharedPrefs: SharedPrefHelper? = null

    companion object {
        fun newInstance() = ProfileFragment()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mFragmentListener = context as HomeFragmentSelectedListener
        sharedPrefs = (context.applicationContext as QuizApplication).getSharedPrefInstance()

    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         usrId = sharedPrefs?.read(SharedPrefHelper.KEY_ID, 0)
         usrName = sharedPrefs?.read(SharedPrefHelper.KEY_FULL_NAME, "")
         usrEmail = sharedPrefs?.read(SharedPrefHelper.KEY_EMAIL, "")
         usrProfileUrl = sharedPrefs?.read(SharedPrefHelper.KEY_PROFILE_PIC, "https://www.abc.com")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = ViewModelProviders.of(this).get(ProfileViewModel::class.java)
        super.onViewCreated(view, savedInstanceState)

        ibtn_close?.setOnClickListener {
            mFragmentListener?.popTopMostFragment()
        }
        fbtn_back?.setOnClickListener {
            mFragmentListener?.popTopMostFragment()
        }

        edt_usr_id?.setText("#"+usrId)
        edt_usr_name?.setText(usrName)
        edt_usr_email?.setText(usrEmail)

        Picasso.get().load(usrProfileUrl)
            .resize(120, 120)
            .centerCrop()
            .placeholder(R.color.colorCyan)
            .error(R.color.colorRed)
            .into(civ_profile_pic)



    }


    private fun initObserver() {

        viewModel.isLoading.observe(viewLifecycleOwner,
            Observer {

            })

//        viewModel.resultGetProfile.observe(viewLifecycleOwner, Observer {
//            when(it.status){
//                Status.SUCCESS -> it.data?.data
//                Status.FAILURE -> if (it.errorMsg != null)
//                    Snackbar.make(clRoot,""+it.errorMsg , Snackbar.LENGTH_LONG).show()
//            }
//        })
//
//        viewModel.resultUpdateProfile.observe(viewLifecycleOwner, Observer {
//            when(it.status){
//                Status.SUCCESS -> it.data?.message
//                Status.FAILURE -> if (it.errorMsg != null)
//                    Snackbar.make(clRoot,""+it.errorMsg , Snackbar.LENGTH_LONG).show()
//            }
//        })


    }

}
