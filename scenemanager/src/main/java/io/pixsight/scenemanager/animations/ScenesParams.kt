package io.pixsight.scenemanager.animations

import android.util.SparseArray
import android.view.View

abstract class ScenesParams protected constructor(protected val scenes: SparseArray<MutableList<View>>)
