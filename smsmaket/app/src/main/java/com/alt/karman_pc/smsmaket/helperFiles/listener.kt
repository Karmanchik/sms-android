package com.alt.karman_pc.smsmaket.helperFiles

import android.content.Intent
import android.content.BroadcastReceiver
import android.content.Context
import android.telephony.SmsMessage
import android.util.Log


class Listener : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val messages: Array<SmsMessage?>?
        var address: String
        if (intent.extras != null) {
            try {
                val pdus = intent.extras.get("pdus") as Array<*>
                messages = arrayOfNulls(pdus.size)
                for (i in messages.indices) {
                    messages[i] = SmsMessage.createFromPdu(pdus[i] as ByteArray)
                    address = messages[i]!!.originatingAddress
                    val body = messages[i]!!.messageBody


                    val intent = Intent(context, MyService::class.java)
                    intent.putExtra("address", address)
                    intent.putExtra("body", body)
                    context.startService(intent)
                }
            } catch (e: Exception) {
                Log.e(WEB_TAG, e.message)
            }
        }
    }

}