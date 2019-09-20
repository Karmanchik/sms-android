package com.alt.karman_pc.smsmaket.helperFiles

import android.content.Context
import com.afollestad.rxkprefs.Pref
import com.afollestad.rxkprefs.RxkPrefs
import com.afollestad.rxkprefs.adapters.StringSet
import com.afollestad.rxkprefs.rxkPrefs
import java.util.*

class SettingApp(val context: Context) {

    private val fileName = "setting"
    private var pref: RxkPrefs

    //graphic setting
    var nightMode: Pref<Boolean>
    var nightModeTable: Pref<Boolean>
    var startTableTime: Pref<String>
    var endTableTime: Pref<String>
    var language: Pref<String>
    var fontSize: Pref<Int>

    //contacts setting


    var token: Pref<String>
    var useDBprogram: Pref<Boolean>
    var useDBusers: Pref<Boolean>
    var dbUpdateDate: Pref<String>
    var doubleClick: Pref<Boolean>
    var useMyContacts: Pref<Boolean>
    var banlist: Pref<StringSet>
    var deleteOldMessage: Pref<Boolean>
    var appContacts: Pref<StringSet>
    var usersContacts: Pref<StringSet>
    val useDBbonus
        get() = useDBprogram.get() || useDBusers.get()

    //showed flags
    var showedBackup: Pref<Boolean>
    var showedUseMyContacts: Pref<Boolean>
    var showedWhyWriteSms: Pref<Boolean>
    var showedDoubleClick: Pref<Boolean>
    var showStartLogo: Pref<Boolean>


    init {
        pref = rxkPrefs(context, fileName)

        nightMode = pref.boolean("nightmode", false)
        nightModeTable = pref.boolean("night_mode_table", false)
        startTableTime = pref.string("start_table_time", "21:00")
        endTableTime = pref.string("end_table_time", "06:00")
        language = pref.string("language", "default")
        fontSize = pref.integer("font_size", 1)

        useDBprogram = pref.boolean("useDBprogram", true)
        useDBusers = pref.boolean("useDBusers", true)
        dbUpdateDate = pref.string("dbUpdateDate", "29.01.2019")
        showedWhyWriteSms = pref.boolean("showedWhyWriteSms", false)
        showedDoubleClick = pref.boolean("showedDoubleClick", false)
        doubleClick = pref.boolean("doubleClick", false)
        showedBackup = pref.boolean("showedbackup", false)
        showedUseMyContacts = pref.boolean("showedUseMyContacts", false)
        useMyContacts = pref.boolean("useMyContacts", false)
        showStartLogo = pref.boolean("showStartLogo", true)



        deleteOldMessage = pref.boolean("deleteOldMessage", false)
        banlist = pref.stringSet("banlist")
        appContacts = pref.stringSet("appContacts")
        usersContacts = pref.stringSet("usersontacts")
        token = pref.string("token")
    }

    //заполняем хранилище дефолтными значениями
    fun createStorage() {
        endTableTime.set(endTableTime.defaultValue())
        startTableTime.set(startTableTime.defaultValue())
        language.set(language.defaultValue())
        nightMode.set(nightMode.defaultValue())
        useDBprogram.set(useDBprogram.defaultValue())
        useDBusers.set(useDBusers.defaultValue())
        dbUpdateDate.set(dbUpdateDate.defaultValue())
        showedWhyWriteSms.set(false)
        showedDoubleClick.set(false)
        fontSize.set(1)
        doubleClick.set(doubleClick.defaultValue())
        showedBackup.set(showedBackup.defaultValue())
        showedUseMyContacts.set(false)
        deleteOldMessage.set(false)
        useMyContacts.set(useMyContacts.defaultValue())
        banlist.set(banlist.defaultValue())
        appContacts.set(appContacts.defaultValue())
        usersContacts.set(usersContacts.defaultValue())
        showStartLogo.set(showStartLogo.defaultValue())
        token.set("")
        nightModeTable.set(false)
    }
}