package com.delet_dis.elementarylauncher.domain.helpers

import android.app.Activity
import android.graphics.Color
import android.view.View
import androidx.fragment.app.Fragment
import com.codertainment.materialintro.shape.Focus
import com.codertainment.materialintro.shape.FocusGravity
import com.codertainment.materialintro.shape.ShapeType
import com.codertainment.materialintro.utils.materialIntro
import com.delet_dis.elementarylauncher.R

fun createIntro(fragment: Fragment, activity: Activity, viewToShape: View) {
    fragment.materialIntro {
        maskColor = activity.getColor(R.color.white_half_transparent)
        delayMillis = 300

        isFadeInAnimationEnabled = true
        isFadeOutAnimationEnabled = true

        focusType = Focus.MINIMUM
        focusGravity = FocusGravity.CENTER

        dismissOnTouch = true

        isInfoEnabled = true
        infoText = activity.getString(R.string.settingsShowcaseInfoText)
        infoTextColor = activity.getColor(R.color.defaultFontColor)
        infoTextSize = 20f
        infoTextAlignment = View.TEXT_ALIGNMENT_CENTER
        infoCardBackgroundColor = activity.getColor(R.color.white)

        isDotViewEnabled = true
        isDotAnimationEnabled = true
        dotIconColor = Color.WHITE

        targetView = viewToShape

        viewId = "afterOnboardingIntro"

        showOnlyOnce = true
        userClickAsDisplayed = true

        isPerformClick = true

        shapeType = ShapeType.RECTANGLE
    }?.show(activity)
}