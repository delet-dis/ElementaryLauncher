package com.delet_dis.elementarylauncher.ui.activities

import android.Manifest
import android.animation.ObjectAnimator
import android.appwidget.AppWidgetHost
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProviderInfo
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.delet_dis.elementarylauncher.R
import com.delet_dis.elementarylauncher.common.extensions.findHomescreenAction
import com.delet_dis.elementarylauncher.common.extensions.findPickedFragmentProgress
import com.delet_dis.elementarylauncher.common.extensions.isOnboardingPassed
import com.delet_dis.elementarylauncher.common.models.ActionType
import com.delet_dis.elementarylauncher.common.models.ContactActionType
import com.delet_dis.elementarylauncher.common.models.HomescreenActionType
import com.delet_dis.elementarylauncher.data.contracts.WidgetConfiguringContract
import com.delet_dis.elementarylauncher.data.contracts.WidgetPickingContract
import com.delet_dis.elementarylauncher.data.database.entities.*
import com.delet_dis.elementarylauncher.data.repositories.ConstantsRepository
import com.delet_dis.elementarylauncher.data.repositories.SharedPreferencesRepository
import com.delet_dis.elementarylauncher.databinding.ActivityOnboardingBinding
import com.delet_dis.elementarylauncher.helpers.buildPermissionAlertDialog
import com.delet_dis.elementarylauncher.recyclerViewAdapters.*
import com.delet_dis.elementarylauncher.ui.fragments.ActionsPickFragment
import com.delet_dis.elementarylauncher.viewmodels.OnboardingActivityViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.coroutines.ExperimentalCoroutinesApi


@ExperimentalCoroutinesApi
class OnboardingActivity : AppCompatActivity(),
    ActionsPickFragment.ParentActivityCallback {

    private var pickedItemId: Int? = null

    private var pickedContactAction: ContactActionType? = null

    private lateinit var binding: ActivityOnboardingBinding

    private lateinit var onboardingActivityViewModel: OnboardingActivityViewModel

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>

    private lateinit var appWidgetManager: AppWidgetManager

    private lateinit var appWidgetHost: AppWidgetHost

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
            pickedItemId?.let { checkForContactPermission(it) }
        }
    }

    private var pickContactContract: ActivityResultLauncher<Void>? =
        registerForActivityResult(ActivityResultContracts.PickContact()) {
            it?.let {
                pickedItemId?.let { position ->
                    pickedContactAction?.let { it1 ->
                        onboardingActivityViewModel.insertContact(
                            it1,
                            it.toString(),
                            position
                        )
                    }
                }
            }
        }

    private var widgetsContract =
        registerForActivityResult(WidgetPickingContract()) {
            if (it.first) {
                if (it.second != null) {
                    val appWidgetInfo: AppWidgetProviderInfo =
                        appWidgetManager.getAppWidgetInfo(it.second!!)

                    if (appWidgetInfo.configure != null) {
                        widgetsConfiguringContract.launch(it.second)
                    }

                    pickedItemId?.let { it1 ->
                        onboardingActivityViewModel.insertWidget(it.second!!, it1)
                    }
                }
            } else {
                pickedItemId?.let { it1 -> onboardingActivityViewModel.deleteAtPosition(it1) }
            }
        }

    private var widgetsConfiguringContract =
        registerForActivityResult(WidgetConfiguringContract()) {
            if (it.first) {
                if (it.second != null) {
                    pickedItemId?.let { it1 ->
                        onboardingActivityViewModel.insertWidget(it.second!!, it1)
                    }
                }
            } else {
                pickedItemId?.let { it1 -> onboardingActivityViewModel.deleteAtPosition(it1) }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOnboardingBinding.inflate(layoutInflater)

        onboardingActivityViewModel = OnboardingActivityViewModel(application)

        appWidgetManager = AppWidgetManager.getInstance(this)

        appWidgetHost = AppWidgetHost(this, 100)

        setContentView(binding.root)

        if (intent.extras != null) {
            with(
                supportFragmentManager
                    .findFragmentById(binding.navigationOnboardingControllerContainerView.id)
                    ?.findNavController()
            ) {
                when (intent.extras!!.getString(ConstantsRepository.SCREEN_TO_NAVIGATE)?.let {
                    findHomescreenAction(
                        it
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

    private fun checkIfOnboardingPassed() {
        if (SharedPreferencesRepository(applicationContext).isOnboardingPassed()) {
            startActivity(Intent(this, LauncherActivity::class.java))
            finish()
        }
    }

    private fun initBottomProgressAnimationValues() {
        supportFragmentManager.findFragmentById(binding.navigationOnboardingControllerContainerView.id)
            ?.findNavController()
            ?.addOnDestinationChangedListener { _, destination, _ ->
                findPickedFragmentProgress(destination.id)?.let {
                    animateProgressbarProgressToInt(it.progress)
                }
            }
    }

    private fun ActivityOnboardingBinding.initItemPickRecycler() {
        itemPickRecycler.layoutManager = LinearLayoutManager(
            applicationContext,
            LinearLayoutManager.VERTICAL,
            false
        )
    }

    private fun ActivityOnboardingBinding.setOutOfBottomSheetClickListener() {
        mainLayout.setOnClickListener {
            if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            }
        }
    }

    private fun initBottomSheetStateObserver() {
        onboardingActivityViewModel.isBottomSheetHidden.observe(this@OnboardingActivity, {
            if (it) {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            } else {
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
        })
    }

    private fun initLoadingObserver() {
        onboardingActivityViewModel.isLoading.observe(this@OnboardingActivity, {
            if (it) {
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

        binding.itemPickRecycler.adapter = ActionsPickingAdapter(ActionType.values()) {
            callSubItemPicking(it, itemId)
        }

        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    private fun callSubItemPicking(it: ActionType, itemPosition: Int) {
        with(onboardingActivityViewModel) {
            with(binding) {
                when (it) {
                    ActionType.APP -> {
                        loadApplicationsPackages()
                        applicationsPackagesLiveData
                            .observe(this@OnboardingActivity, { mutableList ->

                                itemPickRecycler.adapter =
                                    AppsPickingAdapter(mutableList as MutableList<ApplicationInfo>) {
                                        insertApp(it.packageName, itemPosition)
                                    }
                            })
                    }

//                    ActionType.SHORTCUT -> {
//                        loadShortcutsPackages()
//                        shortcutsPackagesLiveData
//                            .observe(this@OnboardingActivity, { mutableList ->
//
//                                itemPickRecycler.adapter =
//                                    ShortcutsPickingAdapter(mutableList) {
//                                        insertShortcut(
//                                            it.resolvePackageName,
//                                            itemId
//                                        )
//                                    }
//                            })
//                    }

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

                                itemPickRecycler.adapter = mutableList?.let { it1 ->
                                    SettingsActionPickingAdapter(it1) {
                                        insertSettingsAction(it.action, itemPosition)
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

}