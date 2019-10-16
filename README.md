# SceneManager-Android

A simple scene manager for Android.<br>
SceneManager allows to define scenes (sets of views) from your layout and switch between them using the animation of your choice.<br>
- You can use the available transition animations (fade, translate X, translate Y, etc..) or create your own (See [AnimationAdapter]).
- SceneManager can inflate your scenes on demand, this can be very useful to improve the performances of your app at launch.

```groovy
dependencies {
    implementation 'io.pixsight.scenemanager:scenemanager:1.0.3'
}
```

Example
=======
<img src="preview/video_sample.gif"  height="700">

Define your scenes - How to use with a SceneCreator (recommended way)
==========
You can use the **SceneCreator** with an activity, viewgroup or fragment.<br>
You have to inflate your viewgroup, fragment or call _setContentView(...)_ in your activity before using the **SceneCreator**.<br>

```kotlin
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
                .first(Scene.MAIN)
                .animation(SceneAnimations.TRANSLATE_X)
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

...
}
```
With a SceneCreator the scenes are registered by calling **.add** with an unique identifier and the id of the view/viewgroup that holds the scene.

Define your scenes - How to use with annotations only
==========
In this case, you have to provide an unique identifier (sceneId) and a valid layout resource for each scene.
The SceneManager will create the root layout of your activity, viewgroup or fragment. You don't need to call _setContentView(...)_ or its equivalent (see below).

```kotlin
@BuildScenes(
    Scene(id = Scene.MAIN, layout = R.layout.sample_activity_main),
    Scene(id = Scene.MAIN, layout = R.layout.sample_activity_main_second_anchor),
    Scene(id = Scene.SPINNER, layout = R.layout.spinner),
    Scene(id = Scene.PLACEHOLDER, layout = R.layout.placeholder),
    Scene(id = SampleActivity.SAMPLE_WITH_VIEW, layout = R.layout.sample_with_view),
    Scene(id = SampleActivity.SAMPLE_HIDDEN, layout = R.layout.hidden),
    first = Scene.MAIN
)
class SampleActivity : FragmentActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SceneManager.create(this)
        
        ...
    }
    ...
}
```

**Activities**: Just call _SceneManager.create(this);_. You don't need to call setContentView();<br>
**ViewGroup**: Just call _SceneManager.create(this);_. All scenes will be automatically created and added to your viewgroup.<br>
**Fragments**: With fragments _SceneManager.create(this)_ will returns the view that must be returned by **onCreateView**<br>

The class **Scene** provides a few scene's identifiers : _Scene.MAIN, Scene.SPINNER and Scene.PLACEHOLDER_, etc...<br>
You are free to use it or not, but be sure that each identifier is unique.


Define your scenes - Mix annotations and SceneCreator
==========
You can also declare your scenes using @BuildScenes and create your own layout (and call _setContentView(...)_ or its equivalent).
In this case you have to provide an unique identifier (sceneId) and the associated list of view ids for each scene.

```kotlin
@BuildScenes(
    value = [
        Scene(
            id = Scene.MAIN,
            viewIds = intArrayOf(
                R.id.activity_no_annotations_sample_main_content,
                R.id.activity_no_annotations_sample_main_content_another_view
            )
        ),
        Scene(
            id = Scene.SPINNER,
            viewIds = intArrayOf(
                R.id.activity_no_annotations_sample_main_content_another_view,
                R.id.activity_no_annotations_sample_loader

            )
        ),
        Scene(
            id = Scene.PLACEHOLDER,
            viewIds = intArrayOf(
                R.id.activity_no_annotations_sample_placeholder
            )
        )
    ]
)
class SampleInflateOnDemandActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inflate_on_demand_sample)
        
        SceneManager.create(
            SceneCreator.with(this)
                .first(Scene.MAIN)
                .animation(SceneAnimations.TRANSLATE_Y)
        )
```

Change the current scene
------------------------
You can easily switch between your scenes with **SceneManager.scene(this, int sceneId);**.<br>
-The first parameter must be the same object that was passed to _SceneManager.create(this);_ or _SceneCreator.with(this)_.<br>
-The second parameter is the unique identifier of the scene you want to display.

```java
...
class SampleActivity : AppCompatActivity(), View.OnClickListener {
    ...
    
override fun onClick(v: View) {
        when (v.id) {
            R.id.sample_switch_to_main -> SceneManager.scene(this, Scene.MAIN)
            R.id.sample_hide -> SceneManager.hide(this) // hide all scenes
            R.id.sample_restore -> SceneManager.restore(this) // undo SceneManager.hide(this)
        }
    }
}
```

Inflate on Demand
------------------------
Since the version 1.0.3, The SceneManager can inflate the views of a scene when this scene is displayed (when calling _SceneManager.scene(this, YOUR_SCENE_ID)_).
This feature can improve the performmances of your app at launch.

* If you are using **SceneCreator** just add _.inflateOnDemand(true)_
```kotlin
SceneManager.create(
            SceneCreator.with(this)
                ...
                .inflateOnDemand(true)
        )
```

* If you are using **@BuildScenes** just add _inflateOnDemand = true_
```kotlin
@BuildScenes(
    ...,
    inflateOnDemand = true
)
```

**!! If you have declared your own layout !!** (called _setContentView(...)_ or its equivalent)
Then, you have to replace the views to be inflated on demand by _InflateOnDemandLayout_
```xml
        <io.pixsight.scenemanager.InflateOnDemandLayout
            android:id="@+id/sample_inflate_on_demand_my_scene"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            app:layoutId="@layout/layout_my_scene_to_be_inflated_on_demand" />
```
The SceneManager will inflate the ressource provided by _app:layoutId_ and replace _InflateOnDemandLayout_ when the scene is displayed. An event will also be triggered to your _SceneListener_ if registered.

Release your scenes
------------------------
Don't forget to release your scenes to avoid a memory leak.

```kotlin
...
class SampleActivity : AppCompatActivity() {
    ...
    
    override fun onDestroy() {
        super.onDestroy()
        SceneManager.release(this)
    }
    ...
}
```

License
======
```
Copyright (C) 2019 PixSight

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
