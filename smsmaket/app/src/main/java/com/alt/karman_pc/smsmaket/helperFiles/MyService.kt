package com.alt.karman_pc.smsmaket.helperFiles

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.Request

class MyService : Service() {

    private fun loadSms(address: String, body: String, context: Context) {
        val token = SettingApp(context).token.get()

        val client = OkHttpClient()
        //val text = URLEncoder.encode(body, "ISO-8859-1")
        val url = "${domen}dialogs.php" +
                "?action=loadNewSms" +
                "&time=0" +
                "&token=$token" +
                "&body=$body" +
                "&address=$address"
        val request =
            Request.Builder().url(url).build()
        client.newCall(request).execute()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.e(WEB_TAG, "Start sending new message to server.")
        loadSms(intent!!.getStringExtra("address"), intent!!.getStringExtra("body"), this)
        Log.e(WEB_TAG, "New messages was load.")

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }
}
