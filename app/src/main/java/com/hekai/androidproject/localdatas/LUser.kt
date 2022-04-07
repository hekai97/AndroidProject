package com.hekai.androidproject.localdatas

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "login_status")
data class LUser(
    @PrimaryKey()var uid:Int,
    var UserName:String,
    var NickName:String,
    var UserAvatar:String,
    var Password:String,
    var PublishNumber:Int,
    var PhoneNumber:String,
    var Email:String,
    var status:Boolean     //登录状态，为true代表该用户已经登录，否则为false
)
