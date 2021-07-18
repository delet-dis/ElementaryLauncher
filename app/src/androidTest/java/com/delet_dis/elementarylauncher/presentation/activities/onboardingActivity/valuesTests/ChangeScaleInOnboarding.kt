package com.delet_dis.elementarylauncher.presentation.activities.onboardingActivity.valuesTests


import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import com.delet_dis.elementarylauncher.R
import com.delet_dis.elementarylauncher.extensions.childAtPosition
import com.delet_dis.elementarylauncher.extensions.setValue
import com.delet_dis.elementarylauncher.extensions.withValue
import com.delet_dis.elementarylauncher.presentation.activities.onboardingActivity.OnboardingActivity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.Matchers.allOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(androidx.test.ext.junit.runners.AndroidJUnit4::class)
class ChangeScaleInOnboarding {

    @ExperimentalCoroutinesApi
    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(OnboardingActivity::class.java)

    @Test
    fun changeScaleInOnboarding() {
        val getStartedButton = onView(
            allOf(
                withId(R.id.getStartedButton), withText(R.string.getStartedButtonText),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.navigationOnboardingControllerContainerView),
                        0
                    ),
                    3
                ),
                isDisplayed()
            )
        )
        getStartedButton.perform(click())

        val nextButtonInScreenLayoutPickFragment = onView(
            allOf(
                withId(R.id.nextButton), withText(R.string.nextButtonText),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.navigationOnboardingControllerContainerView),
                        0
                    ),
                    7
                ),
                isDisplayed()
            )
        )
        nextButtonInScreenLayoutPickFragment.perform(click())

        val interfaceScaleSeekBar = onView(
            withId(R.id.interfaceScaleSeekBar)
        )

        interfaceScaleSeekBar.perform(setValue(4))
        interfaceScaleSeekBar.check(matches(withValue(4)))

//        interfaceScaleSeekBar.check(matches(withValue(3)))



        interfaceScaleSeekBar.perform(setValue(0))
        interfaceScaleSeekBar.check(matches(withValue(0)))
    }
}
