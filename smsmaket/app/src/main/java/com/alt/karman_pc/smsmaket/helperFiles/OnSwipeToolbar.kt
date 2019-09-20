package com.alt.karman_pc.smsmaket.helperFiles

import android.content.Context
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import kotlin.math.abs

class OnSwipeToolbar(var ctx: Context) : View.OnTouchListener {

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
                if (abs(diffX) > abs(diffY)) { }
                else if (abs(diffY) > 100 && abs(velocityY) > 100 && diffY > 0)
                        onSwipeBottom()
                result = true

            } catch (exception: Exception) {
                exception.printStackTrace()
            }
            return result
        }
    }

    fun onSwipeBottom() {
        Log.e("load", "giga")
    }
}