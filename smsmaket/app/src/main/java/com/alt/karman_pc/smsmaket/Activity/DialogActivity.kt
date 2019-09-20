package com.alt.karman_pc.smsmaket.Activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.telephony.SmsManager
import com.alt.karman_pc.smsmaket.R
import com.alt.karman_pc.smsmaket.adapters.MessagesAdapter
import me.everything.providers.android.telephony.Sms
import me.everything.providers.android.telephony.TelephonyProvider
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.view.View
import android.widget.*
import com.squareup.picasso.Picasso
import android.support.v7.app.AlertDialog
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.view.LayoutInflater
import com.alt.karman_pc.smsmaket.adapters.SearchAdapter
import com.alt.karman_pc.smsmaket.helperFiles.*
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import io.realm.Realm
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

class DialogActivity : AppCompatActivity() {

    lateinit var context: Context
    var nightMode: Boolean? = null
    lateinit var dialog: Dialog
    var showSearch = false

    //view
    lateinit var sendSmsView: ImageView
    lateinit var dialogListView: ListView
    lateinit var smsBodyView: EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dialog)

        try {
            sendSmsView = findViewById(R.id.send_image)
            dialogListView = findViewById(R.id.listview_messages)
            smsBodyView = findViewById(R.id.text_for_send_edittext)

            val setting = SettingApp(this)

            nightMode = setting.nightMode.get()
            context = this

            //получение переданных данных
            val avatar = intent.getStringExtra("avatar")
            val address = intent.getStringExtra("address")
            val name = intent.getStringExtra("name")
            dialog = Dialog(address, name, avatar)


            if (nightMode!!)
                applyNightTheme()
            initToolbar()

            sendSmsView.setOnClickListener {
                if (!setting.doubleClick.get())
                    sendSmsAndUpdateAdapter()
                else {
                    if (smsBodyView.text.toString().length > 50) {
                        val dialog = AlertDialog.Builder(this)
                        dialog.setTitle(R.string.long_sms_title)
                        dialog.setMessage(R.string.long_sms_warning)
                        dialog.setPositiveButton(R.string.yes) { dialogInterface: DialogInterface, _: Int ->
                            sendSmsAndUpdateAdapter()
                            dialogInterface.dismiss()
                        }
                        dialog.setNegativeButton(R.string.no) { dialogInterface: DialogInterface, _: Int ->
                            dialogInterface.cancel()
                        }
                    }
                }
            }

            dialog.messages = obtainSms(address)
            dialog.formatDialog()

            dialogListView.adapter = MessagesAdapter(this, R.layout.message_item_day_left, dialog, nightMode!!)
            dialogListView.scrollTo(0, dialogListView.height)
        } catch (e: Exception) {
            Log.e(TAG, "Error DialogActivity onCreate: ${e.message}")
        }
    }

    private fun applyNightTheme() {
        val toolbar = findViewById<LinearLayout>(R.id.toolbar_dialog)
        toolbar.setBackgroundResource(R.color.night_toolbar)

        dialogListView.setBackgroundResource(R.color.nightTheme)
        findViewById<EditText>(R.id.text_for_send_edittext).setTextColor(Color.WHITE)
        findViewById<LinearLayout>(R.id.footer).setBackgroundResource(R.color.nightTheme)

        Picasso.with(this)
            .load(R.drawable.send_white)
            .into(findViewById<ImageView>(R.id.send_image))
    }

    private fun sendSmsAndUpdateAdapter() {
        try {
            SmsManager.getDefault()
                .sendTextMessage(dialog.address, null, smsBodyView.text.toString(), null, null)
            smsBodyView.text.clear()
            dialog.messages = obtainSms(dialog.address)
            dialogListView.adapter = MessagesAdapter(this, R.layout.message_item_day_right, dialog, nightMode!!)
            dialogListView.scrollTo(0, dialogListView.height)
        } catch (e: Exception) {
            Log.e(TAG, "Error DialogActivity sendSms: ${e.message}")
        }
    }

    private fun obtainSms(address: String): Array<RealmMessage> {
        var resultArray = emptyArray<RealmMessage>()
        try {
            val realm = Realm.getDefaultInstance()
            for (sms in getMessages(applicationContext, realm))
                if (getShortFormat(sms.address) == getShortFormat(address))
                    resultArray += sms
            realm.close()
        } catch (e: Exception) {
            Log.e(TAG, "Error DialogActivity obtainSms: ${e.message}")
        }
        return resultArray
    }

    private fun initToolbar() {

        findViewById<TextView>(R.id.textViewName).text = dialog.name

        val avatarImg = findViewById<ImageView>(R.id.imageViewAvatar)
        if (dialog.avatar != "none")
            Picasso.with(this).load(Uri.parse(dialog.avatar)).transform(CirculTransform()).into(avatarImg)
        else
            avatarImg.visibility = View.GONE


        findViewById<ImageView>(R.id.imageViewMore).setOnClickListener {
            showMoreMenu(it)
        }

        findViewById<ImageView>(R.id.imageViewSearch).setOnClickListener {
            switchSearchMode()
        }
    }

    private fun switchSearchMode() {
        val avatarView = findViewById<ImageView>(R.id.imageViewAvatar)
        val nameView = findViewById<TextView>(R.id.textViewName)
        val searchSwitchModeView = findViewById<ImageView>(R.id.imageViewSearch)
        val textSearchView = findViewById<EditText>(R.id.editText)

        if (!showSearch) {
            avatarView.visibility = View.GONE
            nameView.visibility = View.GONE
            textSearchView.visibility = View.VISIBLE
            searchSwitchModeView.setImageResource(R.drawable.close_white)

            textSearchView.addTextChangedListener(object : TextWatcher {
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    val messages = dialog.search(textSearchView.text.toString())
                    dialogListView.adapter =
                        SearchAdapter(context, R.layout.message_item_day_right, messages, nightMode!!)
                }
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun afterTextChanged(s: Editable?) {}
            })
            textSearchView.text.clear()
            showSearch = true
        } else {
            avatarView.visibility = View.VISIBLE
            nameView.visibility = View.VISIBLE
            textSearchView.visibility = View.GONE
            searchSwitchModeView.setImageResource(R.drawable.search_white)

            dialogListView.adapter =
                MessagesAdapter(this, R.layout.message_item_day_left, dialog, nightMode!!)
            showSearch = false
        }
    }

    private fun showMoreMenu(v: View) {
        val popupMenu = PopupMenu(this, v)
        popupMenu.inflate(R.menu.dialog_toolbar_menu)

        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.menuCall -> callToContact(dialog.address)
                R.id.menuStatistic -> showStatistic()
                R.id.menuClear -> clearDialog(dialog.address)
                R.id.menuAddToBan -> addToBan(dialog.address)
            }
            true
        }
        popupMenu.show()
    }

    private fun addToBan(phone: String) {
        val settingApp = SettingApp(context)
        val banlist = settingApp.banlist.get()
        banlist.add(phone)
        settingApp.banlist.set(banlist)
    }

    private fun callToContact(phone: String) {
        val dialIntent = Intent(Intent.ACTION_DIAL)
        dialIntent.data = Uri.parse("tel:$phone")
        startActivity(dialIntent)
    }

    private fun clearDialog(phone: String) {
        try {
            val dialog = AlertDialog.Builder(context)
                .setTitle(R.string.delete_dialog)
                .setMessage(R.string.want_delete_dialog)
                .setPositiveButton(R.string.yes) { _, _ ->
                    contentResolver.delete(
                        Uri.parse("content://sms/"), "address=?",
                        arrayOf(phone)
                    )
                    Toast.makeText(context, getString(R.string.error_delete_chat), Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton(R.string.no) { dial: DialogInterface, _: Int ->
                    dial.dismiss()
                }
            dialog.create().show()
        } catch (e: Exception) {
            Log.e(TAG, "Error DialogActivity clearDialog: ${e.message}")
        }
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    private fun showStatistic() {
        try {
            val statistic = dialog.generateStatistic()
            val dialogView = LayoutInflater.from(context).inflate(R.layout.statistic_act, null)

            val dialog = AlertDialog.Builder(context)
            dialog.setView(dialogView)
            dialogView.findViewById<TextView>(R.id.textViewAllSms).text =
                getString(R.string.all_sms_count) + statistic.allcount
            var tmp = getShortFormatDouble(statistic.getProcInbox().toDouble())
            dialogView.findViewById<TextView>(R.id.textViewInSms).text =
                getString(R.string.inbox_sms_count) + "${statistic.inboxCount} ($tmp%)"
            tmp = getShortFormatDouble(statistic.getProcOutbox().toDouble())
            dialogView.findViewById<TextView>(R.id.textViewSendSms).text =
                getString(R.string.outbox_sms_count) + "${statistic.outboxCount} ($tmp%)"

            val values = ArrayList<BarEntry>()
            for (elem in statistic.chastota.indices)
                values.add(BarEntry(statistic.chastota[elem].toFloat(), elem))

            val dataSet = BarDataSet(values, "")
            val labels = ArrayList<String>()
            for (i in statistic.time) {
                val pattern = SimpleDateFormat("dd.M.yyyy")
                val currentDate = pattern.format(Date(i))
                labels.add(currentDate)
            }
            val chart = dialogView.findViewById<BarChart>(R.id.statttt)
            chart.data = BarData(labels, dataSet)
            dialog.create().show()
        } catch (e: Exception) {
            Log.e(TAG, "Error DialogActivity statistic: ${e.message}")
        }
    }

    fun getShortFormatDouble(data: Double): String {
        val df = DecimalFormat("#.##")
        df.roundingMode = RoundingMode.CEILING
        return df.format(data)
    }

}
