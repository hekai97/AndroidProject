package com.hekai.androidproject.entites

import com.squareup.moshi.Json
import java.io.Serializable

data class Users(
    @Json(name = "uid")var uid:Int?,
    @Json(name = "userName")var UserName:String?,
    @Json(name = "nickName")var NickName:String?,
    @Json(name = "userAvatar")var UserAvatar:String?,
    @Json(name = "password")var Password:String?,
    @Json(name = "publishNumber")var PublishNumber:Int?,
    @Json(name = "phoneNumber")var PhoneNumber:String?,
    @Json(name = "email")var Email:String?
    ): Serializable