package com.delet_dis.elementarylauncher.ui.activities.onboardingActivity.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.delet_dis.elementarylauncher.R
import com.delet_dis.elementarylauncher.common.extensions.checkIfAppIsDefaultLauncher
import com.delet_dis.elementarylauncher.common.interfaces.FragmentParentInterface
import com.delet_dis.elementarylauncher.data.repositories.SharedPreferencesRepository
import com.delet_dis.elementarylauncher.databinding.FragmentSetupDoneScreenBinding
import com.delet_dis.elementarylauncher.ui.activities.launcherActivity.LauncherActivity
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class SetupDoneFragment : Fragment(), FragmentParentInterface {
    private lateinit var binding: FragmentSetupDoneScreenBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return if (savedInstanceState == null) {
            binding = FragmentSetupDoneScreenBinding.inflate(layoutInflater)

            binding.root
        } else {
            view
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!checkIfAppIsDefaultLauncher(requireContext())) {
            initBackButtonOnClickListener()
        }
        initFinishButtonOnClickListener()
    }

    private fun initFinishButtonOnClickListener() =
        with(binding) {
            finishButton.apply {
                visibility = View.VISIBLE

                this.setOnClickListener {
                    SharedPreferencesRepository(requireContext()).setOnboardingPassed()

                    startActivity(Intent(activity, LauncherActivity::class.java))
                    activity?.finish()
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

    override fun getFragmentId(): Int {
        return R.id.setupDoneFragment
    }
}