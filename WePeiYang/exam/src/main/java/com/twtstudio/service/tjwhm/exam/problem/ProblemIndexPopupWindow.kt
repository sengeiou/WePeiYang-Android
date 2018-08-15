package com.twtstudio.service.tjwhm.exam.problem

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LifecycleRegistry
import android.content.Context
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.FrameLayout
import com.twt.wepeiyang.commons.ui.blur.BlurPopupWindow
import com.twtstudio.service.tjwhm.exam.R
import org.jetbrains.anko.dip
import org.jetbrains.anko.horizontalMargin
import org.jetbrains.anko.layoutInflater

/**
 * Created by tjwhm@TWTStudio at 12:31 AM,2018/8/16.
 * Happy coding!
 */

class ProblemIndexPopupWindow(context: Context, val startXY: Pair<Float, Float>) : BlurPopupWindow(context), LifecycleOwner {
    private val lifecycleRegistry = LifecycleRegistry(this)

    override fun getLifecycle(): Lifecycle = lifecycleRegistry

    lateinit var view: View

    override fun createContentView(parent: ViewGroup): View = parent.context.layoutInflater
            .inflate(R.layout.exam_popup_problem_index, parent, false).apply {
                layoutParams = (layoutParams as FrameLayout.LayoutParams).apply {
                    gravity = Gravity.CENTER
                    horizontalMargin = dip(50)
                }
            }.also { view = it }

    override fun onShow() {
        lifecycleRegistry.markState(Lifecycle.State.STARTED)
    }

    override fun onDismiss() {
        super.onDismiss()
        lifecycleRegistry.markState(Lifecycle.State.DESTROYED)
    }

    override fun createShowAnimator(): Animator {
        val alphaAnim = super.createShowAnimator()
        val animSet = AnimatorSet()
        val scaleX = ObjectAnimator.ofFloat(view, "scaleX", 0f, 1f)
        val scaleY = ObjectAnimator.ofFloat(view, "scaleY", 0f, 1f)
        val x = ObjectAnimator.ofFloat(view, "X", startXY.first, 150.0f)
        val y = ObjectAnimator.ofFloat(view, "Y", startXY.second, 715.0f)
        animSet.duration = 200L
        animSet.interpolator = AccelerateDecelerateInterpolator()
        animSet.play(scaleX).with(scaleY).with(alphaAnim).with(x).with(y)
        return animSet
    }

    override fun createDismissAnimator(): Animator {
        val animSet = AnimatorSet()
        val contentAnim = ObjectAnimator.ofFloat(mContentLayout, "alpha", mContentLayout.alpha, 0f)
        val scaleX = ObjectAnimator.ofFloat(view, "scaleX", 1f, 0f)
        val scaleY = ObjectAnimator.ofFloat(view, "scaleY", 1f, 0f)
        val x = ObjectAnimator.ofFloat(view, "X", 150.0f, startXY.first)
        val y = ObjectAnimator.ofFloat(view, "Y", 715.0f, startXY.second)
        animSet.duration = 200L
        Log.i("zzzzzz", "x${mContentLayout.x}   y${mContentLayout.y}")
        Log.i("zzzzzz", "x${view.x}   y${view.y}")

        animSet.interpolator = AccelerateDecelerateInterpolator()
        animSet.play(scaleX).with(scaleY).with(contentAnim).with(x).with(y)
        return animSet
    }

    override fun getAnimationDuration(): Long = 200L

    override fun isDismissOnTouchBackground(): Boolean = true

    init {
        setOnClickListener {
            dismiss()
        }
    }

}