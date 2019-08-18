package io.pixsight.scenemanager.animations

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.View

/**
 * This helper provides a few static methods for the animations.
 */
internal object AnimationHelper {

    /**
     * Do a smooth fade animation to show a view.
     */
    fun showView(view: View) {
        view.animate()
                .alpha(1f)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationStart(animation: Animator) {
                        view.visibility = View.VISIBLE
                    }
                })
    }

    /**
     * Do a smooth fade animation to hide a view.
     */
    fun hideView(view: View) {
        view.animate()
                .alpha(0f)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        view.visibility = View.GONE
                    }
                })
    }
}// ignored - not instantiable
