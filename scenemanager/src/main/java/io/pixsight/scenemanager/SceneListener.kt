package io.pixsight.scenemanager

/**
 * This listener can be used with [SceneManager] to listen
 * the events.
 */
interface SceneListener {

    /**
     * This method is called when the current scene change.
     *
     * @param sceneId the new scene that is displayed to the user.
     */
    fun onSceneChanged(sceneId: Int) = Unit

    /**
     * This method is called when a scene is hidden.
     *
     * @param sceneId the new scene that is has been hidden.
     */
    fun onSceneHidden(sceneId: Int) = Unit

    /**
     * This method is called when a scene is displayed.
     *
     * @param sceneId the new scene that is has been displayed.
     */
    fun onSceneDisplayed(sceneId: Int) = Unit
}