package com.delet_dis.elementarylauncher.presentation.activities.onboardingActivity.fragments

import android.content.Intent
import android.content.ActivityNotFoundException
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.delet_dis.elementarylauncher.R
import com.delet_dis.elementarylauncher.databinding.FragmentSetAsHomescreenScreenBinding
import com.delet_dis.elementarylauncher.domain.extensions.checkIfAppIsDefaultLauncher

/**
 * Fragment suggesting setting as default screen.
 */
class SetAsHomescreenFragment : Fragment() {
    private var _binding: FragmentSetAsHomescreenScreenBinding? = null
    private val binding get() = checkNotNull(_binding)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentSetAsHomescreenScreenBinding.inflate(inflater, container, false)
        .also { _binding = it }
        .root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkIfAppIsLauncher()
        initBackButtonOnClickListener()
        setSkipButtonOnClickListener()
    }

    private fun checkIfAppIsLauncher() =
        with(binding) {
            if (checkIfAppIsDefaultLauncher(requireContext())) {
                makeGoToSettingsButtonAsNextButton()
            } else {
                goToSettingsButton.setOnClickListener {
                    openHomeSettings()
                }
            }
        }

    private fun initBackButtonOnClickListener() =
        with(binding) {
            backButton.setOnClickListener {
                requireActivity().findNavController(R.id.navigationOnboardingControllerContainerView)
                    .popBackStack()
            }
        }

    override fun onResume() {
        super.onResume()

        if (_binding == null) return
        if (checkIfAppIsDefaultLauncher(requireContext())) {
            makeGoToSettingsButtonAsNextButton()
        } else {
            makeGoToSettingsButtonAsGoToSettingsButton()
        }
    }

    private fun makeGoToSettingsButtonAsNextButton() =
        with(binding.goToSettingsButton) {
            text = requireContext().getString(R.string.nextButtonText)
            setOnClickListener {
                requireActivity().findNavController(R.id.navigationOnboardingControllerContainerView)
                    .navigate(R.id.action_setAsHomescreenFragment_to_setupDoneFragment)
            }
        }

    private fun makeGoToSettingsButtonAsGoToSettingsButton() =
        with(binding.goToSettingsButton) {
            text =
                requireContext().getString(R.string.goToSettingsSetAsHomescreenHintTextSetupButtonText)
            setOnClickListener {
                openHomeSettings()
            }
        }

    private fun openHomeSettings() {
        try {
            startActivity(Intent(Settings.ACTION_HOME_SETTINGS))
        } catch (_: ActivityNotFoundException) {
            runCatching { startActivity(Intent(Settings.ACTION_SETTINGS)) }
        } catch (_: SecurityException) {
            runCatching { startActivity(Intent(Settings.ACTION_SETTINGS)) }
        }
    }

    private fun setSkipButtonOnClickListener() =
        with(binding) {
            skipButton.setOnClickListener {
                requireActivity().findNavController(R.id.navigationOnboardingControllerContainerView)
                    .navigate(R.id.action_setAsHomescreenFragment_to_setupDoneFragment)
            }
        }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
