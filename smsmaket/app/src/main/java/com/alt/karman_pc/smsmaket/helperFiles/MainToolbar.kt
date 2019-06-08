package com.alt.karman_pc.smsmaket.helperFiles

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.alt.karman_pc.smsmaket.Activity.*
import com.alt.karman_pc.smsmaket.Activity.ScanActivity
import com.alt.karman_pc.smsmaket.R
import com.google.gson.Gson
import okhttp3.*

class MainToolbar {

    fun nightModeClick(context: Context) {
        val setting = SettingApp(context)
        setting.nightMode.antonimValue()
    }

    fun settingClick(context: Context) = context.startActivity(SettingActivity::class.java)

    fun updateDBClick(context: Context) {
        val client = OkHttpClient()
        val setting = SettingApp(context)

        Thread(
            Runnable {
                try {
                    Log.e(WEB_TAG, "Start update contacts.")
                    //url для получения контактов приложения и других пользователей
                    val appUrl   = "${domen}update.php?action=loadAppContacts"
                    val usersUrl = "${domen}update.php?action=loadUserContacts"

                    val appRequest  = Request.Builder().url(appUrl).build()
                    val userRequest = Request.Builder().url(usersUrl).build()

                    val respApp   = client.newCall(appRequest).execute()
                    val respUsers = client.newCall(userRequest).execute()

                    var appJson   = deleteEcranChar(respApp.body()?.string()!!)
                    var usersJson = deleteEcranChar(respUsers.body()?.string()!!)

                    val startApp = appJson.indexOf("[")
                    val endApp = appJson.indexOf("]") + 1
                    appJson = appJson.substring(startApp, endApp)

                    val startUsers = usersJson.indexOf("[")
                    val endUsers = usersJson.indexOf("]") + 1
                    usersJson = usersJson.substring(startUsers, endUsers)

                    Log.e("load", "Json app contacts: $appJson")
                    Log.e("load", "Json other users contacts: $usersJson")

                    val appList   = getContactsFromJson(appJson)
                    val usersList = getContactsFromJson(usersJson)

                    val appSet = hashSetOf<String>()
                    for (elem in appList)
                        appSet.add(Gson().toJson(elem))

                    val usersSet = hashSetOf<String>()
                    for (elem in usersList)
                        usersSet.add(Gson().toJson(elem))

                    setting.appContacts.set(appSet)
                    setting.usersContacts.set(usersSet)

                    Log.e(WEB_TAG, "End contacts update.")
                } catch (e: Exception) {
                    Log.e(WEB_TAG, e.message)
                }
            }
        ).start()
        Toast.makeText(context, context.getString(R.string.update_end), Toast.LENGTH_SHORT).show()
    }

    private fun getContactsFromJson(json: String): Array<SmallContact> {
        return Gson().fromJson<Array<SmallContact>>(
                json, Array<SmallContact>::class.java
        )
    }

    fun webVersionClick(context: Context) {
        context.startActivity(ScanActivity::class.java)
    }

    fun formatString(data: String): String {
        var str = data.split(" ")[0]
        str = str.replace("\\\\", "\\")
        str = str.replace("\\", "")
        val arr = str.split("u".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        var text = ""
        for (i in 1 until arr.size) {
            val hexVal = Integer.parseInt(arr[i], 16)
            text += hexVal.toChar()
        }
        return text
    }

    fun blackListClick(context: Context) {
        if (context.javaClass != BlackListActivity::javaClass)
            context.startActivity(BlackListActivity::class.java)
    }

    fun aboutClick(context: Context) {
        if (context.javaClass != AboutActivity::javaClass)
            context.startActivity(AboutActivity::class.java)
    }

}