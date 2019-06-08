package com.alt.karman_pc.smsmaket.helperFiles

import android.graphics.*
import com.squareup.picasso.Transformation

//закругление для аватарок
class CirculTransform : Transformation {

    override fun transform(source: Bitmap): Bitmap {
        val paint = Paint()
        paint.isAntiAlias = true
        paint.shader = BitmapShader(source, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        val output = Bitmap.createBitmap(source.width, source.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)
        canvas.drawCircle(source.width / 2f, source.height / 2f, source.width / 2f, paint)
        if (source != output) source.recycle()
        return output
    }

    override fun key() = "circle"
}


