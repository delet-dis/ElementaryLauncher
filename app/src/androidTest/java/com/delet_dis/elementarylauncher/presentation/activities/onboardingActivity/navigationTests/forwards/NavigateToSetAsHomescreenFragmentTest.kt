package com.delet_dis.elementarylauncher.presentation.activities.onboardingActivity.navigationTests.forwards


import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
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
class NavigateToSetAsHomescreenFragmentTest {

    @ExperimentalCoroutinesApi
    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(OnboardingActivity::class.java)

    fun navigateToSetAsHomescreenFragment(){
        NavigateToActionsPickFragmentTest().navigateToActionsPickFragment()

        val nextButtonInActionsPickFragment = onView(
            allOf(
                withId(R.id.nextButton), withText(R.string.nextButtonText),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.navigationOnboardingControllerContainerView),
                        0
                    ),
                    4
                ),
                isDisplayed()
            )
        )
        nextButtonInActionsPickFragment.perform(ViewActions.click())
    }

    @Test
    fun navigateToSetAsHomescreenFragmentTest() {
        navigateToSetAsHomescreenFragment()

        val backButton = onView(
            allOf(
                withId(R.id.backButton),
                withContentDescription(R.string.backButtonContentDescription),
                withParent(withParent(withId(R.id.navigationOnboardingControllerContainerView))),
                isDisplayed()
            )
        )
        backButton.check(matches(isDisplayed()))

        val launcherSettingsImage = onView(
            allOf(
                withId(R.id.launcherSettingsImage),
                withContentDescription(R.string.finishImageDescription),
                withParent(withParent(withId(R.id.navigationOnboardingControllerContainerView))),
                isDisplayed()
            )
        )
        launcherSettingsImage.check(matches(isDisplayed()))

        val setAsHomescreenHint = onView(
            allOf(
                withId(R.id.hint),
                withText(R.string.setAsHomescreenHintText),
                withParent(withParent(withId(R.id.navigationOnboardingControllerContainerView))),
                isDisplayed()
            )
        )
        setAsHomescreenHint.check(matches(withText(R.string.setAsHomescreenHintText)))

        val goToSettingsButton = onView(
            allOf(
                withId(R.id.goToSettingsButton),
                withText(R.string.goToSettingsSetAsHomescreenHintTextSetupButtonText),
                withParent(withParent(withId(R.id.navigationOnboardingControllerContainerView))),
                isDisplayed()
            )
        )
        goToSettingsButton.check(matches(isDisplayed()))

        val skipButton = onView(
            allOf(
                withId(R.id.skipButton),
                withText(R.string.skipSetAsHomescreenHintTextSetupButtonText),
                withParent(withParent(withId(R.id.navigationOnboardingControllerContainerView))),
                isDisplayed()
            )
        )
        skipButton.check(matches(isDisplayed()))

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
