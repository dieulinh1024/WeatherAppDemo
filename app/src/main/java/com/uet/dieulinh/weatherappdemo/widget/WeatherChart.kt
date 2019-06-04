package com.uet.dieulinh.weatherappdemo.widget

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.uet.dieulinh.weatherappdemo.dto.WeatherMainDTO
import com.uet.dieulinh.weatherappdemo.manager.AppPreManager

/**
 * Created by dieulinh on 03/06/2019 at 21:23
 */

class WeatherChart(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    var paint: Paint
    var textPaint: Paint
    var tempList = ArrayList<WeatherMainDTO>()
    var SCALE = 5

    init {
        paint = Paint().apply {
            color = Color.BLACK
            strokeWidth = 2f
            style = Paint.Style.STROKE
            isAntiAlias = true
        }
        textPaint = Paint().apply {
            color = Color.BLACK
            textSize = 36f
            isAntiAlias = true
        }
    }

    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)
        if (tempList.isNotEmpty()) {
            if (AppPreManager.getTempUnit() == "F") {
                SCALE = 2
            }
            val itemWidth = (width / 5).toFloat()
            val centerHeight = (height / 2).toFloat()
            val tempMinPoints = ArrayList<PointF>()
            val tempMaxPoints = ArrayList<PointF>()
            for (i in 0..tempList.size - 1) {
                val left = i * itemWidth

                val minPoint = PointF(left + itemWidth / 2, centerHeight + tempList.get(i).temp_min * SCALE)
                tempMinPoints.add(minPoint)
                val tempMin = "${tempList.get(i).temp_min.toInt()}${Typography.degree}${AppPreManager.getTempUnit()}"
                val tempMinRect = Rect()
                textPaint.getTextBounds(tempMin, 0, tempMin.length, tempMinRect)
                canvas.drawCircle(minPoint.x, minPoint.y, 5f, paint)
                canvas.drawText(tempMin, minPoint.x - tempMinRect.width() / 2, minPoint.y + textPaint.textSize + 7f, textPaint)

                val maxPoint = PointF(left + itemWidth / 2, centerHeight - tempList.get(i).temp_max * SCALE)
                tempMaxPoints.add(maxPoint)
                val tempMax = "${tempList.get(i).temp_max.toInt()}${Typography.degree}${AppPreManager.getTempUnit()}"
                val tempMaxRect = Rect()
                textPaint.getTextBounds(tempMax, 0, tempMax.length, tempMaxRect)
                canvas.drawCircle(maxPoint.x, maxPoint.y, 5f, paint)
                canvas.drawText(tempMax, maxPoint.x - tempMaxRect.width() / 2, maxPoint.y - 15f, textPaint)
            }
            for (i in 1..tempMinPoints.size - 1) {
                canvas.drawLine(tempMinPoints.get(i - 1).x + 5f, tempMinPoints.get(i - 1).y, tempMinPoints.get(i).x - 5f, tempMinPoints.get(i).y, paint)
                canvas.drawLine(tempMaxPoints.get(i - 1).x + 5f, tempMaxPoints.get(i - 1).y, tempMaxPoints.get(i).x - 5f, tempMaxPoints.get(i).y, paint)
            }
        }
    }

    fun updateView(list: List<WeatherMainDTO>) {
        tempList.clear()
        tempList.addAll(list)
        postInvalidate()
    }
}