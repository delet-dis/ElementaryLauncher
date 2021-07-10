package com.delet_dis.elementarylauncher.presentation.activities.onboardingActivity


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.delet_dis.elementarylauncher.R
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.hamcrest.core.IsInstanceOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class NavigateToActionsPickFragmentTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(OnboardingActivity::class.java)

    @Test
    fun navigateToActionsPickFragmentTest() {
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

        val materialButton3 = onView(
            allOf(
                withId(R.id.nextButton), withText(R.string.nextButtonText),
                childAtPosition(
                    allOf(
                        withId(R.id.mainConstraint),
                        childAtPosition(
                            withId(R.id.navigationOnboardingControllerContainerView),
                            0
                        )
                    ),
                    5
                ),
                isDisplayed()
            )
        )
        materialButton3.perform(click())

        val imageButton = onView(
            allOf(
                withId(R.id.backButton),
                withContentDescription(R.string.backButtonContentDescription),
                withParent(withParent(withId(R.id.navigationOnboardingControllerContainerView))),
                isDisplayed()
            )
        )
        imageButton.check(matches(isDisplayed()))

        val recyclerView = onView(
            allOf(
                withId(R.id.actionsPickingRecycler),
                withParent(withParent(withId(R.id.navigationOnboardingControllerContainerView))),
                isDisplayed()
            )
        )
        recyclerView.check(matches(isDisplayed()))

        val textView = onView(
            allOf(
                withId(R.id.actionsPickHint), withText(R.string.chooseActionsForHomeScreenText),
                withParent(withParent(withId(R.id.navigationOnboardingControllerContainerView))),
                isDisplayed()
            )
        )
        textView.check(matches(withText(R.string.chooseActionsForHomeScreenText)))

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

        val button = onView(
            allOf(
                withId(R.id.nextButton), withText(R.string.incompleteActionPickingButtonText),
                withParent(withParent(withId(R.id.navigationOnboardingControllerContainerView))),
                isDisplayed()
            )
        )
        button.check(matches(isDisplayed()))
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
