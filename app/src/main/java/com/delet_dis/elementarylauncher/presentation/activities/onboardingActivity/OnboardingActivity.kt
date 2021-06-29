package com.delet_dis.elementarylauncher.presentation.activities.onboardingActivity

import android.Manifest
import android.animation.ObjectAnimator
import android.appwidget.AppWidgetHost
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProviderInfo
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.res.Configuration
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.delet_dis.elementarylauncher.R
import com.delet_dis.elementarylauncher.data.interfaces.FragmentParentInterface
import com.delet_dis.elementarylauncher.data.models.ActionType
import com.delet_dis.elementarylauncher.data.models.ContactActionType
import com.delet_dis.elementarylauncher.data.models.HomescreenActionType
import com.delet_dis.elementarylauncher.databinding.ActivityOnboardingBinding
import com.delet_dis.elementarylauncher.domain.contracts.WidgetConfiguringContract
import com.delet_dis.elementarylauncher.domain.contracts.WidgetPickingContract
import com.delet_dis.elementarylauncher.domain.extensions.findHomescreenAction
import com.delet_dis.elementarylauncher.domain.extensions.findPickedFragmentProgress
import com.delet_dis.elementarylauncher.domain.extensions.isOnboardingPassed
import com.delet_dis.elementarylauncher.domain.helpers.buildPermissionAlertDialog
import com.delet_dis.elementarylauncher.domain.repositories.ConstantsRepository
import com.delet_dis.elementarylauncher.domain.repositories.SharedPreferencesRepository
import com.delet_dis.elementarylauncher.presentation.activities.launcherActivity.LauncherActivity
import com.delet_dis.elementarylauncher.presentation.activities.onboardingActivity.fragments.actionsPickFragment.ActionsPickFragment
import com.delet_dis.elementarylauncher.presentation.activities.onboardingActivity.recyclerViewAdapters.ActionsPickingAdapter
import com.delet_dis.elementarylauncher.presentation.activities.onboardingActivity.recyclerViewAdapters.AppsPickingAdapter
import com.delet_dis.elementarylauncher.presentation.activities.onboardingActivity.recyclerViewAdapters.SettingsActionPickingAdapter
import com.delet_dis.elementarylauncher.presentation.activities.onboardingActivity.viewModel.OnboardingActivityViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi


