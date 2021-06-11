package com.app.armygyan.quizz.view

import android.Manifest
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.app.armygyan.R
import com.app.armygyan.annotation.FragmentType
import com.app.armygyan.base.BaseFragment
import com.app.armygyan.databinding.FragmentScoreCardBinding
import com.app.armygyan.extension.general.hasPermission
import com.app.armygyan.helper.MediaScannerUtility
import com.app.armygyan.helper.PaintUtil
import com.app.armygyan.interfacor.HomeFragmentSelectedListener
import com.app.armygyan.quizz.model.QuestionSet
import com.app.armygyan.quizz.viewmodel.ScoreCardViewModel
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.material.snackbar.Snackbar
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.File
import java.util.*
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE


class ScoreCardFragment : BaseFragment() {
    private  var saveImageJob: Job? = null
    private var quizDetail: ArrayList<QuestionSet>? = null
    private var mFragmentListener: HomeFragmentSelectedListener? = null
    private lateinit var viewModel: ScoreCardViewModel
    private lateinit var mInterstitialAd: InterstitialAd
    private var _binding: FragmentScoreCardBinding? = null
    private val binder get() = _binding!!

    companion object {
        @Suppress("UNCHECKED_CAST")
        fun newInstance(payload: Any?): ScoreCardFragment {
            val fragment = ScoreCardFragment()
            val bundle = Bundle()
            val quizDetail = payload as ArrayList<QuestionSet>
            bundle.putParcelableArrayList("KEY", quizDetail)
            fragment.arguments = bundle
            return fragment
        }
    }
    private val mediaScannerUtility by lazy {
        MediaScannerUtility(requireActivity().applicationContext)
    }

    override fun getRootView(): View {
        return binder.clRoot
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mFragmentListener = context as HomeFragmentSelectedListener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ScoreCardViewModel::class.java)

        // collect our intent
        if (arguments != null) {
            quizDetail = arguments?.getParcelableArrayList("KEY")
            quizDetail?.let { viewModel.setQuizDetail(it) }
            quizDetail?.forEach { println(it.toString()) }
        }

    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScoreCardBinding.inflate(inflater, container, false)
        return binder.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListener()
        initObserver()



        mInterstitialAd = InterstitialAd(activity)
        mInterstitialAd.adUnitId =
            "ca-app-pub-3940256099942544/103317371" // test ca-app-pub-3940256099942544/103317371
        mInterstitialAd.loadAd(AdRequest.Builder().build())
        mInterstitialAd.adListener = object : AdListener() {
            override fun onAdLoaded() {
                if (mInterstitialAd.isLoaded) mInterstitialAd.show()
            }
        }

        if(requireActivity().hasPermission(WRITE_EXTERNAL_STORAGE) && requireActivity().hasPermission(READ_EXTERNAL_STORAGE))
        viewModel.setAllPermGranted(true)
    }

    private fun initObserver() {
        viewModel.getQuizDetail().observe(viewLifecycleOwner, { lstOfQuestions ->
            val totQues: Int = lstOfQuestions.size
            val totAttemptedQues: Int = lstOfQuestions.filter { it.ansOptSelected != 0 }.size
            var correctAnsCount = 0
            lstOfQuestions.forEach {
                when (it.ansOptSelected) {
                    1 -> if ("a" == it.ansOption) ++correctAnsCount
                    2 -> if ("b" == it.ansOption) ++correctAnsCount
                    3 -> if ("c" == it.ansOption) ++correctAnsCount
                    4 -> if ("d" == it.ansOption) ++correctAnsCount
                }
            }


            val totWrongQues = totAttemptedQues - correctAnsCount
            val totScore = ((correctAnsCount * 2) - (totWrongQues * 0.5))

            binder.tvTotQues.text = String.format(Locale.getDefault(), "%02d", totQues)
            binder.tvTotAttemptedQues.text = String.format(
                Locale.getDefault(),
                "%02d",
                totAttemptedQues
            )
            binder.tvTotCorrectQues.text = String.format(
                Locale.getDefault(),
                "%02d",
                correctAnsCount
            )
            binder.tvTotWrongQues.text = String.format(Locale.getDefault(), "%02d", totWrongQues)
            binder.tvYourScore.text = String.format(Locale.getDefault(), "%.01f", totScore)

            val percentage = (totScore * 100) / (totQues * 2)
            val performance = when (percentage.toInt()) {
                in 90..100 -> resources.getString(R.string.title_excellent)
                in 80..90 -> resources.getString(R.string.title_good)
                in 60..79 -> resources.getString(R.string.title_average)
                else -> resources.getString(R.string.title_poor)
            }
            binder.tvOverAllPerformance.text = performance
        })
        viewModel.getToastInfo().observe(viewLifecycleOwner, { showSnackBar(it) })

        viewModel.getLoading().observe(viewLifecycleOwner, {
            when (it) {
                true -> {
                    binder.pbSave.visibility = View.VISIBLE
                    binder.fbtnSaveResult.visibility = View.VISIBLE
                }
                false -> {
                    binder.pbSave.visibility = View.INVISIBLE
                    binder.fbtnSaveResult.visibility = View.INVISIBLE
                }
            }

        })

    }

    private fun initListener() {
        binder.fbtnSaveResult.setOnClickListener {
            if (Build.VERSION.SDK_INT >= 23 && !viewModel.isAllPermGranted()) {
                Dexter.withContext(requireActivity())
                    .withPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .withListener(multiplePermissionsListener)
                    .check()

                return@setOnClickListener
            }



            saveImageJob = viewLifecycleOwner.lifecycleScope.launch {
                viewModel.isLoading.postValue(true)
                val outputFile = viewModel.getOutputFile()
                val bitmap = PaintUtil.createBitmapOf(binder.clRoot)
                val resultFile = PaintUtil.saveImageOf(bitmap, outputFile)
                viewModel.isLoading.postValue(false)
                if (resultFile.exists()){
                    showSnackBar(getString(R.string.alert_result_saved), R.color.colorGreen)
                    mediaScannerUtility.scan(resultFile)
                    binder.fbtnSaveResult.visibility=View.INVISIBLE
                }
                else showSnackBar(getString(R.string.alert_cannot_create_output))

            }
        }
        binder.fbtnShowAnswers.setOnClickListener {
            mFragmentListener?.showFragment(FragmentType.SHOW_ANSWER, quizDetail)
        }
    }

    private var multiplePermissionsListener: MultiplePermissionsListener =object:
        MultiplePermissionsListener {
        override fun onPermissionsChecked(report: MultiplePermissionsReport) {
            if(report.areAllPermissionsGranted()){
                viewModel.setAllPermGranted(true)
            }
            if(!report.areAllPermissionsGranted()){
                viewModel.setAllPermGranted(false)
            }
            if(report.isAnyPermissionPermanentlyDenied){
                viewModel.setAllPermGranted(false)
            }
        }
        override fun onPermissionRationaleShouldBeShown(
            permissions: MutableList<PermissionRequest>,
            token: PermissionToken
        ) {
            token.continuePermissionRequest()
        }
    }



    override fun onDestroyView() {
        saveImageJob?.cancel()
        super.onDestroyView()
        _binding = null
    }


}
