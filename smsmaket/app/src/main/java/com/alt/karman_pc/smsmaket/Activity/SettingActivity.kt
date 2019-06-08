package com.alt.karman_pc.smsmaket.Activity

import android.content.DialogInterface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Switch
import android.widget.TextView
import com.alt.karman_pc.smsmaket.R
import com.alt.karman_pc.smsmaket.helperFiles.*
import me.everything.providers.android.contacts.ContactsProvider
import okhttp3.OkHttpClient
import okhttp3.Request
import java.net.URL


class SettingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            val setting = SettingApp(this)
            if (setting.nightMode.get())
                setContentView(R.layout.activity_setting_nightmode)
            else
                setContentView(R.layout.activity_setting)


            //setSupportActionBar(findViewById(R.id.toolbar))

            val nightView = findViewById<Switch>(R.id.nightModeView)
            val useDBallView = findViewById<Switch>(R.id.useNumberDbView)
            val doubleClickView = findViewById<Switch>(R.id.doubleClickView)
            val useMyContactsView = findViewById<Switch>(R.id.useMyContactsView)

            nightView.isChecked = setting.nightMode.get()
            useDBallView.isChecked = setting.useDBbonus
            doubleClickView.isChecked = setting.doubleClick.get()
            useMyContactsView.isChecked = setting.useMyContacts.get()

            findViewById<TextView>(R.id.openAboutView).setOnClickListener {
                startActivity(AboutActivity::class.java)
            }
            findViewById<TextView>(R.id.languageView).setOnClickListener {
                val mChooseCats = arrayOf("Русский", "English", "Українська")
                val builder = AlertDialog.Builder(this)
                    .setTitle(R.string.select_language)
                    .setCancelable(false)
                    .setSingleChoiceItems(mChooseCats, -1) { dialog: DialogInterface, i: Int ->
                        when (i) {
                            0 -> setting.language.set("ru")
                            1 -> setting.language.set("en")
                            2 -> setting.language.set("uk")
                        }
                        dialog.dismiss()
                    }
                builder.create().show()
            }
            findViewById<TextView>(R.id.resetView).setOnClickListener {
                AlertDialog.Builder(this)
                    .setTitle(R.string.setting_reset_title)
                    .setMessage(R.string.setting_reset_message)
                    .setPositiveButton(R.string.yes) { dialog, _ ->
                        dialog.dismiss()
                        setting.createStorage()
                        finish()
                        startActivity(intent)
                    }
                    .setNegativeButton(R.string.no) { dialog, _ ->
                        dialog.cancel()
                    }
                    .show()
            }
            findViewById<TextView>(R.id.openblackListView).setOnClickListener {
                startActivity(BlackListActivity::class.java)
            }
            doubleClickView.setOnClickListener {
                if (!setting.showedDoubleClick.get()) {
                    AlertDialog.Builder(this)
                        .setTitle(R.string.send_confirm_title)
                        .setMessage(R.string.send_confirm_message)
                        .setPositiveButton(R.string.yes) { dialog, _ ->
                            dialog.dismiss()
                            setting.showedDoubleClick.antonimValue()
                            setting.doubleClick.set(doubleClickView.isChecked)
                        }
                        .show()
                } else
                    setting.doubleClick.set(doubleClickView.isChecked)
            }
            useMyContactsView.setOnClickListener {
                if (!setting.showedUseMyContacts.get()) {
                    AlertDialog.Builder(this)
                        .setTitle(R.string.contacts_use_title)
                        .setMessage(R.string.contacts_use_message)
                        .setPositiveButton(R.string.yes) { dialog, _ ->
                            dialog.dismiss()
                            setting.showedUseMyContacts.antonimValue()
                            setting.useMyContacts.set(useMyContactsView.isChecked)
                            if (useMyContactsView.isChecked) loadMyContactsToServer()
                        }
                        .show()
                } else {
                    setting.useMyContacts.set(useMyContactsView.isChecked)
                    if (useMyContactsView.isChecked) loadMyContactsToServer()
                }
            }
            useDBallView.setOnClickListener {
                if (!setting.showedWhyWriteSms.get()) {
                    AlertDialog.Builder(this)
                        .setTitle(R.string.phone_detect_title)
                        .setMessage(R.string.phone_detect_message)
                        .setPositiveButton(R.string.yes) { dialog, _ ->
                            dialog.dismiss()
                            setting.showedWhyWriteSms.antonimValue()
                            useDbAppDialog(useDBallView)
                        }
                        .show()
                } else
                    useDbAppDialog(useDBallView)
            }
            nightView.setOnClickListener {
                setting.nightMode.set(nightView.isChecked)
                recreate()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error SettingActivity onCreate: ${e.message}")
        }
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.maintoolbar, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        try {
            when (item!!.itemId) {
                R.id.itemNightMode -> MainToolbar().nightModeClick(this)
                R.id.itemUpdate -> MainToolbar().updateDBClick(this)
                R.id.itemBlackList -> MainToolbar().blackListClick(this)
                R.id.itemAbout -> MainToolbar().aboutClick(this)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error SettingActivity onOptionsItemSelected: ${e.message}")
        }
        return super.onOptionsItemSelected(item)
    }

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

}