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
        if (isInEditMode) {
            inflate()
        }
    }

    fun inflate(): View? {
        if (childCount == 0) {
            if (parent == null || parent !is ViewGroup) {
                return null // ignore
                //throw RuntimeException("InflateOnDemandLayout must have a non-null ViewGroup viewParent")
            }
            if (layoutId == 0) {
                throw RuntimeException("InflateOnDemandLayout must have a valid layout resource")
            }
            val view = LayoutInflater.from(context).inflate(layoutId, this, false)

            val parent = (parent as ViewGroup)
            val index = parent.indexOfChild(this)
            parent.removeViewInLayout(this)

            parent.addView(view, index)

            /*val layoutParams = layoutParams
            if (layoutParams != null) {
                parent.addView(view, index, layoutParams)
            } else {
                parent.addView(view, index)
            }*/
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