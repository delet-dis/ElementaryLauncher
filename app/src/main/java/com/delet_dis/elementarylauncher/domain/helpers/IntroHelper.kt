package com.delet_dis.elementarylauncher.domain.helpers

import android.app.Activity
import android.annotation.SuppressLint
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.graphics.RectF
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.view.setPadding
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.delet_dis.elementarylauncher.R

private const val TUTORIAL_PREFERENCES = "TUTORIAL_PREFERENCES"
private const val CLOCK_TUTORIAL_SHOWN = "AFTER_ONBOARDING_INTRO_SHOWN"

/**
 * Small project-owned, one-time coach mark for the clock. It deliberately avoids retaining the
 * Fragment or Activity after the Fragment view lifecycle ends.
 */
fun createIntro(fragment: Fragment, activity: Activity, viewToShape: View) {
    val preferences = activity.getSharedPreferences(TUTORIAL_PREFERENCES, Activity.MODE_PRIVATE)
    if (preferences.getBoolean(CLOCK_TUTORIAL_SHOWN, false)) return

    viewToShape.postDelayed({
        if (!fragment.isAdded || fragment.view == null) return@postDelayed

        val decor = activity.window.decorView as? ViewGroup ?: return@postDelayed
        val overlay = TutorialOverlay(activity, viewToShape).apply {
            isClickable = true
            isFocusable = true
            setOnClickListener {
                decor.removeView(this)
                viewToShape.performClick()
            }
        }
        decor.addView(
            overlay,
            ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        )
        preferences.edit().putBoolean(CLOCK_TUTORIAL_SHOWN, true).apply()

        fragment.viewLifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onDestroy(owner: LifecycleOwner) {
                (overlay.parent as? ViewGroup)?.removeView(overlay)
            }
        })
    }, 300)
}

@SuppressLint("ViewConstructor")
private class TutorialOverlay(
    activity: Activity,
    private val target: View
) : FrameLayout(activity) {
    private val maskPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = activity.getColor(R.color.introMaskColor)
    }
    private val clearPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
    }
    private val targetBounds = Rect()
    private val targetBoundsF = RectF()
    private val rootLocation = IntArray(2)

    init {
        setWillNotDraw(false)
        setLayerType(View.LAYER_TYPE_SOFTWARE, null)

        addView(
            TextView(activity).apply {
                text = activity.getString(R.string.settingsShowcaseInfoText)
                textSize = 20f
                gravity = Gravity.CENTER
                setTextColor(activity.getColor(R.color.textColor))
                setBackgroundColor(activity.getColor(R.color.cardBackgroundColor))
                setPadding(32)
            },
            LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT).apply {
                gravity = Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
                setMargins(32, 32, 32, 96)
            }
        )
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), maskPaint)

        getLocationOnScreen(rootLocation)
        if (target.getGlobalVisibleRect(targetBounds)) {
            targetBounds.offset(-rootLocation[0], -rootLocation[1])
            targetBoundsF.set(targetBounds)
            canvas.drawRoundRect(targetBoundsF, 24f, 24f, clearPaint)
        }
    }
}
