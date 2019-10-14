package io.pixsight.scenemanager.animations

import android.util.SparseArray
import android.view.View
import io.pixsight.scenemanager.SceneListener
import io.pixsight.scenemanager.SceneManager

/**
 * [AnimationAdapter] can be used to override the
 * transition animations.
 */
interface AnimationAdapter<T : ScenesParams> {

    /**
     *
     * Called once at the beginning when the scenes are created.
     * The adapter is free to use [ScenesParams], it will be provided
     * to [.doChangeScene] at each call.
     *
     *
     * There is one [ScenesParams] per
     * [SceneManager.doCreate]
     */
    fun generateScenesParams(scenes: SparseArray<MutableList<View>>): T?

    /**
     *
     * This is called each time the current scene changes. This is where the animations are
     * done.
     *
     * @param scenesIdsToViews An [SparseArray] that links each scene id to
     * its associated views.
     * @param scenesParams The [ScenesParams] returned
     * by [.generateScenesParams] for these scenes.
     * @param sceneId The scene id to be displayed.
     * @param animate true if you can animate the transition.
     * false if an animation is not recommended. Ex: When the default scene is
     * displayed.
     */
    fun doChangeScene(
        scenesIdsToViews: SparseArray<MutableList<View>>,
        scenesParams: T?,
        sceneId: Int,
        animate: Boolean,
        listener: SceneListener?
    )

    /**
     * The animation adapter may want to change the default attribute (visibility, alpha...)
     * to avoid the view from blinking. (before doChangeScene is called)
     *
     * @param sceneId The scene id of the inflated view.
     * @param view The inflated view.
     */
    fun onViewInflatedOnDemand(sceneId: Int, view: View)
}
