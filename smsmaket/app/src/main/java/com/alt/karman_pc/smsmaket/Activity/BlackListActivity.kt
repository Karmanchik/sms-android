package com.alt.karman_pc.smsmaket.Activity

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import com.alt.karman_pc.smsmaket.R
import com.alt.karman_pc.smsmaket.adapters.BanPhonesAdapter
import com.alt.karman_pc.smsmaket.helperFiles.*

class BlackListActivity : AppCompatActivity() {

    lateinit var blackListView: ListView
    lateinit var toBlackListButton: Button
    lateinit var phoneEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_black_list)

        try {
            blackListView = findViewById(R.id.black_list_view)
            toBlackListButton = findViewById(R.id.viewToBanButton)
            phoneEditText = findViewById(R.id.viewToBanPhone)

            val setting = SettingApp(this)

            val nightMode = setting.nightMode.get()
            if (nightMode)
                applyNightTheme()

            var banList = setting.banlist.get().toTypedArray()
            blackListView.adapter =
                BanPhonesAdapter(this, R.layout.ban_item, banList, nightMode, blackListView)

            toBlackListButton.setOnClickListener {

                val newBanPhone = phoneEditText.text.toString()
                phoneEditText.text.clear()

                banList += getShortFormat(newBanPhone)
                setting.banlist.set(banList.toMutableSet())

                Toast.makeText(this, getString(R.string.phone_is_baned), Toast.LENGTH_SHORT).show()
                blackListView.adapter =
                    BanPhonesAdapter(this, R.layout.ban_item, banList, nightMode, blackListView)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error BlackListActivity onCreate: ${e.message}")
        }
    }

    private fun applyNightTheme() {
        blackListView.setBackgroundResource(R.color.nightTheme)
        phoneEditText.setTextColor(Color.WHITE)
        findViewById<LinearLayout>(R.id.banView).setBackgroundResource(R.color.nightTheme)//footer
        toBlackListButton.setTextColor(Color.WHITE)
        toBlackListButton.setBackgroundResource(R.color.button_back)
    }
}
