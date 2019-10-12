package io.pixsight.scenemanager

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout


class InflateOnDemandLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    var useChildLayoutParams: Boolean = true
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
            this.useChildLayoutParams = a.getBoolean(
                R.styleable.InflateOnDemandLayout_useChildLayoutParams,
                this.useChildLayoutParams
            )
            a.recycle()
        }

        if (isInEditMode) {
            // We can't modify the hierarchy of the preview, so we simply inflate the layout.
            LayoutInflater.from(context).inflate(layoutId, this, true)
        }
    }

    fun inflate(): View? {
        if (childCount == 0) {
            if (parent == null || parent !is ViewGroup) {
                return null // ignore
            }
            if (layoutId == 0) {
                throw RuntimeException("InflateOnDemandLayout must have a valid layout resource")
            }
            val view = LayoutInflater.from(context).inflate(layoutId, this, false)

            val parent = (parent as ViewGroup)
            val index = parent.indexOfChild(this)
            parent.removeViewInLayout(this)

            if (useChildLayoutParams || layoutParams == null) {
                parent.addView(view, index)
            } else {
                parent.addView(view, index, layoutParams)
            }
            return view
        }
        return null
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