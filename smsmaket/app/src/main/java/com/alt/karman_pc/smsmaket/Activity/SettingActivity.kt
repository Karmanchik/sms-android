package com.alt.karman_pc.smsmaket.Activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Switch
import android.widget.Toast
import com.alt.karman_pc.smsmaket.R
import com.alt.karman_pc.smsmaket.helperFiles.*


class SettingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val setting = SettingApp(this)

        if (setting.nightMode.get())
            setContentView(R.layout.activity_setting_menu_night)
        else
            setContentView(R.layout.activity_setting_menu)

        findViewById<Switch>(R.id.ddsetting).isChecked = setting.doubleClick.get()
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

    fun openGraphicSetting(v: View) = startActivity(GraphicSettingActivity::class.java)

    fun openContactsSetting(v: View) = startActivity(ContactsSettingActivity::class.java)

    fun smsLimiterClick(v: View) {
        AlertDialog.Builder(this)
            .setTitle("Старые сообщения")
            .setMessage("Удалять старые сообщения? (старее двух месяцев)")
            .setPositiveButton(R.string.yes) { dialog, _ ->
                dialog.dismiss()
                SettingApp(this).deleteOldMessage.set(true)
            }
            .setNegativeButton(R.string.no) { dialog, _ ->
                dialog.dismiss()
                SettingApp(this).deleteOldMessage.set(false)
            }
    }

    fun resetClick(v: View) {
        AlertDialog.Builder(this)
            .setTitle(R.string.setting_reset_title)
            .setMessage(R.string.setting_reset_message)
            .setPositiveButton(R.string.yes) { dialog, _ ->
                dialog.dismiss()
                SettingApp(this).createStorage()
                finish()
                startActivity(intent)
            }
            .setNegativeButton(R.string.no) { dialog, _ ->
                dialog.cancel()
            }
            .show()
    }

    fun showHistory(v: View) = Toast.makeText(this, "function is blocked", Toast.LENGTH_SHORT).show() //

    fun openAboutActivity(v: View) = startActivity(AboutActivity::class.java)

    fun showDoubleClickDialog(v: View) {
        val setting = SettingApp(this)
        if (!setting.showedDoubleClick.get()) {
            AlertDialog.Builder(this)
                .setTitle(R.string.send_confirm_title)
                .setMessage(R.string.send_confirm_message)
                .setPositiveButton(R.string.yes) { dialog, _ ->
                    dialog.dismiss()
                    setting.showedDoubleClick.antonimValue()
                    setting.doubleClick.set((v as Switch).isChecked)
                }
                .show()
        } else
            setting.doubleClick.set((v as Switch).isChecked)
    }

    fun backup(v: View) = Toast.makeText(this, "function is blocked", Toast.LENGTH_SHORT).show() //
}