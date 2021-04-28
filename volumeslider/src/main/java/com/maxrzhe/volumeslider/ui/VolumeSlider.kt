package com.maxrzhe.volumeslider.ui

import android.content.Context
import android.graphics.*
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.Px
import androidx.core.view.GestureDetectorCompat
import com.maxrzhe.volumeslider.R
import com.maxrzhe.volumeslider.extensions.dpToPx
import kotlin.math.atan2
import kotlin.math.min
import kotlin.math.sin

class VolumeSlider @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr), GestureDetector.OnGestureListener {

    companion object {
        private const val DEFAULT_SLIDER_SIZE_DP = 100
        private const val DEFAULT_STROKE_RADIUS_DP = 50
        private const val DEFAULT_STROKE_COLOR = Color.BLACK
        private const val DEFAULT_STROKE_WIDTH_DP = 3
        private const val DEFAULT_NICK_COLOR = Color.RED
        private const val DEFAULT_NICK_WIDTH_DP = 3
        private const val DEFAULT_NICK_LENGTH_DP = 20
    }

    @Px
    private var sliderStrokeRadius: Float = context.dpToPx(DEFAULT_STROKE_RADIUS_DP)

    @Px
    private var slideStrokeWidth: Float = context.dpToPx(DEFAULT_STROKE_WIDTH_DP)

    @Px
    private var nickLength: Float = context.dpToPx(DEFAULT_NICK_LENGTH_DP)

    @Px
    private var nickWidth: Float = context.dpToPx(DEFAULT_NICK_WIDTH_DP)

    @ColorInt
    private var sliderColor: Int = DEFAULT_STROKE_COLOR

    @ColorInt
    private var nickColor = DEFAULT_NICK_COLOR

    private var currentValue: Int = 0

    private val center = Point()
    private val nickLeftStart = Point()
    private val nickLeftFinish = Point()
    private val nickRightStart = Point()
    private val nickRightFinish = Point()
    private val nickMovingStart = Point()
    private val nickMovingFinish = Point()

    private val sliderFrame = RectF()

    private var gestureDetector: GestureDetectorCompat
    private var listener: SliderRotationListener? = null

    private val strokePaint = Paint()
    private val nickPaint = Paint()
    private val textPaint = Paint()

    init {

        if (attrs != null) {
            val ta = context.obtainStyledAttributes(attrs, R.styleable.VolumeSlider)
            sliderStrokeRadius = ta.getDimension(
                R.styleable.VolumeSlider_vs_strokeRadius, context.dpToPx(
                    DEFAULT_STROKE_RADIUS_DP
                )
            )
            slideStrokeWidth = ta.getDimension(
                R.styleable.VolumeSlider_vs_strokeWidth, context.dpToPx(
                    DEFAULT_STROKE_WIDTH_DP
                )
            )
            nickLength = ta.getDimension(
                R.styleable.VolumeSlider_vs_nickLength, context.dpToPx(
                    DEFAULT_NICK_LENGTH_DP
                )
            )
            nickWidth = ta.getDimension(
                R.styleable.VolumeSlider_vs_nickWidth, context.dpToPx(
                    DEFAULT_NICK_WIDTH_DP
                )
            )
            sliderColor =
                ta.getColor(R.styleable.VolumeSlider_vs_strokeColor, DEFAULT_STROKE_COLOR)
            nickColor = ta.getColor(R.styleable.VolumeSlider_vs_nickColor, DEFAULT_NICK_COLOR)
            currentValue = ta.getInt(R.styleable.VolumeSlider_vs_currentValue, 0)
            ta.recycle()
        }
        setup()
        gestureDetector = GestureDetectorCompat(context, this)
    }

