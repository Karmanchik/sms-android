package com.alt.karman_pc.smsmaket.helperFiles

import android.content.Context
import com.google.gson.Gson
import me.everything.providers.android.contacts.Contact
import me.everything.providers.android.telephony.Sms

class DialogsManager(messages: Array<RealmMessage>, val context: Context) {

    var dialogs = emptyArray<Dialog>()

    init {
        val settingApp = SettingApp(context)
        val listBanedPhones = settingApp.banlist.get()
        for (message in messages)
            if (!listBanedPhones.contains(getShortFormat(message.address)))
                addMessage(message)
    }

    private fun addMessage(sms: RealmMessage) {
        var index = findDialog(sms.address)
        if (index == -1) {
            val dialog = Dialog(sms.address, sms.address)
            dialogs += dialog
            index = dialogs.size - 1
        }
        dialogs[index].addMessage(sms)
    }

    private fun findDialog(address: String): Int {
        for (index in dialogs.indices)
            if (getShortFormat(dialogs[index].address) == getShortFormat(address))
                return index
        return -1
    }

    fun setContacts(contacts: Array<RealmContact>) {
        val setting = SettingApp(context)
        if (setting.useDBusers.get())
            setUsersContacts()
        if (setting.useDBprogram.get())
            setAppContacts()

        for (dialog in dialogs)
            for (contact in contacts) {
                if (getShortFormat(dialog.address) == getShortFormat(contact.phone)) {
                    dialog.name = contact.displayName
                    dialog.avatar = contact.uriPhoto!!
                }
            }
    }

    private fun setAppContacts() {
        val contacts = SettingApp(context).appContacts.get()
        for (dialog in dialogs)
            for (contact in contacts) {
                val cont = Gson().fromJson<SmallContact>(contact, SmallContact::class.java)
                if (getShortFormat(dialog.address) == getShortFormat(cont.phone)) {
                    dialog.name = cont.name
                    dialog.avatar = "none"
                }
            }
    }

    private fun setUsersContacts() {
        val contacts = SettingApp(context).usersContacts.get()
        for (dialog in dialogs)
            for (contact in contacts) {
                val cont = Gson().fromJson<SmallContact>(contact, SmallContact::class.java)
                if (getShortFormat(dialog.address) == getShortFormat(cont.phone)) {
                    dialog.name = cont.name
                    dialog.avatar = "none"
                }
            }
    }
    
}