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
import android.widget.Toast
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
import com.delet_dis.elementarylauncher.presentation.activities.launcherActivity.LauncherActivity
import com.delet_dis.elementarylauncher.presentation.activities.onboardingActivity.fragments.actionsPickFragment.ActionsPickFragment
import com.delet_dis.elementarylauncher.presentation.activities.onboardingActivity.recyclerViewAdapters.ActionsPickingAdapter
import com.delet_dis.elementarylauncher.presentation.activities.onboardingActivity.recyclerViewAdapters.AppsPickingAdapter
import com.delet_dis.elementarylauncher.presentation.activities.onboardingActivity.recyclerViewAdapters.SettingsActionPickingAdapter
import com.delet_dis.elementarylauncher.presentation.activities.onboardingActivity.viewModel.OnboardingActivityViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * Class showing the initial setup screen
 */
@AndroidEntryPoint
@ExperimentalCoroutinesApi
class OnboardingActivity : AppCompatActivity(),
    ActionsPickFragment.ParentActivityCallback {

    private var pickedItemId: Int? = null

    private var pickedContactAction: ContactActionType? = null

    private lateinit var binding: ActivityOnboardingBinding

    private val viewModel: OnboardingActivityViewModel by viewModels()

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>

    private lateinit var appWidgetManager: AppWidgetManager

    private lateinit var appWidgetHost: AppWidgetHost

    private var hostFragment: Fragment? = null

    private var isAvailableToSaveShortcutsSettings = true

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
            uri?.let {
                pickedItemId?.let { position ->
                    pickedContactAction?.let { contactActionType ->
                        viewModel.insertContact(
                            contactActionType,
                            uri.toString(),
                            position
                        )
                    }
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
                        try {
                            widgetsConfiguringContract.launch(pair.second)
                        } catch (exception: SecurityException) {
                            Toast.makeText(
                                applicationContext,
                                applicationContext.getString(R.string.widgetInstallingError),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    pickedItemId?.let { pickedItemId ->
                        viewModel.insertWidget(pair.second!!, pickedItemId)
                    }
                }
            } else {
                pickedItemId?.let { pickedItemId ->
                    viewModel.deleteAtPosition(
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
                        viewModel.insertWidget(pair.second!!, pickedItemId)
                    }
                }
            } else {
                pickedItemId?.let { pickedItemId ->
                    viewModel.deleteAtPosition(
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

        appWidgetHost = AppWidgetHost(
            this,
            widgetHostId
        )

        setContentView(binding.root)

        if (intent.extras?.getString(ConstantsRepository.SCREEN_TO_NAVIGATE) != null) {
            navigateToIntentExtrasFragment()
        } else {
            checkIfOnboardingPassed()
        }

        initBottomProgressAnimationValues()

        bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheetLayout)

        initItemPickRecycler()

        setOutOfBottomSheetClickListener()

        initLoadingObserver()

        initBottomSheetStateObserver()
    }

    private fun navigateToIntentExtrasFragment() =
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
                else -> {
                }
            }

            binding.progressIndicator.visibility = View.INVISIBLE
        }

    private fun checkIfOnboardingPassed() {
        if (isOnboardingPassed(applicationContext)) {
            startActivity(Intent(this, LauncherActivity::class.java))
            finish()
        }
    }

    private fun initBottomProgressAnimationValues() =
        hostFragment?.findNavController()
            ?.addOnDestinationChangedListener { _, destination, _ ->
                findPickedFragmentProgress(destination.id)?.let { pickedFragmentProgressType ->
                    animateProgressbarProgressToInt(pickedFragmentProgressType.progress)
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

    private fun initBottomSheetStateObserver() =
        viewModel.isBottomSheetHidden.observe(
            this@OnboardingActivity,
            { isBottomSheetHidden ->
                if (isBottomSheetHidden) {
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                } else {
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                }
            })

    private fun initLoadingObserver() =
        viewModel.isLoading.observe(this@OnboardingActivity, { isLoading ->
            if (isLoading) {
                showBottomSheetLoading()
            } else {
                hideBottomSheetLoading()
            }
        })

    private fun animateProgressbarProgressToInt(progress: Int) =
        ObjectAnimator.ofInt(
            binding.progressIndicator,
            "progress",
            progress
        ).setDuration(progressbarAnimationDuration).start()

    override fun onBackPressed() {
        if (!isOnboardingPassed(applicationContext)) {
            if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            } else {
                super.onBackPressed()
            }
        } else if (isAvailableToSaveShortcutsSettings) {
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

    override fun setAvailabilityToPressBackButton(availability: Boolean) {
        isAvailableToSaveShortcutsSettings = availability
    }

    override fun backFragmentButtonPress() {
        onBackPressed()
    }

    private fun callSubItemPicking(actionType: ActionType, itemPosition: Int) =
        with(viewModel) {
            when (actionType) {
                ActionType.APP -> {
                    loadApplicationsPackages()
                    applicationsPackagesLiveData
                        .observe(this@OnboardingActivity, { mutableList ->

                            binding.itemPickRecycler.adapter =
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

                            binding.itemPickRecycler.adapter =
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

    private fun checkForContactPermission(itemPosition: Int) =
        viewModel.checkForPermission(
            Manifest.permission.READ_CONTACTS,
            ::pickContact
        ) { (::buildContactActionMaterialDialog)(itemPosition) }


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

    private fun buildContactActionMaterialDialog(itemId: Int) =
        buildPermissionAlertDialog(
            this@OnboardingActivity,
            R.string.contactPermissionRequiredMessage
        ) { (::contactActionPickPositiveMaterialDialogButtonFunction)(itemId) }

    private fun callActionPickPositiveMaterialDialogButtonFunction(itemId: Int) {
        requestCallsPermissionLauncher.launch(
            Manifest.permission.CALL_PHONE
        )
        pickedItemId = itemId
    }

    private fun buildContactCallActionMaterialDialog(itemId: Int) =
        buildPermissionAlertDialog(
            this@OnboardingActivity,
            R.string.phonePermissionRequiredMessage
        ) { (::callActionPickPositiveMaterialDialogButtonFunction)(itemId) }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        val currentFragment =
            hostFragment
                ?.childFragmentManager
                ?.primaryNavigationFragment

        hostFragment?.findNavController()
            ?.navigate((currentFragment as FragmentParentInterface).getFragmentId())
    }

    private companion object OnboardingActivityConstantsRepository {
        const val widgetHostId = 100
        const val progressbarAnimationDuration: Long = 460
    }
}
