package io.pixsight.scenemanager.animations

import android.util.SparseArray
import android.view.View
import androidx.core.util.forEach
import io.pixsight.scenemanager.annotations.Scene

class TranslateScenesParams(
    scenes: SparseArray<MutableList<View>>,
    private val hideRtl: Boolean
) : ScenesParams(scenes) {
    private val scenesIds: MutableList<Int> = mutableListOf()
    var lastSceneId = Integer.MIN_VALUE

    init {
        scenes.forEach { viewSceneId, _ -> scenesIds.add(viewSceneId) }
        scenesIds.sort()
    }

    fun positionOf(sceneId: Int): Int {
        return if (sceneId == Scene.NONE) {
            if (hideRtl) (scenes.keyAt(scenes.size() -1) + 1) else -1
        } else {
            scenesIds
                .indexOfFirst { it == sceneId }
                .apply {
                    if (this == -1) throw RuntimeException("position not found")
                }
        }
    }

    fun hasValidLastSceneId(): Boolean {
        return lastSceneId != Integer.MIN_VALUE
    }
}
