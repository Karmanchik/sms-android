package com.alt.karman_pc.smsmaket.Activity

import android.content.DialogInterface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.view.View
import android.widget.Switch
import com.alt.karman_pc.smsmaket.R
import com.alt.karman_pc.smsmaket.helperFiles.SettingApp
import com.alt.karman_pc.smsmaket.helperFiles.antonimValue
import com.alt.karman_pc.smsmaket.helperFiles.setLocale
import android.app.TimePickerDialog
import android.widget.TextView

class GraphicSettingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val setting = SettingApp(this)
        if (setting.nightMode.get())
            setContentView(R.layout.activity_graphic_setting_night)
        else
            setContentView(R.layout.activity_graphic_setting)

        findViewById<Switch>(R.id.nightSwitch).isChecked = setting.nightMode.get()
        findViewById<Switch>(R.id.logoSwitch).isChecked = setting.showStartLogo.get()
        findViewById<Switch>(R.id.night_table_switch).isChecked = setting.nightModeTable.get()

        findViewById<TextView>(R.id.start_table).text = "С " + getDateFormat(setting.startTableTime.get())
        findViewById<TextView>(R.id.end_table).text = "До " + getDateFormat(setting.endTableTime.get())
    }

    fun languageClick(v: View) {
        val setting = SettingApp(this)
        val languages = arrayOf("Русский", "English", "Українська")
        val builder = AlertDialog.Builder(this)
            .setTitle(R.string.select_language)
            .setSingleChoiceItems(languages, -1) { dialog: DialogInterface, i: Int ->
                when (i) {
                    0 -> setting.language.set("ru")
                    1 -> setting.language.set("en")
                    2 -> setting.language.set("uk")
                }
                setLocale(this, setting.language.get())
                recreate()
                dialog.dismiss()
            }
        builder.create().show()
    }

    fun nightModeClick(v: View) {
        SettingApp(this).nightMode.antonimValue()
        recreate()
    }

    fun hideLogoClick(v: View) = SettingApp(this).showStartLogo.antonimValue()

    fun fontClick(v: View) {
        val setting = SettingApp(this)
        val sizes =
            arrayOf(getString(R.string.small), getString(R.string.medium), getString(R.string.big))
        val builder = AlertDialog.Builder(this)
            .setTitle(R.string.font_size_title)
            .setSingleChoiceItems(sizes, -1) { dialog: DialogInterface, size: Int ->
                setting.fontSize.set(size)
                recreate()
                dialog.dismiss()
            }
        builder.create().show()
    }

    fun nightTableClick(v: View) = SettingApp(this).nightModeTable.antonimValue()

    fun startTableClick(v: View) {
        val timePickerDialog = TimePickerDialog(this,
            TimePickerDialog.OnTimeSetListener { _, hour, minute ->
                SettingApp(this).startTableTime.set("$hour:$minute")
                (v as TextView).text = "С " + getDateFormat("$hour:$minute")
            }, 21, 0, true)
        timePickerDialog.show()
    }

    fun endTableClick(v: View) {
        val timePickerDialog = TimePickerDialog(this,
            TimePickerDialog.OnTimeSetListener { _, hour, minute ->
                SettingApp(this).endTableTime.set("$hour:$minute")
                (v as TextView).text = "До " + getDateFormat("$hour:$minute")
            }, 21, 0, true)
        timePickerDialog.show()
    }

    private fun getDateFormat(date: String): String {
        var hours = date.split(":")[0]
        var minutes = date.split(":")[1]

        if (hours.length == 1) hours = "0$hours"
        if (minutes.length == 1) minutes = "0$minutes"

        return "$hours:$minutes"
    }
}
