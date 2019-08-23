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

class TranslateXAnimationAdapter(
        private val interpolator: TimeInterpolator? = DecelerateInterpolator(),
        private val animationDuration: Int = 200
) : AnimationAdapter<TranslateScenesParams> {

    private val dummyAnimationListener = object : AnimatorListenerAdapter() {}

    override fun generateScenesParams(scenes: SparseArray<MutableList<View>>): TranslateScenesParams? {
        return TranslateScenesParams(scenes)
    }

    override fun doChangeScene(
            scenesIdsToViews: SparseArray<MutableList<View>>,
            scenesParams: TranslateScenesParams?,
            sceneId: Int,
            animate: Boolean
    ) {
        scenesParams ?: throw NullPointerException("Scenes params are null")

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
                allGone(views, currentSceneViews)
                return@forEach
            }

            val isNewScene = viewSceneId == sceneId
            if (!shouldTranslate) {
                showOrHideWithoutAnimations(isNewScene, views)
                return@forEach
            }

            if (scenePosition < lastScenePosition) {
                // left to right
                doLeftToRight(isNewScene, views)
            } else {
                // right to left
                doRightToLeft(isNewScene, views)
            }
        }
        scenesParams.lastSceneId = sceneId
    }

    private fun showOrHideWithoutAnimations(
            isNewScene: Boolean,
            views: List<View>
    ) = views.forEach { view ->
        if (isNewScene) {
            view.translationX = 0f
        }
        view.isVisible = isNewScene
    }

    private fun doLeftToRight(isNewScene: Boolean, views: List<View>) = views.forEach { view ->
        view.clearAnimation()
        val parentWidth = (view.parent as ViewGroup).width
        if (isNewScene) {
            if (!view.isVisible) {
                view.visibility = View.VISIBLE
                view.translationX = (-parentWidth).toFloat()
            }
            view.animate()
                    .translationX(0f)
                    .setDuration(animationDuration.toLong())
                    .setInterpolator(interpolator)
                    .setListener(dummyAnimationListener)
        } else {
            if (!view.isVisible) {
                view.visibility = View.VISIBLE
                view.translationX = 0f
            }
            view.animate()
                    .translationX(parentWidth.toFloat())
                    .setDuration(animationDuration.toLong())
                    .setInterpolator(interpolator)
                    .setListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator) {
                            view.visibility = View.GONE
                        }
                    })
        }
    }

    private fun doRightToLeft(isNewScene: Boolean, views: List<View>) = views.forEach { view ->
        view.clearAnimation()
        val parentWidth = (view.parent as ViewGroup).width
        if (isNewScene) {
            if (!view.isVisible) {
                view.visibility = View.VISIBLE
                view.translationX = parentWidth.toFloat()
            }
            view.animate()
                    .translationX(0f)
                    .setDuration(animationDuration.toLong())
                    .setInterpolator(interpolator)
                    .setListener(dummyAnimationListener)
        } else {
            if (!view.isVisible) {
                view.visibility = View.VISIBLE
                view.translationX = 0f
            }
            view.animate()
                    .translationX((-parentWidth).toFloat())
                    .setDuration(animationDuration.toLong())
                    .setInterpolator(interpolator)
                    .setListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator) {
                            view.visibility = View.GONE
                        }
                    })
        }
    }

    private fun allGone(views: List<View>, forceShowIfHidden: List<View>?) = views.forEach { view ->
        if (forceShowIfHidden == null || !forceShowIfHidden.contains(view)) {
            view.visibility = View.GONE
        }
    }
}
