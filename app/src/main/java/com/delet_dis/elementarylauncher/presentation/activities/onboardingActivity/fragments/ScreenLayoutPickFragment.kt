package com.delet_dis.elementarylauncher.presentation.activities.onboardingActivity.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.delet_dis.elementarylauncher.R
import com.delet_dis.elementarylauncher.data.interfaces.FragmentParentInterface
import com.delet_dis.elementarylauncher.data.models.LayoutType
import com.delet_dis.elementarylauncher.databinding.FragmentLayoutPickScreenBinding
import com.delet_dis.elementarylauncher.domain.extensions.isOnboardingPassed
import com.delet_dis.elementarylauncher.domain.repositories.SharedPreferencesRepository

class ScreenLayoutPickFragment : Fragment(), FragmentParentInterface {
    private lateinit var binding: FragmentLayoutPickScreenBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return if (savedInstanceState == null) {
            binding = FragmentLayoutPickScreenBinding.inflate(layoutInflater)

            binding.root
        } else {
            view
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState == null) {
            initLayoutCardsListeners()

            initBackButtonOnClickListener()

            initNextButtonParams()
        }
    }

    private fun initBackButtonOnClickListener() =
        with(binding) {
            if (isOnboardingPassed(requireContext())) {
                backButton.setOnClickListener {
                    requireActivity().finish()
                }
            } else {
                backButton.setOnClickListener {
                    requireActivity().findNavController(R.id.navigationOnboardingControllerContainerView)
                        .popBackStack()
                }
            }
        }

    private fun initNextButtonParams() =
        with(binding) {
            if (isOnboardingPassed(requireContext())) {
                nextButton.text = getString(R.string.ok)
            }

            nextButton.setOnClickListener {
                var pickedLayout = LayoutType.TWO_BY_THREE

                when (binding.radioGroup.checkedRadioButtonId) {
                    twoByThreeRadio.id -> pickedLayout = LayoutType.TWO_BY_THREE
                    twoByTwoRadio.id -> pickedLayout = LayoutType.TWO_BY_TWO
                }

                SharedPreferencesRepository(requireContext())
                    .setLayoutType(pickedLayout)

                if (isOnboardingPassed(requireContext())) {
                    requireActivity().finish()
                } else {
                    requireActivity().findNavController(R.id.navigationOnboardingControllerContainerView)
                        .navigate(R.id.action_layoutPickScreenFragment_to_interfaceScalePickFragment)
                }
            }
        }

    private fun initLayoutCardsListeners() =
        with(binding) {
            twoByThreeCard.setOnClickListener {
                binding.twoByThreeRadio.isChecked = true
            }

            twoByTwoCard.setOnClickListener {
                binding.twoByTwoRadio.isChecked = true
            }
        }

    override fun getFragmentId(): Int {
        return R.id.screenLayoutPickFragment
    }
}