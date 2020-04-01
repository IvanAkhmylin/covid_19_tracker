package com.example.tracker

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Build
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.facebook.drawee.backends.pipeline.Fresco


class App : Application(){

    override fun onCreate() {
        Fresco.initialize(this)
        super.onCreate()
    }

    companion object{
        fun getBitmapFromVectorDrawable(context: Context?, drawableId: Int): Bitmap? {
            val drawable = ContextCompat.getDrawable(context!!, drawableId)
            val bitmap = Bitmap.createBitmap(
                drawable!!.intrinsicWidth,
                drawable.intrinsicHeight, Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight())
            drawable.draw(canvas)
            return bitmap
        }
    }


}