package com.app.armygyan.profile.view


import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProviders
import com.app.armygyan.QuizApplication
import com.app.armygyan.R
import com.app.armygyan.databinding.FragmentProfileBinding
import com.app.armygyan.helper.SharedPrefHelper
import com.app.armygyan.interfacor.HomeFragmentSelectedListener
import com.app.armygyan.profile.viewmodel.ProfileViewModel
import com.squareup.picasso.Picasso


class ProfileFragment : Fragment() {
    private var usrId: Long?=null
    private var usrName:String?=null
    private var usrEmail:String?=null
    private var usrProfileUrl:String?=null
    private var mFragmentListener: HomeFragmentSelectedListener? = null
    private lateinit var viewModel: ProfileViewModel
    private var sharedPrefs: SharedPrefHelper? = null
    private var _binding: FragmentProfileBinding? = null
    private val binder get() = _binding!!

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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binder.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel = ViewModelProviders.of(this).get(ProfileViewModel::class.java)
        super.onViewCreated(view, savedInstanceState)

        binder.ibtnClose.setOnClickListener {
            mFragmentListener?.popTopMostFragment()
        }
        binder.fbtnBack.setOnClickListener {
            mFragmentListener?.popTopMostFragment()
        }

        binder.edtUsrId.setText("#$usrId")
        binder.edtUsrName.setText(usrName)
        binder.edtUsrEmail.setText(usrEmail)

        Picasso.get().load(usrProfileUrl)
            .resize(120, 120)
            .centerCrop()
            .placeholder(R.color.colorCyan)
            .error(R.color.colorRed)
            .into(binder.civProfilePic)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}
