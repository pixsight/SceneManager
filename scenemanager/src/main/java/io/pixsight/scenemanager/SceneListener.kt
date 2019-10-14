package io.pixsight.scenemanager

/**
 * This listener can be used with [SceneManager] to listen
 * the events.
 */
interface SceneListener {

    /**
     * Triggered when the out animation of the given sceneId begin
     *
     * @param sceneId the scene that is about to be hidden.
     */
    fun onSceneHiding(sceneId: Int) = Unit

    /**
     * Triggered when the out animation of the given sceneId finish
     *
     * @param sceneId the scene that has been hidden.
     */
    fun onSceneHidden(sceneId: Int) = Unit

    /**
     * Triggered when the in animation of the given sceneId begin
     *
     * @param sceneId the scene that is about to be displayed.
     */
    fun onSceneDisplaying(sceneId: Int) = Unit

    /**
     * Triggered when the in animation of the given sceneId finish
     *
     * @param sceneId the scene that has been displayed.
     */
    fun onSceneDisplayed(sceneId: Int) = Unit
}