    private fun setup() {
        strokePaint.apply {
            isAntiAlias = true
            style = Paint.Style.STROKE
            strokeWidth = slideStrokeWidth
            color = sliderColor
        }
        nickPaint.apply {
            isAntiAlias = true
            style = Paint.Style.STROKE
            strokeWidth = nickWidth
            color = nickColor
        }
        textPaint.apply {
            isAntiAlias = true
            color = nickColor
            textAlign = Paint.Align.CENTER
            textSize = sliderStrokeRadius * 0.25f
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val initSize = resolveDefaultSize(widthMeasureSpec)
        setMeasuredDimension(initSize, initSize)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        center.x = w / 2
        center.y = h / 2
        resolveSliderFrame()
        resolveFixedNicks()
        resolveMovingNick()
    }

    override fun onDraw(canvas: Canvas) {
        drawCircleFrame(canvas)
        drawText(canvas)
        drawFixedNicks(canvas)
        drawMovingNick(canvas)
    }

    override fun onSaveInstanceState(): Parcelable? {
        val savedState = SavedState(super.onSaveInstanceState())
        savedState.sliderStrokeRadius = sliderStrokeRadius
        savedState.slideStrokeWidth = slideStrokeWidth
        savedState.nickLength = nickLength
        savedState.nickWidth = nickWidth
        savedState.sliderColor = sliderColor
        savedState.nickColor = nickColor
        savedState.currentValue = currentValue
        return savedState
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        if (state is SavedState) {
            super.onRestoreInstanceState(state)
            sliderStrokeRadius = state.sliderStrokeRadius
            slideStrokeWidth = state.slideStrokeWidth
            nickLength = state.nickLength
            nickWidth = state.nickWidth
            sliderColor = state.sliderColor
            nickColor = state.nickColor
            currentValue = state.currentValue
        } else {
            super.onRestoreInstanceState(state)
        }
    }

    private fun drawText(canvas: Canvas) {
        val offsetY = (textPaint.descent() + textPaint.ascent()) / 2
        canvas.drawText(
            "$currentValue%",
            center.x.toFloat(),
            center.y.toFloat() - offsetY,
            textPaint
        )
    }

    private fun drawFixedNicks(canvas: Canvas) {
        canvas.drawLine(
            nickLeftStart.x.toFloat(),
            nickLeftStart.y.toFloat(),
            nickLeftFinish.x.toFloat(),
            nickLeftFinish.y.toFloat(),
            nickPaint
        )
        canvas.drawLine(
            nickRightStart.x.toFloat(),
            nickRightStart.y.toFloat(),
            nickRightFinish.x.toFloat(),
            nickRightFinish.y.toFloat(),
            nickPaint
        )
    }

    private fun drawMovingNick(canvas: Canvas) {
        canvas.drawLine(
            nickMovingStart.x.toFloat(),
            nickMovingStart.y.toFloat(),
            nickMovingFinish.x.toFloat(),
            nickMovingFinish.y.toFloat(),
            nickPaint
        )
    }

    private fun drawCircleFrame(canvas: Canvas) {
        canvas.drawOval(sliderFrame, strokePaint)
    }

    private fun resolveDefaultSize(spec: Int): Int {
        resolveFixedNicks()
        return when (MeasureSpec.getMode(spec)) {
            MeasureSpec.AT_MOST -> min(
                MeasureSpec.getSize(spec),
                (nickRightStart.x - nickLeftStart.x)
            )
            MeasureSpec.EXACTLY -> MeasureSpec.getSize(spec)
            MeasureSpec.UNSPECIFIED -> context.dpToPx(DEFAULT_SLIDER_SIZE_DP).toInt()
            else -> MeasureSpec.getSize(spec)
        }
    }

    private fun resolveSliderFrame() {
        val halfStroke = slideStrokeWidth / 2
        sliderFrame.apply {
            left = center.x.toFloat() - sliderStrokeRadius + halfStroke
            top = center.y.toFloat() - sliderStrokeRadius + halfStroke
            right = center.x.toFloat() + sliderStrokeRadius - halfStroke
            bottom = center.y.toFloat() + sliderStrokeRadius - halfStroke
        }
    }

    private fun resolveFixedNicks() {
        val startLine = sliderStrokeRadius + nickWidth * 2 + nickLength
        nickLeftStart.x = center.x - (startLine * sin(Math.toRadians(60.0))).toInt()
        nickLeftStart.y = center.y + (startLine * sin(Math.toRadians(30.0))).toInt()
        nickLeftFinish.x = nickLeftStart.x + (nickLength * sin(Math.toRadians(60.0))).toInt()
        nickLeftFinish.y = nickLeftStart.y - (nickLength * sin(Math.toRadians(30.0))).toInt()

        nickRightStart.x = center.x + (startLine * sin(Math.toRadians(60.0))).toInt()
        nickRightStart.y = center.y + (startLine * sin(Math.toRadians(30.0))).toInt()
        nickRightFinish.x = nickRightStart.x - (nickLength * sin(Math.toRadians(60.0))).toInt()
        nickRightFinish.y = nickRightStart.y - (nickLength * sin(Math.toRadians(30.0))).toInt()
    }

    private fun resolveMovingNick() {
        val startLine = sliderStrokeRadius - slideStrokeWidth - nickWidth * 2
        nickMovingStart.x =
            center.x - (startLine * sin(Math.toRadians(60.0 + currentValue.toFloat() * 2.4))).toInt()
        nickMovingStart.y =
            center.y + (startLine * sin(Math.toRadians(30.0 - currentValue.toFloat() * 2.4))).toInt()
        nickMovingFinish.x =
            nickMovingStart.x + (nickLength * sin(Math.toRadians(60.0 + currentValue.toFloat() * 2.4))).toInt()
        nickMovingFinish.y =
            nickMovingStart.y - (nickLength * sin(Math.toRadians(30.0 - currentValue.toFloat() * 2.4))).toInt()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return if (gestureDetector.onTouchEvent(event)) true else super.onTouchEvent(event)
    }

    override fun onScroll(
        e1: MotionEvent?,
        e2: MotionEvent?,
        distanceX: Float,
        distanceY: Float
    ): Boolean {
        val rotationDegrees = calculateAngle(e2?.x, e2?.y)
        currentValue = (rotationDegrees / 2.4).toInt()
        if (rotationDegrees >= 0 && currentValue <= 100) {
            resolveMovingNick()
            invalidate()
        }

        if (listener != null) {
            listener?.onRotate(currentValue)
        }
        return true
    }

    override fun onDown(e: MotionEvent?): Boolean = true

    override fun onShowPress(e: MotionEvent?) {}

    override fun onSingleTapUp(e: MotionEvent?): Boolean = false

    override fun onLongPress(e: MotionEvent?) {}

    override fun onFling(
        e1: MotionEvent?,
        e2: MotionEvent?,
        velocityX: Float,
        velocityY: Float
    ): Boolean = false

    private fun calculateAngle(x: Float?, y: Float?): Float {
        if (x != null && y != null) {
            val px = (x / width.toFloat()) - 0.5
            val py = (1 - y / height.toFloat()) - 0.5
            var angle = -Math.toDegrees(atan2(py, px)).toFloat() + 210
            if (angle > 240) angle -= 360
            return angle
        }
        return 0f
    }

    private class SavedState : BaseSavedState, Parcelable {
        var sliderStrokeRadius: Float = 0f
        var slideStrokeWidth: Float = 0f
        var nickLength: Float = 0f
        var nickWidth: Float = 0f
        var sliderColor: Int = 0
        var nickColor: Int = 0
        var currentValue: Int = 0

        constructor(state: Parcelable?) : super(state)

        constructor(parcel: Parcel) : super(parcel) {
            sliderStrokeRadius = parcel.readFloat()
            slideStrokeWidth = parcel.readFloat()
            nickLength = parcel.readFloat()
            nickWidth = parcel.readFloat()
            sliderColor = parcel.readInt()
            nickColor = parcel.readInt()
            currentValue = parcel.readInt()
        }

        override fun writeToParcel(parcel: Parcel, flags: Int) {
            super.writeToParcel(parcel, flags)
            parcel.writeFloat(sliderStrokeRadius)
            parcel.writeFloat(slideStrokeWidth)
            parcel.writeFloat(nickLength)
            parcel.writeFloat(nickWidth)
            parcel.writeInt(sliderColor)
            parcel.writeInt(nickColor)
            parcel.writeInt(currentValue)
        }

        override fun describeContents(): Int = 0

        companion object CREATOR : Parcelable.Creator<SavedState> {
            override fun createFromParcel(parcel: Parcel): SavedState = SavedState(parcel)
            override fun newArray(size: Int): Array<SavedState?> = arrayOfNulls(size)
        }
    }

    interface SliderRotationListener {
        fun onRotate(value: Int)
    }
}