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
class NavigateToTwoByThreeLayoutFragmentTest {

    @ExperimentalCoroutinesApi
    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(OnboardingActivity::class.java)

    fun navigateToTwoByThreeLayoutFragment(){
        NavigateToSetupDoneFragmentTest().navigateToSetupDoneFragment()

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
        finishButton.perform(ViewActions.click())
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
