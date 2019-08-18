package io.pixsight.scenemanager

import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import androidx.core.util.Pair
import io.pixsight.scenemanager.animations.AnimationAdapter
import io.pixsight.scenemanager.animations.ScenesParams
import io.pixsight.scenemanager.annotations.Scene
import java.util.*

/**
 * Contains data about a [io.pixsight.scenemanager.annotations.SceneManager]
 */
internal class ScenesMeta {
    var sceneAnimationAdapter: AnimationAdapter<ScenesParams>
        private set
    var scenesIdsToViews: SparseArray<MutableList<View>>
        private set
    var listener: Listener? = null
        private set
    var scenesParams: ScenesParams? = null
        private set
    var currentSceneId = Integer.MIN_VALUE

    constructor(root: ViewGroup,
                sceneAnimationAdapter: AnimationAdapter<ScenesParams>,
                scenes: Array<out Scene>,
                listener: Listener?) {
        this.sceneAnimationAdapter = sceneAnimationAdapter
        this.listener = listener
        scenesIdsToViews = SparseArray()
        for (i in scenes.indices) {
            val sceneId = scenes[i].scene
            var list = scenesIdsToViews.get(sceneId)
            if (list == null) {
                list = ArrayList()
                assertValidScene(sceneId, list)
                scenesIdsToViews.put(sceneId, list)
            }
            list.add(root.getChildAt(i))
        }
        scenesParams = this.sceneAnimationAdapter.generateScenesParams(scenesIdsToViews)
    }

    constructor(sceneAnimationAdapter: AnimationAdapter<ScenesParams>,
                scenesIds: MutableList<Pair<Int, View>>,
                listener: Listener?) {
        this.sceneAnimationAdapter = sceneAnimationAdapter
        this.listener = listener
        scenesIdsToViews = SparseArray()
        for (pair in scenesIds) {
            var list: MutableList<View>? = scenesIdsToViews.get(pair.first!!)
            if (list == null) {
                list = ArrayList()
                assertValidScene(pair.first!!, list)
                scenesIdsToViews.put(pair.first!!, list)
            }
            list.add(pair.second!!)
        }
        scenesParams = this.sceneAnimationAdapter.generateScenesParams(scenesIdsToViews)
    }

    @Suppress("UNUSED_PARAMETER")
    private fun assertValidScene(sceneId: Int, views: List<View>) {
        if (sceneId == Integer.MIN_VALUE) {
            throw RuntimeException("Invalid scene id, do not use Integer.MIN_VALUE.")
        }
    }
}
