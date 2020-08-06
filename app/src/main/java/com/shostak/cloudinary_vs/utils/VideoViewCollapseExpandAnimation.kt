package com.shostak.cloudinary_vs.utils

import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.Transformation

object VideoViewCollapseExpandAnimation {

    fun expand(v: View, duration: Long = 0, onFinishListener: () -> Unit = {}) {
        v.clearAnimation()
        v.measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        val targtetHeight = v.measuredHeight
        val a = object : Animation() {
            override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
                if (v.visibility != View.VISIBLE && interpolatedTime > 0) {
                    v.visibility = View.VISIBLE
                }
                v.layoutParams.height = if (interpolatedTime == 1f)
                    200F.toPx(context = v.context).toInt()
                else
                    (targtetHeight * interpolatedTime).toInt()
                v.requestLayout()
            }

            override fun willChangeBounds(): Boolean {
                return true
            }
        }
        if (duration > 0) {
            a.duration = duration
        } else {
            a.duration =
                (targtetHeight / v.context.resources.displayMetrics.density).toInt().toLong()
        }
        a.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationEnd(animation: Animation?) {
                onFinishListener()
            }

            override fun onAnimationRepeat(animation: Animation?) {
            }

            override fun onAnimationStart(animation: Animation?) {
            }
        })
        v.startAnimation(a)
        v.tag = 1
    }

    fun collapse(v: View, duration: Long = 0, onFinishListener: () -> Unit = {}) {
        if (v.tag == 0) return
        v.clearAnimation()
        val initialHeight = v.measuredHeight

        val a = object : Animation() {
            override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
                if (interpolatedTime == 1f) {
                    v.visibility = View.GONE
                } else {
                    v.layoutParams.height =
                        initialHeight - (initialHeight * interpolatedTime).toInt()
                    v.requestLayout()
                }
            }

            override fun willChangeBounds(): Boolean {
                return true
            }
        }
        if (duration > 0) {
            a.duration = duration
        } else {
            a.duration =
                (initialHeight / v.context.resources.displayMetrics.density).toInt().toLong()
        }
        a.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationEnd(animation: Animation?) {
                onFinishListener()
            }

            override fun onAnimationRepeat(animation: Animation?) {
            }

            override fun onAnimationStart(animation: Animation?) {
            }
        })
        v.startAnimation(a)
        v.tag = 0
    }

}
