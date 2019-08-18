package io.pixsight.scenemanager.animations

import android.util.Pair
import android.util.SparseArray
import android.view.View
import java.util.*

class TranslateScenesParams(scenes: SparseArray<MutableList<View>>) : ScenesParams(scenes) {
    private val scenesIdsToViews: MutableList<Pair<Int, MutableList<View>>>
    var lastSceneId = Integer.MIN_VALUE

    init {
        scenesIdsToViews = ArrayList()
        for (i in 0 until scenes.size()) {
            val viewSceneId = scenes.keyAt(i)
            val views = scenes.get(viewSceneId)

            scenesIdsToViews.add(Pair.create(viewSceneId, views))
        }

        scenesIdsToViews.sortWith(Comparator { o1, o2 -> o1.first - o2.first })
    }

    fun positionOf(sceneId: Int): Int {
        for (i in scenesIdsToViews.indices) {
            val scenesIdsToView = scenesIdsToViews[i]
            if (scenesIdsToView.first == sceneId) {
                return i
            }
        }
        throw RuntimeException("position not found")
    }

    fun hasValidLastSceneId(): Boolean {
        return lastSceneId != Integer.MIN_VALUE
    }
}
