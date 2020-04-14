package com.acorn.downloadsimulator.view

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import com.acorn.downloadsimulator.utils.DensityUtils

/**
 * Created by acorn on 2020/4/14.
 */
class ProducerDrawable(private val context: Context, private val maxShowAmount: Int) : Drawable() {
    private val blueFillPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLUE
        style = Paint.Style.FILL
    }
    private val greyFillPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.GRAY
        style = Paint.Style.FILL
    }
    private val producerTextPaint = Paint().apply {
        color = Color.BLACK
        textAlign = Paint.Align.RIGHT
        textSize = DensityUtils.sp2px(context, 18f).toFloat()
    }
    private val threadTextPaint = Paint().apply {
        color = Color.BLACK
        textAlign = Paint.Align.RIGHT
        textSize = DensityUtils.sp2px(context, 8f).toFloat()
    }

    private lateinit var textPointF: PointF

    private lateinit var testPath: TruckPath
    private lateinit var testTrucks: List<TruckPath>

    companion object {
        const val producerWidth = 100
        private const val producerHeightRatio = 0.7f
        const val producerHeight = (producerWidth * producerHeightRatio).toInt()
        //间隔空间
        const val xSpace = 1
        const val ySpace = 1
        //第一行距离顶部的高度
        const val startTop = 30
    }

    init {

    }

    override fun draw(canvas: Canvas) {
//        canvas.drawRect(0f, 0f, intrinsicWidth.toFloat(), intrinsicHeight.toFloat(), greyFillPaint)
        for (truck in testTrucks) {
            truck.draw(canvas)
        }
        canvas.drawText("生产者", textPointF.x, textPointF.y, producerTextPaint)
    }

    override fun onBoundsChange(bounds: Rect) {
        super.onBoundsChange(bounds)
//        bounds.offset(80, 0)
        testPath = generateTruckPath(Rect(20, 20, 20 + producerWidth, 20 + producerHeight))
        testTrucks = generateTrucks(50)
        textPointF = PointF(intrinsicWidth.toFloat(), intrinsicHeight.toFloat() - 10f)
    }

    private fun generateTrucks(amount: Int): List<TruckPath> {
        var vAmount = amount
        if (vAmount > maxShowAmount)
            vAmount = maxShowAmount
        val trucks = arrayListOf<TruckPath>()

        val width = bounds.width()
        val maxColumn = width / (producerWidth + xSpace)
        val startLeft: Int = (width - (maxColumn * (producerWidth + xSpace))) / 2
        var lastRow = 0
        var column = 0
        var row = 0
        for (i in 1..vAmount) {
            val left = startLeft + column * producerWidth + if (column == 0) 0 else xSpace
            val top = startTop + row * producerHeight + if (row == 0) 0 else ySpace
            val right = left + producerWidth
            val bottom = top + producerHeight
            val rect = Rect(left, top, right, bottom)
            trucks.add(generateTruckPath(rect))

            row = i / maxColumn
            if (lastRow != row)
                column = 0
            else {
                ++column
            }
            lastRow = row
        }
        return trucks
    }

    private fun generateTruckPath(rect: Rect): TruckPath {
        val path = Path()
        val wheelPath = Path()
        var textPoint: PointF
        with(rect) {
            val width = width().toFloat()
            val height = height().toFloat()
            path.moveTo(width * 0.08f, height * 0.08f)
            val truckLeft = left + width * 0.08f
            val truckTop = top + height * 0.2f
            val truckRight = truckLeft + width * 0.32f
            val truckBottom = truckTop + height * 0.5f
            val truckHead = RectF(truckLeft, truckTop, truckRight, truckBottom)
            path.addRect(truckHead, Path.Direction.CW)
            val hopperLeft = truckRight
            val hopperTop = truckTop + (truckBottom - truckTop) / 2f
            val hopperRight = left + width * 0.95f
            val hopperBottom = truckBottom
            path.addRect(RectF(hopperLeft, hopperTop, hopperRight, hopperBottom), Path.Direction.CW)
            val radius = (truckRight - truckLeft) * 0.3f
            wheelPath.addCircle(truckLeft + (truckRight - truckLeft) / 2f, truckBottom, radius, Path.Direction.CW)
            wheelPath.addCircle(hopperLeft + (hopperRight - hopperLeft) * 0.68f, hopperBottom, radius, Path.Direction.CW)
            textPoint = PointF(right.toFloat(), top + 20f)
        }
        return TruckPath(path, wheelPath, blueFillPaint, greyFillPaint, threadTextPaint, textPoint)
    }

    override fun setAlpha(alpha: Int) {
    }

    override fun getOpacity(): Int {
        return PixelFormat.TRANSLUCENT
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
    }

    override fun getIntrinsicWidth(): Int {
        return bounds.width()
    }

    override fun getIntrinsicHeight(): Int {
        val maxColumn = bounds.width() / (producerWidth + xSpace)
        val rowNumber = maxShowAmount / maxColumn + 1.coerceAtMost(maxShowAmount % maxColumn)
        return startTop + rowNumber * (producerHeight + ySpace)
    }

    /**
     * 车轮车身颜色不同,分成2个Path
     */
    class TruckPath(private val truckBody: Path, private val truckWheel: Path, private val bodyPaint: Paint,
                    private val wheelPaint: Paint, private val textPaint: Paint, private val textPoint: PointF) {
        fun draw(canvas: Canvas) {
            canvas.drawPath(truckBody, bodyPaint)
            canvas.drawPath(truckWheel, wheelPaint)
            canvas.drawText("T1", textPoint.x, textPoint.y, textPaint)
        }
    }
}