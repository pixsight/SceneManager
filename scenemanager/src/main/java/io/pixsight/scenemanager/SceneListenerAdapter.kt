package io.pixsight.scenemanager


/**
 * This is a dummy implementation of [Listener].
 * This adapter can be used to avoid to implements all methods.
 */
open class SceneManagerListenerAdapter : Listener {
    override fun onSceneChanged(sceneId: Int) {
        // nothing to do by default
    }

    override fun onSceneHidden(sceneId: Int) {
        // nothing to do by default
    }

    override fun onSceneDisplayed(sceneId: Int) {
        // nothing to do by default
    }
}
