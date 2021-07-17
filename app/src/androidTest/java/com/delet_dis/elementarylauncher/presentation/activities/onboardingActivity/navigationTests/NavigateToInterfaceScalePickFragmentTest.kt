package com.delet_dis.elementarylauncher.presentation.activities.onboardingActivity.navigationTests


import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import com.delet_dis.elementarylauncher.R
import com.delet_dis.elementarylauncher.extensions.childAtPosition
import com.delet_dis.elementarylauncher.presentation.activities.onboardingActivity.OnboardingActivity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.Matchers.allOf
import org.hamcrest.core.IsInstanceOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(androidx.test.ext.junit.runners.AndroidJUnit4::class)
class NavigateToInterfaceScalePickFragmentTest {

    @ExperimentalCoroutinesApi
    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(OnboardingActivity::class.java)

    @Test
    fun navigateToInterfaceScalePickFragmentTest() {
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

        val backButton = onView(
            allOf(
                withId(R.id.backButton),
                withContentDescription(R.string.backButtonContentDescription),
                withParent(
                    allOf(
                        withId(R.id.mainConstraint),
                        withParent(withId(R.id.navigationOnboardingControllerContainerView))
                    )
                ),
                isDisplayed()
            )
        )
        backButton.check(matches(isDisplayed()))

        val exampleCard = onView(
            allOf(
                withId(R.id.exampleCard),
                withParent(
                    allOf(
                        withId(R.id.mainConstraint),
                        withParent(withId(R.id.navigationOnboardingControllerContainerView))
                    )
                ),
                isDisplayed()
            )
        )
        exampleCard.check(matches(isDisplayed()))

        val appLogo = onView(
            allOf(
                withId(R.id.imageCardView),
                withParent(
                    allOf(
                        withId(R.id.cardConstraint),
                        withParent(withId(R.id.cardView))
                    )
                ),
                isDisplayed()
            )
        )
        appLogo.check(matches(isDisplayed()))

        val scaleHintText = onView(
            allOf(
                withId(R.id.scaleHintText), withText(R.string.chooseInterfaceScaleText),
                withParent(
                    allOf(
                        withId(R.id.mainConstraint),
                        withParent(withId(R.id.navigationOnboardingControllerContainerView))
                    )
                ),
                isDisplayed()
            )
        )
        scaleHintText.check(matches(withText(R.string.chooseInterfaceScaleText)))

        val seekBar = onView(
            allOf(
                withId(R.id.interfaceScaleSeekBar),
                withParent(
                    allOf(
                        withId(R.id.mainConstraint),
                        withParent(withId(R.id.navigationOnboardingControllerContainerView))
                    )
                ),
                isDisplayed()
            )
        )
        seekBar.check(matches(isDisplayed()))

        val nextButtonInInterfaceScalePickFragment = onView(
            allOf(
                withId(R.id.nextButton), withText(R.string.nextButtonText),
                withParent(
                    allOf(
                        withId(R.id.mainConstraint),
                        withParent(withId(R.id.navigationOnboardingControllerContainerView))
                    )
                ),
                isDisplayed()
            )
        )
        nextButtonInInterfaceScalePickFragment.check(matches(isDisplayed()))

        val progressBar = onView(
            allOf(
                withId(R.id.progressIndicator),
                withParent(
                    allOf(
                        withId(R.id.mainLayout),
                        withParent(IsInstanceOf.instanceOf(android.view.ViewGroup::class.java))
                    )
                ),
                isDisplayed()
            )
        )
        progressBar.check(matches(isDisplayed()))
    }
}
