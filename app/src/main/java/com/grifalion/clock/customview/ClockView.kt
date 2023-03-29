package com.grifalion.clock.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import java.lang.Math.cos
import java.lang.Math.sin
import java.util.Calendar

class ClockView @JvmOverloads constructor(
    context: Context,
    attr: AttributeSet? = null,
    defStyle: Int = 0
): View(context, attr, defStyle){

    private var mHeight: Int = 0
    private var mWidth: Int = 0
    private var padding: Int = 0
    private var fontSize: Int = 0
    private var handTruncation: Int = 0
    private var hourHandTruncation: Int = 0
    private var numeralSpacing: Int = 0
    private var radius: Int = 0
    private var mIsInit = false
    private val mNumbers = intArrayOf(1,2,3,4,5,6,7,8,9,10,11,12)
    private val rect = Rect()
    private val mPaint = Paint()

    fun initClock() {
        mHeight = height
        mWidth = width
        padding = numeralSpacing + 50
        fontSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 13F, resources.displayMetrics).toInt()
        val min = Math.min(mHeight,mWidth)
        radius = min / 2 - padding
        handTruncation = min / 20
        hourHandTruncation = min / 7
        mIsInit = true

    }
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if(!mIsInit){
            initClock()
        }
        canvas.drawColor(Color.WHITE)
        drawCircle(canvas)
        drawCenter(canvas)
        drawNumeral(canvas)
        drawHands(canvas)
        postInvalidateDelayed(500)
        invalidate()
    }

    private fun drawNumeral(canvas: Canvas){
        mPaint.textSize = fontSize.toFloat()
        for(number in mNumbers) {
            var tmp: String = number.toString()
            mPaint.getTextBounds(tmp, 0, tmp.length, rect)
            var angle = Math.PI / 6 * (number - 3)
            var x = (mWidth / 2 + Math.cos(angle) * radius - rect.width())
            var y = (mHeight / 2 + Math.sin(angle) * radius + rect.height())
            canvas.drawText(tmp, x.toFloat(), y.toFloat(), mPaint)
        }
    }

    private fun drawCenter(canvas: Canvas){
        mPaint.style = Paint.Style.FILL
        canvas.drawCircle(mWidth/ 2F, mHeight / 2F, 12F, mPaint)
    }

    private fun drawCircle(canvas: Canvas) {
        mPaint.reset()
        mPaint.color = Color.BLACK
        mPaint.strokeWidth = 12F
        mPaint.style = Paint.Style.STROKE
        mPaint.isAntiAlias = true
        canvas.drawCircle(mWidth / 2F,mHeight / 2F,radius+padding.toFloat() - 10F, mPaint)
    }

    private fun drawHour(canvas: Canvas, loc: Float){
        mPaint.reset()
        mPaint.color = Color.BLUE
        mPaint.strokeWidth = 26F
        var angle = Math.PI * loc / 30 - Math.PI / 2
        var handRadius = radius - handTruncation * 2
        canvas.drawLine(
            (mWidth / 2 - cos(angle) * handRadius / 4).toFloat(),
            (mHeight / 2 - sin(angle) * handRadius / 4).toFloat(),
            (mWidth / 2 + cos(angle) * handRadius / 2).toFloat(),
            (mHeight / 2 + sin(angle) * handRadius / 2).toFloat(),
            mPaint)
    }


    private fun drawMinute(canvas: Canvas, loc: Float){
        mPaint.reset()
        mPaint.color = Color.RED
        mPaint.strokeWidth = 16F
        var angle = Math.PI * loc / 30 - Math.PI / 2
        var handRadius = radius - handTruncation
        canvas.drawLine(
            (mWidth / 2 - cos(angle) * handRadius / 4).toFloat(),
            (mHeight / 2 - sin(angle) * handRadius / 4).toFloat(),
            (mWidth / 2 + cos(angle) * handRadius / 1.6).toFloat(),
            (mHeight / 2 + sin(angle)* handRadius / 1.6).toFloat(),
            mPaint)
    }

    private fun drawSecond(canvas: Canvas,loc: Float){
        mPaint.reset()
        mPaint.color = Color.BLACK
        mPaint.strokeWidth = 12F
        var angle = Math.PI * loc / 30 - Math.PI / 2
        var handRadius = radius - handTruncation
        canvas.drawLine(
            (mWidth / 2 - cos(angle) * handRadius / 4).toFloat(),
            (mHeight / 2 - sin(angle) * handRadius / 4).toFloat(),
            (mWidth / 2 + cos(angle) * handRadius  ).toFloat(),
            (mHeight / 2 + sin(angle) * handRadius ).toFloat(),
            mPaint)
    }

    private fun drawHands(canvas: Canvas){
        var calendar: Calendar = Calendar.getInstance()
        var hour: Int = calendar.get(Calendar.HOUR_OF_DAY)
        hour = if(hour > 12) hour - 12 else hour
        drawHour(canvas,(hour + calendar.get(Calendar.MINUTE) / 60) * 5F)
        drawMinute(canvas, calendar.get(Calendar.MINUTE).toFloat())
        drawSecond(canvas, calendar.get(Calendar.SECOND).toFloat())
    }
}