package io.pixsight.scenemanager.annotations

import androidx.annotation.IdRes
import androidx.annotation.LayoutRes

/**
 *
 * Create a scene by providing an id [Scene.id]
 * and a layout [Scene.layout].
 *
 *
 * This annotation can be used with [BuildScenes].
 */
@Target(AnnotationTarget.ANNOTATION_CLASS)
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class Scene(
    /**
     * The unique identifier associated to this scene.
     */
    val id: Int,
    /**
     * The [LayoutRes] that will be inflated for this scene
     */
    @LayoutRes val layout: Int = 0,
    @IdRes vararg val viewIds: Int = []
) {

    companion object {
        /**
         *
         * Those constants can be reused by your application for [Scene.id].
         * You can of course also use your own ids.
         *
         *
         * Do not use the value [Integer.MIN_VALUE] for a scene id.
         * This value is already used internally.
         */
        const val MAIN = 0x420
        const val SPINNER = 0x421
        const val PLACEHOLDER = 0x422
        const val SUCCESS = 0x423
        const val ERROR = 0x424
        const val EMPTY = 0x425
        const val EMPTY_PLACEHOLDER = 0x426
        const val LOADING = 0x427
        const val RETRY = 0x428
        const val UNKNOWN = 0x429
        const val CAMERA = 0x40A
        internal const val NONE = Int.MIN_VALUE
    }
}
