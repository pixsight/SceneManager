package io.pixsight.scenemanager.animations

import android.util.SparseArray
import android.view.View
import androidx.core.util.forEach

class TranslateScenesParams(scenes: SparseArray<MutableList<View>>) : ScenesParams(scenes) {
    private val scenesIds: MutableList<Int> = mutableListOf()
    var lastSceneId = Integer.MIN_VALUE

    init {
        scenes.forEach { viewSceneId, _ -> scenesIds.add(viewSceneId) }
        scenesIds.sortDescending()
    }

    fun positionOf(sceneId: Int): Int = scenesIds.indexOfFirst { it == sceneId }.apply {
        if (this == -1) throw RuntimeException("position not found")
    }

    fun hasValidLastSceneId(): Boolean {
        return lastSceneId != Integer.MIN_VALUE
    }
}
