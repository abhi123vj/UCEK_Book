package com.abhi.ucekbook

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import com.google.android.material.transition.platform.MaterialArcMotion
import com.google.android.material.transition.platform.MaterialContainerTransform
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback

class DownloadActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
        findViewById<View>(android.R.id.content).transitionName = "shared_element_container"
        setEnterSharedElementCallback(MaterialContainerTransformSharedElementCallback())
        window.sharedElementEnterTransition = MaterialContainerTransform().apply {
            addTarget(android.R.id.content)
            duration = 300L
            fadeProgressThresholds = MaterialContainerTransform.ProgressThresholds(0f, 1f)
        }
        window.sharedElementReturnTransition = MaterialContainerTransform().apply {
            addTarget(android.R.id.content)
            duration = 250L
            pathMotion = MaterialArcMotion()
            scrimColor = Color.TRANSPARENT
        }
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_download)
    }
}