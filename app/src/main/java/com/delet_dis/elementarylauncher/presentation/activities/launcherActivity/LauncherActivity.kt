package com.delet_dis.elementarylauncher.presentation.activities.launcherActivity

import android.content.Intent
import android.os.Bundle
import android.view.View
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
import com.delet_dis.elementarylauncher.presentation.activities.launcherActivity.fragments.twoByThreeLayoutFragment.TwoByThreeLayoutFragment
import com.delet_dis.elementarylauncher.presentation.activities.launcherActivity.fragments.twoByTwoLayoutFragment.TwoByTwoLayoutFragment
import com.delet_dis.elementarylauncher.presentation.activities.launcherActivity.recyclerViewAdapters.OnHomescreenActionsPickingAdapter
import com.delet_dis.elementarylauncher.presentation.activities.onboardingActivity.OnboardingActivity
import com.delet_dis.elementarylauncher.presentation.views.clockView.ClockView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.coroutines.ExperimentalCoroutinesApi


@Suppress("DEPRECATION")
@ExperimentalCoroutinesApi
class LauncherActivity : AppCompatActivity(), ClockView.ParentActivityCallback {
    private lateinit var binding: ActivityLauncherBinding

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLauncherBinding.inflate(layoutInflater)

        recalculateWindowInsets()

        setContentView(binding.root)

        with(binding) {
            itemPickRecycler.layoutManager = LinearLayoutManager(
                applicationContext,
                LinearLayoutManager.VERTICAL,
                false
            )

            bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetLayout)

            setOutOfBottomSheetClickListener()
        }
    }

    override fun onResume() {
        super.onResume()
        inflateHomescreenLayout()
    }

    private fun inflateHomescreenLayout() {
        when (SharedPreferencesRepository(applicationContext).getLayoutType()) {
            LayoutType.TWO_BY_THREE -> {
                replaceFragment(TwoByThreeLayoutFragment())
            }

            LayoutType.TWO_BY_TWO -> {
                replaceFragment(TwoByTwoLayoutFragment())
            }
        }
    }

    private fun recalculateWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { _, insets ->
            val systemGestureInsets = insets.systemGestureInsets
            WindowInsetsCompat.Builder()
                .setSystemWindowInsets(insets.systemWindowInsets)
                .setSystemGestureInsets(systemGestureInsets).build()
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

    private fun callHomescreenAction(action: HomescreenActionType) {
        applicationContext.startActivity(
            Intent(applicationContext, OnboardingActivity::class.java).putExtra(
                ConstantsRepository.SCREEN_TO_NAVIGATE,
                action.name
            ).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        )
    }

    private fun replaceFragment(fragmentToReplace: Fragment) {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        supportFragmentManager.commit {
            setCustomAnimations(R.anim.nav_default_pop_enter_anim, R.anim.nav_default_pop_exit_anim)
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

    override fun onBackPressed() {
        if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }
    }
}
