@file:Suppress("unused")

package io.pixsight.scenemanager

import android.app.Activity
import android.app.Fragment
import android.content.Context
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.util.forEach
import io.pixsight.scenemanager.animations.AnimationAdapter
import io.pixsight.scenemanager.animations.SceneAnimations
import io.pixsight.scenemanager.animations.ScenesParams
import io.pixsight.scenemanager.annotations.BuildScenes
import io.pixsight.scenemanager.annotations.Scene
import java.lang.ref.WeakReference
import java.util.*
import kotlin.collections.ArrayList

/**
 *
 * The [SceneManager] is used to initialize an [Activity],
 * [ViewGroup], [android.app.Fragment] or
 * [androidx.fragment.app.Fragment] annotated with [BuildScenes].
 *
 * [SceneManager] is also used to switch the scenes.
 */
object SceneManager {

    /**
     * A dictionary of [ScenesMeta] associated with their view, activity or fragment.
     */
    private val sScenesMeta = LinkedList<Pair<WeakReference<Any>, ScenesMeta>>()

    /**
     *
     * Parse the annotation [BuildScenes] of an Object
     * and creates the scenes.
     *
     * @param context the holding context used to inflate the views.
     * @param reference an object that has a [BuildScenes]
     */
    fun create(context: Context, reference: Any): ViewGroup {
        return doCreate(
            context,
            reference,
            SceneAnimations.FADE,
            FrameLayout(context), null
        )
    }

    /**
     *
     * Parse the annotation [BuildScenes] of an Object
     * and creates the scenes.
     *
     * @param context the holding context used to inflate the views.
     * @param reference an object that has a [BuildScenes]
     * @param adapter The [AnimationAdapter] to be used.
     */
    fun create(
        context: Context,
        reference: Any,
        adapter: AnimationAdapter<ScenesParams>?
    ): ViewGroup {
        return doCreate(
            context,
            reference,
            adapter,
            FrameLayout(context), null
        )
    }

    /**
     *
     * Parse the annotation [BuildScenes] of an [Activity]
     * and add the scenes into the [Activity].
     *
     *
     * This method will add each scene into [Activity] with
     * [Activity.setContentView] (View)}.
     *
     * @param activity an [Activity] that has a [BuildScenes]
     */
    fun create(activity: Activity): ViewGroup {
        val root = doCreate(
            activity,
            activity,
            SceneAnimations.FADE,
            FrameLayout(activity), null
        )
        activity.setContentView(root)
        return root
    }

    /**
     *
     * Parse the annotation [BuildScenes] of an [Activity]
     * and add the scenes into the [Activity].
     *
     *
     * This method will add each scene into [Activity] with
     * [Activity.setContentView] (View)}.
     *
     * @param activity an [Activity] that has a [BuildScenes]
     * @param adapter The [AnimationAdapter] to be used.
     */
    fun create(
        activity: Activity,
        adapter: AnimationAdapter<ScenesParams>?
    ): ViewGroup {
        val root = doCreate(activity, activity, adapter, FrameLayout(activity), null)
        activity.setContentView(root)
        return root
    }

    /**
     *
     * Parse the annotation [BuildScenes] of a [ViewGroup]
     * and add the scenes into the [ViewGroup].
     *
     *
     * This method will add each scene into [ViewGroup] with
     * [ViewGroup.addView].
     *
     * @param view a [ViewGroup] that has a [BuildScenes]
     */
    fun create(view: ViewGroup): ViewGroup {
        return doCreate(view.context, view, SceneAnimations.FADE, view, null)
    }

    /**
     *
     * Parse the annotation [BuildScenes] of a [ViewGroup]
     * and add the scenes into the [ViewGroup].
     *
     *
     * This method will add each scene into [ViewGroup] with
     * [ViewGroup.addView].
     *
     * @param view a [ViewGroup] that has a [BuildScenes]
     * @param adapter The [AnimationAdapter] to be used.
     */
    fun create(
        view: ViewGroup,
        adapter: AnimationAdapter<ScenesParams>?
    ): ViewGroup {
        return doCreate(view.context, view, adapter, view, null)
    }

