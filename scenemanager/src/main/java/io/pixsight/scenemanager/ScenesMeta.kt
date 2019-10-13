package io.pixsight.scenemanager

import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import io.pixsight.scenemanager.animations.AnimationAdapter
import io.pixsight.scenemanager.animations.ScenesParams
import io.pixsight.scenemanager.annotations.Scene

/**
 * Contains data about a [io.pixsight.scenemanager.annotations.Scene]
 */
internal class ScenesMeta private constructor(
    val sceneAnimationAdapter: AnimationAdapter<ScenesParams>,
    val listener: SceneListener? = null,
    val inflateOnDemand: Boolean
) {
    val scenesIdsToViews: SparseArray<MutableList<View>> = SparseArray()
    var scenesParams: ScenesParams? = null
    var currentSceneId = Int.MIN_VALUE

    constructor(
        root: ViewGroup,
        sceneAnimationAdapter: AnimationAdapter<ScenesParams>,
        scenes: Array<out Scene>,
        listener: SceneListener?,
        inflateOnDemand: Boolean
    ) : this(sceneAnimationAdapter, listener, inflateOnDemand) {
        scenes.forEachIndexed { i, scene ->
            val sceneId = scene.scene
            var list = scenesIdsToViews.get(sceneId)
            if (list == null) {
                assertValidSceneId(sceneId)
                list = mutableListOf()
                scenesIdsToViews.put(sceneId, list)
            }
            list.add(root.getChildAt(i))
        }
        scenesParams = this.sceneAnimationAdapter.generateScenesParams(scenesIdsToViews)
    }

    constructor(
        sceneAnimationAdapter: AnimationAdapter<ScenesParams>,
        scenesIds: MutableList<Pair<Int, View>>,
        listener: SceneListener?,
        inflateOnDemand: Boolean
    ) : this(sceneAnimationAdapter, listener, inflateOnDemand) {
        scenesIds.forEach { (id, view) ->
            var list: MutableList<View>? = scenesIdsToViews.get(id)
            if (list == null) {
                assertValidSceneId(id)
                list = mutableListOf()
                scenesIdsToViews.put(id, list)
            }
            list.add(view)
        }
        scenesParams = this.sceneAnimationAdapter.generateScenesParams(scenesIdsToViews)
    }

    private fun assertValidSceneId(sceneId: Int) {
        if (sceneId == Int.MIN_VALUE) {
            throw RuntimeException("Invalid scene id, do not use Int.MIN_VALUE.")
        }
    }
}
