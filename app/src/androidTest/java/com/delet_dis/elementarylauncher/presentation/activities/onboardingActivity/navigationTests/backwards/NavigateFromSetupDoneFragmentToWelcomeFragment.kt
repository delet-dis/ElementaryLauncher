package com.delet_dis.elementarylauncher.presentation.activities.onboardingActivity.navigationTests.backwards

import androidx.test.espresso.Espresso.pressBack
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import com.delet_dis.elementarylauncher.presentation.activities.onboardingActivity.OnboardingActivity
import com.delet_dis.elementarylauncher.presentation.activities.onboardingActivity.navigationTests.forwards.NavigateToSetupDoneFragmentTest
import com.delet_dis.elementarylauncher.presentation.activities.onboardingActivity.navigationTests.forwards.NavigateToWelcomeFragmentTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(androidx.test.ext.junit.runners.AndroidJUnit4::class)
class NavigateFromSetupDoneFragmentToWelcomeFragment {

    @ExperimentalCoroutinesApi
    @Rule
    @JvmField
    var mActivityTestRule = ActivityTestRule(OnboardingActivity::class.java)

    @Test
    fun navigateFromSetupDoneFragmentToWelcomeFragment(){
        NavigateToSetupDoneFragmentTest().navigateToSetupDoneFragment()

        for (i in 1..5){
            pressBack()
        }

        NavigateToWelcomeFragmentTest().checkIsAllElementsDisplayed()
    }
}