package io.pixsight.scenemanager.animations

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.View
import androidx.viewpager.widget.ViewPager
import io.pixsight.scenemanager.SceneListener

object SceneAnimations {

    /**
     * No animation.
     * The visibility changes from [View.VISIBLE] to [View.GONE].
     */
    val NO_ANIMATION: AnimationAdapter<ScenesParams> =
        object : SimpleAnimationAdapter<ScenesParams>() {

            override fun showView(view: View,
                                  params: ScenesParams?,
                                  animate: Boolean,
                                  sceneId: Int,
                                  listener: SceneListener?) {
                view.visibility = View.VISIBLE
                notifyAnimationEnd(true, sceneId, listener)
            }

            override fun hideView(view: View,
                                  params: ScenesParams?,
                                  animate: Boolean,
                                  sceneId: Int,
                                  listener: SceneListener?) {
                view.visibility = View.GONE
                notifyAnimationEnd(false, sceneId, listener)
            }
        }

    /**
     * Fade in or out and change the visibility from [View.VISIBLE] to [View.GONE].
     * This is the default animation adapter.
     */
    val FADE: AnimationAdapter<ScenesParams> = object : SimpleAnimationAdapter<ScenesParams>() {

        override fun showView(view: View,
                              params: ScenesParams?,
                              animate: Boolean,
                              sceneId: Int,
                              listener: SceneListener?) {
            if (animate) {
                view.animate()
                    .alpha(1f)
                    .setListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationStart(animation: Animator) {
                            view.visibility = View.VISIBLE
                        }

                        override fun onAnimationEnd(animation: Animator?) {
                            notifyAnimationEnd(true, sceneId, listener)
                        }
                    })
            } else {
                view.alpha = 1f
                view.visibility = View.VISIBLE
                notifyAnimationEnd(true, sceneId, listener)
            }
        }

        override fun hideView(view: View,
                              params: ScenesParams?,
                              animate: Boolean,
                              sceneId: Int,
                              listener: SceneListener?) {
            if (animate) {
                view.animate()
                    .alpha(0f)
                    .setListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator) {
                            view.visibility = View.INVISIBLE
                        }

                        override fun onAnimationEnd(animation: Animator?, isReverse: Boolean) {
                            notifyAnimationEnd(false, sceneId, listener)
                        }
                    })
            } else {
                view.alpha = 0f
                view.visibility = View.GONE
                notifyAnimationEnd(false, sceneId, listener)
            }
        }
    }

    /**
     * Fade in or out and call [View.setEnabled] on the views.
     * The visibility changes from [View.VISIBLE] to [View.INVISIBLE].
     */
    val ALPHA_ENABLE: AnimationAdapter<ScenesParams> =
        object : SimpleAnimationAdapter<ScenesParams>() {

            public override fun showView(view: View,
                                         params: ScenesParams?,
                                         animate: Boolean,
                                         sceneId: Int,
                                         listener: SceneListener?) {
                if (animate) {
                    view.animate()
                        .alpha(1f)
                        .setListener(object : AnimatorListenerAdapter() {
                            override fun onAnimationStart(animation: Animator) {
                                view.visibility = View.VISIBLE
                                view.isEnabled = true
                            }

                            override fun onAnimationEnd(animation: Animator?) {
                                notifyAnimationEnd(true, sceneId, listener)
                            }
                        })
                } else {
                    view.alpha = 1f
                    view.visibility = View.VISIBLE
                    view.isEnabled = true
                    notifyAnimationEnd(true, sceneId, listener)
                }
            }

            public override fun hideView(view: View,
                                         params: ScenesParams?,
                                         animate: Boolean,
                                         sceneId: Int,
                                         listener: SceneListener?) {
                if (animate) {
                    view.animate()
                        .alpha(0f)
                        .setListener(object : AnimatorListenerAdapter() {
                            override fun onAnimationEnd(animation: Animator) {
                                view.visibility = View.INVISIBLE
                                view.isEnabled = false
                            }

                            override fun onAnimationEnd(animation: Animator?, isReverse: Boolean) {
                                notifyAnimationEnd(false, sceneId, listener)
                            }
                        })
                } else {
                    view.alpha = 0f
                    view.visibility = View.INVISIBLE
                    view.isEnabled = false
                    notifyAnimationEnd(false, sceneId, listener)
                }
            }
        }

    /**
     * Translate the views like a [ViewPager].
     */
    @JvmStatic
    val TRANSLATE_X: AnimationAdapter<TranslateScenesParams> = TranslateXAnimationAdapter()

}
