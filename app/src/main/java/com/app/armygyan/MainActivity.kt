package com.app.armygyan

import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.core.app.ShareCompat
import androidx.fragment.app.Fragment
import com.app.armygyan.annotation.FragmentType
import com.app.armygyan.base.BaseActivity
import com.app.armygyan.broadcastreceiver.InternetBroadcastReceiver
import com.app.armygyan.extra.NetworkUtil
import com.app.armygyan.helper.LocaleHelper
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import kotlinx.coroutines.*


class MainActivity : BaseActivity(){
    var backPressedTwice: Boolean = false
    private lateinit var mInterstitialAd: InterstitialAd
    var isFirstTime: Boolean = true

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        val bundle = intent.extras
        intent.extras?.let {
            if (it.containsKey("profile")) {
                // do nothing ......
            } else {
                showFragment(FragmentType.NOTIFICATION_FRAGMENT, null)
            }


        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE) // no capture
        setContentView(R.layout.activity_main)
        checkPlayServices()
        showFragment(FragmentType.SPLASH_FRAGMENT, null)
        // Initialize the Mobile Ads SDK.
        MobileAds.initialize(this) {}
        mInterstitialAd = InterstitialAd(this)
        mInterstitialAd.adUnitId = "ca-app-pub-7872107105590310/4646905684"
        // test ca-app-pub-3940256099942544/103317371
        mInterstitialAd.loadAd(AdRequest.Builder().build())
        registerReceiver(broadcastReceiver, intentFilter)


    }





    override fun onBackPressed() {
        val fr: Fragment = supportFragmentManager.findFragmentById(R.id.fl_container) ?: return
        when (fr.javaClass.simpleName) {
            FragmentType.SIGN_IN_FRAGMENT,FragmentType.HOME_FRAGMENT -> onBackPressDoubleClick()
            FragmentType.QUESTION_SET_FRAGMENT -> return // do nothing .....
            FragmentType.CHOOSE_LANGUAGE_FRAGMENT -> return // do nothing .....
            FragmentType.SCORECARD_FRAGMENT -> popTillFragment(FragmentType.QUIZ_CATEGORY_FRAGMENT, 0)
            else -> {
                popTopMostFragment()
                if (mInterstitialAd.isLoaded) mInterstitialAd.show() // remove id not required !!
            }
        }

    }

    override fun resetLocale(locale: String) {
        LocaleHelper.setLocale(this, locale)
        recreate()

    }

    override fun shareApp() {
        ShareCompat.IntentBuilder.from(this)
            .setType("text/plain")
            .setChooserTitle(getString(R.string.title_share_with))
            .setText("https://play.google.com/store/apps/details?id=" + packageName)
            .startChooser()
    }


    private fun onBackPressDoubleClick() {
        if (backPressedTwice) finish()

        backPressedTwice = true;
        Toast.makeText(this, getString(R.string.alert_back_press), Toast.LENGTH_SHORT).show();
        // IO,Main,Default
        CoroutineScope(Dispatchers.Default).launch {
            delay(2000)
            withContext(Dispatchers.Main) {
                backPressedTwice = false
            }
        }

    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase))
    }

    private fun checkPlayServices(): Boolean {
        val gApi: GoogleApiAvailability = GoogleApiAvailability.getInstance()
        val resultCode: Int = gApi.isGooglePlayServicesAvailable(this)
        if (resultCode != ConnectionResult.SUCCESS) {
            Toast.makeText(this, getString(R.string.alert_google_play_services), Toast.LENGTH_LONG)
                .show()
            finish()
            return false
        }
        return true
    }

    override fun getRootView(): View {
        return findViewById(R.id.fl_container)
    }

    override fun onDestroy() {
        unregisterReceiver(broadcastReceiver)
        super.onDestroy()
    }

    // detect internet throughout
    private val intentFilter = IntentFilter("android.net.conn.CONNECTIVITY_CHANGE")
    private val broadcastReceiver: InternetBroadcastReceiver =
        object : InternetBroadcastReceiver() {
            override fun onConnectionChanged() {
                if (!isFirstTime) {
                    if (NetworkUtil.isNetAvail(applicationContext)) {
                        showSnackBar(getString(R.string.alert_internet), R.color.colorGreen)
                    } else {
                        showSnackBar(getString(R.string.alert_no_internet))
                    }
                } else {
                    isFirstTime = false
                }
            }
        }
}

/*


                        BACKGROUND ------> onCreate called....
                        FOREGROUND ------> onNewIntent onCreate
                        KILLED     ------> onCreate
 */