package com.alt.karman_pc.smsmaket.helperFiles

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.Log
import com.afollestad.rxkprefs.Pref
import com.google.gson.Gson
import me.everything.providers.android.telephony.Sms
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

val WEB_TAG = "load"
val TAG = "karman"
val domen = "http://vtk.kl.com.ua/smsserver/"

// to 0951112233
fun getShortFormat(phone: String): String {
    val tmp = phone.replace(" ", "")
    return if (tmp.length < 10) tmp
    else tmp.substring(tmp.length - 10)
}

fun RealmMessage.getDate(): Long {
    return if (type == "INBOX")
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

fun deleteEcranChar(data: String): String {
    var result = data.replace("\\\"", "\"")
    result = result.replace("\"{", "{")
    result = result.replace("}\"", "}")
    result = result.replace("\\\\", "\\")
    return result
}

fun getCharInImg(char: Char): Bitmap {
    val bitmap = Bitmap.createBitmap(50, 50, Bitmap.Config.RGB_565)
    bitmap.eraseColor(Color.WHITE)
    val canvas = Canvas(bitmap)
    canvas.drawText("$char", 5, 5, 40f, 40f, Paint())
    return bitmap
}

fun setLocale(context: Context, language: String) {
    if (language != "default") {
        val res = context.resources
        val dm = res.displayMetrics
        val conf = res.configuration
        conf.locale = Locale(language)
        res.updateConfiguration(conf, dm)
    }
}