package io.pixsight.scenemanager

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout

class InflateOnDemandLayout(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    var layoutId: Int = 0

    init {
        if (attrs != null) {
            val a = context.obtainStyledAttributes(
                attrs,
                R.styleable.InflateOnDemandLayout,
                defStyleAttr,
                0
            )
            this.layoutId = a.getResourceId(
                R.styleable.InflateOnDemandLayout_layoutId,
                0
            )
            a.recycle()
        }
    }

    fun inflate(): View {
        if (childCount == 0) {
            if (layoutId == 0) {
                throw RuntimeException("Invalid layout id")
            }
            LayoutInflater.from(context).inflate(layoutId, this, true)
        }
        return getChildAt(0)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (childCount == 0) {
            setMeasuredDimension(0, 0)
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        if (childCount != 0) {
            super.onLayout(changed, left, top, right, bottom)
        }
    }
}