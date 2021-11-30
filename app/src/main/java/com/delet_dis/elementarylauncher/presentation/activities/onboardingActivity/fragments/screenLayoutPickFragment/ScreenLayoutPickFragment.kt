package com.delet_dis.elementarylauncher.presentation.activities.onboardingActivity.fragments.screenLayoutPickFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.delet_dis.elementarylauncher.R
import com.delet_dis.elementarylauncher.data.interfaces.FragmentParentInterface
import com.delet_dis.elementarylauncher.data.models.LayoutType
import com.delet_dis.elementarylauncher.databinding.FragmentLayoutPickScreenBinding
import com.delet_dis.elementarylauncher.domain.extensions.isOnboardingPassed
import com.delet_dis.elementarylauncher.presentation.activities.onboardingActivity.fragments.screenLayoutPickFragment.viewModel.ScreenLayoutPickFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * Fragment used to display the layout setting for home screen.
 */
@AndroidEntryPoint
class ScreenLayoutPickFragment : Fragment(), FragmentParentInterface {
    private lateinit var binding: FragmentLayoutPickScreenBinding

    private val viewModel: ScreenLayoutPickFragmentViewModel by viewModels()

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
            initRadioButtonsListener()

            loadTempRadioConfiguration()

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
                    requireActivity().findNavController(
                        R.id.navigationOnboardingControllerContainerView
                    )
                        .popBackStack()
                }
            }
        }

    private fun loadTempRadioConfiguration() =
        with(binding) {
            when (viewModel.getTempLayoutType()) {
                1 -> {
                    twoByThreeRadio.isChecked = true
                }
                2 -> {
                    twoByTwoRadio.isChecked = true
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
                    twoByThreeRadio.id -> {
                        pickedLayout = LayoutType.TWO_BY_THREE
                    }
                    twoByTwoRadio.id -> {
                        pickedLayout = LayoutType.TWO_BY_TWO
                    }
                }

                viewModel.setLayoutType(pickedLayout)

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
                viewModel.setTempLayoutType(1)
            }

            twoByTwoCard.setOnClickListener {
                binding.twoByTwoRadio.isChecked = true
                viewModel.setTempLayoutType(2)
            }
        }

    private fun initRadioButtonsListener() {
        with(binding) {
            twoByThreeRadio.setOnClickListener {
                viewModel.setTempLayoutType(1)
            }

            twoByTwoRadio.setOnClickListener {
                viewModel.setTempLayoutType(2)
            }
        }
    }

    override fun getFragmentId(): Int {
        return R.id.screenLayoutPickFragment
    }
}
