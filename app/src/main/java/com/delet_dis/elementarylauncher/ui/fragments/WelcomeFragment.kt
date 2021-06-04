package com.delet_dis.elementarylauncher.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.delet_dis.elementarylauncher.R
import com.delet_dis.elementarylauncher.common.extensions.checkIfAppIsDefaultLauncher
import com.delet_dis.elementarylauncher.common.extensions.isOnboardingPassed
import com.delet_dis.elementarylauncher.common.interfaces.FragmentParentInterface
import com.delet_dis.elementarylauncher.data.repositories.SharedPreferencesRepository
import com.delet_dis.elementarylauncher.databinding.FragmentWelcomeScreenBinding

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
            with(binding) {
                initNextButtonOnClickListener()
            }
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
