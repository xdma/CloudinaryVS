package com.shostak.cloudinary_vs.utils

import android.content.Context
import android.graphics.Point
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.dynamicanimation.animation.DynamicAnimation
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce
import kotlinx.android.synthetic.main.fragment_video_player_screen.*

/**
 * Created by Shostak Dima on 05/08/20.
 */
fun Float.toPx(context: Context) = (this * context.resources.displayMetrics.scaledDensity + 0.5F)

fun createSpringAnimation(
    view: View?,
    property: DynamicAnimation.ViewProperty,
    stiffnessVal: Float,
    dampingRatioVal: Float
): SpringAnimation {
    return SpringAnimation(view, property).setSpring(SpringForce().apply {
        stiffness = stiffnessVal
        dampingRatio = dampingRatioVal
    })
}


fun hideKeyboard(view: View) {
    val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}

fun requestFocusAndShowKeyboard(view: View) {
    view.requestFocus()
    view.post {
        val inputMethodManager =
            view.context
                .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(
            view,
            InputMethodManager.SHOW_IMPLICIT
        )
    }
}

fun Long.convertPositionToTimingString(): Array<String> {
    val minutes: String = (this / 1000 / 60).toString()
    var seconds: String = ("%.1f".format(this / 1000F % 60F))
    if (seconds.endsWith("."))
        seconds += "0"
    if (seconds.split(".")[0].length == 1)
        seconds = "0$seconds"

    val endPosition = this + 3000
    val minutesEnd: String = (endPosition / 1000 / 60).toString()
    var secondsEnd = ("%.1f".format(endPosition / 1000F % 60F))
    if (secondsEnd.endsWith("."))
        secondsEnd += "0"
    if (secondsEnd.split(".")[0].length == 1)
        secondsEnd = "0$secondsEnd"

    return arrayOf(
        "%s:%s".format(minutes, seconds),
        "%s:%s".format(minutesEnd, secondsEnd)
    )
}


