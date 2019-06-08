//Адаптер для заполнения списка диалогов в MainActivity

package com.alt.karman_pc.smsmaket.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.net.Uri
import android.view.View
import android.view.ViewGroup
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import android.widget.ImageView
import com.alt.karman_pc.smsmaket.helperFiles.CirculTransform
import com.alt.karman_pc.smsmaket.helperFiles.Dialog
import com.alt.karman_pc.smsmaket.Activity.DialogActivity
import com.alt.karman_pc.smsmaket.R
import com.squareup.picasso.Picasso


class DialogsAdapter(context: Context, private val layout_id: Int, var dialogs: Array<Dialog>, var nightMode: Boolean)
    : ArrayAdapter<Dialog>(context, layout_id, dialogs) {

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val bindingUtil: ViewDataBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context), layout_id, parent, false
        )
        bindingUtil.setVariable(1, dialogs[position])
        val view = bindingUtil.root

        view.setOnClickListener {
            val intent = Intent(context, DialogActivity::class.java)
            intent.putExtra("address", dialogs[position].address)
            intent.putExtra("name", dialogs[position].name)
            intent.putExtra("avatar", dialogs[position].avatar)
            context.startActivity(intent)
        }

        val avatar = view.findViewById<ImageView>(R.id.iv)
        if (dialogs[position].avatar != "none")
            Picasso
                .with(context)
                .load(Uri.parse(dialogs[position].avatar))
                .transform(CirculTransform())
                .into(avatar)
        else
            avatar.setImageResource(
                if (nightMode) R.drawable.face_white
                else R.drawable.face_black
            )
        return view
    }
}