    /**
     *
     * Parse the annotation [BuildScenes] of a [Fragment]
     * and returns the [ViewGroup] to returned by
     * [Fragment.onCreateView].
     *
     * @param fragment an [Fragment] that has a [BuildScenes]
     */
    @Suppress("DEPRECATION")
    fun create(fragment: Fragment): ViewGroup {
        return doCreate(
            fragment.activity,
            fragment,
            SceneAnimations.FADE,
            FrameLayout(fragment.activity),
            null
        )
    }

    /**
     *
     * Parse the annotation [BuildScenes] of a [Fragment]
     * and returns the [ViewGroup] to returned by
     * [Fragment.onCreateView]
     *
     * @param fragment an [Fragment] that has a [BuildScenes]
     * @param adapter The [AnimationAdapter] to be used.
     */
    @Suppress("DEPRECATION")
    fun create(
        fragment: Fragment,
        adapter: AnimationAdapter<ScenesParams>?
    ): ViewGroup {
        return doCreate(
            fragment.activity,
            fragment,
            adapter,
            FrameLayout(fragment.activity), null
        )
    }

    /**
     *
     * Parse the annotation [BuildScenes] of a [androidx.fragment.app.Fragment]
     * and returns the [ViewGroup] to returned by
     * [androidx.fragment.app.Fragment.onCreateView].
     *
     * @param fragment an [androidx.fragment.app.Fragment] that has a [BuildScenes]
     */
    fun create(fragment: androidx.fragment.app.Fragment): ViewGroup {
        return doCreate(
            fragment.activity!!,
            fragment,
            SceneAnimations.FADE,
            FrameLayout(fragment.activity!!), null
        )
    }

    /**
     *
     * Parse the annotation [BuildScenes] of a [androidx.fragment.app.Fragment]
     * and returns the [ViewGroup] to returned by
     * [androidx.fragment.app.Fragment.onCreateView]
     *
     * @param fragment an [androidx.fragment.app.Fragment] that has a [BuildScenes]
     * @param adapter The [AnimationAdapter] to be used.
     */
    fun create(
        fragment: androidx.fragment.app.Fragment,
        adapter: AnimationAdapter<ScenesParams>?
    ): ViewGroup {
        return doCreate(
            fragment.activity!!,
            fragment,
            adapter,
            FrameLayout(fragment.activity!!), null
        )
    }

    /**
     * Creates the scene manager by providing a [SceneCreator].
     *
     * @param creator a [SceneCreator]
     */
    fun create(creator: SceneCreator) {
        val setup = getBuildAnnotation(creator.reference)
        val scenes = setup?.value

        scenes?.forEach { scene ->
            scene.viewIds.forEach {
                creator.add(scene.scene, it)
            }
        }

        if (setup?.inflateOnDemand == true && !creator.inflateOnDemand) {
            creator.inflateOnDemand(true)
        }

        // Save the scene's meta data
        val adapter = creator.adapter
        sScenesMeta.add(
            Pair(
                WeakReference(creator.reference),
                ScenesMeta(
                    adapter ?: SceneAnimations.FADE,
                    creator.scenes,
                    creator.listener,
                    creator.inflateOnDemand
                )
            )
        )

        if (setup != null || creator.firstSceneId != -1) {
            doChangeScene(
                creator.reference,
                if (creator.firstSceneId == -1) setup!!.first else creator.firstSceneId,
                false
            )
        }
    }

    /**
     *
     * Release all reference linked with an [Activity].
     *
     * @param activity an [Activity] that has called [.create]
     */
    fun release(activity: Activity) {
        releaseMeta(activity)
    }

    /**
     *
     * Release all reference linked with an [androidx.fragment.app.Fragment].
     *
     * @param fragment an [Activity] that has called
     * [.create]
     */
    fun release(fragment: androidx.fragment.app.Fragment) {
        releaseMeta(fragment)
    }

    /**
     *
     * Release all reference linked with an [Fragment].
     *
     * @param fragment an [Activity] that has called [.create]
     */
    @Suppress("DEPRECATION")
    fun release(fragment: Fragment) {
        releaseMeta(fragment)
    }

    /**
     *
     * Release all reference linked with an [ViewGroup].
     *
     * @param view an [Activity] that has called [.create]
     */
    fun release(view: ViewGroup) {
        releaseMeta(view)
    }

