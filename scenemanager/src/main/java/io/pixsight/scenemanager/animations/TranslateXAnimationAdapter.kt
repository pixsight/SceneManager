package io.pixsight.scenemanager.animations

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.TimeInterpolator
import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator

class TranslateXAnimationAdapter : AnimationAdapter<TranslateScenesParams> {

    private val dummyAnimationListener = object : AnimatorListenerAdapter() {}
    private var interpolator: TimeInterpolator? = null
    private var animationDuration: Int = 0

    internal constructor() {
        animationDuration = 200
        interpolator = DecelerateInterpolator()
    }

    constructor(interpolator: TimeInterpolator?,
                animationDuration: Int) {
        this.interpolator = interpolator
        this.animationDuration = animationDuration
    }

    override fun generateScenesParams(scenes: SparseArray<MutableList<View>>): TranslateScenesParams? {
        return TranslateScenesParams(scenes)
    }

    override fun doChangeScene(scenesIdsToViews: SparseArray<MutableList<View>>,
                               scenesParams: TranslateScenesParams?,
                               sceneId: Int,
                               animate: Boolean) {
        var shouldTranslate = animate
        if (scenesParams == null) {
            throw NullPointerException("Scenes params are null")
        }

        var lastScenePosition = 0
        if (!scenesParams.hasValidLastSceneId()) {
            shouldTranslate = false
        } else {
            lastScenePosition = scenesParams.positionOf(scenesParams.lastSceneId)
        }
        val scenePosition = scenesParams.positionOf(sceneId)
        val currentSceneViews = scenesIdsToViews.get(sceneId)

        for (i in 0 until scenesIdsToViews.size()) {
            // do change scene
            val viewSceneId = scenesIdsToViews.keyAt(i)
            val views = scenesIdsToViews.get(viewSceneId)

            // If the scene is not displayed nor to be animated
            if (viewSceneId != sceneId && viewSceneId != scenesParams.lastSceneId) {
                allGone(views, currentSceneViews)
                continue
            }

            val isNewScene = viewSceneId == sceneId
            if (!shouldTranslate) {
                showOrHideWithoutAnimations(isNewScene, views)
                continue
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

    private fun showOrHideWithoutAnimations(isNewScene: Boolean, views: List<View>) {
        for (view in views) {
            if (isNewScene) {
                view.translationX = 0f
                view.visibility = View.VISIBLE
            } else {
                view.visibility = View.GONE
            }
        }
    }

    private fun doLeftToRight(isNewScene: Boolean, views: List<View>) {
        for (view in views) {
            view.clearAnimation()
            val parentWidth = (view.parent as ViewGroup).width
            if (isNewScene) {
                if (view.visibility == View.GONE) {
                    view.visibility = View.VISIBLE
                    view.translationX = (-parentWidth).toFloat()
                }
                view.animate()
                        .translationX(0f)
                        .setDuration(animationDuration.toLong())
                        .setInterpolator(interpolator)
                        .setListener(dummyAnimationListener)
            } else {
                if (view.visibility == View.GONE) {
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
    }

    private fun doRightToLeft(isNewScene: Boolean, views: List<View>) {
        for (view in views) {
            view.clearAnimation()
            val parentWidth = (view.parent as ViewGroup).width
            if (isNewScene) {
                if (view.visibility == View.GONE) {
                    view.visibility = View.VISIBLE
                    view.translationX = parentWidth.toFloat()
                }
                view.animate()
                        .translationX(0f)
                        .setDuration(animationDuration.toLong())
                        .setInterpolator(interpolator)
                        .setListener(dummyAnimationListener)
            } else {
                if (view.visibility == View.GONE) {
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
    }

    private fun allGone(views: List<View>, forceShowIfHidden: List<View>?) {
        for (view in views) {
            if (forceShowIfHidden == null || !forceShowIfHidden.contains(view)) {
                view.visibility = View.GONE
            }
        }
    }
}
