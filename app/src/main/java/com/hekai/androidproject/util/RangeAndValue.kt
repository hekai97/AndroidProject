package com.hekai.androidproject.util

data class RangeAndValue(
    var type:Int=1,       //类型，图片用1表示，视频用2表示
    var startIndex:Int,
    var endIndex:Int,
    var url:String
)