    /**
     * @param reference The reference to manage all scenes, it can be a [ViewGroup],
     * [androidx.fragment.app.Fragment], [Fragment],
     * [Activity] or a custom reference if you know what you are doing.
     * @return The current scene if linked to the provided reference or [Integer.MIN_VALUE].
     */
    fun current(reference: Any): Int {
        val meta = safeGetMetaData(reference) ?: return Integer.MIN_VALUE
        return meta.currentSceneId
    }

    private fun releaseMeta(reference: Any) {
        val node = safeGetMetaPair(reference)
        if (node != null) {
            sScenesMeta.remove(node)
        }
    }

    /**
     * Parse the annotation [BuildScenes], of an reference.
     * Creates the scenes and add them into a view group.
     * Save the metadata of those scenes into [SceneManager.sScenesMeta].
     *
     * @param context Holding context for the inflater
     * @param reference The associated activity, view or fragments that has a [BuildScenes]
     * @param adapter The animation adapter to be used.
     * @param root The root view group in which the scenes will be added.
     *
     * @return The root view group that contains the scenes
     */
    private fun doCreate(
        context: Context,
        reference: Any,
        adapter: AnimationAdapter<ScenesParams>?,
        root: ViewGroup,
        listener: SceneListener?
    ): ViewGroup {
        var animationAdapter = adapter
        // Retrieve annotations
        val setup = safeGetBuildAnnotation(reference)
        val scenes = setup.value

        // Create root node with all scenes
        if (animationAdapter == null) {
            animationAdapter = SceneAnimations.FADE
        }
        val inflater = LayoutInflater.from(context)
        scenes.forEach { scene ->
            val view = if (setup.inflateOnDemand) {
                val onDemandView = InflateOnDemandLayout(root.context)
                onDemandView.layoutId = scene.layout
                onDemandView.id = scene.layout
                onDemandView
            } else {
                inflater.inflate(scene.layout, root, false)
            }
            root.addView(view)
        }

        // Save the scene's meta data
        val meta = ScenesMeta(root, animationAdapter, scenes, listener, setup.inflateOnDemand)
        sScenesMeta.add(
            Pair(
                WeakReference(reference),
                meta
            )
        )

        doChangeScene(
            reference,
            getValidFirstScene(setup, scenes),
            false
        )
        return root
    }

    /**
     * Switch to another [Scene].
     *
     * @param reference The reference.
     * @param scene The scene id. See [Scene.scene].
     */
    fun scene(reference: Any, scene: Int) {
        doChangeScene(reference, scene)
    }

    /**
     * Switch to another [Scene].
     *
     * @param reference The reference.
     * @param scene The scene id. See [Scene.scene].
     */
    fun scene(reference: Any, scene: Int, animate: Boolean) {
        doChangeScene(reference, scene, animate)
    }

    /**
     * Switch to another [Scene].
     *
     * @param activity The parent activity.
     * @param scene The scene id. See [Scene.scene].
     */
    fun scene(activity: Activity, scene: Int) {
        doChangeScene(activity, scene)
    }

    /**
     * Switch to another [Scene].
     *
     * @param view The holder view.
     * @param scene The scene id. See [Scene.scene].
     */
    fun scene(view: ViewGroup, scene: Int) {
        doChangeScene(view, scene)
    }

    /**
     * Switch to another [Scene].
     *
     * @param fragment The holder fragment.
     * @param scene The scene id. See [Scene.scene].
     */
    @Suppress("DEPRECATION")
    fun scene(fragment: Fragment, scene: Int) {
        doChangeScene(fragment, scene)
    }

    /**
     * Switch to another [Scene].
     *
     * @param fragment The holder fragment.
     * @param scene The scene id. See [Scene.scene].
     */
    fun scene(fragment: androidx.fragment.app.Fragment, scene: Int) {
        doChangeScene(fragment, scene)
    }

    /**
     * Switch to another [Scene].
     *
     * @param activity The parent activity.
     * @param scene The scene id. See [Scene.scene].
     */
    fun scene(activity: Activity, scene: Int, animate: Boolean) {
        doChangeScene(activity, scene, animate)
    }

