package com.delet_dis.elementarylauncher.presentation.activities.onboardingActivity.fragments.actionsPickFragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.delet_dis.elementarylauncher.R
import com.delet_dis.elementarylauncher.data.interfaces.FragmentParentInterface
import com.delet_dis.elementarylauncher.databinding.FragmentActionsPickScreenBinding
import com.delet_dis.elementarylauncher.domain.extensions.checkIfAppIsDefaultLauncher
import com.delet_dis.elementarylauncher.domain.extensions.isOnboardingPassed
import com.delet_dis.elementarylauncher.domain.repositories.SharedPreferencesRepository
import com.delet_dis.elementarylauncher.presentation.activities.onboardingActivity.fragments.actionsPickFragment.viewModel.ActionsPickFragmentViewModel
import com.delet_dis.elementarylauncher.presentation.activities.onboardingActivity.recyclerViewAdapters.SmallCardAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * Fragment used to display the shortcut selection screen.
 */
@ExperimentalCoroutinesApi
@AndroidEntryPoint
class ActionsPickFragment : Fragment(), FragmentParentInterface {
    private lateinit var binding: FragmentActionsPickScreenBinding

    private val actionsPickFragmentViewModel by viewModels<ActionsPickFragmentViewModel>()

    private lateinit var parentActivityCallback: ParentActivityCallback

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return if (savedInstanceState == null) {
            binding = FragmentActionsPickScreenBinding.inflate(layoutInflater)

            binding.root
        } else {
            view
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        parentActivityCallback = context as ParentActivityCallback
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState == null) {
            initActionsPickingRecycler()

            initDatabaseRecordingsLiveDataObserver()

            initIsAvailableToEndFirstSetupObserver()

            initBackButtonOnClickListener()

            initNextButtonOnClickListener()

            checkIfAppIsAlreadyPickedAsHomescreen()
        }
    }

    private fun initBackButtonOnClickListener() =
        with(binding) {
            backButton.setOnClickListener {
                if (isOnboardingPassed(requireContext())) {
                    requireActivity().finish()
                } else {
                    parentActivityCallback.backFragmentButtonPress()
                }
            }
        }

    private fun checkIfAppIsAlreadyPickedAsHomescreen() =
        with(binding) {
            if (!isOnboardingPassed(requireContext()) and checkIfAppIsDefaultLauncher(requireContext())) {
                nextButton.setOnClickListener {
                    requireActivity().findNavController(R.id.navigationOnboardingControllerContainerView)
                        .navigate(R.id.action_actionsPickFragment_to_setupDoneFragment)
                }

            }
        }

    private fun initDatabaseRecordingsLiveDataObserver() =
        with(binding) {
            actionsPickFragmentViewModel.databaseRecordingsLiveData.observe(viewLifecycleOwner) { list ->
                actionsPickingRecycler.adapter = SmallCardAdapter(list) { itemPosition ->
                    parentActivityCallback.callItemPicking(itemPosition)
                }
            }
        }

    private fun initIsAvailableToEndFirstSetupObserver() =
        with(binding) {
            actionsPickFragmentViewModel.isAvailableToEndFirstSetup.observe(viewLifecycleOwner) { isAvailable ->
                context?.let { context ->
                    if (isAvailable) {
                        nextButton.text = context.getString(R.string.nextButtonText)
                        backButton.isEnabled = true
                        parentActivityCallback.setAvailabilityToPressBackButton(true)
                    }

                    if (isOnboardingPassed(requireContext())) {
                        nextButton.text = getString(R.string.ok)
                    }

                    if (!isAvailable) {
                        nextButton.text =
                            context.getString(R.string.incompleteActionPickingButtonText)
                        backButton.isEnabled = false
                        parentActivityCallback.setAvailabilityToPressBackButton(false)
                    }

                    if (!isAvailable and !isOnboardingPassed(requireContext())) {
                        backButton.isEnabled = true
                    }
                }

                nextButton.isEnabled = isAvailable
            }
        }

    private fun initNextButtonOnClickListener() =
        with(binding) {
            nextButton.setOnClickListener {
                if (isOnboardingPassed(requireContext())) {
                    requireActivity().finish()
                } else {
                    SharedPreferencesRepository(requireContext()).setActionsPicked()

                    requireActivity().findNavController(R.id.navigationOnboardingControllerContainerView)
                        .navigate(R.id.action_actionsPickFragment_to_setAsHomescreenFragment)

                }
            }
        }

    private fun initActionsPickingRecycler() =
        with(binding) {
            actionsPickingRecycler.layoutManager = GridLayoutManager(
                requireContext(),
                2,
                GridLayoutManager.VERTICAL,
                false
            )
        }

    interface ParentActivityCallback {
        fun callItemPicking(itemId: Int)
        fun setAvailabilityToPressBackButton(availability: Boolean)
        fun backFragmentButtonPress()
    }

    override fun getFragmentId(): Int {
        return R.id.actionsPickFragment
    }
}
