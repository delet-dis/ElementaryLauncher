package com.delet_dis.elementarylauncher.presentation.activities.onboardingActivity


import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.delet_dis.elementarylauncher.R
import org.hamcrest.Matchers.allOf
import org.hamcrest.core.IsInstanceOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class NavigateToWelcomeFragmentTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(OnboardingActivity::class.java)

    @Test
    fun navigateToWelcomeFragmentTest() {
        val imageView = onView(
            allOf(
                withId(R.id.appLogo), withContentDescription(R.string.appLogoContentDescription),
                withParent(withParent(withId(R.id.navigationOnboardingControllerContainerView))),
                isDisplayed()
            )
        )
        imageView.check(matches(isDisplayed()))

        val textView = onView(
            allOf(
                withId(R.id.appName), withText(R.string.appName),
                withParent(withParent(withId(R.id.navigationOnboardingControllerContainerView))),
                isDisplayed()
            )
        )
        textView.check(matches(withText(R.string.appName)))

        val button = onView(
            allOf(
                withId(R.id.getStartedButton), withText(R.string.getStartedButtonText),
                withParent(withParent(withId(R.id.navigationOnboardingControllerContainerView))),
                isDisplayed()
            )
        )
        button.check(matches(isDisplayed()))

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
