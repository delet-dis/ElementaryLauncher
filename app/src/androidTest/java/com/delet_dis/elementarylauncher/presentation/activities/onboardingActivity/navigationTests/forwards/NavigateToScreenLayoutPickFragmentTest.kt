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
class NavigateToScreenLayoutPickFragmentTest {

    @ExperimentalCoroutinesApi
    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(OnboardingActivity::class.java)

    fun navigateToScreenLayoutPickFragment(){
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
        getStartedButton.perform(ViewActions.click())
    }

    @Test
    fun navigateToScreenLayoutPickFragmentTest() {
        navigateToScreenLayoutPickFragment()

        val backButton = onView(
            allOf(
                withId(R.id.backButton),
                withContentDescription(R.string.backButtonContentDescription),
                withParent(withParent(withId(R.id.navigationOnboardingControllerContainerView))),
                isDisplayed()
            )
        )
        backButton.check(matches(isDisplayed()))

        val twoByThreeCard = onView(
            allOf(
                withId(R.id.twoByThreeCard),
                withParent(withParent(withId(R.id.navigationOnboardingControllerContainerView))),
                isDisplayed()
            )
        )
        twoByThreeCard.check(matches(isDisplayed()))

        val twoByThreeCardImage = onView(
            allOf(
                withId(R.id.gridImageTwoByThree),
                withContentDescription(R.string.gridImageTwoByThreeDescription),
                withParent(withParent(withId(R.id.twoByThreeCard))),
                isDisplayed()
            )
        )
        twoByThreeCardImage.check(matches(isDisplayed()))

        val twoByTwoCard = onView(
            allOf(
                withId(R.id.twoByTwoCard),
                withParent(withParent(withId(R.id.navigationOnboardingControllerContainerView))),
                isDisplayed()
            )
        )
        twoByTwoCard.check(matches(isDisplayed()))

        val twoByTwoCardImage = onView(
            allOf(
                withId(R.id.gridImageTwoByTwo),
                withContentDescription(R.string.gridImageTwoByTwoDescription),
                withParent(withParent(withId(R.id.twoByTwoCard))),
                isDisplayed()
            )
        )
        twoByTwoCardImage.check(matches(isDisplayed()))

        val radioGroup = onView(
            allOf(
                withId(R.id.radioGroup),
                withParent(withParent(withId(R.id.navigationOnboardingControllerContainerView))),
                isDisplayed()
            )
        )
        radioGroup.check(matches(isDisplayed()))

        val twoByThreeRadio = onView(
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
        twoByThreeRadio.check(matches(isDisplayed()))

        val twoByTwoRadio = onView(
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
        twoByTwoRadio.check(matches(isDisplayed()))

        val layoutPickHint = onView(
            allOf(
                withId(R.id.layoutPickHint), withText(R.string.chooseGridLayoutText),
                withParent(withParent(withId(R.id.navigationOnboardingControllerContainerView))),
                isDisplayed()
            )
        )
        layoutPickHint.check(matches(withText(R.string.chooseGridLayoutText)))

        val nextButtonInScreenLayoutPickFragment = onView(
            allOf(
                withId(R.id.nextButton), withText(R.string.nextButtonText),
                withParent(withParent(withId(R.id.navigationOnboardingControllerContainerView))),
                isDisplayed()
            )
        )
        nextButtonInScreenLayoutPickFragment.check(matches(isDisplayed()))

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
