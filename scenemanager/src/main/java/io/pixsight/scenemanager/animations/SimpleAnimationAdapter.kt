package io.pixsight.scenemanager.animations

import android.util.SparseArray
import android.view.View
import androidx.core.util.forEach
import io.pixsight.scenemanager.SceneListener

import io.pixsight.scenemanager.annotations.Scene

/**
 * [SimpleAnimationAdapter] can be used to override animations.
 */
abstract class SimpleAnimationAdapter<T : ScenesParams> : AnimationAdapter<T> {

    /**
     * Called once when the scenes are created.
     */
    override fun generateScenesParams(scenes: SparseArray<MutableList<View>>): T? = null

    /**
     * Called when a scene has to be displayed.
     *
     * @param view The view holding a scene. See [Scene.layout]
     * @param animate true if you can animate the transition.
     * false if an animation is not recommended. Ex: When the default scene is
     * displayed.
     * @param sceneId The sceneId of the view to animate
     * @param listener The listener to pass to [notifyAnimationEnd] once the animation has ended
     */
    abstract fun showView(view: View,
                                   params: T?,
                                   animate: Boolean,
                                   sceneId: Int,
                                   listener: SceneListener?)

    /**
     * Called when a scene has to be hidden.
     *
     * @param view The view holding a scene. See [Scene.layout]
     * @param animate true if you can animate the transition.
     * false if an animation is not recommended. Ex: When the default scene is
     * displayed.
     * @param sceneId The sceneId of the view to animate
     * @param listener The listener to pass to [notifyAnimationEnd] once the animation has ended
     */
    abstract fun hideView(view: View,
                                   params: T?,
                                   animate: Boolean,
                                   sceneId: Int,
                                   listener: SceneListener?)

    override fun doChangeScene(
        scenesIdsToViews: SparseArray<MutableList<View>>,
        scenesParams: T?,
        sceneId: Int,
        animate: Boolean,
        listener: SceneListener?
    ) {
        // current scene's views are displayed even if present in another scene
        val forceShowIfHidden = scenesIdsToViews.get(sceneId)

        scenesIdsToViews.forEach { viewSceneId, views ->
            // Notify listener
            val isNewScene = viewSceneId == sceneId
            if (isNewScene) {
                listener?.onSceneDisplaying(viewSceneId)
            } else {
                listener?.onSceneHiding(viewSceneId)
            }

            // do change scene
            showOrHideScene(
                views,
                viewSceneId == sceneId,
                forceShowIfHidden,
                listener,
                scenesParams,
                animate,
                viewSceneId
            )
        }
    }

    private fun showOrHideScene(
        views: MutableList<View>,
        show: Boolean,
        forceShowIfHidden: MutableList<View>?,
        listener: SceneListener?,
        scenesParams: T?,
        animate: Boolean,
        viewSceneId: Int
    ) {
        views.forEach { view ->
            if (!show && forceShowIfHidden != null && forceShowIfHidden.contains(view)) {
                return@forEach
            }
            // notify the listener only on the last view
            val endListener = if (views.last() == view) listener else null
            showOrHideView(show, view, scenesParams, animate, viewSceneId, endListener)
        }
    }

    private fun showOrHideView(
        show: Boolean,
        view: View,
        scenesParams: T?,
        animate: Boolean,
        sceneId: Int,
        listener: SceneListener?
    ) {
        view.clearAnimation()
        if (show) {
            showView(view, scenesParams, animate, sceneId, listener)
        } else {
            hideView(view, scenesParams, animate, sceneId, listener)
        }
    }

    protected fun notifyAnimationEnd(isDisplayed: Boolean, sceneId: Int, listener: SceneListener?) {
        if (isDisplayed) {
            listener?.onSceneDisplayed(sceneId)
        } else {
            listener?.onSceneHidden(sceneId)
        }
    }

    override fun onViewInflatedOnDemand(sceneId: Int, view: View) {
        hideView(view, null, false, sceneId, null)
    }
}
