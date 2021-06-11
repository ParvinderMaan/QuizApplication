package com.app.armygyan.base

import android.view.View
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.app.armygyan.R
import com.app.armygyan.annotation.FragmentType
import com.app.armygyan.home.HomeFragment
import com.app.armygyan.interfacor.HomeFragmentSelectedListener
import com.app.armygyan.miscellaneous.view.*
import com.app.armygyan.notification.view.NotificationFragment
import com.app.armygyan.profile.view.ProfileFragment
import com.app.armygyan.quizz.view.AnswerFragment
import com.app.armygyan.quizz.view.QuestionSetFragment
import com.app.armygyan.quizz.view.QuizCategoryFragment
import com.app.armygyan.quizz.view.ScoreCardFragment
import com.app.armygyan.signin.view.SignInFragment
import com.app.armygyan.studymaterial.view.ChapterDetailFragment
import com.app.armygyan.studymaterial.view.ChapterFragment
import com.app.armygyan.studymaterial.view.StudyMaterialFragment
import com.google.android.material.snackbar.Snackbar

abstract class BaseActivity : AppCompatActivity() , HomeFragmentSelectedListener {

    fun showSnackBar(msg: String, @ColorRes colorId: Int = R.color.colorRed) {
        val snackBar = Snackbar.make(getRootView(), msg, Snackbar.LENGTH_LONG)
        snackBar.view.setBackgroundColor(ContextCompat.getColor(this, colorId))
        snackBar.show()
    }
    override fun showFragment(@FragmentType tag: String, payload: Any?) {
        when (tag) {
            FragmentType.SPLASH_FRAGMENT -> {
                supportFragmentManager.beginTransaction()
                    .apply {
                        replace(R.id.fl_container, SplashFragment.newInstance())  // #
                        addToBackStack(FragmentType.SPLASH_FRAGMENT)              //
                        commitAllowingStateLoss()
                    }

            }
            FragmentType.SIGN_IN_FRAGMENT -> {
                supportFragmentManager.beginTransaction()
                    .apply {
                        replace(R.id.fl_container, SignInFragment.newInstance(), FragmentType.SIGN_IN_FRAGMENT)
                        addToBackStack(FragmentType.SIGN_IN_FRAGMENT)
                        commit()
                    }

            }
            FragmentType.HOME_FRAGMENT -> {
                supportFragmentManager.beginTransaction()
                    .apply {
                        replace(R.id.fl_container, HomeFragment.newInstance())
                        addToBackStack(FragmentType.HOME_FRAGMENT)
                        commit()
                    }

            }
            FragmentType.SETTING_FRAGMENT -> {
                supportFragmentManager.beginTransaction()
                    .apply {
                        replace(R.id.fl_container, SettingFragment.newInstance())
                        addToBackStack(FragmentType.SETTING_FRAGMENT)
                        commit()
                    }
            }
            FragmentType.PROFILE_FRAGMENT -> {
                supportFragmentManager.beginTransaction()
                    .apply {
                        replace(R.id.fl_container, ProfileFragment.newInstance())
                        addToBackStack(FragmentType.PROFILE_FRAGMENT)
                        commit()
                    }
            }
            FragmentType.ABOUT_US_FRAGMENT -> {
                supportFragmentManager.beginTransaction()
                    .apply {
                        replace(R.id.fl_container, AboutUsFragment.newInstance())
                        addToBackStack(FragmentType.ABOUT_US_FRAGMENT)
                        commit()
                    }
            }
            FragmentType.NOTIFICATION_FRAGMENT -> {
                supportFragmentManager.beginTransaction()
                    .apply {
                        replace(R.id.fl_container, NotificationFragment.newInstance())
                        addToBackStack(FragmentType.NOTIFICATION_FRAGMENT)
                        commit()
                    }

            }
            FragmentType.STUDY_MATERIAL_FRAGMENT -> {
                supportFragmentManager.beginTransaction()
                    .apply {
                        replace(R.id.fl_container, StudyMaterialFragment.newInstance())
                        addToBackStack(FragmentType.STUDY_MATERIAL_FRAGMENT)
                        commit()
                    }
            }
            FragmentType.CHAPTER_FRAGMENT -> {
                supportFragmentManager.beginTransaction()
                    .apply {
                        replace(R.id.fl_container, ChapterFragment.newInstance(payload))
                        addToBackStack(FragmentType.CHAPTER_FRAGMENT)
                        commit()
                    }

            }
            FragmentType.CHAPTER_DETAIL_FRAGMENT -> {
                supportFragmentManager.beginTransaction()
                    .apply {
                        replace(R.id.fl_container, ChapterDetailFragment.newInstance(payload))
                        addToBackStack(FragmentType.CHAPTER_DETAIL_FRAGMENT)
                        commit()
                    }
            }
            FragmentType.QUESTION_SET_FRAGMENT -> {
                supportFragmentManager.beginTransaction()
                    .apply {
                        replace(R.id.fl_container, QuestionSetFragment.newInstance(payload)) // replace vs add
                        addToBackStack(FragmentType.QUESTION_SET_FRAGMENT)
                        commit()
                    }
            }
            FragmentType.SCORECARD_FRAGMENT -> {
                supportFragmentManager.beginTransaction()
                    .apply {
                        replace(R.id.fl_container, ScoreCardFragment.newInstance(payload))
                        addToBackStack(FragmentType.SCORECARD_FRAGMENT)
                        commit()
                    }
            }
            FragmentType.QUIZ_CATEGORY_FRAGMENT -> {
                supportFragmentManager.beginTransaction()
                    .apply {
                        replace(R.id.fl_container, QuizCategoryFragment.newInstance())
                        addToBackStack(FragmentType.QUIZ_CATEGORY_FRAGMENT)
                        commit()
                    }
            }
            FragmentType.TERM_CONDITION_FRAGMENT -> {
                supportFragmentManager.beginTransaction()
                    .apply {
                        add(R.id.fl_container, TermAndConditionFragment.newInstance())
                        addToBackStack(FragmentType.TERM_CONDITION_FRAGMENT)
                        commit()
                    }

            }
            FragmentType.SHOW_ANSWER -> {
                supportFragmentManager.beginTransaction()
                    .apply {
                        replace(R.id.fl_container, AnswerFragment.newInstance(payload))
                        addToBackStack(FragmentType.SHOW_ANSWER)
                        commit()
                    }
            }
            FragmentType.CHOOSE_LANGUAGE_FRAGMENT -> {
                supportFragmentManager.beginTransaction()
                    .apply {
                        replace(R.id.fl_container, ChooseLanguageFragment.newInstance())
                        addToBackStack(FragmentType.CHOOSE_LANGUAGE_FRAGMENT)
                        commit()
                    }
            }
        }
    }

    override fun popTillFragment(tag: String, flag: Int) {
        when (tag) {
            FragmentType.SIGN_IN_FRAGMENT -> {
                var fragment =
                    supportFragmentManager.findFragmentByTag(FragmentType.SIGN_IN_FRAGMENT);
                if (fragment != null) supportFragmentManager.popBackStack(tag, flag)
                else {
                    popTillFragment(FragmentType.HOME_FRAGMENT, 0)
                    showFragment(FragmentType.SIGN_IN_FRAGMENT, null)
                }
            }
            else -> supportFragmentManager.popBackStack(tag, flag)
        }
    }

    override fun popTopMostFragment() {
        supportFragmentManager.popBackStack()
    }


    override fun onBackPressed() {
        super.onBackPressed()
    }


    abstract fun getRootView(): View

}