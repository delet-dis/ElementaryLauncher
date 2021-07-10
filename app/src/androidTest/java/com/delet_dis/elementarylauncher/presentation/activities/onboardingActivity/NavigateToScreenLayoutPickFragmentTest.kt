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
class NavigateToScreenLayoutPickFragmentTest {

    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(OnboardingActivity::class.java)

    @Test
    fun navigateToScreenLayoutPickFragmentTest() {
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

        val imageButton = onView(
            allOf(
                withId(R.id.backButton), withContentDescription(R.string.backButtonContentDescription),
                withParent(withParent(withId(R.id.navigationOnboardingControllerContainerView))),
                isDisplayed()
            )
        )
        imageButton.check(matches(isDisplayed()))

        val frameLayout = onView(
            allOf(
                withId(R.id.twoByThreeCard),
                withParent(withParent(withId(R.id.navigationOnboardingControllerContainerView))),
                isDisplayed()
            )
        )
        frameLayout.check(matches(isDisplayed()))

        val imageView = onView(
            allOf(
                withId(R.id.gridImageTwoByThree),
                withContentDescription(R.string.gridImageTwoByThreeDescription),
                withParent(withParent(withId(R.id.twoByThreeCard))),
                isDisplayed()
            )
        )
        imageView.check(matches(isDisplayed()))

        val frameLayout2 = onView(
            allOf(
                withId(R.id.twoByTwoCard),
                withParent(withParent(withId(R.id.navigationOnboardingControllerContainerView))),
                isDisplayed()
            )
        )
        frameLayout2.check(matches(isDisplayed()))

        val imageView2 = onView(
            allOf(
                withId(R.id.gridImageTwoByTwo),
                withContentDescription(R.string.gridImageTwoByTwoDescription),
                withParent(withParent(withId(R.id.twoByTwoCard))),
                isDisplayed()
            )
        )
        imageView2.check(matches(isDisplayed()))

        val radioGroup = onView(
            allOf(
                withId(R.id.radioGroup),
                withParent(withParent(withId(R.id.navigationOnboardingControllerContainerView))),
                isDisplayed()
            )
        )
        radioGroup.check(matches(isDisplayed()))

        val radioButton = onView(
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
        radioButton.check(matches(isDisplayed()))

        val radioButton2 = onView(
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
        radioButton2.check(matches(isDisplayed()))

        val textView = onView(
            allOf(
                withId(R.id.layoutPickHint), withText(R.string.chooseGridLayoutText),
                withParent(withParent(withId(R.id.navigationOnboardingControllerContainerView))),
                isDisplayed()
            )
        )
        textView.check(matches(withText(R.string.chooseGridLayoutText)))

        val button = onView(
            allOf(
                withId(R.id.nextButton), withText(R.string.nextButtonText),
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
