package com.delet_dis.elementarylauncher.extensions


import android.util.Log
import android.view.View
import androidx.appcompat.widget.AppCompatSeekBar
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers
import org.hamcrest.Description
import org.hamcrest.Matcher

fun withValue(expectedValue: Int): Matcher<View?> {
    return object : BoundedMatcher<View?, AppCompatSeekBar>(AppCompatSeekBar::class.java) {
        override fun describeTo(description: Description) {
            description.appendText("expected: $expectedValue")
        }

        override fun matchesSafely(seekBar: AppCompatSeekBar?): Boolean {
            Log.d("test", seekBar?.progress.toString())
            return seekBar?.progress == expectedValue
        }
    }
}

fun setValue(value: Int): ViewAction {
    return object : ViewAction {
        override fun getDescription(): String {
            return "Set Slider value to $value"
        }

        override fun getConstraints(): Matcher<View> {
            return ViewMatchers.isAssignableFrom(AppCompatSeekBar::class.java)
        }

        override fun perform(uiController: UiController?, view: View) {
            val seekBar = view as AppCompatSeekBar
            seekBar.progress = value
            Log.d("test", seekBar.progress.toString())
        }
    }
}