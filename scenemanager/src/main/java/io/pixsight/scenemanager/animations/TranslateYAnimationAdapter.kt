package io.pixsight.scenemanager.animations

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.TimeInterpolator
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import io.pixsight.scenemanager.SceneListener

class TranslateYAnimationAdapter(
    private val interpolator: TimeInterpolator? = DecelerateInterpolator(),
    private val animationDuration: Int = 200
) : TranslateXAnimationAdapter(interpolator, animationDuration) {

    override fun doLeftToRight(isNewScene: Boolean,
                              view: View,
                              sceneId: Int,
                              listener: SceneListener?) {
        view.clearAnimation()
        val parentHeight = (view.parent as ViewGroup).height
        if (isNewScene) {
            view.visibility = View.VISIBLE
            view.translationY = (-parentHeight).toFloat()

            view.animate()
                .translationY(0f)
                .setDuration(animationDuration.toLong())
                .setInterpolator(interpolator)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        notifyAnimationEnd(isNewScene, sceneId, listener)
                    }
                })
        } else {
            view.visibility = View.VISIBLE
            view.translationY = 0f

            view.animate()
                .translationY(parentHeight.toFloat())
                .setDuration(animationDuration.toLong())
                .setInterpolator(interpolator)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        view.visibility = View.GONE
                        notifyAnimationEnd(isNewScene, sceneId, listener)
                    }
                })
        }
    }

    override fun doRightToLeft(isNewScene: Boolean,
                              view: View,
                              sceneId: Int,
                              listener: SceneListener?) {
        view.clearAnimation()
        val parentHeight = (view.parent as ViewGroup).height
        if (isNewScene) {
            view.visibility = View.VISIBLE
            view.translationY = parentHeight.toFloat()

            view.animate()
                .translationY(0f)
                .setDuration(animationDuration.toLong())
                .setInterpolator(interpolator)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        notifyAnimationEnd(isNewScene, sceneId, listener)
                    }
                })
        } else {
            view.visibility = View.VISIBLE
            view.translationY = 0f

            view.animate()
                .translationY((-parentHeight).toFloat())
                .setDuration(animationDuration.toLong())
                .setInterpolator(interpolator)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        view.visibility = View.GONE
                        notifyAnimationEnd(isNewScene, sceneId, listener)
                    }
                })
        }
    }
}