    /**
     * Switch to another [Scene].
     *
     * @param view The holder view.
     * @param scene The scene id. See [Scene.scene].
     */
    fun scene(view: ViewGroup, scene: Int, animate: Boolean) {
        doChangeScene(view, scene, animate)
    }

    /**
     * Switch to another [Scene].
     *
     * @param fragment The holder fragment.
     * @param scene The scene id. See [Scene.scene].
     */
    @Suppress("DEPRECATION")
    fun scene(fragment: Fragment, scene: Int, animate: Boolean) {
        doChangeScene(fragment, scene, animate)
    }

    /**
     * Switch to another [Scene].
     *
     * @param fragment The holder fragment.
     * @param scene The scene id. See [Scene.scene].
     */
    fun scene(
        fragment: androidx.fragment.app.Fragment,
        scene: Int,
        animate: Boolean
    ) {
        doChangeScene(fragment, scene, animate)
    }

    private fun getValidFirstScene(setup: BuildScenes, scenes: Array<out Scene>): Int {
        val firstScene = setup.first
        // the default scene specified by the user is valid
        return scenes.firstOrNull { it.scene == firstScene }?.scene
        // the default scene is not valid
            ?: scenes[0].scene
    }

    private fun safeGetBuildAnnotation(obj: Any): BuildScenes {
        val objClass = obj.javaClass
        if (!objClass.isAnnotationPresent(BuildScenes::class.java)) {
            throw RuntimeException("Annotation @BuildScenes is missing")
        }
        return objClass.getAnnotation(BuildScenes::class.java)!!
    }

    private fun getBuildAnnotation(obj: Any): BuildScenes? {
        val objClass = obj.javaClass
        if (!objClass.isAnnotationPresent(BuildScenes::class.java)) {
            return null
        }
        return objClass.getAnnotation(BuildScenes::class.java)
    }

    private fun safeGetMetaData(obj: Any): ScenesMeta? {
        val node = safeGetMetaPair(obj)
        return node?.second
    }

    private fun safeGetMetaPair(obj: Any): Pair<WeakReference<Any>, ScenesMeta>? {
        return sScenesMeta.firstOrNull { (ref, _) -> ref.get() == obj }
    }

    private fun doChangeScene(obj: Any, sceneId: Int, animate: Boolean = true) {
        val meta = safeGetMetaData(obj) ?: return
        val scenesIdsToViews = meta.scenesIdsToViews

        // inflate on demand
        if (meta.inflateOnDemand) {
            val currentSceneViews = scenesIdsToViews.get(sceneId)
            val inflatedViews = ArrayList<Pair<Int, View>>()
            currentSceneViews.forEach {
                if (it is InflateOnDemandLayout) {
                    inflatedViews.add(Pair(it.id, it.inflate()!!))
                }
            }

            scenesIdsToViews.forEach { _, sceneViews ->
                inflatedViews.forEach { pair ->
                    if (sceneViews.removeAll { it.id == pair.first }) {
                        sceneViews.add(pair.second)
                    }
                }
            }

            if (inflatedViews.isNotEmpty()) {
                inflatedViews.first().second.post {
                    doChangeSceneAndNotify(meta, scenesIdsToViews, sceneId, animate)
                }
                return
            }
        }

        doChangeSceneAndNotify(meta, scenesIdsToViews, sceneId, animate)
    }

    private fun doChangeSceneAndNotify(
        meta: ScenesMeta,
        scenesIdsToViews: SparseArray<MutableList<View>>,
        sceneId: Int,
        animate: Boolean
    ) {
        // start animations
        meta.sceneAnimationAdapter
            .doChangeScene(scenesIdsToViews, meta.scenesParams, sceneId, animate)

        // notify listener
        meta.currentSceneId = sceneId
        notifyListener(scenesIdsToViews, sceneId, meta.listener)
    }

    private fun notifyListener(
        scenesIdsToViews: SparseArray<MutableList<View>>,
        sceneId: Int,
        listener: SceneListener?
    ) {
        listener ?: return // nothing to do if no listener
        scenesIdsToViews.forEach { viewSceneId, _ ->
            val show = viewSceneId == sceneId

            if (show) {
                listener.onSceneDisplayed(sceneId)
            } else {
                listener.onSceneHidden(sceneId)
            }
        }
        listener.onSceneChanged(sceneId)
    }
}
