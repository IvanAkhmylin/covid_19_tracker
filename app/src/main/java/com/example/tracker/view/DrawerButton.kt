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
//        setHomeAsUp(slideOffset)
    }

    override fun onDrawerClosed(drawerView: View) {
        this.isOpened = false
    }

    override fun onDrawerOpened(drawerView: View) {
        this.isOpened = true
    }

    fun changeDrawerState() {
        if (mDrawerLayout?.isDrawerOpen(side)!!) {
            mDrawerLayout?.closeDrawer(side)
        } else {
            mDrawerLayout?.openDrawer(side)
        }
    }

    fun changeSearchState(fragmentsCount: Int) {
        val anim =
            if (fragmentsCount == 1) {
                ValueAnimator.ofFloat(1f, 0f)
            } else {
                ValueAnimator.ofFloat(0f, 1f)
            }
        anim.addUpdateListener { valueAnimator ->
            val slideOffset = valueAnimator.animatedValue as Float
            mDrawerArrowDrawable.progress = slideOffset
        }
        anim.interpolator = DecelerateInterpolator()
        anim.duration = 300
        anim.start()
    }


    fun getDrawerLayout(): DrawerLayout? {
        return mDrawerLayout
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