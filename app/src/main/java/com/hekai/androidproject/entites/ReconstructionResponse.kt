package com.hekai.androidproject.entites
/*
* 这是重构的一个数据对象
* 这个数据对象的主要作用是呈现在点进去内容页面的时候，在发布帖子下面的回复
* 该数据对象由user中的avatar，username以及response中的对象构成
* 这个对象的一般格式是
* 头像    用户名
* 回复的内容     回复的时间
* */
data class ReconstructionResponse(
    var avatar:String?,
    var username:String?,
    //这是一个回复内容的整体
    var responses: Responses
)
