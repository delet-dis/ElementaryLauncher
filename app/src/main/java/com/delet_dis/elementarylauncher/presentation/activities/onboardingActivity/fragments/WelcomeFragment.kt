package com.delet_dis.elementarylauncher.presentation.activities.onboardingActivity.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.delet_dis.elementarylauncher.R
import com.delet_dis.elementarylauncher.data.interfaces.FragmentParentInterface
import com.delet_dis.elementarylauncher.databinding.FragmentWelcomeScreenBinding
import com.delet_dis.elementarylauncher.domain.extensions.checkIfAppIsDefaultLauncher
import com.delet_dis.elementarylauncher.domain.extensions.isOnboardingPassed
import com.delet_dis.elementarylauncher.domain.repositories.SharedPreferencesRepository

/**
 * Fragment showing the welcome screen.
 */
class WelcomeFragment : Fragment(), FragmentParentInterface {
    private lateinit var binding: FragmentWelcomeScreenBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return if (savedInstanceState == null) {
            binding = FragmentWelcomeScreenBinding.inflate(layoutInflater)

            binding.root
        } else {
            view
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkIfSetupIsDone()

        if (savedInstanceState == null) {
            initNextButtonOnClickListener()
        }
    }

    private fun checkIfSetupIsDone() {
        if (checkIfAppIsDefaultLauncher(requireContext()) && SharedPreferencesRepository(
                requireContext()
            ).isActionsPicked() && !isOnboardingPassed(requireContext())
        ) {
            requireActivity().findNavController(R.id.navigationOnboardingControllerContainerView)
                .navigate(R.id.action_welcomeFragment_to_setupDoneFragment)
        }
    }

    private fun initNextButtonOnClickListener() =
        with(binding) {
            getStartedButton.setOnClickListener {
                requireActivity().findNavController(R.id.navigationOnboardingControllerContainerView)
                    .navigate(R.id.action_welcomeFragment_to_layoutPickScreenFragment_animated)
            }
        }

    override fun getFragmentId(): Int {
        return R.id.welcomeFragment
    }
}
