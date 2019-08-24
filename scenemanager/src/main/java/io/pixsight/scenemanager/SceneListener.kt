package io.pixsight.scenemanager

/**
 * This listener can be used with [SceneManager] to listen
 * the events.
 */
interface SceneListener {

    /**
     * This method is called when the current scene is hidden.
     *
     * Note that this method is not called if there is no current scene set
     *
     * @param sceneId the scene that has been hidden.
     */
    fun onSceneHidden(sceneId: Int) = Unit

    /**
     * This method is called when a scene is displayed.
     *
     * @param sceneId the scene that has been displayed.
     */
    fun onSceneDisplayed(sceneId: Int) = Unit
}