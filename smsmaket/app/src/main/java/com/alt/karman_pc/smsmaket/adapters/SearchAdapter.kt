package com.alt.karman_pc.smsmaket.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.alt.karman_pc.smsmaket.R
import com.alt.karman_pc.smsmaket.helperFiles.RealmMessage
import com.alt.karman_pc.smsmaket.helperFiles.getDate
import me.everything.providers.android.telephony.Sms
import java.text.SimpleDateFormat
import java.util.*

class SearchAdapter(context: Context, layout_id_stuf: Int, var messages: Array<RealmMessage>, val nightMode: Boolean)
    : ArrayAdapter<RealmMessage>(context, layout_id_stuf, messages) {

    @SuppressLint("ViewHolder", "SimpleDateFormat")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val sms = messages[position]
        val type = sms.type
        val layout =
            if (nightMode && type == "INBOX")
                R.layout.message_item_night_left
            else if (nightMode && type == "OUTBOX")
                R.layout.message_item_night_right
            else if (!nightMode && type == "INBOX")
                R.layout.message_item_day_left
            else
                R.layout.message_item_day_right

        val view = (context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater)
            .inflate(layout, parent, false)

        view.findViewById<TextView>(R.id.message_view).text = sms.body
        view.setOnClickListener {
            val timeView = view.findViewById<TextView>(R.id.textViewDateMessage)
            val pattern = SimpleDateFormat("hh:mm:ss")
            val currentDate = pattern.format(Date(sms.getDate()))
            timeView.text = currentDate
            if (timeView.visibility == View.GONE)
                timeView.visibility = View.VISIBLE
            else
                timeView.visibility = View.GONE
        }
        return view

    }
}
