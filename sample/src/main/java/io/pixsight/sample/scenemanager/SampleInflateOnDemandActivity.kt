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
import kotlinx.android.synthetic.main.main.*
import kotlinx.android.synthetic.main.placeholder.*
import kotlinx.android.synthetic.main.spinner.*

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
    inflateOnDemand = true
)
class SampleInflateOnDemandActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inflate_on_demand_sample)
        SceneManager.create(
            SceneCreator.with(this)
                .first(Scene.MAIN)
                .animation(SceneAnimations.TRANSLATE_X)
                .listener(object : SceneListener {
                    override fun onSceneChanged(sceneId: Int) {
                        Log.d("SceneListener", "New scene $sceneId")
                    }
                })
        )

        bindListeners()
    }

    private fun bindListeners() {
        sample_main?.setOnClickListener(this)
        go_to_main_from_loader?.setOnClickListener(this)
        go_to_main_from_placeholder?.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.sample_main ->
                SceneManager.scene(this, Scene.SPINNER)
            R.id.go_to_main_from_loader ->
                SceneManager.scene(this, Scene.PLACEHOLDER)
            R.id.go_to_main_from_placeholder ->
                SceneManager.scene(this, Scene.MAIN)
            else -> throw IllegalArgumentException("Invalid view id")
        }

        bindListeners()
    }

    override fun onDestroy() {
        super.onDestroy()
        SceneManager.release(this)
    }

    companion object {

        fun startActivity(context: Context) {
            context.startActivity(Intent(context, SampleInflateOnDemandActivity::class.java))
        }
    }
}