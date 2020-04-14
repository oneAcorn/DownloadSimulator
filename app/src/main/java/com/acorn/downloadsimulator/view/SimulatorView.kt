package com.acorn.downloadsimulator.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import com.acorn.downloadsimulator.utils.LogUtil

/**
 * Created by acorn on 2020/4/14.
 */
class SimulatorView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : View(context, attrs) {
    private val producerDrawable = ProducerDrawable(context, 50)

    init {
        producerDrawable.callback = this
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        LogUtil.d("View draw")
        producerDrawable.draw(canvas)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        computeSize(w, h)
        producerDrawable.setBounds(0, 0, w, h)
    }

    private fun computeSize(w: Int, h: Int) {
        LogUtil.d("computeSize w:$w,h:$h")
    }

    override fun verifyDrawable(who: Drawable): Boolean {
        //允许drawable刷新自己以执行动画
        return super.verifyDrawable(who) || who == producerDrawable
    }
}