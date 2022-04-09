package com.hekai.androidproject.network

import com.hekai.androidproject.entites.Contents
import com.hekai.androidproject.entites.Posts
import com.hekai.androidproject.entites.Users
import com.hekai.androidproject.util.myBaseURL
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

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

    //Posts相关的方法
    @GET("Posts/getPostsList")
    suspend fun getPostsList():List<Posts>

    //Content相关的方法
    @GET("Contents/getContentById/{uid}")
    suspend fun getContentById(@Path("uid")user:Int) : Contents
}