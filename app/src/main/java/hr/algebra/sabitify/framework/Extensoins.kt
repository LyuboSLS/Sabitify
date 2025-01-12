package hr.algebra.sabitify.framework

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import android.view.animation.AnimationUtils

fun View.applyAnimation(id: Int) {
    val anim = AnimationUtils.loadAnimation(
        context,
        id
    )
    startAnimation(anim)
}

inline fun <reified T : Activity> Context.startActivity() {
    startActivity(
        Intent(this, T::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
    )
}