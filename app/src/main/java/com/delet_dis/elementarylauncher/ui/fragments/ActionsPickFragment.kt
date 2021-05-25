package com.delet_dis.elementarylauncher.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.delet_dis.elementarylauncher.R
import com.delet_dis.elementarylauncher.common.extensions.checkIfAppIsDefaultLauncher
import com.delet_dis.elementarylauncher.common.extensions.isOnboardingPassed
import com.delet_dis.elementarylauncher.data.repositories.SharedPreferencesRepository
import com.delet_dis.elementarylauncher.databinding.FragmentActionsPickScreenBinding
import com.delet_dis.elementarylauncher.recyclerViewAdapters.SmallCardAdapter
import com.delet_dis.elementarylauncher.viewmodels.ActionsPickFragmentViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class ActionsPickFragment : Fragment() {
    private lateinit var binding: FragmentActionsPickScreenBinding

    private lateinit var actionsPickFragmentViewModel: ActionsPickFragmentViewModel

    private lateinit var parentActivityCallback: ParentActivityCallback

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return if (savedInstanceState == null) {
            binding = FragmentActionsPickScreenBinding.inflate(layoutInflater)

            actionsPickFragmentViewModel =
                ActionsPickFragmentViewModel(requireActivity().application)

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
            with(binding) {
                initActionsPickingRecycler()

                initDatabaseRecordingsLiveDataObserver()

                initIsAvailableToEndFirstSetupObserver()

                initBackButtonOnClickListener()

                initNextButtonOnClickListener()

                checkIfAppIsAlreadyPickedAsHomescreen()
            }
        }
    }

    private fun FragmentActionsPickScreenBinding.initBackButtonOnClickListener() {
        backButton.setOnClickListener {
            if (isOnboardingPassed(requireContext())) {
                requireActivity().finish()
            } else {
                requireActivity().findNavController(R.id.navigationOnboardingControllerContainerView)
                    .popBackStack()
            }
        }
    }

    private fun FragmentActionsPickScreenBinding.checkIfAppIsAlreadyPickedAsHomescreen() {
        if (!isOnboardingPassed(requireContext())) {
            if (checkIfAppIsDefaultLauncher(requireContext())) {
                nextButton.setOnClickListener {
                    requireActivity().findNavController(R.id.navigationOnboardingControllerContainerView)
                        .navigate(R.id.action_actionsPickFragment_to_setupDoneFragment)
                }
            }
        }
    }

    private fun FragmentActionsPickScreenBinding.initDatabaseRecordingsLiveDataObserver() {
        actionsPickFragmentViewModel.databaseRecordingsLiveData.observe(viewLifecycleOwner) { list ->
            actionsPickingRecycler.adapter = SmallCardAdapter(list) {
                parentActivityCallback.callItemPicking(it)
            }
        }
    }

    private fun FragmentActionsPickScreenBinding.initIsAvailableToEndFirstSetupObserver() {
        actionsPickFragmentViewModel.isAvailableToEndFirstSetup.observe(viewLifecycleOwner) { isAvailable ->
            context?.let {
                if (isAvailable) {
                    nextButton.text = it.getString(R.string.nextButtonText)
                }

                if (isOnboardingPassed(requireContext())) {
                    nextButton.text = getString(R.string.ok)

                }

                if (!isAvailable) {
                    nextButton.text = it.getString(R.string.incompleteActionPickingButtonText)
                }
            }

            nextButton.isEnabled = isAvailable
        }
    }

    private fun FragmentActionsPickScreenBinding.initNextButtonOnClickListener() {
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

    private fun FragmentActionsPickScreenBinding.initActionsPickingRecycler() {
        actionsPickingRecycler.layoutManager = GridLayoutManager(
            requireContext(),
            2,
            GridLayoutManager.VERTICAL,
            false
        )
    }

    interface ParentActivityCallback {
        fun callItemPicking(itemId: Int)
    }
}