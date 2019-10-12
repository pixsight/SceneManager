package io.pixsight.scenemanager

/**
 * This listener can be used with [SceneManager] to listen
 * the events.
 */
interface SceneListener {

    /**
     * Triggered when the out animation of the given sceneId begin
     *
     * @param sceneId the new scene that is displayed to the user.
     */
    fun onSceneHiding(sceneId: Int) = Unit

    /**
     * Triggered when the out animation of the given sceneId finish
     *
     * @param sceneId the new scene that is displayed to the user.
     */
    fun onSceneHidden(sceneId: Int) = Unit

    /**
     * Triggered when the in animation of the given sceneId begin
     *
     * @param sceneId the new scene that is displayed to the user.
     */
    fun onSceneDisplaying(sceneId: Int) = Unit

    /**
     * Triggered when the in animation of the given sceneId finish
     *
     * @param sceneId the new scene that is displayed to the user.
     */
    fun onSceneDisplayed(sceneId: Int) = Unit
}