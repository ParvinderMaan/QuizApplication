package com.app.quiz

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.app.quiz.annotation.FragmentType
import com.app.quiz.helper.LocaleHelper
import com.app.quiz.home.HomeFragment
import com.app.quiz.interfacor.HomeFragmentSelectedListener
import com.app.quiz.miscellaneous.view.AboutUsFragment
import com.app.quiz.miscellaneous.view.SettingFragment
import com.app.quiz.miscellaneous.view.SplashFragment
import com.app.quiz.miscellaneous.view.TermAndConditionFragment
import com.app.quiz.notification.view.NotificationFragment
import com.app.quiz.profile.view.ProfileFragment
import com.app.quiz.quizz.view.QuestionSetFragment
import com.app.quiz.quizz.view.QuizCategoryFragment
import com.app.quiz.quizz.view.ScoreCardFragment
import com.app.quiz.signin.view.SignInFragment
import com.app.quiz.studymaterial.view.ChapterDetailFragment
import com.app.quiz.studymaterial.view.ChapterFragment
import com.app.quiz.studymaterial.view.StudyMaterialFragment
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import kotlinx.coroutines.*


class MainActivity : AppCompatActivity(), HomeFragmentSelectedListener {
    var backPressedTwice:Boolean=false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE) // no capture
        setContentView(R.layout.activity_main)
        checkPlayServices()
        showFragment(FragmentType.SPLASH_FRAGMENT, null)

    }

    override fun showFragment(@FragmentType tag: String, payload: Any?) {
        when (tag) {
            FragmentType.SPLASH_FRAGMENT -> {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fl_container, SplashFragment.newInstance())
                    .addToBackStack(FragmentType.SPLASH_FRAGMENT)
                    .commitAllowingStateLoss()
            }
            FragmentType.SIGN_IN_FRAGMENT -> {
                supportFragmentManager.beginTransaction()
//                    .setCustomAnimations(
//                        R.anim.enter_from_right,
//                        R.anim.exit_to_left,
//                        R.anim.enter_from_left,
//                        R.anim.exit_to_right
//                    )
                    .replace(R.id.fl_container, SignInFragment.newInstance())
                    .addToBackStack(FragmentType.SIGN_IN_FRAGMENT)
                    .commit()

            }
            FragmentType.HOME_FRAGMENT -> {
                supportFragmentManager.beginTransaction()
//                    .setCustomAnimations(
//                        R.anim.enter_from_right,
//                        R.anim.exit_to_left,
//                        R.anim.enter_from_left,
//                        R.anim.exit_to_right
//                    )
                    .replace(R.id.fl_container, HomeFragment.newInstance())
                    .addToBackStack(FragmentType.HOME_FRAGMENT)
                    .commit()
            }
            FragmentType.SETTING_FRAGMENT -> {
                supportFragmentManager.beginTransaction()
//                    .setCustomAnimations(
//                        R.anim.enter_from_right,
//                        R.anim.exit_to_left,
//                        R.anim.enter_from_left,
//                        R.anim.exit_to_right
//                    )
                    .replace(R.id.fl_container, SettingFragment.newInstance())
                    .addToBackStack(FragmentType.SETTING_FRAGMENT)
                    .commit()

            }
            FragmentType.PROFILE_FRAGMENT -> {
                supportFragmentManager.beginTransaction()
//                    .setCustomAnimations(
//                        R.anim.enter_from_right,
//                        R.anim.exit_to_left,
//                        R.anim.enter_from_left,
//                        R.anim.exit_to_right
//                    )
                    .replace(R.id.fl_container, ProfileFragment.newInstance())
                    .addToBackStack(FragmentType.PROFILE_FRAGMENT)
                    .commit()

            }
            FragmentType.ABOUT_US_FRAGMENT -> {
                supportFragmentManager.beginTransaction()
//                    .setCustomAnimations(
//                        R.anim.enter_from_right,
//                        R.anim.exit_to_left,
//                        R.anim.enter_from_left,
//                        R.anim.exit_to_right
//                    )
                    .replace(R.id.fl_container, AboutUsFragment.newInstance())
                    .addToBackStack(FragmentType.ABOUT_US_FRAGMENT)
                    .commit()

            }
            FragmentType.NOTIFICATION_FRAGMENT -> {
                supportFragmentManager.beginTransaction()
//                    .setCustomAnimations(
//                        R.anim.enter_from_right,
//                        R.anim.exit_to_left,
//                        R.anim.enter_from_left,
//                        R.anim.exit_to_right
//                    )
                    .replace(R.id.fl_container, NotificationFragment.newInstance())
                    .addToBackStack(FragmentType.NOTIFICATION_FRAGMENT)
                    .commit()

            }
            FragmentType.STUDY_MATERIAL_FRAGMENT -> {
                supportFragmentManager.beginTransaction()
//                    .setCustomAnimations(
//                        R.anim.enter_from_right,
//                        R.anim.exit_to_left,
//                        R.anim.enter_from_left,
//                        R.anim.exit_to_right
//                    )
                    .replace(R.id.fl_container, StudyMaterialFragment.newInstance())
                    .addToBackStack(FragmentType.STUDY_MATERIAL_FRAGMENT )
                    .commit()

            }

            FragmentType.CHAPTER_FRAGMENT -> {
                supportFragmentManager.beginTransaction()
//                    .setCustomAnimations(
//                        R.anim.enter_from_right,
//                        R.anim.exit_to_left,
//                        R.anim.enter_from_left,
//                        R.anim.exit_to_right
//                    )
                    .replace(R.id.fl_container, ChapterFragment.newInstance())
                    .addToBackStack(FragmentType.CHAPTER_FRAGMENT)
                    .commit()

            }

            FragmentType.CHAPTER_DETAIL_FRAGMENT -> {
                supportFragmentManager.beginTransaction()
//                    .setCustomAnimations(
//                        R.anim.enter_from_right,
//                        R.anim.exit_to_left,
//                        R.anim.enter_from_left,
//                        R.anim.exit_to_right
//                    )
                    .replace(R.id.fl_container, ChapterDetailFragment.newInstance())
                    .addToBackStack(FragmentType.CHAPTER_DETAIL_FRAGMENT)
                    .commit()

            }
            FragmentType.QUESTION_SET_FRAGMENT -> {
                supportFragmentManager.beginTransaction()
//                    .setCustomAnimations(
//                        R.anim.enter_from_right,
//                        R.anim.exit_to_left,
//                        R.anim.enter_from_left,
//                        R.anim.exit_to_right
//                    )
                    .replace(R.id.fl_container, QuestionSetFragment.newInstance())
                    .addToBackStack(FragmentType.QUESTION_SET_FRAGMENT)
                    .commit()

            }

            FragmentType.SCORECARD_FRAGMENT -> {
                supportFragmentManager.beginTransaction()
                    .setCustomAnimations(
                        R.anim.enter_from_right,
                        R.anim.exit_to_left,
                        R.anim.enter_from_left,
                        R.anim.exit_to_right
                    )
                    .replace(R.id.fl_container, ScoreCardFragment.newInstance(""))
                    .addToBackStack(FragmentType.SCORECARD_FRAGMENT)
                    .commit()
            }
            FragmentType.QUIZ_CATEGORY_FRAGMENT -> {
                supportFragmentManager.beginTransaction()
//                    .setCustomAnimations(
//                        R.anim.enter_from_right,
//                        R.anim.exit_to_left,
//                        R.anim.enter_from_left,
//                        R.anim.exit_to_right
//                    )
                    .replace(R.id.fl_container, QuizCategoryFragment.newInstance())
                    .addToBackStack(FragmentType.QUIZ_CATEGORY_FRAGMENT)
                    .commit()
            }

            FragmentType.TERM_CONDITION_FRAGMENT -> {
                supportFragmentManager.beginTransaction()
//                    .setCustomAnimations(
//                        R.anim.enter_from_right,
//                        R.anim.exit_to_left,
//                        R.anim.enter_from_left,
//                        R.anim.exit_to_right
//                    )
                    .replace(R.id.fl_container, TermAndConditionFragment.newInstance())
                    .addToBackStack(FragmentType.TERM_CONDITION_FRAGMENT)
                    .commit()
            }

            else ->{}
        }
    }


    override fun popTillFragment(tag: String, flag: Int) {
        supportFragmentManager.popBackStack(tag, flag)
    }

    override fun popTopMostFragment() {
        supportFragmentManager.popBackStack()
    }

    override fun onBackPressed() {
        val fr: Fragment = supportFragmentManager.findFragmentById(R.id.fl_container) ?: return
        when (fr.javaClass.simpleName) {
            FragmentType.SIGN_IN_FRAGMENT,FragmentType.HOME_FRAGMENT -> onBackPressDoubleClick()
            FragmentType.QUESTION_SET_FRAGMENT -> return // do nothing .....
            FragmentType.SCORECARD_FRAGMENT -> popTillFragment(FragmentType.QUIZ_CATEGORY_FRAGMENT,0)
            else -> popTopMostFragment()
        }

    }

    override fun resetLocale(locale: String) {
        LocaleHelper.setLocale(this,locale)
        recreate()

    }


    fun onBackPressDoubleClick(){
        if (backPressedTwice) finish()

        backPressedTwice = true;
         Toast.makeText(this, "Click back again to exit", Toast.LENGTH_SHORT).show();
        // IO,Main,Default
        CoroutineScope(Dispatchers.Default).launch {
            delay(2000)
            withContext(Dispatchers.Main) {
                backPressedTwice=false
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
            Toast.makeText(this, "install google play services", Toast.LENGTH_LONG).show()
            finish()
            return false
        }
        return true
    }
}
