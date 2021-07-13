package com.delet_dis.elementarylauncher.presentation.activities.onboardingActivity


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import com.delet_dis.elementarylauncher.R
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
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
        val materialButton = onView(
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
        materialButton.perform(click())

        val materialButton2 = onView(
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
        materialButton2.perform(click())

        val imageButton = onView(
            allOf(
                withId(R.id.backButton), withContentDescription(R.string.backButtonContentDescription),
                withParent(
                    allOf(
                        withId(R.id.mainConstraint),
                        withParent(withId(R.id.navigationOnboardingControllerContainerView))
                    )
                ),
                isDisplayed()
            )
        )
        imageButton.check(matches(isDisplayed()))

        val viewGroup = onView(
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
        viewGroup.check(matches(isDisplayed()))

        val frameLayout = onView(
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
        frameLayout.check(matches(isDisplayed()))

        val textView = onView(
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
        textView.check(matches(withText(R.string.chooseInterfaceScaleText)))

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

        val button = onView(
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

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}
