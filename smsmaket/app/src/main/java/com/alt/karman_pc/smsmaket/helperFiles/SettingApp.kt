package com.alt.karman_pc.smsmaket.helperFiles

import android.content.Context
import com.afollestad.rxkprefs.Pref
import com.afollestad.rxkprefs.RxkPrefs
import com.afollestad.rxkprefs.adapters.StringSet
import com.afollestad.rxkprefs.rxkPrefs

class SettingApp(val context: Context) {

    private val fileName = "setting"
    private var pref: RxkPrefs

    var token: Pref<String>
    var language: Pref<String>
    var nightMode: Pref<Boolean>
    var useDBprogram: Pref<Boolean>
    var useDBusers: Pref<Boolean>
    var dbUpdateDate: Pref<String>
    var doubleClick: Pref<Boolean>
    var useMyContacts: Pref<Boolean>
    var banlist: Pref<StringSet>
    var appContacts: Pref<StringSet>
    var usersContacts: Pref<StringSet>
    val useDBbonus
        get() = useDBprogram.get() || useDBusers.get()

    //showed flags
    var showedBackup: Pref<Boolean>
    var showedUseMyContacts: Pref<Boolean>
    var showedWhyWriteSms: Pref<Boolean>
    var showedDoubleClick: Pref<Boolean>


    init {
        pref = rxkPrefs(context, fileName)
        language = pref.string("language", "default")
        nightMode = pref.boolean("nightmode", false)
        useDBprogram = pref.boolean("useDBprogram", true)
        useDBusers = pref.boolean("useDBusers", true)
        dbUpdateDate = pref.string("dbUpdateDate", "29.01.2019")
        showedWhyWriteSms = pref.boolean("showedWhyWriteSms", false)
        showedDoubleClick = pref.boolean("showedDoubleClick", false)
        doubleClick = pref.boolean("doubleClick", false)
        showedBackup = pref.boolean("showedbackup", false)
        showedUseMyContacts = pref.boolean("showedUseMyContacts", false)
        useMyContacts = pref.boolean("useMyContacts", false)

        banlist = pref.stringSet("banlist")
        appContacts = pref.stringSet("appContacts")
        usersContacts = pref.stringSet("usersontacts")
        token = pref.string("token")
    }

    //заполняем хранилище дефолтными значениями
    fun createStorage() {
        language.set(language.defaultValue())
        nightMode.set(nightMode.defaultValue())
        useDBprogram.set(useDBprogram.defaultValue())
        useDBusers.set(useDBusers.defaultValue())
        dbUpdateDate.set(dbUpdateDate.defaultValue())
        showedWhyWriteSms.set(false)
        showedDoubleClick.set(false)
        doubleClick.set(doubleClick.defaultValue())
        showedBackup.set(showedBackup.defaultValue())
        showedUseMyContacts.set(false)
        useMyContacts.set(useMyContacts.defaultValue())
        banlist.set(banlist.defaultValue())
        appContacts.set(appContacts.defaultValue())
        usersContacts.set(usersContacts.defaultValue())
        token.set("")
    }
}