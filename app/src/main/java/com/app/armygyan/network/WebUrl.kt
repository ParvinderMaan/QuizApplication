package com.app.armygyan.network

/**
  this class has private constructor
 in order to restrict its object creation
 it contains all the url used for network request
**/
class WebUrl private constructor() {

    companion object {
        // base urls
        const val BASE = "https://christosonline.com/armygyan/public/api/"    //

        // child urls
        const val SIGN_IN = "register"
        const val SIGN_OUT = "logout"
        const val FETCH_PROFILE = ""
        const val UPDATE_PROFILE = ""
        const val ALTER_PROFILE_IMG = ""
        const val STUDY_MATERIAL_CATEGORY = "categories"
        const val FAVOURITE_CATEGORY = "favourite-category"
        const val FETCH_CHAPTERS = "chapters"
        const val FETCH_CHAPTER_DETAIL = "chapter-details"
        const val FETCH_QUIZZES = "quizzes"
        const val FETCH_QUIZ_DETAIL = "quiz-details"
        const val EN_DI_NOTIFICATION = "enable-disable-notifications"
        const val FETCH_NOTIFICATIONS = "notifications"
        const val DELETE_ALL_NOTIFICATIONS = ""
        const val ABOUT_US = "page-details"
        const val TERMS_AND_CONDITIONS = "page-details"

    }




}
