package com.hekai.androidproject.util

import java.security.MessageDigest

fun myhash(s:String): String {
    val bytes = s.toByteArray()
    val md = MessageDigest.getInstance("SHA-256")
    val digest = md.digest(bytes)
    return digest.fold("") { str, it -> str + "%02x".format(it) }
}