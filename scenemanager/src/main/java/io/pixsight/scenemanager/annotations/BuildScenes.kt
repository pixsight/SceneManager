package io.pixsight.scenemanager.annotations

import androidx.fragment.app.Fragment

/**
 *
 * [BuildScenes] is used to define a list of [Scene] with a default scene
 * that will be displayed first.
 *
 *
 * [BuildScenes] can be used with an [android.app.Activity],
 * a [android.view.ViewGroup], a [android.app.Fragment]
 * or a [Fragment].
 *
 *
 * See [io.pixsight.scenemanager.SceneManager].
 */
@Target(AnnotationTarget.CLASS, AnnotationTarget.FILE)
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class BuildScenes(
        /**
         * The list of [Scene] that will be created.
         */
        vararg val value: Scene,
        /**
         * The default scene id. See [Scene.scene].
         * The [Scene] associated to this id will be the displayed first.
         */
        val first: Int = Scene.MAIN)