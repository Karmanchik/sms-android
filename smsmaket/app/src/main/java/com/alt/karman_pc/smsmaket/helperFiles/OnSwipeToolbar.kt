package com.alt.karman_pc.smsmaket.helperFiles

import android.content.Context
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.ListView
import android.widget.Toast
import com.alt.karman_pc.smsmaket.R
import com.alt.karman_pc.smsmaket.adapters.DialogsAdapter
import me.everything.providers.android.contacts.ContactsProvider
import me.everything.providers.android.telephony.TelephonyProvider

class OnSwipeToolbar(var ctx: Context, var listView: ListView) : View.OnTouchListener {

    private var gestureDetector: GestureDetector

    init {
        gestureDetector = GestureDetector(ctx, GestureListener())
    }

    override fun onTouch(v: View, event: MotionEvent) = gestureDetector.onTouchEvent(event)

    private inner class GestureListener : GestureDetector.SimpleOnGestureListener() {

        override fun onDown(e: MotionEvent) = true

        override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
            var result = false
            try {
                val diffY = e2.y - e1.y
                val diffX = e2.x - e1.x
                if (Math.abs(diffX) > Math.abs(diffY)) { }
                else if (Math.abs(diffY) > 100 && Math.abs(velocityY) > 100 && diffY > 0)
                        onSwipeBottom()
                result = true

            } catch (exception: Exception) {
                exception.printStackTrace()
            }
            return result
        }
    }

    fun onSwipeBottom() {
        Toast.makeText(ctx, "Updating", Toast.LENGTH_SHORT).show()

        val messages =
            TelephonyProvider(ctx).getSms(TelephonyProvider.Filter.ALL).list.toTypedArray()
        val contacts =
            ContactsProvider(ctx).contacts.list.toTypedArray()

        val dialogManager = DialogsManager(messages, ctx)
        dialogManager.setContacts(contacts)

        val nightMode = SettingApp(ctx).nightMode.get()
        val dialogLayout =
            if (nightMode) R.layout.dialog_item_night
            else R.layout.dialog_item_day

        listView.adapter =
            DialogsAdapter(ctx, dialogLayout, dialogManager.dialogs, nightMode)
    }
}