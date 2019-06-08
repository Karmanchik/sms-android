package com.alt.karman_pc.smsmaket.helperFiles

import android.content.Context
import android.content.Intent
import android.util.Log
import com.afollestad.rxkprefs.Pref
import com.google.gson.Gson
import me.everything.providers.android.telephony.Sms
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

val WEB_TAG = "load"
val TAG = "karman"
val domen = "http://vtk.kl.com.ua/smsserver/"

// to 0951112233
fun getShortFormat(phone: String): String {
    val tmp = phone.replace(" ", "")
    return if (tmp.length < 10) tmp
    else tmp.substring(tmp.length - 10)
}

fun Sms.getDate(): Long {
    return if (type.name == "INBOX")
        receivedDate
    else
        sentDate
}

fun Context.startActivity(activity_name: Class<*>) {
    startActivity(Intent(this, activity_name))
}

fun Pref<Boolean>.antonimValue() {
    set(!get())
}

//класс контакта из базы данных с сервера
data class SmallContact(val phone: String, var name: String)
//класс сообщения-черновика
data class DraftSms(val address: String, val body: String)

//удаляет лишние символы экранирования
fun deleteEcranChar(data: String): String {
    var result = data.replace("\\\"", "\"")
    result = result.replace("\"{", "{")
    result = result.replace("}\"", "}")
    result = result.replace("\\\\", "\\")
    return result
}