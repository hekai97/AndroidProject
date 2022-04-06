package com.hekai.androidproject.network

import android.app.Application
import com.hekai.androidproject.entites.Posts
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

private val moshi= Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()
private val BASE_URL="http://10.20.92.222:8082/Posts/"
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
    @GET("getPostsList")
    suspend fun getPostsList():List<Posts>
}