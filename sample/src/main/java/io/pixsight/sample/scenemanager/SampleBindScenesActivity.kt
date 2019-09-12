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
import io.pixsight.scenemanager.animations.SceneAnimations
import io.pixsight.scenemanager.annotations.BuildScenes
import io.pixsight.scenemanager.annotations.Scene
import kotlinx.android.synthetic.main.activity_no_annotations_sample.*

@BuildScenes(
    value = [
        Scene(scene = Scene.MAIN, viewIds = intArrayOf(
            R.id.activity_no_annotations_sample_main_content,
            R.id.activity_no_annotations_sample_main_content_another_view
        )),
        Scene(scene = Scene.SPINNER, viewIds = intArrayOf(
            R.id.activity_no_annotations_sample_main_content_another_view,
            R.id.activity_no_annotations_sample_loader

        )),
        Scene(scene = Scene.PLACEHOLDER, viewIds = intArrayOf(
            R.id.activity_no_annotations_sample_placeholder
        ))
    ],
    first = Scene.PLACEHOLDER
)
class SampleBindScenesActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_no_annotations_sample)
        SceneManager.create(
            SceneCreator.with(this)
                .animation(SceneAnimations.TRANSLATE_X)
                .listener(object : SceneListener {
                    override fun onSceneChanged(sceneId: Int) {
                        Log.d("SceneListener", "New scene $sceneId")
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
                SceneManager.scene(this, Scene.MAIN)
            else -> throw IllegalArgumentException("Invalid view id")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        SceneManager.release(this)
    }

    companion object {

        fun startActivity(context: Context) {
            context.startActivity(Intent(context, SampleBindScenesActivity::class.java))
        }
    }
}
