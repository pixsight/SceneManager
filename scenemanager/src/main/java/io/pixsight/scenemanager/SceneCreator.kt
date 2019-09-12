@file:Suppress("DEPRECATION")

package io.pixsight.scenemanager

import android.app.Activity
import android.app.Fragment
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import io.pixsight.scenemanager.animations.AnimationAdapter
import io.pixsight.scenemanager.animations.ScenesParams

/**
 *
 * This [SceneCreator] allows to create scenes from an existing layout.
 *
 *
 * Example:
 * SceneManager.create(
 * SceneCreator.with(this)
 * .add(Scene.MAIN, R.id.activity_no_annotations_sample_main_content)
 * .add(Scene.SPINNER, R.id.activity_no_annotations_sample_loader)
 * .add(Scene.PLACEHOLDER, R.id.activity_no_annotations_sample_placeholder)
 * .main(Scene.MAIN)
 * );
 */
class SceneCreator private constructor(
    internal val reference: Any,
    private val rootView: ViewGroup
) {
    internal var listener: SceneListener? = null
        private set
    internal var adapter: AnimationAdapter<ScenesParams>? = null
        private set
    internal var firstSceneId: Int = -1
        private set
    internal val scenes: MutableList<Pair<Int, View>> = mutableListOf()

    /**
     * Change the animation adapter.
     *
     * @param adapter the new animation adapter
     * @return a [SceneCreator] for more configurations.
     */
    fun animation(adapter: AnimationAdapter<out ScenesParams>?): SceneCreator {
        @Suppress("UNCHECKED_CAST")
        this.adapter = adapter as AnimationAdapter<ScenesParams>
        return this
    }

    /**
     * Add a listener. See [SceneListener].
     *
     * @param listener the new listener
     * @return a [SceneCreator] for more configurations.
     */
    fun listener(listener: SceneListener?): SceneCreator {
        this.listener = listener
        return this
    }

    /**
     * Change the default view.
     *
     * @param defaultSceneId the default sceneId.
     * @return a [SceneCreator] for more configurations.
     */
    fun first(defaultSceneId: Int): SceneCreator {
        firstSceneId = defaultSceneId
        return this
    }

    /**
     * Create a new scene by providing a view and sceneId.
     *
     * @param sceneId the new sceneId
     * @param view the view associated the the sceneId.
     * @return a [SceneCreator] for more configurations.
     */
    fun add(sceneId: Int, view: View): SceneCreator {
        scenes.add(Pair(sceneId, view))
        return this
    }

    /**
     * Create a new scene by providing a view id and sceneId.
     *
     * @param sceneId the new sceneId
     * @param idRes the view id associated the the sceneId.
     * @return a [SceneCreator] for more configurations.
     */
    fun add(sceneId: Int, @IdRes idRes: Int): SceneCreator {
        return add(sceneId, rootView.findViewById<View>(idRes))
    }

    companion object {

        /**
         * Setup by using an Object. The view classes or view ids added by
         * [SceneCreator.add] and [SceneCreator.add]
         * will be searched into the parameter rootView.
         *
         * @param reference the reference that will be used to change scene.
         * @param rootView the view group that holds the scenes anchors.
         *
         * @return a [SceneCreator] for more configurations.
         */
        fun with(reference: Any, rootView: ViewGroup): SceneCreator {
            return SceneCreator(
                reference,
                rootView
            )
        }

        /**
         * Setup by using an activity. The view classes or view ids added by
         * [SceneCreator.add] and [SceneCreator.add]
         * will be searched into the main layout of the activity.
         *
         * @param activity the reference activity which contains the children.
         * @return a [SceneCreator] for more configurations.
         */
        fun with(activity: Activity): SceneCreator {
            return SceneCreator(
                activity,
                (activity.findViewById<View>(android.R.id.content) as ViewGroup)
                    .getChildAt(0) as ViewGroup
            )
        }

        /**
         * Setup by using a [Fragment]. The view classes or view ids added by
         * [SceneCreator.add] and [SceneCreator.add]
         * will be searched into the main layout of the fragment.
         *
         * @param fragment the fragment which contains the children.
         * @return a [SceneCreator] for more configurations.
         */
        @Suppress("DEPRECATION")
        fun with(fragment: Fragment): SceneCreator {
            fragment.view ?: throw NullPointerException("fragment.getView() == null")
            return SceneCreator(
                fragment,
                (fragment.view as ViewGroup?)!!
            )
        }

        /**
         * Setup by using a [androidx.fragment.app.Fragment].
         * The view classes or view ids added by [SceneCreator.add]
         * and [SceneCreator.add] will be searched into
         * the main layout of the fragment.
         *
         * @param fragment the fragment which contains the children.
         * @return a [SceneCreator] for more configurations.
         */
        fun with(fragment: androidx.fragment.app.Fragment): SceneCreator {
            fragment.view ?: throw NullPointerException("fragment.getView() == null")
            return SceneCreator(
                fragment,
                (fragment.view as ViewGroup?)!!
            )
        }

        /**
         * Setup by using a [ViewGroup].
         * The view classes or view ids added by [SceneCreator.add]
         * and [SceneCreator.add] will be searched into
         * the provided view group.
         *
         * @param viewGroup the fragment which contains the children.
         * @return a [SceneCreator] for more configurations.
         */
        fun with(viewGroup: ViewGroup): SceneCreator {
            return SceneCreator(viewGroup, viewGroup)
        }
    }
}
