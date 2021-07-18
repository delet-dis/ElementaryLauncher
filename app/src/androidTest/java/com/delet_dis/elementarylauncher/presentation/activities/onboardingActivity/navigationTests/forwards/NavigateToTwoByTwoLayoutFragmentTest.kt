package com.delet_dis.elementarylauncher.presentation.activities.onboardingActivity.navigationTests.forwards


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
class NavigateToTwoByTwoLayoutFragmentTest {

    @ExperimentalCoroutinesApi
    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(OnboardingActivity::class.java)

    fun navigateToTwoByThreeLayoutFragment(){
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
        twoByTwoCard.perform(click())

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

        val finishButton = onView(
            allOf(
                withId(R.id.finishButton), withText(R.string.finishButtonText),
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
        finishButton.perform(click())
    }

    @Test
    fun navigateToTwoByThreeLayoutFragmentTest() {
        navigateToTwoByThreeLayoutFragment()

        val infoCardView = onView(
            allOf(
                withId(R.id.info_card_view),
                withParent(withParent(IsInstanceOf.instanceOf(android.widget.RelativeLayout::class.java))),
                isDisplayed()
            )
        )
        infoCardView.check(matches(isDisplayed()))

        val clockView = onView(
            allOf(
                withId(R.id.clockView),
                withParent(withParent(withId(R.id.navigationLauncherControllerContainerView))),
                isDisplayed()
            )
        )
        clockView.check(matches(isDisplayed()))

        val timeStamp = onView(
            allOf(
                withId(R.id.timeStamp),
                withParent(withParent(withId(R.id.cardView))),
                isDisplayed()
            )
        )
        timeStamp.check(matches(isDisplayed()))

        val dateStamp = onView(
            allOf(
                withId(R.id.dateStamp),
                withParent(withParent(IsInstanceOf.instanceOf(android.view.ViewGroup::class.java))),
                isDisplayed()
            )
        )
        dateStamp.check(matches(isDisplayed()))

        val firstCardView = onView(
            allOf(
                withId(R.id.firstCardView),
                withParent(withParent(withId(R.id.navigationLauncherControllerContainerView))),
                isDisplayed()
            )
        )
        firstCardView.check(matches(isDisplayed()))

        val secondCardView = onView(
            allOf(
                withId(R.id.secondCardView),
                withParent(withParent(withId(R.id.navigationLauncherControllerContainerView))),
                isDisplayed()
            )
        )
        secondCardView.check(matches(isDisplayed()))

        val cardTextInFirstCardView = onView(
            allOf(
                withId(R.id.cardText), withText(R.string.settingsPickingWifiText),
                withParent(
                    allOf(
                        withId(R.id.cardConstraint),
                        withParent(withId(R.id.cardView))
                    )
                ),
                isDisplayed()
            )
        )
        cardTextInFirstCardView.check(matches(withText(R.string.settingsPickingWifiText)))
    }
}
