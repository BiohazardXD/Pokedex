package com.code.pokedex.presentation.ui.home.adapter

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.code.pokedex.R


class CircleView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    View(context, attrs, defStyleAttr) {
    private val circleColour: Int
    private val paint = Paint()
    private var circleRadius = 0f
    private var active = false
    private val activeColour: Int
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        paint.color = if (active) Color.RED else circleColour
        setBackgroundColor(Color.TRANSPARENT)
        canvas.drawCircle(circleRadius, circleRadius, circleRadius, paint)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)
        circleRadius = if (width == 0 && height == 0) {
            0f
        } else {
            ((if (width > height) height else width) / 2).toFloat()
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_UP) {
            performClick()
        }
        return true
    }

    override fun performClick(): Boolean {
        active = !active
        invalidate()
        return super.performClick()
    }

    init {
        val a = context.obtainStyledAttributes(
            attrs,
            R.styleable.CircleView,
            defStyleAttr,
            0
        )
        circleColour = a.getColor(R.styleable.CircleView_inactive_background, Color.RED)
        activeColour = a.getColor(R.styleable.CircleView_active_background, Color.GRAY)
        a.recycle()
    }
}