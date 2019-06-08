package com.alt.karman_pc.smsmaket.Activity

import android.content.DialogInterface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.widget.ListView
import com.alt.karman_pc.smsmaket.helperFiles.DialogsManager
import com.alt.karman_pc.smsmaket.helperFiles.MainToolbar
import com.alt.karman_pc.smsmaket.R
import com.alt.karman_pc.smsmaket.adapters.DialogsAdapter
import com.alt.karman_pc.smsmaket.helperFiles.*
import me.everything.providers.android.contacts.ContactsProvider
import me.everything.providers.android.telephony.TelephonyProvider
import android.content.Intent
import android.support.v7.app.AlertDialog
import android.telephony.SmsManager
import android.util.Log
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import java.net.URLEncoder


class MainActivity : AppCompatActivity() {

    lateinit var toolbar: Toolbar
    lateinit var dialogManager: DialogsManager
    var nightMode: Boolean? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        try {
            toolbar = findViewById(R.id.toolbar)
            toolbar.setOnTouchListener(OnSwipeToolbar(this, findViewById(R.id.dialogs_list)))
            setSupportActionBar(toolbar)

            findViewById<FloatingActionButton>(R.id.fab).setOnClickListener {
                startActivity(CreateDialogActivity::class.java)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error onCreate: ${e.message}")
        }
    }

    private fun updateNightModeStatus() {

        val nightModeNew = SettingApp(this).nightMode.get()
        if (nightMode != null && nightMode!! == nightModeNew) return
        nightMode = nightModeNew

        if (nightMode!!)
            toolbar.setBackgroundResource(R.color.night_toolbar)
        else
            toolbar.setBackgroundResource(R.color.colorPrimary)

        val dialogLayout =
            if (nightMode!!) R.layout.dialog_item_night
            else R.layout.dialog_item_day

        val listView = findViewById<ListView>(R.id.dialogs_list)
        listView.adapter =
            DialogsAdapter(this, dialogLayout, dialogManager.dialogs, nightMode!!)
    }

    override fun onStart() {
        super.onStart()
        try {
            val messages =
                TelephonyProvider(applicationContext).getSms(TelephonyProvider.Filter.ALL).list.toTypedArray()
            val contacts =
                ContactsProvider(applicationContext).contacts.list.toTypedArray()

            dialogManager = DialogsManager(messages, this)
            dialogManager.setContacts(contacts)
            updateNightModeStatus()
        } catch (e: Exception) {
            Log.e(TAG, "Error MainActivity onStart: ${e.message}")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.maintoolbar, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        try {
            val token = data.getStringExtra("101")
            Thread(
                Runnable {
                    try {
                        val client = OkHttpClient()
                        val url = "http://vtk.kl.com.ua/smsserver/dialogs.php?action=connectByToken&token=$token"
                        Log.e(WEB_TAG, url)
                        val request = Request.Builder().url(url).build()
                        client.newCall(request).execute()
                        SettingApp(this).token.set(token)
                        createListenerThread(token)

                        for (dialog in dialogManager.dialogs) {
                            for (sms in dialog.messages) {
                                val urlLoad = "${domen}dialogs.php?action=loadDialogs" +
                                        "&date=${sms.getDate()}" +
                                        "&token=$token" +
                                        "&body=${sms.body}" +
                                        "&address=${sms.address}" +
                                        "&name=${dialog.name}" +
                                        "&type=${sms.type.name}"
                                val requestLoad = Request.Builder().url(urlLoad).build()
                                client.newCall(requestLoad).execute()
                                Thread.sleep(10)
                            }
                        }
                    } catch (e: Exception) {
                        Log.e(WEB_TAG, e.message)
                    }
                }
            ).start()
        } catch (e: Exception) {
            Log.e(TAG, e.message)
        }
    }

    private fun createListenerThread(token: String) {
        val code = Runnable {
            Log.e(WEB_TAG, "Start listener thread.")
            while (true) {
                try {
                    Log.e(WEB_TAG, "Loading new drafts.")
                    val url = "${domen}dialogs.php?action=getDraftToSend&token=$token"
                    val client = OkHttpClient()
                    val request = Request.Builder().url(url).build()
                    val response = client.newCall(request).execute()
                    var json = deleteEcranChar(response.body()?.string()!!)

                    val start = json.indexOf("[")
                    val end = json.indexOf("]") + 1
                    json = json.substring(start, end)

                    val smsDrafts = Gson().fromJson<Array<DraftSms>>(json, Array<DraftSms>::class.java)
                    Log.e(WEB_TAG, "Download ${smsDrafts.size} draft messages.")
                    for (sms in smsDrafts) {
                        Log.e(WEB_TAG, "Send draft message.")
                        SmsManager.getDefault()
                            .sendTextMessage(sms.address, null, sms.body, null, null)
                    }
                } catch (e: Exception) {
                    Log.e(WEB_TAG, e.message)
                }
                Thread.sleep(10000)
            }
        }
        val thread = Thread(code)
        thread.isDaemon = false
        thread.start()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        try {
            when (item!!.itemId) {
                R.id.itemNightMode -> {
                    MainToolbar().nightModeClick(this)
                    updateNightModeStatus()
                }
                R.id.itemSetting -> MainToolbar().settingClick(this)
                R.id.itemUpdate -> MainToolbar().updateDBClick(this)
                R.id.itemWeb -> {
                    AlertDialog.Builder(this)
                        .setMessage("vtk.kl.com.ua/smsserver")
                        .setPositiveButton("OK") { dialog: DialogInterface, i: Int ->
                            startActivityForResult(Intent(this, ScanActivity::class.java), 101)
                        }
                        .show()
                }
                R.id.itemBlackList -> MainToolbar().blackListClick(this)
                R.id.itemAbout -> MainToolbar().aboutClick(this)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error MainActivity onOptionsItemSelected: ${e.message}")
        }
        return super.onOptionsItemSelected(item)
    }

}
