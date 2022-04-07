package com.hekai.androidproject.entites

import com.squareup.moshi.Json
import java.io.Serializable

data class Contents(
    @Json(name = "cid")var cid:Int,
    @Json(name = "content")var Content:String
):Serializable
