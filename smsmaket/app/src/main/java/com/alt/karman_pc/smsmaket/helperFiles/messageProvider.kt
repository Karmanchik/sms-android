package com.alt.karman_pc.smsmaket.helperFiles

import me.everything.providers.android.telephony.TelephonyProvider
import android.content.Context
import android.util.Log
import io.realm.Realm
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass


fun getMessages(context: Context, realm: Realm): Array<RealmMessage> {

    try {
        realm.createObject(RealmMessage::class.java)
    } catch (e: Exception) { }

    var result: Array<RealmMessage> = realm.where(RealmMessage::class.java).findAll().toTypedArray()
    if (result.isEmpty()) {
        val messages =
            TelephonyProvider(context).getSms(TelephonyProvider.Filter.ALL).list.toTypedArray()
        Log.e("karman", "kek")
        messages.forEach {
            result += RealmMessage(it.address, it.body, it.id, it.errorCode,
                it.locked, it.person, it.protocol, it.read,
                it.receivedDate, it.seen, it.sentDate, it.status.name,
                it.subject, it.type.name
            )
        }
        realm.executeTransaction {
            it.copyToRealmOrUpdate<RealmMessage>(result.toMutableList())
        }
    }
    return result
}

@RealmClass
open class RealmMessage(
    var address: String = "",
    var body: String = "",
    @PrimaryKey var id: Long = -1,
    var errorCode: Int = -1,
    var locked: Boolean = false,
    var person: Int = -1,
    var protocol: Int = -1,
    var read: Boolean = false,
    var receivedDate: Long = -1,
    var seen: Boolean = false,
    var sentDate: Long = -1,
    var status: String = "",
    var subject: String? = "",
    var type: String = ""
) : RealmObject()