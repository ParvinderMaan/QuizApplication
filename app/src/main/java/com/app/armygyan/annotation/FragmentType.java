package com.app.armygyan.annotation;

import androidx.annotation.StringDef;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
@StringDef({FragmentType.SPLASH_FRAGMENT, FragmentType.SIGN_IN_FRAGMENT,
        FragmentType.HOME_FRAGMENT,FragmentType.PROFILE_FRAGMENT,FragmentType.ABOUT_US_FRAGMENT,
        FragmentType.NOTIFICATION_FRAGMENT,FragmentType.STUDY_MATERIAL_FRAGMENT,FragmentType.CHAPTER_FRAGMENT,
        FragmentType.CHAPTER_DETAIL_FRAGMENT,FragmentType.QUESTION_SET_FRAGMENT,FragmentType.SCORECARD_FRAGMENT
        ,FragmentType.QUIZ_CATEGORY_FRAGMENT,FragmentType.TERM_CONDITION_FRAGMENT,FragmentType.SETTING_FRAGMENT,
        FragmentType.SHOW_ANSWER,FragmentType.CHOOSE_LANGUAGE_FRAGMENT })
public @interface FragmentType {
    String SPLASH_FRAGMENT = "SplashFragment";
    String SIGN_IN_FRAGMENT= "SignInFragment";
    String HOME_FRAGMENT = "HomeFragment";
    String SETTING_FRAGMENT = "SettingFragment";
    String PROFILE_FRAGMENT = "ProfileFragment";
    String ABOUT_US_FRAGMENT = "AboutUsFragment";
    String NOTIFICATION_FRAGMENT = "NotificationFragment";
    String STUDY_MATERIAL_FRAGMENT = "StudyMaterialFragment";
    String CHAPTER_FRAGMENT = "ChapterFragment";
    String CHAPTER_DETAIL_FRAGMENT = "ChapterDetailFragment";
    String QUESTION_SET_FRAGMENT = "QuestionSetFragment";
    String SCORECARD_FRAGMENT = "ScoreCardFragment";
    String QUIZ_CATEGORY_FRAGMENT= "QuizCategoryFragment";
    String TERM_CONDITION_FRAGMENT="TermAndConditionFragment";
    String SHOW_ANSWER="AnswerFragment";
    String CHOOSE_LANGUAGE_FRAGMENT="ChooseLanguageFragment";
}
    // Declare the constants
