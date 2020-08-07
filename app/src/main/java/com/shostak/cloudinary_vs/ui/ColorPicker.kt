package com.shostak.cloudinary_vs.ui

import android.animation.Animator
import android.animation.ObjectAnimator
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.view.animation.OvershootInterpolator
import android.widget.LinearLayout
import androidx.fragment.app.DialogFragment
import com.shostak.cloudinary_vs.R
import com.shostak.cloudinary_vs.utils.toPx


/**
 * Created by Dima Shostak, on 05/08/2020.
 */
class ColorPicker : DialogFragment() {

    var onSetColorListner: OnColorSetListener? = null
    private lateinit var colorsArray: Array<String>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.color_picker, container, false)
    }

    override fun onStart() {
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        val window = dialog?.window
        val wlp = window!!.attributes
        wlp.dimAmount = 0F
        wlp.flags = wlp.flags or WindowManager.LayoutParams.FLAG_DIM_BEHIND
        dialog?.window?.setGravity(Gravity.CENTER_HORIZONTAL or Gravity.BOTTOM)
        dialog!!.window
            ?.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.transparent)))
        window.attributes = wlp

        super.onStart()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        createPicker()

    }


    private fun createPicker() {
        colorsArray = requireContext().resources.getStringArray(R.array.color_picker)
        var main = LinearLayout(activity)
        main.orientation = LinearLayout.HORIZONTAL
        val lp =
            LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                60F.toPx(requireContext()).toInt()
            )
        main.layoutParams = lp
        //parent.addView(main);
        for (i in colorsArray.indices) {
            if (i % 6 == 0) {
                main = LinearLayout(activity)
                main.orientation = LinearLayout.HORIZONTAL
                main.layoutParams = lp
                view?.findViewById<LinearLayout>(R.id.parent)?.addView(main)
            }

            val vlp = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
                1f
            )
            val vlp2 = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )

            val view = View(requireContext())
            view.visibility = View.INVISIBLE

            val mainView = LinearLayout(activity)
            mainView.layoutParams = vlp
            mainView.setPadding(5, 5, 5, 5)
            view.layoutParams = vlp2

            val color = Color.parseColor(colorsArray[i])
            view.tag = color

            view.setBackgroundColor(color)
            mainView.addView(view)

            view.setOnClickListener { v ->
                onSetColorListner?.onColorSet(v.tag as Int)
                dismiss()
            }


            main.addView(mainView)

            val animation = ObjectAnimator.ofFloat(view, "rotationY", 0.0f, 180f)

            animation.duration = 1000
            animation.startDelay = i * 30.toLong()
            animation.interpolator = OvershootInterpolator()

            animation.addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {
                    view.visibility = View.VISIBLE
                }

                override fun onAnimationEnd(animation: Animator) {}
                override fun onAnimationCancel(animation: Animator) {}
                override fun onAnimationRepeat(animation: Animator) {}
            })
            animation.start()
        }
    }


    interface OnColorSetListener {
        fun onColorSet(color: Int)
    }
}