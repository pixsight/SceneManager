package io.pixsight.sample.scenemanager

import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentActivity
import io.pixsight.scenemanager.SceneManager
import io.pixsight.scenemanager.annotations.BuildScenes
import io.pixsight.scenemanager.annotations.Scene
import kotlinx.android.synthetic.main.hidden.*
import kotlinx.android.synthetic.main.placeholder.*
import kotlinx.android.synthetic.main.sample_activity_main.*
import kotlinx.android.synthetic.main.sample_activity_main.sample_switch_to_main
import kotlinx.android.synthetic.main.sample_activity_main.sample_switch_to_placeholder
import kotlinx.android.synthetic.main.sample_activity_main.sample_switch_to_progress
import kotlinx.android.synthetic.main.sample_view_main.*
import kotlinx.android.synthetic.main.spinner.*

@BuildScenes(
    Scene(id = Scene.MAIN, layout = R.layout.sample_activity_main),
    Scene(id = Scene.MAIN, layout = R.layout.sample_activity_main_second_anchor),
    Scene(id = Scene.SPINNER, layout = R.layout.spinner),
    Scene(id = Scene.PLACEHOLDER, layout = R.layout.placeholder),
    Scene(id = SampleActivity.SAMPLE_WITH_VIEW, layout = R.layout.sample_with_view),
    Scene(id = SampleActivity.SAMPLE_HIDDEN, layout = R.layout.hidden),
    inflateOnDemand = true,
    first = Scene.MAIN
)
class SampleActivity : FragmentActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SceneManager.create(this)

        bindListeners()
    }

    private fun bindListeners() {
        sample_switch_to_main?.setOnClickListener(this)
        sample_switch_to_progress?.setOnClickListener(this)
        go_to_main_from_loader?.setOnClickListener(this)
        sample_switch_to_placeholder?.setOnClickListener(this)
        go_to_main_from_placeholder?.setOnClickListener(this)
        sample_switch_to_custom_view?.setOnClickListener(this)
        sample_switch_to_activity?.setOnClickListener(this)
        sample_switch_to_fragment?.setOnClickListener(this)
        sample_switch_to_fragment_v4?.setOnClickListener(this)
        sample_switch_to_existing_layout?.setOnClickListener(this)
        sample_switch_to_bind_scenes?.setOnClickListener(this)
        sample_switch_to_iod?.setOnClickListener(this)
        sample_hide?.setOnClickListener(this)
        restore_from_hidden_bt?.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.sample_switch_to_main,
            R.id.sample_switch_to_activity,
            R.id.go_to_main_from_loader,
            R.id.restore_from_hidden_bt,
            R.id.go_to_main_from_placeholder ->
                SceneManager.scene(this, Scene.MAIN)
            R.id.sample_switch_to_progress -> SceneManager.scene(this, Scene.SPINNER)
            R.id.sample_switch_to_placeholder -> SceneManager.scene(this, Scene.PLACEHOLDER)
            R.id.sample_switch_to_custom_view -> SceneManager.scene(this, SAMPLE_WITH_VIEW)
            R.id.sample_switch_to_fragment ->
                @Suppress("DEPRECATION")
                SampleFragment.newInstance().show(fragmentManager, "SampleFragment")
            R.id.sample_switch_to_fragment_v4 ->
                SampleFragmentV4.newInstance().show(supportFragmentManager, "SampleFragmentv4")
            R.id.sample_switch_to_existing_layout ->
                SampleNoAnnotationsActivity.startActivity(this)
            R.id.sample_switch_to_bind_scenes ->
                SampleBindScenesActivity.startActivity(this)
            R.id.sample_switch_to_iod ->
                SampleInflateOnDemandActivity.startActivity(this)
            R.id.sample_hide -> {
                SceneManager.scene(
                    this,
                    SAMPLE_HIDDEN,
                    true
                ) // load a scene with a button so you can come back
                SceneManager.hide(this, true) // all is now hidden
                SceneManager.restore(this, true) // restore to SAMPLE_HIDDEN
            }
            else -> throw IllegalArgumentException("Nope")
        }

        bindListeners()
    }

    override fun onDestroy() {
        super.onDestroy()
        SceneManager.release(this)
    }

    companion object {
        const val SAMPLE_WITH_VIEW = 3
        const val SAMPLE_HIDDEN = 4
    }
}
