package com.fancylight.helpdesk.network

import android.os.Build
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.lang.reflect.Type
import java.util.*

interface UserService {

    //모든 접수리스트 가져오기
    @GET("requests")
    fun testGet(
            @Header ("Authorization") Authorization :String
    ): retrofit2.Call<Array<getRequest>>

    //사원의 완료되지 않은 요청 모두 보여
    @GET("mypage")
    fun requestListGet(
            @Header ("Authorization") Authorization :String
    ): retrofit2.Call<Array<getRequest>>

    //요원 자신에게 할당된 요청 가져오기
    @GET("agent")
    fun workListGet(
            @Header ("Authorization") Authorization :String
    ): retrofit2.Call<Array<getRequest>>

    //요원에게 할당전인 요청 관리자에게 가져오
    @GET("admin")
    fun adminListGet(
            @Header ("Authorization") Authorization :String
    ): retrofit2.Call<Array<getRequest>>

    //csr 상태 가져오기
    @GET("csrstatus")
    fun csrget(): retrofit2.Call<CSR>


    @FormUrlEncoded
    @POST("auth/login")
    fun loginPost(@Field("User_id") User_id: String,
                 @Field("User_password") User_password: String,
    ): retrofit2.Call<Login>


    @FormUrlEncoded
    @POST("android")
    fun FcmPost(
            @Header ("Authorization") Authorization :String,
            @Field("DEVICE_ID") DEVICE_ID: String,
    ): retrofit2.Call<Fcm>

    @Multipart
    @POST("requests")
    fun dataPost(
            @Header ("Authorization") Authorization :String,
            @Part imagefile : MultipartBody.Part,
            @Part("body") body : String
    ): retrofit2.Call<JsonData>

    //챗봇 통신
    @FormUrlEncoded
    @POST("dialogflow/textQuery")
    fun ChatbotPost(
            @Header ("Authorization") Authorization :String,
            @Field("text") text: String,
    ): retrofit2.Call<ChatbotReturn>

    //비밀번호 변경할 때
    @FormUrlEncoded
    @PUT("users/password")
    fun passwordChangePut(
        @Header ("Authorization") Authorization :String,
        @Field("origin_password") origin_password: String,
        @Field("new_password") new_password: String
    ): retrofit2.Call<ChangePassword>

    //요원이 할당된 요청을 작업중이나 작업완료로 바꿀 때
    @FormUrlEncoded
    @PUT("agent/{num}/{complete}")
    fun WorkChangePut(
            @Header ("Authorization") Authorization :String,
            @Path("num") num : Int,
            @Path("complete") complete : Int,
            @Field("CSR_STATUS") CSR_STATUS: String,
            @Field("DATE") DATE: String
    ): retrofit2.Call<ResultMessage>

    //관리자가 접수된 요청을 반려할 때
    @FormUrlEncoded
    @PUT("admin/deny")
    fun adminDenyPut(
            @Header ("Authorization") Authorization :String,
            @Field("REQ_SEQ") REQ_SEQ: Int,
    ): retrofit2.Call<ResultMessage>




}

val retrofit = Retrofit.Builder()
    .baseUrl("http://54.180.123.42/api/")
    .addConverterFactory(GsonConverterFactory.create())
    .build()

object UserApi{
    var ttt : String =""
    var fcmToken : String =""
    val service : UserService by lazy {
        retrofit.create(UserService::class.java)
    }

    fun extractJwt(jwt: String): String {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return "Requires SDK 26"
        val parts = jwt.split(".")
        return try {
            val charset = charset("UTF-8")
            val header = String(Base64.getUrlDecoder().decode(parts[0].toByteArray(charset)), charset)
            val payload = String(Base64.getUrlDecoder().decode(parts[1].toByteArray(charset)), charset)
            //"$header\n$payload"
            "$payload"
        } catch (e: Exception) {
            "Error parsing JWT: $e"
        }
    }
}