@ExperimentalCoroutinesApi
@AndroidEntryPoint
class OnboardingActivity : AppCompatActivity(),
    ActionsPickFragment.ParentActivityCallback {

    private var pickedItemId: Int? = null

    private var pickedContactAction: ContactActionType? = null

    private lateinit var binding: ActivityOnboardingBinding

    private val onboardingActivityViewModel by viewModels<OnboardingActivityViewModel>()

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>

    private lateinit var appWidgetManager: AppWidgetManager

    private lateinit var appWidgetHost: AppWidgetHost

    private var hostFragment: Fragment? = null

    private val requestContactsPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            pickContact()
        }
    }

    private val requestCallsPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            pickedItemId?.let { pickedItemId -> checkForContactPermission(pickedItemId) }
        }
    }

    private var pickContactContract: ActivityResultLauncher<Void>? =
        registerForActivityResult(ActivityResultContracts.PickContact()) { uri ->
            pickedItemId?.let { position ->
                pickedContactAction?.let { contactActionType ->
                    onboardingActivityViewModel.insertContact(
                        contactActionType,
                        uri.toString(),
                        position
                    )
                }
            }
        }

    private var widgetsContract =
        registerForActivityResult(WidgetPickingContract()) { pair ->
            if (pair.first) {
                if (pair.second != null) {
                    val appWidgetInfo: AppWidgetProviderInfo =
                        appWidgetManager.getAppWidgetInfo(pair.second!!)

                    if (appWidgetInfo.configure != null) {
                        widgetsConfiguringContract.launch(pair.second)
                    }

                    pickedItemId?.let { pickedItemId ->
                        onboardingActivityViewModel.insertWidget(pair.second!!, pickedItemId)
                    }
                }
            } else {
                pickedItemId?.let { pickedItemId ->
                    onboardingActivityViewModel.deleteAtPosition(
                        pickedItemId
                    )
                }
            }
        }

    private var widgetsConfiguringContract =
        registerForActivityResult(WidgetConfiguringContract()) { pair ->
            if (pair.first) {
                if (pair.second != null) {
                    pickedItemId?.let { pickedItemId ->
                        onboardingActivityViewModel.insertWidget(pair.second!!, pickedItemId)
                    }
                }
            } else {
                pickedItemId?.let { pickedItemId ->
                    onboardingActivityViewModel.deleteAtPosition(
                        pickedItemId
                    )
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOnboardingBinding.inflate(layoutInflater)

        hostFragment =
            supportFragmentManager
                .findFragmentById(binding.navigationOnboardingControllerContainerView.id)

        appWidgetManager = AppWidgetManager.getInstance(this)

        appWidgetHost = AppWidgetHost(this, 100)

        setContentView(binding.root)

        if (intent.extras != null) {
            navigateToIntentExtrasFragment()
        } else {
            checkIfOnboardingPassed()
        }

        initBottomProgressAnimationValues()

        with(binding) {
            bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetLayout)

            initItemPickRecycler()

            setOutOfBottomSheetClickListener()

            initLoadingObserver()

            initBottomSheetStateObserver()
        }
    }

    private fun navigateToIntentExtrasFragment() {
        with(
            hostFragment?.findNavController()
        ) {
            when (intent.extras!!.getString(ConstantsRepository.SCREEN_TO_NAVIGATE)
                ?.let { screenToNavigate ->
                    findHomescreenAction(
                        screenToNavigate
                    )
                }) {
                HomescreenActionType.ACTIONS_PICK -> {
                    this?.navigate(R.id.action_welcomeFragment_to_actionsPickFragment)
                }
                HomescreenActionType.LAYOUT_PICK -> {
                    this?.navigate(R.id.action_welcomeFragment_to_layoutPickScreenFragment)
                }
                HomescreenActionType.INTERFACE_SCALE_PICK -> {
                    this?.navigate(R.id.action_welcomeFragment_to_interfaceScalePickFragment)
                }
                HomescreenActionType.APPS_LIST -> {
                    this?.navigate(R.id.action_welcomeFragment_to_appsListFragment)
                }
            }

            binding.progressIndicator.visibility = View.INVISIBLE
        }
    }

    private fun checkIfOnboardingPassed() {
        if (SharedPreferencesRepository(applicationContext).isOnboardingPassed()) {
            startActivity(Intent(this, LauncherActivity::class.java))
            finish()
        }
    }

    private fun initBottomProgressAnimationValues() {
        hostFragment?.findNavController()
            ?.addOnDestinationChangedListener { _, destination, _ ->
                findPickedFragmentProgress(destination.id)?.let { pickedFragmentProgressType ->
                    animateProgressbarProgressToInt(pickedFragmentProgressType.progress)
                }
            }
    }

    private fun initItemPickRecycler() =
        with(binding) {
            itemPickRecycler.layoutManager = LinearLayoutManager(
                applicationContext,
                LinearLayoutManager.VERTICAL,
                false
            )
        }

    private fun setOutOfBottomSheetClickListener() =
        with(binding) {
            mainLayout.setOnClickListener {
                if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                }
            }
        }

    private fun initBottomSheetStateObserver() {
        onboardingActivityViewModel.isBottomSheetHidden.observe(
            this@OnboardingActivity,
            { isBottomSheetHidden ->
                if (isBottomSheetHidden) {
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                } else {
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                }
            })
    }

    private fun initLoadingObserver() {
        onboardingActivityViewModel.isLoading.observe(this@OnboardingActivity, { isLoading ->
            if (isLoading) {
                showBottomSheetLoading()
            } else {
                hideBottomSheetLoading()
            }
        })
    }

    private fun animateProgressbarProgressToInt(progress: Int) {
        ObjectAnimator.ofInt(
            binding.progressIndicator,
            "progress",
            progress
        ).setDuration(460).start()
    }

    override fun onBackPressed() {
        if (!isOnboardingPassed(applicationContext)) {
            if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            } else {
                super.onBackPressed()
            }
        } else {
            finish()
        }
    }

    override fun callItemPicking(itemId: Int) {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        binding.itemPickRecycler.adapter =
            ActionsPickingAdapter(ActionType.values()) { actionType ->
                callSubItemPicking(actionType, itemId)
            }

        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    private fun callSubItemPicking(actionType: ActionType, itemPosition: Int) {
        with(onboardingActivityViewModel) {
            with(binding) {
                when (actionType) {
                    ActionType.APP -> {
                        loadApplicationsPackages()
                        applicationsPackagesLiveData
                            .observe(this@OnboardingActivity, { mutableList ->

                                itemPickRecycler.adapter =
                                    AppsPickingAdapter(mutableList as MutableList<ApplicationInfo>)
                                    { applicationInfo ->
                                        insertApp(applicationInfo.packageName, itemPosition)
                                    }
                            })
                    }

                    ActionType.CONTACT -> {
                        pickedContactAction = ContactActionType.CARD
                        pickedItemId = itemPosition

                        checkForContactPermission(itemPosition)
                    }

                    ActionType.WIDGET -> {
                        pickedItemId = itemPosition

                        val pickedWidgetId = appWidgetHost.allocateAppWidgetId()
                        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

                        widgetsContract.launch(pickedWidgetId)
                    }

                    ActionType.CONTACT_CALL -> {
                        pickedContactAction = ContactActionType.CALL
                        pickedItemId = itemPosition

                        checkForPermission(
                            Manifest.permission.CALL_PHONE,
                            { (::checkForContactPermission)(itemPosition) }
                        ) { (::buildContactCallActionMaterialDialog)(itemPosition) }
                    }

                    ActionType.CONTACT_SMS -> {
                        pickedContactAction = ContactActionType.SMS
                        pickedItemId = itemPosition

                        checkForContactPermission(itemPosition)
                    }

                    ActionType.SETTINGS_ACTION -> {
                        loadSettingsActionsPackages()
                        settingsActionsLiveData
                            .observe(this@OnboardingActivity, { mutableList ->

                                itemPickRecycler.adapter =
                                    mutableList?.let { arrayOfSettingsActionTypes ->
                                        SettingsActionPickingAdapter(arrayOfSettingsActionTypes) { settingsActionType ->
                                            insertSettingsAction(
                                                settingsActionType.action,
                                                itemPosition
                                            )
                                        }
                                    }
                            })
                    }

                    ActionType.CLEAR -> {
                        deleteAtPosition(itemPosition)
                    }
                }
            }
        }
    }

    private fun checkForContactPermission(itemPosition: Int) {
        onboardingActivityViewModel.checkForPermission(
            Manifest.permission.READ_CONTACTS,
            ::pickContact
        ) { (::buildContactActionMaterialDialog)(itemPosition) }
    }


    private fun hideBottomSheetLoading() {
        binding.itemPickRecycler.visibility = View.VISIBLE

        binding.progressBar.visibility = View.INVISIBLE
    }

    private fun showBottomSheetLoading() {
        binding.itemPickRecycler.visibility = View.INVISIBLE

        binding.progressBar.visibility = View.VISIBLE
    }

    private fun pickContact() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        pickContactContract?.launch(null)
    }

    private fun contactActionPickPositiveMaterialDialogButtonFunction(itemId: Int) {
        requestContactsPermissionLauncher.launch(
            Manifest.permission.READ_CONTACTS
        )
        pickedItemId = itemId
    }

    private fun buildContactActionMaterialDialog(itemId: Int) {
        buildPermissionAlertDialog(
            this@OnboardingActivity,
            R.string.contactPermissionRequiredMessage
        ) { (::contactActionPickPositiveMaterialDialogButtonFunction)(itemId) }
    }

    private fun callActionPickPositiveMaterialDialogButtonFunction(itemId: Int) {
        requestCallsPermissionLauncher.launch(
            Manifest.permission.CALL_PHONE
        )
        pickedItemId = itemId
    }

    private fun buildContactCallActionMaterialDialog(itemId: Int) {
        buildPermissionAlertDialog(
            this@OnboardingActivity,
            R.string.phonePermissionRequiredMessage
        ) { (::callActionPickPositiveMaterialDialogButtonFunction)(itemId) }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        val currentFragment =
            hostFragment
                ?.childFragmentManager
                ?.primaryNavigationFragment

        hostFragment?.findNavController()
            ?.navigate((currentFragment as FragmentParentInterface).getFragmentId())
    }
}
