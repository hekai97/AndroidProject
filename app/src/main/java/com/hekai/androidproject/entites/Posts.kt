package com.hekai.androidproject.entites
import com.squareup.moshi.Json
data class Posts (
    @Json(name = "pid")var pid: Int?,
    @Json(name = "publishUserId")var PublishUserId: Int?,
    @Json(name = "replyQuestionId")var ReplyQuestionId: Int?,
    @Json(name = "publishUserNickName") var PublishUserNickName:String,
    @Json(name = "publishUserAvatar") var PublishUserAvatar:String="",
    @Json(name = "publishTitle")var PublishTitle: String,
    @Json(name = "publishDescription")val PublishDescription: String,
    @Json(name = "publishTime")var PublishTime: String,
    @Json(name = "publishState")var PublishState: Byte?,
    @Json(name = "publishUpdateTime")var PublishUpdateTime: String,
    @Json(name = "publishReadCount")var PublishReadCount: Int?,
    @Json(name = "publishLikeNumber")var PublishLikeNumber: Int?
)