package com.delet_dis.elementarylauncher.presentation.activities.onboardingActivity.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.delet_dis.elementarylauncher.R
import com.delet_dis.elementarylauncher.data.database.entities.App
import com.delet_dis.elementarylauncher.data.interfaces.FragmentParentInterface
import com.delet_dis.elementarylauncher.data.mappers.mapEntityToCard
import com.delet_dis.elementarylauncher.data.models.SizeType
import com.delet_dis.elementarylauncher.databinding.FragmentInterfaceScalePickScreenBinding
import com.delet_dis.elementarylauncher.domain.extensions.isOnboardingPassed
import com.delet_dis.elementarylauncher.domain.repositories.SharedPreferencesRepository

class InterfaceScalePickFragment : Fragment(), FragmentParentInterface {
    private lateinit var binding: FragmentInterfaceScalePickScreenBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return if (savedInstanceState == null) {
            binding = FragmentInterfaceScalePickScreenBinding.inflate(layoutInflater)

            binding.root
        } else {
            view
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState == null) {
            with(binding) {
                initExampleCard(this)

                applySeekBarPreferences()

                initSeekBarOnChangeListener()

                initBackButtonOnClickListener()

                initNextButtonParams()
            }
        }
    }

    private fun initBackButtonOnClickListener() =
        with(binding) {
            backButton.setOnClickListener {
                if (isOnboardingPassed(requireContext())) {
                    requireActivity().finish()
                } else {
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
                if (isOnboardingPassed(requireContext())) {
                    requireActivity().finish()
                } else {
                    requireActivity().findNavController(R.id.navigationOnboardingControllerContainerView)
                        .navigate(R.id.action_interfaceScalePickFragment_to_actionsPickFragment)

                }
            }
        }

    private fun initSeekBarOnChangeListener() =
        with(binding) {
            interfaceScaleSeekBar.setOnSeekBarChangeListener(object :
                SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    val size = SizeType.values()[progress]
                    this@with.exampleCard.size = size

                    SharedPreferencesRepository(requireContext()).setScale(size)
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) = Unit

                override fun onStopTrackingTouch(seekBar: SeekBar?) = Unit
            }
            )
        }

    private fun applySeekBarPreferences() =
        with(binding) {
            interfaceScaleSeekBar.apply {
                max = SizeType.values().size - 1
                progress = max / 2
            }
        }

    private fun initExampleCard(
        fragmentInterfaceScalePickScreenBinding: FragmentInterfaceScalePickScreenBinding
    ) {
        with(fragmentInterfaceScalePickScreenBinding.exampleCard) {
            card = mapEntityToCard(
                App(
                    requireContext().packageName
                ), requireContext()
            )
        }
    }

    override fun getFragmentId(): Int {
        return R.id.interfaceScalePickFragment
    }
}