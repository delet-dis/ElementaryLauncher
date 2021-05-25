package com.delet_dis.elementarylauncher.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.delet_dis.elementarylauncher.R
import com.delet_dis.elementarylauncher.common.extensions.checkIfAppIsDefaultLauncher
import com.delet_dis.elementarylauncher.databinding.FragmentSetAsHomescreenScreenBinding

class SetAsHomescreenFragment : Fragment() {
    private lateinit var binding: FragmentSetAsHomescreenScreenBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return if (savedInstanceState == null) {
            binding = FragmentSetAsHomescreenScreenBinding.inflate(layoutInflater)

            binding.root
        } else {
            view
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState == null) {
            with(binding) {
                checkIfAppIsLauncher()

                initBackButtonOnClickListener()

                setSkipButtonOnClickListener()
            }
        }
    }

    private fun FragmentSetAsHomescreenScreenBinding.checkIfAppIsLauncher() {
        if (checkIfAppIsDefaultLauncher(requireContext())) {
            makeGoToSettingsButtonAsNextButton()
        } else {
            goToSettingsButton.setOnClickListener {
                startActivity(Intent(Settings.ACTION_HOME_SETTINGS))
            }
        }
    }

    private fun FragmentSetAsHomescreenScreenBinding.initBackButtonOnClickListener() {
        backButton.setOnClickListener {
            requireActivity().findNavController(R.id.navigationOnboardingControllerContainerView)
                .popBackStack()
        }
    }

    override fun onResume() {
        super.onResume()

        if (checkIfAppIsDefaultLauncher(requireContext())) {
            makeGoToSettingsButtonAsNextButton()
        } else {
            makeGoToSettingsButtonAsGoToSettingsButton()
        }
    }

    private fun makeGoToSettingsButtonAsNextButton() {
        with(binding.goToSettingsButton) {
            text = requireContext().getString(R.string.nextButtonText)
            setOnClickListener {
                requireActivity().findNavController(R.id.navigationOnboardingControllerContainerView)
                    .navigate(R.id.action_setAsHomescreenFragment_to_setupDoneFragment)
            }
        }
    }

    private fun makeGoToSettingsButtonAsGoToSettingsButton() {
        with(binding.goToSettingsButton) {
            text =
                requireContext().getString(R.string.goToSettingsSetAsHomescreenHintTextSetupButtonText)
            setOnClickListener {
                startActivity(Intent(Settings.ACTION_HOME_SETTINGS))
            }
        }
    }

    private fun FragmentSetAsHomescreenScreenBinding.setSkipButtonOnClickListener() {
        skipButton.setOnClickListener {
            requireActivity().findNavController(R.id.navigationOnboardingControllerContainerView)
                .navigate(R.id.action_setAsHomescreenFragment_to_setupDoneFragment)
        }
    }
}