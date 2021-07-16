package com.delet_dis.elementarylauncher.presentation.activities.onboardingActivity.values


import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import com.delet_dis.elementarylauncher.R
import com.delet_dis.elementarylauncher.presentation.activities.onboardingActivity.OnboardingActivity
import com.delet_dis.elementarylauncher.presentation.activities.onboardingActivity.extensions.childAtPosition
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.Matchers.`is`
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

        val cardView = onView(
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
        cardView.perform(click())

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
        radioButton.check(matches(isNotChecked()))

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
        radioButton2.check(matches(isChecked()))

        val cardView2 = onView(
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
        cardView2.perform(click())

        val radioButton3 = onView(
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
        radioButton3.check(matches(isNotChecked()))

        val radioButton4 = onView(
            allOf(
                withId(R.id.twoByThreeCard),
                withParent(
                    allOf(
                        withId(R.id.radioGroup),
                        withParent(IsInstanceOf.instanceOf(android.view.ViewGroup::class.java))
                    )
                ),
                isDisplayed()
            )
        )
        radioButton4.check(matches(isChecked()))

        val materialRadioButton = onView(
            allOf(
                withId(R.id.twoByTwoRadio),
                childAtPosition(
                    allOf(
                        withId(R.id.radioGroup),
                        childAtPosition(
                            withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            3
                        )
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        materialRadioButton.perform(click())

        val radioButton5 = onView(
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
        radioButton5.check(matches(isNotChecked()))

        val radioButton6 = onView(
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
        radioButton6.check(matches(isChecked()))

        val materialRadioButton2 = onView(
            allOf(
                withId(R.id.twoByThreeRadio),
                childAtPosition(
                    allOf(
                        withId(R.id.radioGroup),
                        childAtPosition(
                            withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                            3
                        )
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        materialRadioButton2.perform(click())

        val radioButton7 = onView(
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
        radioButton7.check(matches(isNotChecked()))

        val radioButton8 = onView(
            allOf(
                withId(R.id.twoByThreeCard),
                withParent(
                    allOf(
                        withId(R.id.radioGroup),
                        withParent(IsInstanceOf.instanceOf(android.view.ViewGroup::class.java))
                    )
                ),
                isDisplayed()
            )
        )
        radioButton8.check(matches(isChecked()))
    }
}
