package com.delet_dis.elementarylauncher.presentation.activities.onboardingActivity.navigationTests.forwards


import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
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
class NavigateToActionsPickFragmentTest {

    @ExperimentalCoroutinesApi
    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(OnboardingActivity::class.java)
    fun navigateToActionsPickFragment(){
        NavigateToInterfaceScalePickFragmentTest().navigateToInterfaceScalePickFragment()

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
        nextButtonInInterfaceScalePickFragment.perform(ViewActions.click())

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
        cardView.perform(ViewActions.click())

        for (counter in 0..1){
            onView(withId(R.id.itemPickRecycler))
                .perform(
                    RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                        5,
                        ViewActions.click()
                    )
                )
        }

        onView(withId(R.id.itemPickRecycler))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    0,
                    ViewActions.click()
                )
            )
    }

    @Test
    fun navigateToActionsPickFragmentTest() {
        navigateToActionsPickFragment()

        val backButton = onView(
            allOf(
                withId(R.id.backButton),
                withContentDescription(R.string.backButtonContentDescription),
                withParent(withParent(withId(R.id.navigationOnboardingControllerContainerView))),
                isDisplayed()
            )
        )
        backButton.check(matches(isDisplayed()))

        val recyclerView = onView(
            allOf(
                withId(R.id.actionsPickingRecycler),
                withParent(withParent(withId(R.id.navigationOnboardingControllerContainerView))),
                isDisplayed()
            )
        )
        recyclerView.check(matches(isDisplayed()))

        val actionsPickingHint = onView(
            allOf(
                withId(R.id.actionsPickHint), withText(R.string.chooseActionsForHomeScreenText),
                withParent(withParent(withId(R.id.navigationOnboardingControllerContainerView))),
                isDisplayed()
            )
        )
        actionsPickingHint.check(matches(withText(R.string.chooseActionsForHomeScreenText)))

        val nextButtonInActionsPickFragment = onView(
            allOf(
                withId(R.id.nextButton), withText(R.string.nextButtonText),
                withParent(withParent(withId(R.id.navigationOnboardingControllerContainerView))),
                isDisplayed()
            )
        )
        nextButtonInActionsPickFragment.check(matches(isDisplayed()))

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

        for (counter in 0..5) {
            onView(
                allOf(
                    withId(R.id.cardView),
                    childAtPosition(
                        childAtPosition(
                            withId(R.id.actionsPickingRecycler),
                            counter
                        ),
                        0
                    ),
                    isDisplayed()
                )
            ).check(matches(isDisplayed()))
        }
    }
}
