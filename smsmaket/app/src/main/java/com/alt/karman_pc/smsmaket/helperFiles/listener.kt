package com.alt.karman_pc.smsmaket.helperFiles

import android.content.Intent
import android.content.BroadcastReceiver
import android.content.Context
import android.telephony.SmsMessage
import android.util.Log


class listener : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != "android.provider.Telephony.SMS_RECEIVED")
            return
        val bundle = intent.extras
        val messages: Array<SmsMessage?>?
        var address: String
        if (bundle != null) {
            try {
                val pdus = bundle.get("pdus") as Array<*>
                messages = arrayOfNulls(pdus.size)
                for (i in messages.indices) {
                    messages[i] = SmsMessage.createFromPdu(pdus[i] as ByteArray)
                    address = messages[i]!!.originatingAddress
                    val body = messages[i]!!.messageBody

                    val intent = Intent(context, MyService::class.java)
                    intent.putExtra("address", address)
                    intent.putExtra("body", body)
                    context.startService(intent)
                    //abortBroadcast()
                }
            } catch (e: Exception) {
                Log.e(WEB_TAG, e.message)
            }
        }
    }

}