package com.app.quiz.network

import com.app.quiz.miscellaneous.model.AboutUsResponse
import com.app.quiz.miscellaneous.model.NotificationStatusRequest
import com.app.quiz.miscellaneous.model.TermAndConditionResponse
import com.app.quiz.miscellaneous.model.GeneralResponse
import com.app.quiz.notification.model.NotificationResponse
import com.app.quiz.profile.ProfileInfoResponse
import com.app.quiz.profile.model.ProfileInfo
import com.app.quiz.quizz.model.QuizDetailResponse
import com.app.quiz.quizz.model.QuizzesResponse
import com.app.quiz.signin.model.SignInResponse
import com.app.quiz.studymaterial.FavouriteRequest
import com.app.quiz.studymaterial.model.StudyMaterialCategoryResponse
import com.app.quiz.studymaterial.model.StudyMaterialChapterDetailResponse
import com.app.quiz.studymaterial.model.StudyMaterialChaptersResponse
import okhttp3.MultipartBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.*


interface WebService {
    @FormUrlEncoded
    @POST(WebUrl.SIGN_IN)
    fun attemptSignIn(@HeaderMap token:Map<String, String>,
                      @Field("name") name: String?,
                      @Field("email") email: String?,
                      @Field("device_id") deviceId: String?): Call<SignInResponse>


    @POST(WebUrl.SIGN_OUT)
    fun attemptSignOut(@HeaderMap token:Map<String, String>): Call<GeneralResponse>


    @GET(WebUrl.FETCH_PROFILE)
    fun fetchProfileInfo(@Query("userId") userId: Int?): Call<ProfileInfoResponse>


    @POST(WebUrl.UPDATE_PROFILE)
    fun updateProfileInfo(@Body input: ProfileInfo?): Call<GeneralResponse>

    @Multipart
    @POST(WebUrl.ALTER_PROFILE_IMG)
    fun alterProfilePic(@Part imgFile: MultipartBody.Part?): Call<GeneralResponse> // fileData


    @GET(WebUrl.STUDY_MATERIAL_CATEGORY)  //@HeaderMap token:Map<String, String>,
    fun fetchStudyMaterialCategories(@Query("page") pageNo: Long?): Call<StudyMaterialCategoryResponse>

    @FormUrlEncoded
    @POST(WebUrl.FAVOURITE_CATEGORY)
    fun isFavouriteCategory(@Field("category_id") categoryId: Long?): Call<GeneralResponse>


    @GET(WebUrl.FETCH_CHAPTERS)
    fun fetchStudyMaterialChapters(@Query("page") pageNo: Long?,@Query("id") categoryId: Long?): Call<StudyMaterialChaptersResponse>

    @FormUrlEncoded
    @POST(WebUrl.FETCH_CHAPTER_DETAIL)
    fun fetchStudyMaterialChapterDetail(@Field("id") chapterId: Long?): Call<StudyMaterialChapterDetailResponse>


    @GET(WebUrl.FETCH_QUIZZES)
    fun fetchQuizzes(@Query("page") pageNo: Long?): Call<QuizzesResponse>

    @FormUrlEncoded
    @POST(WebUrl.FETCH_QUIZ_DETAIL)
    fun fetchQuizDetail(@Field("id") quizId: Long?): Call<QuizDetailResponse>


    @POST(WebUrl.EN_DI_NOTIFICATION)
    fun alterNotificationStatus(@Body input: NotificationStatusRequest?): Call<GeneralResponse>

    @GET(WebUrl.FETCH_NOTIFICATIONS)
    fun fetchNotifications(): Call<NotificationResponse>

    @POST(WebUrl.DELETE_ALL_NOTIFICATIONS) //JSONObject== userId
    fun deleteAllNotifications(@Body input: JSONObject): Call<GeneralResponse>

    @FormUrlEncoded
    @POST(WebUrl.ABOUT_US)
    fun fetchAboutUs(@Field("id") id: Long?): Call<AboutUsResponse>

    @FormUrlEncoded
    @POST(WebUrl.TERMS_AND_CONDITIONS)
    fun fetchTermAndCondition(@Field("id") id: Long?): Call<TermAndConditionResponse>


}