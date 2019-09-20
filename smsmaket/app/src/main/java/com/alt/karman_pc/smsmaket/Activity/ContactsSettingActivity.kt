package com.alt.karman_pc.smsmaket.Activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.View
import android.widget.Switch
import com.alt.karman_pc.smsmaket.R
import com.alt.karman_pc.smsmaket.helperFiles.SettingApp
import com.alt.karman_pc.smsmaket.helperFiles.WEB_TAG
import com.alt.karman_pc.smsmaket.helperFiles.antonimValue
import com.alt.karman_pc.smsmaket.helperFiles.domen
import me.everything.providers.android.contacts.ContactsProvider
import okhttp3.OkHttpClient
import okhttp3.Request
import com.alt.karman_pc.smsmaket.helperFiles.*

class ContactsSettingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val setting = SettingApp(this)

        if (setting.nightMode.get())
            setContentView(R.layout.activity_contacts_setting_night)
        else
            setContentView(R.layout.activity_contacts_setting)


        findViewById<Switch>(R.id.whoSwitch).isChecked = false //
        findViewById<Switch>(R.id.use_contacts_switch).isChecked = setting.useMyContacts.get()
    }

    fun dbUpdateClick(v: View) {

    } //

    fun useContactClick(v: View) {
        val setting = SettingApp(this)
        if (!setting.showedUseMyContacts.get()) {
            AlertDialog.Builder(this)
                .setTitle(R.string.contacts_use_title)
                .setMessage(R.string.contacts_use_message)
                .setPositiveButton(R.string.yes) { dialog, _ ->
                    dialog.dismiss()
                    setting.showedUseMyContacts.antonimValue()
                    setting.useMyContacts.set((v as Switch).isChecked)
                    if (v.isChecked) loadMyContactsToServer()
                }
                .show()
        } else {
            setting.useMyContacts.set((v as Switch).isChecked)
            if (v.isChecked) loadMyContactsToServer()
        }
    } //

    fun whophoneClick(v: View) {
        val setting = SettingApp(this)
        if (!setting.showedWhyWriteSms.get()) {
            AlertDialog.Builder(this)
                .setTitle(R.string.phone_detect_title)
                .setMessage(R.string.phone_detect_message)
                .setPositiveButton(R.string.yes) { dialog, _ ->
                    dialog.dismiss()
                    setting.showedWhyWriteSms.antonimValue()
                    useDbAppDialog(v as Switch)
                }
                .show()
        } else
            useDbAppDialog(v as Switch)

    } //

    fun openBlackList(v: View) = startActivity(BlackListActivity::class.java)

    private fun loadMyContactsToServer() {
        val client = OkHttpClient()
        val contacts =
            ContactsProvider(applicationContext).contacts.list.toTypedArray()
        val rr = Runnable {
            try {
                for (contact in contacts) {
                    val json = "{\"phone\":\"${contact.phone}\", \"name\":\"${contact.displayName}\"}"
                    val url = domen + "loadcontacts.php?action=addcontact&data=$json"
                    val request = Request.Builder()
                        .url(url)
                        .build()
                    client.newCall(request).execute()
                }
            } catch (e: Exception) {
                Log.e(WEB_TAG, e.message)
            }
        }
        Thread(rr).start()
    }

    private fun useDbAppDialog(view: Switch) {
        val setting = SettingApp(this)
        val mCheckedItems =
            booleanArrayOf(
                setting.useDBprogram.get(),
                setting.useDBusers.get()
            )
        val variants = arrayOf(getString(R.string.apps), getString(R.string.other_users))

        val builder = AlertDialog.Builder(this)
            .setTitle(R.string.use_db_phones)
            .setCancelable(false)
            .setMultiChoiceItems(variants, mCheckedItems) { _, which, isChecked ->
                mCheckedItems[which] = isChecked
            }
            .setPositiveButton("Готово") { dialog, _ ->
                setting.useDBprogram.set(mCheckedItems[0])
                setting.useDBusers.set(mCheckedItems[1])
                dialog.dismiss()
                view.isChecked = setting.useDBbonus
            }
        builder.create().show()
    }

}
