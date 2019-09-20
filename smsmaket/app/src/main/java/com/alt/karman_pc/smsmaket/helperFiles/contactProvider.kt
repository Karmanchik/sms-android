package com.alt.karman_pc.smsmaket.helperFiles

import android.content.Context
import android.util.Log
import io.realm.Realm
import me.everything.providers.android.contacts.ContactsProvider
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass


fun getContacts(context: Context, realm: Realm): Array<RealmContact> {

    try {
        realm.createObject(RealmContact::class.java)
    } catch (e: Exception) { }

    var result: Array<RealmContact> = realm.where(RealmContact::class.java).findAll().toTypedArray()
    if (result.isEmpty()) {
        val contacts = ContactsProvider(context).contacts.list.toTypedArray()
        contacts.forEach {
            result += RealmContact(it.displayName, it.phone?:"", it.id, it.uriPhoto?:"")
        }
        realm.executeTransaction {
            it.copyToRealmOrUpdate<RealmContact>(result.toMutableList())
        }
    }
    result.forEach {
        Log.e("karman", it.displayName+":::"+it.phone+":::"+it.uriPhoto)
    }
    return result
}

@RealmClass
open class RealmContact(
    var displayName: String = "",
    var phone: String = "",
    @PrimaryKey var id: Long = -1,
    var uriPhoto: String? = ""
) : RealmObject()