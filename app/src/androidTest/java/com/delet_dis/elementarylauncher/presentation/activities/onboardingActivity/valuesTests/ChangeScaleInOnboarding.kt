package com.delet_dis.elementarylauncher.presentation.activities.onboardingActivity.valuesTests


import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import com.delet_dis.elementarylauncher.R
import com.delet_dis.elementarylauncher.extensions.setValue
import com.delet_dis.elementarylauncher.extensions.withValue
import com.delet_dis.elementarylauncher.presentation.activities.onboardingActivity.OnboardingActivity
import com.delet_dis.elementarylauncher.presentation.activities.onboardingActivity.navigationTests.forwards.NavigateToInterfaceScalePickFragmentTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
        NavigateToInterfaceScalePickFragmentTest().navigateToInterfaceScalePickFragment()

        val interfaceScaleSeekBar = onView(
            withId(R.id.interfaceScaleSeekBar)
        )

        interfaceScaleSeekBar.perform(setValue(4))
        interfaceScaleSeekBar.check(matches(withValue(4)))

        interfaceScaleSeekBar.perform(setValue(0))
        interfaceScaleSeekBar.check(matches(withValue(0)))
    }
}
