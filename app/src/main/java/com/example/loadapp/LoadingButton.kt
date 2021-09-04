package com.example.loadapp

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.animation.addListener
import androidx.core.content.res.ResourcesCompat
import androidx.core.content.withStyledAttributes
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0

    private var initBackgroundColor =
        ResourcesCompat.getColor(resources, R.color.colorPrimary, null)
    private var progressRectColor =
        ResourcesCompat.getColor(resources, R.color.colorPrimaryDark, null)
    private val progressCircleColor = Color.YELLOW

    private var progressRect = RectF(0F, 0F, 0F, 0F)
    private val progressRectPaint by lazy {
        Paint().apply {
            color = progressRectColor
        }
    }

    private var progressCircleAngle = 0F
    private val progressCirclePaint = Paint().apply {
        color = progressCircleColor
    }

    private var currentText: String? = null
    private var initText: String? = null
    private var progressText: String? = null
    private val textBounds = Rect()
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = 60F
        color = Color.WHITE
        textAlign = Paint.Align.CENTER
        style = Paint.Style.FILL
        typeface = Typeface.DEFAULT_BOLD
    }

    private var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->
        when (new) {
            ButtonState.Clicked -> clicked()
            ButtonState.Loading -> loading()
            ButtonState.Completed -> completed()
        }
    }

    init {
        isClickable = true
        context.withStyledAttributes(attrs, R.styleable.LoadingButton) {
            initBackgroundColor = getColor(
                R.styleable.LoadingButton_initBackgroundColor,
                ResourcesCompat.getColor(resources, R.color.colorPrimary, null)
            )
            progressRectColor = getColor(
                R.styleable.LoadingButton_progressColor,
                ResourcesCompat.getColor(resources, R.color.colorPrimaryDark, null)
            )
        }
        initText = context.getString(R.string.button_name)
        progressText = context.getString(R.string.button_loading)
        currentText = initText
    }

    private fun clicked() {
        buttonState = ButtonState.Loading
    }

    private fun loading() {
        val valueFrom = 0F
        val valueTo = 1F
        currentText = progressText
        val calculateRect = widthSize.toFloat() / valueTo
        val calculateCircle = 360 / valueTo
        ValueAnimator.ofFloat(valueFrom, valueTo).apply {
            duration = 1000
            interpolator = LinearOutSlowInInterpolator()
            addUpdateListener {
                progressRect.right = it.animatedValue as Float * calculateRect
                progressRect.bottom = heightSize.toFloat()
                progressCircleAngle = it.animatedValue as Float * calculateCircle
                invalidate()
            }
            addListener(
                onEnd = {
                    buttonState = ButtonState.Completed
                    invalidate()
                })
        }?.start()
    }

    private fun completed() {
        currentText = initText
        progressRect.right = 0F
        progressCircleAngle = 0F
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawColor(initBackgroundColor)
        canvas?.drawRect(progressRect, progressRectPaint)
        currentText?.let {
            textPaint.getTextBounds(it, 0, it.length, textBounds)
            canvas?.drawText(
                it,
                (widthSize / 2).toFloat(),
                heightSize / 2 - textBounds.exactCenterY(),
                textPaint
            )
        }
        val startArc = (widthSize / 2 + textBounds.exactCenterX()) + textBounds.height() / 2
        canvas?.drawArc(
            startArc,
            (heightSize / 2 - textBounds.height() / 2).toFloat(),
            startArc + textBounds.height(),
            (heightSize / 2 + textBounds.height() / 2).toFloat(),
            0F,
            progressCircleAngle,
            true,
            progressCirclePaint
        )
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    }

    override fun performClick(): Boolean {
        super.performClick()
        buttonState = ButtonState.Clicked
        invalidate()
        return true
    }
}