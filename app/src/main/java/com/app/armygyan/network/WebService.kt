package com.app.armygyan.network

import com.app.armygyan.miscellaneous.model.AboutUsResponse
import com.app.armygyan.miscellaneous.model.TermAndConditionResponse
import com.app.armygyan.miscellaneous.model.GeneralResponse
import com.app.armygyan.notification.model.NotificationControllerResponse
import com.app.armygyan.notification.model.NotificationResponse
import com.app.armygyan.profile.ProfileInfoResponse
import com.app.armygyan.profile.model.ProfileInfo
import com.app.armygyan.quizz.model.QuizDetailResponse
import com.app.armygyan.quizz.model.QuizzesResponse
import com.app.armygyan.signin.model.SignInResponse
import com.app.armygyan.studymaterial.model.StudyMaterialCategoryResponse
import com.app.armygyan.studymaterial.model.StudyMaterialChapterDetailResponse
import com.app.armygyan.studymaterial.model.StudyMaterialChaptersResponse
import retrofit2.Call
import retrofit2.http.*
import java.util.HashMap


interface WebService {
    @FormUrlEncoded
    @POST(WebUrl.SIGN_IN)
    fun attemptSignIn(@HeaderMap token:Map<String, String>,
                      @Field("name") name: String?,
                      @Field("email") email: String?,
                      @Field("device_id") deviceId: String?): Call<SignInResponse>


    @POST(WebUrl.SIGN_OUT)
    fun attemptSignOut(@HeaderMap token:HashMap<String, String>): Call<GeneralResponse>


    @GET(WebUrl.STUDY_MATERIAL_CATEGORY)
    fun fetchStudyMaterialCategories(@HeaderMap headers:HashMap<String, String>): Call<StudyMaterialCategoryResponse>

    @FormUrlEncoded
    @POST(WebUrl.FAVOURITE_CATEGORY)
    fun isFavouriteCategory(@HeaderMap headers:HashMap<String, String>,@Field("category_id") categoryId: Long?): Call<GeneralResponse>


    @GET(WebUrl.FETCH_CHAPTERS)
    fun fetchStudyMaterialChapters(@HeaderMap headers:HashMap<String, String>,@Query("page") pageNo: Long?,@Query("id") categoryId: Long?): Call<StudyMaterialChaptersResponse>

    @FormUrlEncoded
    @POST(WebUrl.FETCH_CHAPTER_DETAIL)
    fun fetchStudyMaterialChapterDetail(@HeaderMap headers:HashMap<String, String>,@Field("id") chapterId: Long?): Call<StudyMaterialChapterDetailResponse>


    @GET(WebUrl.FETCH_QUIZZES)
    fun fetchQuizzes(@HeaderMap headers:HashMap<String, String>,@Query("page") pageNo: Long?): Call<QuizzesResponse>

    @FormUrlEncoded
    @POST(WebUrl.FETCH_QUIZ_DETAIL)
    fun fetchQuizDetail(@HeaderMap headers:HashMap<String, String>,@Field("id") quizId: Long?): Call<QuizDetailResponse>

    @GET(WebUrl.EN_DI_NOTIFICATION)
    fun alterNotificationStatus(@HeaderMap headers:HashMap<String, String>): Call<NotificationControllerResponse>

    @GET(WebUrl.FETCH_NOTIFICATIONS)
    fun fetchNotifications(@HeaderMap headers:HashMap<String, String>): Call<NotificationResponse>

    @FormUrlEncoded
    @POST(WebUrl.ABOUT_US)
    fun fetchAboutUs(@HeaderMap headers: HashMap<String, String>, @Field("id") id: Long?): Call<AboutUsResponse>

    @FormUrlEncoded
    @POST(WebUrl.TERMS_AND_CONDITIONS)
    fun fetchTermAndCondition(@HeaderMap headers:HashMap<String, String>,@Field("id") id: Long?): Call<TermAndConditionResponse>


}