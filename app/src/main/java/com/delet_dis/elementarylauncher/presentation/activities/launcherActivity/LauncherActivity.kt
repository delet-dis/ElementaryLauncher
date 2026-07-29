package com.delet_dis.elementarylauncher.presentation.activities.launcherActivity

import android.content.Intent
import android.content.ActivityNotFoundException
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.recyclerview.widget.LinearLayoutManager
import com.delet_dis.elementarylauncher.R
import com.delet_dis.elementarylauncher.data.models.HomescreenActionType
import com.delet_dis.elementarylauncher.data.models.LayoutType
import com.delet_dis.elementarylauncher.databinding.ActivityLauncherBinding
import com.delet_dis.elementarylauncher.domain.repositories.ConstantsRepository
import com.delet_dis.elementarylauncher.domain.repositories.SharedPreferencesRepository
import com.delet_dis.elementarylauncher.domain.widgets.LauncherWidgetHost
import com.delet_dis.elementarylauncher.presentation.activities.aboutActivity.AboutActivity
import com.delet_dis.elementarylauncher.presentation.activities.launcherActivity.fragments.twoByThreeLayoutFragment.TwoByThreeLayoutFragment
import com.delet_dis.elementarylauncher.presentation.activities.launcherActivity.fragments.twoByTwoLayoutFragment.TwoByTwoLayoutFragment
import com.delet_dis.elementarylauncher.presentation.activities.launcherActivity.recyclerViewAdapters.OnHomescreenActionsPickingAdapter
import com.delet_dis.elementarylauncher.presentation.activities.onboardingActivity.OnboardingActivity
import com.delet_dis.elementarylauncher.presentation.views.clockView.ClockView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * Class showing the main application screen with shortcuts.
 */
@AndroidEntryPoint
@ExperimentalCoroutinesApi
class LauncherActivity : AppCompatActivity(), ClockView.ParentActivityCallback {
    private lateinit var binding: ActivityLauncherBinding

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>
    private val widgetHost by lazy { LauncherWidgetHost.get(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityLauncherBinding.inflate(layoutInflater)
        setContentView(binding.root)
        applyWindowInsets()

        with(binding) {
            itemPickRecycler.layoutManager = LinearLayoutManager(
                applicationContext,
                LinearLayoutManager.VERTICAL,
                false
            )

            bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetLayout)

            setOutOfBottomSheetClickListener()
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                }
            }
        })
    }

    override fun onStart() {
        super.onStart()
        widgetHost.startListening()
    }

    override fun onStop() {
        widgetHost.stopListening()
        super.onStop()
    }

    override fun onResume() {
        super.onResume()
        inflateHomescreenLayout()
    }

    private fun inflateHomescreenLayout() {
        val currentFragment = supportFragmentManager.findFragmentById(
            binding.navigationLauncherControllerContainerView.id
        )
        val desiredFragment = when (SharedPreferencesRepository(applicationContext).getLayoutType()) {
            LayoutType.TWO_BY_THREE -> TwoByThreeLayoutFragment()
            LayoutType.TWO_BY_TWO -> TwoByTwoLayoutFragment()
        }

        if (currentFragment?.javaClass != desiredFragment.javaClass) {
            replaceFragment(desiredFragment)
        }
    }

    private fun applyWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.mainLayout) { view, insets ->
            val safe = insets.getInsets(
                WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.displayCutout()
            )
            view.setPadding(safe.left, safe.top, safe.right, safe.bottom)
            insets
        }
        ViewCompat.setOnApplyWindowInsetsListener(binding.bottomSheetLayout) { view, insets ->
            val safe = insets.getInsets(
                WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.displayCutout()
            )
            view.setPadding(safe.left, 0, safe.right, safe.bottom)
            insets
        }
    }

    override fun callHomescreenBottomSheet() {
        binding.itemPickRecycler.adapter = OnHomescreenActionsPickingAdapter(
            HomescreenActionType.values()
        ) { homescreenActionType ->
            callHomescreenAction(homescreenActionType)
        }
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    override fun callToHideHomescreenBottomSheet() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
    }

    private fun callHomescreenAction(action: HomescreenActionType) =
        if (action.isOnboardingPart) {
            applicationContext.startActivity(
                Intent(applicationContext, OnboardingActivity::class.java).putExtra(
                    ConstantsRepository.SCREEN_TO_NAVIGATE,
                    action.name
                ).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            )
        } else {
            when (action) {
                HomescreenActionType.HOMESCREEN_PICK -> {
                    try {
                        startActivity(Intent(Settings.ACTION_HOME_SETTINGS))
                    } catch (_: ActivityNotFoundException) {
                        runCatching { startActivity(Intent(Settings.ACTION_SETTINGS)) }
                    } catch (_: SecurityException) {
                        runCatching { startActivity(Intent(Settings.ACTION_SETTINGS)) }
                    }
                }
                HomescreenActionType.ABOUT_APP -> {
                    startActivity(Intent(this, AboutActivity::class.java))
                }
                else -> {
                }
            }
        }

    private fun replaceFragment(fragmentToReplace: Fragment) {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        supportFragmentManager.commit {
            setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
            setReorderingAllowed(true)
            replace(
                binding.navigationLauncherControllerContainerView.id,
                fragmentToReplace
            )
        }
    }

    private fun setOutOfBottomSheetClickListener() =
        with(binding) {
            mainLayout.setOnClickListener {
                if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                }
            }
        }

}
