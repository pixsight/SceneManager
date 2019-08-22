package io.pixsight.scenemanager.animations

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.View
import androidx.viewpager.widget.ViewPager

object SceneAnimations {

    /**
     * No animation.
     * The visibility changes from [View.VISIBLE] to [View.GONE].
     */
    val NO_ANIMATION: AnimationAdapter<ScenesParams> = object : SimpleAnimationAdapter<ScenesParams>() {

        override fun showView(view: View, params: ScenesParams?, animate: Boolean) {
            view.visibility = View.VISIBLE
        }

        override fun hideView(view: View, params: ScenesParams?, animate: Boolean) {
            view.visibility = View.GONE
        }
    }

    /**
     * Fade in or out and change the visibility from [View.VISIBLE] to [View.GONE].
     * This is the default animation adapter.
     */
    val FADE: AnimationAdapter<ScenesParams> = object : SimpleAnimationAdapter<ScenesParams>() {

        override fun showView(view: View, params: ScenesParams?, animate: Boolean) {
            if (animate) {
                AnimationHelper.showView(view)
            } else {
                view.alpha = 1f
                view.visibility = View.VISIBLE
            }
        }

        override fun hideView(view: View, params: ScenesParams?, animate: Boolean) {
            if (animate) {
                AnimationHelper.hideView(view)
            } else {
                view.alpha = 0f
                view.visibility = View.GONE
            }
        }
    }

    /**
     * Fade in or out and call [View.setEnabled] on the views.
     * The visibility changes from [View.VISIBLE] to [View.INVISIBLE].
     */
    val ALPHA_ENABLE: AnimationAdapter<ScenesParams> = object : SimpleAnimationAdapter<ScenesParams>() {

        public override fun showView(view: View, params: ScenesParams?, animate: Boolean) {
            if (animate) {
                view.animate()
                        .alpha(1f)
                        .setListener(object : AnimatorListenerAdapter() {
                            override fun onAnimationStart(animation: Animator) {
                                view.visibility = View.VISIBLE
                                view.isEnabled = true
                            }
                        })
            } else {
                view.alpha = 1f
                view.visibility = View.VISIBLE
                view.isEnabled = true
            }
        }

        public override fun hideView(view: View, params: ScenesParams?, animate: Boolean) {
            if (animate) {
                view.animate()
                        .alpha(0f)
                        .setListener(object : AnimatorListenerAdapter() {
                            override fun onAnimationEnd(animation: Animator) {
                                view.visibility = View.INVISIBLE
                                view.isEnabled = false
                            }
                        })
            } else {
                view.alpha = 0f
                view.visibility = View.INVISIBLE
                view.isEnabled = false
            }
        }
    }

    /**
     * Translate the views like a [ViewPager].
     */
    @JvmStatic
    val TRANSLATE_X: AnimationAdapter<TranslateScenesParams> = TranslateXAnimationAdapter()

}// Not instantiable
