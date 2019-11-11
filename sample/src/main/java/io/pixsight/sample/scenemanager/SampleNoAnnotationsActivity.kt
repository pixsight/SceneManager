package io.pixsight.sample.scenemanager

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import io.pixsight.scenemanager.SceneCreator
import io.pixsight.scenemanager.SceneListener
import io.pixsight.scenemanager.SceneManager
import io.pixsight.scenemanager.annotations.Scene
import kotlinx.android.synthetic.main.activity_no_annotations_sample.*

class SampleNoAnnotationsActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_no_annotations_sample)

        SceneManager.create(
            SceneCreator.with(this)
                .add(Scene.MAIN, R.id.activity_no_annotations_sample_main_content)
                .add(Scene.MAIN, R.id.activity_no_annotations_sample_main_content_another_view)
                .add(Scene.SPINNER, R.id.activity_no_annotations_sample_main_content_another_view)
                .add(Scene.SPINNER, R.id.activity_no_annotations_sample_loader)
                .add(Scene.PLACEHOLDER, R.id.activity_no_annotations_sample_placeholder)
                .addScene(Scene.UNKNOWN, Scene.MAIN)
                .first(Scene.MAIN)
                //.animation(SceneAnimations.TRANSLATE_X)
                .listener(object : SceneListener {
                    override fun onSceneHiding(sceneId: Int) {
                        Log.d("SceneListener", "onSceneHiding $sceneId")
                    }

                    override fun onSceneHidden(sceneId: Int) {
                        Log.d("SceneListener", "onSceneHidden $sceneId")
                    }

                    override fun onSceneDisplaying(sceneId: Int) {
                        Log.d("SceneListener", "onSceneDisplaying $sceneId")
                    }

                    override fun onSceneDisplayed(sceneId: Int) {
                        Log.d("SceneListener", "onSceneDisplayed $sceneId")
                    }
                })
        )

        activity_no_annotations_sample_main_content.setOnClickListener(this)
        activity_no_annotations_sample_loader.setOnClickListener(this)
        activity_no_annotations_sample_placeholder.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.activity_no_annotations_sample_main_content ->
                SceneManager.scene(this, Scene.SPINNER)
            R.id.activity_no_annotations_sample_loader ->
                SceneManager.scene(this, Scene.PLACEHOLDER)
            R.id.activity_no_annotations_sample_placeholder ->
                SceneManager.scene(this, Scene.UNKNOWN) // Scene.UNKNOWN is copied from Scene.MAIN
            else -> throw IllegalArgumentException("Invalid view id")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        SceneManager.release(this)
    }

    companion object {

        fun startActivity(context: Context) {
            context.startActivity(Intent(context, SampleNoAnnotationsActivity::class.java))
        }
    }
}
