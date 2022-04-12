package com.hekai.androidproject.network

import com.hekai.androidproject.entites.Contents
import com.hekai.androidproject.entites.Posts
import com.hekai.androidproject.entites.Responses
import com.hekai.androidproject.entites.Users
import com.hekai.androidproject.util.myBaseURL
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.MultipartBody
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*

private val moshi= Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()
private val BASE_URL= myBaseURL()
private val retrofit= Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

object NWPost{
    val nwPosts:NetWorkPosts by lazy {
        retrofit.create(NetWorkPosts::class.java)
    }
}
interface NetWorkPosts {
    //User相关的方法
    @POST("Users/validUser/{username}/{password}")
    suspend fun vaLidUser(@Path("username")username:String,@Path("password")password:String):Users
    @POST("Users/insert")
    suspend fun insertUser(@Body users: Users):Boolean
    @POST("Users/validUserByUserName/{username}")
    suspend fun isUserExist(@Path("username")username: String):Boolean
    @POST("Users/updatePublishNumber/{id}")
    suspend fun updatePublishNumber(@Path("id")id:Int)
    @POST("Users/getUserById/{id}")
    suspend fun getUserById(@Path("id")id:Int):Users

    //Posts相关的方法
    @GET("Posts/getPostsList")
    suspend fun getPostsList():List<Posts>
    @POST("Posts/insert")
    suspend fun insertPost(@Body posts: Posts):Int

    //Content相关的方法
    @GET("Contents/getContentById/{uid}")
    suspend fun getContentById(@Path("uid")user:Int) : Contents
    @POST("Contents/insert")
    suspend fun insertIntoContents(@Body contents: Contents)

    //上传图片相关的方法
    @Multipart
    @POST("imageUpload/upload")
    suspend fun imageUpload(@Part file:MultipartBody.Part)

    //Response相关方法
    @POST("Responses/getAllResponseByResponsePost/{postId}")
    suspend fun getAllResponseByPostID(@Path("postId")postId:Int):List<Responses>
    @POST("Responses/insertResponse")
    suspend fun insertResponse(@Body response:Responses)
}