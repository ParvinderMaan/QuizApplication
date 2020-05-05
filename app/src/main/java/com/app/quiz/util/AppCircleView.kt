package com.app.quiz.util

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.app.quiz.R


class AppCircleView(context: Context, attrs: AttributeSet) : View(context, attrs) {
    // Paint object for coloring and styling
    private val paint = Paint()
    var circleSizeArray = arrayOf<Float>(10f,5f,8f,6f,10f,5f,8f) // 7
    var circleCxArray = arrayOf<Float>(0f,0f,0f,0f,0f,0f,0f) // 7
    var circleCyArray = arrayOf<Float>(0f,0f,0f,0f) // 4
    private var viewTotWidth=0    // px   720 , 1440
    private var viewTotHeight=0   // px
    var noOfCircles = 7
    private var widthSlice: Int=0
    private var heightSlice: Int=0


//    private var rect1=Rect()
//    private var rect2=Rect()
//    private var rect3=Rect()
//    private var rect4=Rect()
//    private var rect5=Rect()
//    private var rect6=Rect()
//    private var rect7=Rect()
//    private  val SQUARE_SIZE=100

    init {
        paint.isAntiAlias = true
        setupAttributes(attrs)
        Log.e("AppCircleView","init called....:" );

    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        Log.e("AppCircleView","onDraw called....:" );
//        canvas.drawColor(resources.getColor(R.color.colorBlue)) // to draw background color
        widthSlice=viewTotWidth/noOfCircles

        circleCxArray[0]=0f // init
        for (i in 1 until noOfCircles){
            circleCxArray[i]=circleCxArray[i-1]+widthSlice
        }
//
        heightSlice=viewTotHeight/4

        circleCyArray[0]=0f // init
        for (i in 1 until 4){ //
            circleCyArray[i]=circleCyArray[i-1]+heightSlice
        }

//        Log.e("AppCircleView","sliceValue: "+slice)

//        for (i in 0 until noOfCircles){
//            Log.e("AppCircleView","all slice values: at "+i+"  "+circleCxArray[i])
//
//        }
        drawCircles(canvas)
    }

    private fun drawCircles(canvas: Canvas) {
        paint.style = Paint.Style.FILL

        //1
        paint.color =resources.getColor(R.color.colorLightBlue)
        canvas.drawCircle(circleCxArray[0]+widthSlice/2, circleCyArray[3]+heightSlice/2, circleSizeArray[0], paint)
        //2
        paint.color =resources.getColor(R.color.colorPink)
        canvas.drawCircle(circleCxArray[1]+widthSlice/2, circleCyArray[2]+heightSlice/2, circleSizeArray[1], paint)
        //3
        paint.color =resources.getColor(R.color.colorRed)
        canvas.drawCircle(circleCxArray[2]+widthSlice/2, circleCyArray[3]+heightSlice/2, circleSizeArray[2], paint)

        //4
        paint.color =resources.getColor(R.color.colorCyan)
        canvas.drawCircle(circleCxArray[3]+widthSlice/2, circleCyArray[1]+heightSlice/2, circleSizeArray[3], paint)

        //5
        paint.color =resources.getColor(R.color.colorYellow)
        canvas.drawCircle(circleCxArray[4]+widthSlice/2, circleCyArray[2]+heightSlice/2, circleSizeArray[4], paint)

        //6
        paint.color =resources.getColor(R.color.colorOrange)
        canvas.drawCircle(circleCxArray[5]+widthSlice/2, circleCyArray[1]+heightSlice/2, circleSizeArray[5], paint)

        //7
        paint.color =resources.getColor(R.color.colorGreen)
        canvas.drawCircle(circleCxArray[6]+widthSlice/2, circleCyArray[3]+heightSlice/2, circleSizeArray[6], paint)


    }
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        Log.e("AppCircleView","onMeasure called....:" );
//        Log.e("AppCircleView","widthMeasureSpec:"+widthMeasureSpec+"heightMeasureSpec:"+heightMeasureSpec)
        viewTotWidth = MeasureSpec.getSize(widthMeasureSpec)
        viewTotHeight = MeasureSpec.getSize(heightMeasureSpec)
//        Log.e("AppCircleView","viewWidth:"+viewTotWidth+"viewHeight:"+viewTotHeight)
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        // 1
//        size = Math.min(measuredWidth, measuredHeight)
       // 2
       // setMeasuredDimension(size, size)
    }
    private fun setupAttributes(attrs: AttributeSet?) {
        // 6
        // Obtain a typed array of attributes

        // 8
        // TypedArray objects are shared and must be recycled.
        //typedArray.recycle()
    }


//    private fun drawRectanges(canvas: Canvas) {
//        //1
//        paint.style = Paint.Style.FILL
//        paint.color =resources.getColor(R.color.colorGreen)
//         rect1.set( 50, 50, SQUARE_SIZE+50, SQUARE_SIZE+50)
//         canvas.drawRect(rect1,paint)
//
//        //2
//        paint.style = Paint.Style.FILL
//        paint.color =resources.getColor(R.color.colorOrange)
//        rect2.set( 200, 50, SQUARE_SIZE+200, SQUARE_SIZE+50)
//        canvas.drawRect(rect2,paint)
//
//        //3
//        paint.style = Paint.Style.FILL
//        paint.color =resources.getColor(R.color.colorYellow)
//        rect3.set( 350, 50, SQUARE_SIZE+350, SQUARE_SIZE+50)
//        canvas.drawRect(rect3,paint)
//
//        //4
//        paint.style = Paint.Style.FILL
//        paint.color =resources.getColor(R.color.colorRed)
//        rect4.set( 500, 50, SQUARE_SIZE+500, SQUARE_SIZE+50)
//        canvas.drawRect(rect4,paint)
//
//        //5
//        paint.style = Paint.Style.FILL
//        paint.color =resources.getColor(R.color.colorCyan)
//        rect5.set( 650, 50, SQUARE_SIZE+650, SQUARE_SIZE+50)
//        canvas.drawRect(rect5,paint)
//
//        //6
//        paint.style = Paint.Style.FILL
//        paint.color =resources.getColor(R.color.colorLightBlue)
//        rect6.set( 800, 50, SQUARE_SIZE+800, SQUARE_SIZE+50)
//        canvas.drawRect(rect6,paint)
//
//        //7
//        paint.style = Paint.Style.FILL
//        paint.color =resources.getColor(R.color.colorPink)
//        rect7.set( 950, 50, SQUARE_SIZE+950, SQUARE_SIZE+50)
//        canvas.drawRect(rect7,paint)
//
//    }

}