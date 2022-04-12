package com.hekai.androidproject.util

import android.content.Context
import com.google.android.material.dialog.MaterialAlertDialogBuilder

fun showErrorAlert(context: Context){
    val dialogBuilder= MaterialAlertDialogBuilder(context)
    dialogBuilder.setTitle("Oops")
    dialogBuilder.setMessage("这个功能还没有实现\uD83D\uDE48")
    dialogBuilder.setPositiveButton("确定"){_,_->}
    dialogBuilder.show()
    //测试test分支
}