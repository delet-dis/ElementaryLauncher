package com.delet_dis.elementarylauncher

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.pressBack
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.delet_dis.elementarylauncher.presentation.activities.launcherActivity.LauncherActivity
import com.delet_dis.elementarylauncher.presentation.activities.onboardingActivity.OnboardingActivity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@OptIn(ExperimentalCoroutinesApi::class)
class ActivityLifecycleInstrumentedTest {
    @Test
    fun launcherBackIsConsumedOnHomeScreen() {
        ActivityScenario.launch(LauncherActivity::class.java).use { scenario ->
            pressBack()
            scenario.onActivity { assertFalse(it.isFinishing) }
        }
    }

    @Test
    fun onboardingRestoresNavigationAfterRecreation() {
        ActivityScenario.launch(OnboardingActivity::class.java).use { scenario ->
            scenario.recreate()
            scenario.onActivity {
                assertNotNull(
                    it.supportFragmentManager.findFragmentById(
                        R.id.navigationOnboardingControllerContainerView
                    )
                )
            }
        }
    }
}
