package com.hekai.androidproject.entites

import com.squareup.moshi.Json
import java.io.Serializable

data class Responses(
    @Json(name = "rid")var rid:Int?,
    @Json(name = "responseUser")var responseUser:Int,
    @Json(name = "responsePost")var responsePost:Int,
    @Json(name = "responseContent")var responseContent:String,
    @Json(name = "responseTime")var responseTime:String
):Serializable
