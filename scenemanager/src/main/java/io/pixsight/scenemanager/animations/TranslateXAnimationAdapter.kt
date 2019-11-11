package io.pixsight.scenemanager.animations

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.TimeInterpolator
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import androidx.core.util.forEach
import androidx.core.view.isVisible
import io.pixsight.scenemanager.SceneListener

open class TranslateXAnimationAdapter(
    private val interpolator: TimeInterpolator? = DecelerateInterpolator(),
    private val animationDuration: Int = 200
) : AnimationAdapter<TranslateScenesParams> {

    final override fun doChangeScene(
        scenesIdsToViews: SparseArray<MutableList<View>>,
        scenesParams: TranslateScenesParams?,
        sceneId: Int,
        animate: Boolean,
        listener: SceneListener?
    ) {
        scenesParams ?: throw NullPointerException("Parameter scenesParams is null")

        var shouldTranslate = animate
        var lastScenePosition = 0
        if (!scenesParams.hasValidLastSceneId()) {
            shouldTranslate = false
        } else {
            lastScenePosition = scenesParams.positionOf(scenesParams.lastSceneId)
        }
        val scenePosition = scenesParams.positionOf(sceneId)
        val currentSceneViews = scenesIdsToViews.get(sceneId)

        scenesIdsToViews.forEach { viewSceneId, views ->
            // do change scene
            // If the scene is not displayed nor to be animated
            if (viewSceneId != sceneId && viewSceneId != scenesParams.lastSceneId) {
                allGone(viewSceneId, listener, views, currentSceneViews)
                return@forEach
            }

            // Notify listener
            val isNewScene = viewSceneId == sceneId
            if (isNewScene) {
                listener?.onSceneDisplaying(viewSceneId)
            } else {
                listener?.onSceneHiding(viewSceneId)
            }

            // No animation
            if (!shouldTranslate) {
                showOrHideWithoutAnimations(viewSceneId, listener, isNewScene, views)
                return@forEach
            }

            // Animate each view if needed
            val isLeftToRight = scenePosition < lastScenePosition
            // If a view is in both scene, then we shouldn't animate
            val animatedViews = views.filter {
                !(scenesIdsToViews[scenesParams.lastSceneId].contains(it) &&
                        scenesIdsToViews[sceneId].contains(it))
            }
            animatedViews.forEach { view ->
                // notify the listener only on the last view
                val endListener = if (animatedViews.last() == view) listener else null
                if (isLeftToRight) {
                    // left to right
                    doLeftToRight(isNewScene, view, viewSceneId, endListener)
                } else {
                    // right to left
                    doRightToLeft(isNewScene, view, viewSceneId, endListener)
                }
            }
        }
        scenesParams.lastSceneId = sceneId
    }

    private fun showOrHideWithoutAnimations(sceneId: Int,
                                            listener: SceneListener?,
                                            isNewScene: Boolean,
                                            views: List<View>) {
        views.forEach { view ->
            if (isNewScene) {
                view.translationX = 0f
            }
            view.isVisible = isNewScene
        }
        notifyAnimationEnd(isNewScene, sceneId, listener)
    }

    open fun doLeftToRight(isNewScene: Boolean,
                           view: View,
                           sceneId: Int,
                           listener: SceneListener?) {
        view.clearAnimation()
        val parentWidth = (view.parent as ViewGroup).width
        if (isNewScene) {
            view.visibility = View.VISIBLE
            view.translationX = (-parentWidth).toFloat()

            view.animate()
                .translationX(0f)
                .setDuration(animationDuration.toLong())
                .setInterpolator(interpolator)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        notifyAnimationEnd(isNewScene, sceneId, listener)
                    }
                })
        } else {
            view.visibility = View.VISIBLE
            view.translationX = 0f

            view.animate()
                .translationX(parentWidth.toFloat())
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

    protected fun notifyAnimationEnd(isNewScene: Boolean, sceneId: Int, listener: SceneListener?) {
        if (isNewScene) {
            listener?.onSceneDisplayed(sceneId)
        } else {
            listener?.onSceneHidden(sceneId)
        }
    }

    open fun doRightToLeft(isNewScene: Boolean,
                           view: View,
                           sceneId: Int,
                           listener: SceneListener?) {
        view.clearAnimation()
        val parentWidth = (view.parent as ViewGroup).width
        if (isNewScene) {
            view.visibility = View.VISIBLE
            view.translationX = parentWidth.toFloat()

            view.animate()
                .translationX(0f)
                .setDuration(animationDuration.toLong())
                .setInterpolator(interpolator)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        notifyAnimationEnd(isNewScene, sceneId, listener)
                    }
                })
        } else {
            view.visibility = View.VISIBLE
            view.translationX = 0f

            view.animate()
                .translationX((-parentWidth).toFloat())
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

    private fun allGone(sceneId: Int,
                        listener: SceneListener?,
                        views: List<View>,
                        forceShowIfHidden: List<View>?) {
        listener?.onSceneHiding(sceneId)
        views.forEach { view ->
            if (forceShowIfHidden == null || !forceShowIfHidden.contains(view)) {
                view.visibility = View.GONE
            }
        }
        listener?.onSceneHidden(sceneId)
    }

    override fun onViewInflatedOnDemand(sceneId: Int, view: View) {
        view.visibility = View.GONE
    }

    override fun generateScenesParams(scenes: SparseArray<MutableList<View>>): TranslateScenesParams? {
        return TranslateScenesParams(scenes)
    }
}
