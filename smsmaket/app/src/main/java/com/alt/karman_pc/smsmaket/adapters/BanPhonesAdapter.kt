package com.alt.karman_pc.smsmaket.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.support.constraint.ConstraintLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.alt.karman_pc.smsmaket.R
import com.alt.karman_pc.smsmaket.helperFiles.SettingApp


class BanPhonesAdapter(context: Context, private val layout_id: Int, var banedPhones: Array<String>, var nightMode: Boolean, val myView: ListView):
    ArrayAdapter<String>(context, layout_id, banedPhones) {

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = (context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater)
            .inflate(layout_id, parent, false)
        view.findViewById<ImageView>(R.id.imageViewDel).setOnClickListener {

            val settingApp = SettingApp(context)
            val list = settingApp.banlist.get().toMutableSet()
            list.remove(banedPhones[position])
            settingApp.banlist.set(list)

            myView.adapter = BanPhonesAdapter(context, R.layout.ban_item, list.toTypedArray(), nightMode, myView)
            
            Toast.makeText(context, context.getString(R.string.info_unban_phone), Toast.LENGTH_SHORT).show()
        }

        view.findViewById<TextView>(R.id.textViewPhone).text = banedPhones[position]

        if (nightMode) {
            view.findViewById<TextView>(R.id.textViewPhone).setTextColor(Color.WHITE)
            view.findViewById<ConstraintLayout>(R.id.fon).setBackgroundResource(R.color.nightTheme)
            view.findViewById<ImageView>(R.id.imageViewDel).setImageResource(R.drawable.close_white)
        }

        return view
    }
}