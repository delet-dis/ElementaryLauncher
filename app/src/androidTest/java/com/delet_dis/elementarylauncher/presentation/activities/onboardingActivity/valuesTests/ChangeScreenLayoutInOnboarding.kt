package com.delet_dis.elementarylauncher.presentation.activities.onboardingActivity.valuesTests


import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import com.delet_dis.elementarylauncher.R
import com.delet_dis.elementarylauncher.extensions.childAtPosition
import com.delet_dis.elementarylauncher.presentation.activities.onboardingActivity.OnboardingActivity
import com.delet_dis.elementarylauncher.presentation.activities.onboardingActivity.navigationTests.forwards.NavigateToScreenLayoutPickFragmentTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.Matchers.allOf
import org.hamcrest.core.IsInstanceOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(androidx.test.ext.junit.runners.AndroidJUnit4::class)
class ChangeScreenLayoutInOnboarding {

    @ExperimentalCoroutinesApi
    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(OnboardingActivity::class.java)

    @Test
    fun changeScreenLayoutInOnboarding() {
        NavigateToScreenLayoutPickFragmentTest().navigateToScreenLayoutPickFragment()

        val twoByTwoCard = onView(
            allOf(
                withId(R.id.twoByTwoCard),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.navigationOnboardingControllerContainerView),
                        0
                    ),
                    2
                ),
                isDisplayed()
            )
        )

        val twoByThreeCard = onView(
            allOf(
                withId(R.id.twoByThreeCard),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.navigationOnboardingControllerContainerView),
                        0
                    ),
                    1
                ),
                isDisplayed()
            )
        )

        val twoByThreeRadio = onView(
            allOf(
                withId(R.id.twoByThreeRadio),
                withParent(
                    allOf(
                        withId(R.id.radioGroup),
                        withParent(IsInstanceOf.instanceOf(android.view.ViewGroup::class.java))
                    )
                ),
                isDisplayed()
            )
        )

        val twoByTwoRadio = onView(
            allOf(
                withId(R.id.twoByTwoRadio),
                withParent(
                    allOf(
                        withId(R.id.radioGroup),
                        withParent(IsInstanceOf.instanceOf(android.view.ViewGroup::class.java))
                    )
                ),
                isDisplayed()
            )
        )

        twoByTwoCard.perform(click())
        twoByThreeRadio.check(matches(isNotChecked()))
        twoByTwoRadio.check(matches(isChecked()))

        twoByThreeCard.perform(click())
        twoByThreeRadio.check(matches(isChecked()))
        twoByTwoRadio.check(matches(isNotChecked()))

        twoByTwoRadio.perform(click())
        twoByThreeRadio.check(matches(isNotChecked()))
        twoByTwoRadio.check(matches(isChecked()))

        twoByThreeRadio.perform(click())
        twoByThreeRadio.check(matches(isChecked()))
        twoByTwoRadio.check(matches(isNotChecked()))
    }
}
