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
        const val PLACEHOLDER = 0x423
        internal const val NONE = Int.MIN_VALUE
    }
}
