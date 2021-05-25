package com.delet_dis.elementarylauncher.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.delet_dis.elementarylauncher.R
import com.delet_dis.elementarylauncher.common.extensions.isOnboardingPassed
import com.delet_dis.elementarylauncher.common.mappers.mapEntityToCard
import com.delet_dis.elementarylauncher.common.models.SizeType
import com.delet_dis.elementarylauncher.data.database.entities.App
import com.delet_dis.elementarylauncher.data.repositories.SharedPreferencesRepository
import com.delet_dis.elementarylauncher.databinding.FragmentInterfaceScalePickScreenBinding

class InterfaceScalePickFragment : Fragment() {
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

    private fun FragmentInterfaceScalePickScreenBinding.initBackButtonOnClickListener() {
        backButton.setOnClickListener {
            if (isOnboardingPassed(requireContext())) {
                requireActivity().finish()
            } else {
                requireActivity().findNavController(R.id.navigationOnboardingControllerContainerView)
                    .popBackStack()
            }
        }
    }

    private fun FragmentInterfaceScalePickScreenBinding.initNextButtonParams() {
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

    private fun FragmentInterfaceScalePickScreenBinding.initSeekBarOnChangeListener() {
        interfaceScaleSeekBar.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(
                seekBar: SeekBar?,
                progress: Int,
                fromUser: Boolean
            ) {
                val size = SizeType.values()[progress]
                exampleCard.size = size

                SharedPreferencesRepository(requireContext()).setScale(size)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) = Unit

            override fun onStopTrackingTouch(seekBar: SeekBar?) = Unit
        }
        )
    }

    private fun FragmentInterfaceScalePickScreenBinding.applySeekBarPreferences() {
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
}