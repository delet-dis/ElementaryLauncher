package com.delet_dis.elementarylauncher.presentation.activities.onboardingActivity.navigationTests


import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
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
class NavigateToSetupDoneFragmentTest {

    @ExperimentalCoroutinesApi
    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(OnboardingActivity::class.java)

    @Test
    fun navigateToSetupDoneFragmentTest() {
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

        val nextButtonInInterfaceScalePickFragment = onView(
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
        nextButtonInInterfaceScalePickFragment.perform(click())

        val cardView = onView(
            allOf(
                withId(R.id.cardView),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.actionsPickingRecycler),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        cardView.perform(click())

        for (counter in 0..1) {
            onView(withId(R.id.itemPickRecycler))
                .perform(
                    RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                        5,
                        click()
                    )
                )
        }

        onView(withId(R.id.itemPickRecycler))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    0,
                    click()
                )
            )

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
        nextButtonInActionsPickFragment.perform(click())

        val skipButtonInSetAsHomescreenFragment = onView(
            allOf(
                withId(R.id.skipButton),
                withText(R.string.skipSetAsHomescreenHintTextSetupButtonText),
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
        skipButtonInSetAsHomescreenFragment.perform(click())

        val finishImage = onView(
            allOf(
                withId(R.id.finishImage), withContentDescription(R.string.finishImageDescription),
                withParent(withParent(withId(R.id.navigationOnboardingControllerContainerView))),
                isDisplayed()
            )
        )
        finishImage.check(matches(isDisplayed()))

        val finishHint = onView(
            allOf(
                withId(R.id.finishHint), withText(R.string.finishSetupText),
                withParent(withParent(withId(R.id.navigationOnboardingControllerContainerView))),
                isDisplayed()
            )
        )
        finishHint.check(matches(withText(R.string.finishSetupText)))

        val finishButton = onView(
            allOf(
                withId(R.id.finishButton), withText(R.string.finishButtonText),
                withParent(withParent(withId(R.id.navigationOnboardingControllerContainerView))),
                isDisplayed()
            )
        )
        finishButton.check(matches(isDisplayed()))

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
