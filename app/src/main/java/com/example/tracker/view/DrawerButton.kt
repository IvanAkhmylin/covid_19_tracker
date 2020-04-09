package com.example.tracker.view

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.ImageButton
import androidx.appcompat.graphics.drawable.DrawerArrowDrawable
import androidx.drawerlayout.widget.DrawerLayout

@SuppressLint("AppCompatCustomView")
class DrawerButton : ImageButton, DrawerLayout.DrawerListener {
    private var mDrawerLayout: DrawerLayout? = null
    private var side = Gravity.LEFT
    private lateinit var mDrawerArrowDrawable: DrawerArrowDrawable
    private var isOpened = false
    private var floatValue = 0f

    constructor(context: Context?) : super(context) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
    }

    override fun onDrawerStateChanged(newState: Int) {

    }

    override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
        Log.d("TAG", "Drawer ${if (isOpened) 1 else 0} <> $slideOffset ")

        setHomeAsUp(slideOffset)
    }

    override fun onDrawerClosed(drawerView: View) {
        this.isOpened = false
    }

    override fun onDrawerOpened(drawerView: View) {
        this.isOpened = true
    }

    fun changeState() {
        if (mDrawerLayout?.isDrawerOpen(side)!!) {
            mDrawerLayout?.closeDrawer(side)
        } else {
            mDrawerLayout?.openDrawer(side)
        }
    }

    fun getDrawerLayout(): DrawerLayout? {
        return mDrawerLayout
    }

    private fun setHomeAsUp(float: Float) {
        if (float != this.floatValue) {
            this.floatValue = float
            val anim =
                if (isOpened) {
                    ValueAnimator.ofFloat(this.floatValue, float)
                } else {
                    ValueAnimator.ofFloat(this.floatValue, float)
                }
            anim.addUpdateListener { valueAnimator ->
                val slideOffset = valueAnimator.animatedValue as Float
                mDrawerArrowDrawable.progress = slideOffset
            }
            anim.interpolator = DecelerateInterpolator()
            anim.duration = 100
            anim.start()
        }
    }

    fun setDrawerArrowDrawable(drawable: DrawerArrowDrawable) {
        this.mDrawerArrowDrawable = drawable
        this.setImageDrawable(drawable)
    }

    fun setDrawerLayout(drawerLayout: DrawerLayout): DrawerButton {
        this.mDrawerLayout = drawerLayout
        return this
    }
}