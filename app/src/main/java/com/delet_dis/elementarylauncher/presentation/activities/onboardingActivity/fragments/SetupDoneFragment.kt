package com.delet_dis.elementarylauncher.presentation.activities.onboardingActivity.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.delet_dis.elementarylauncher.R
import com.delet_dis.elementarylauncher.data.interfaces.FragmentParentInterface
import com.delet_dis.elementarylauncher.databinding.FragmentSetupDoneScreenBinding
import com.delet_dis.elementarylauncher.domain.extensions.checkIfAppIsDefaultLauncher
import com.delet_dis.elementarylauncher.domain.repositories.SharedPreferencesRepository
import com.delet_dis.elementarylauncher.presentation.activities.launcherActivity.LauncherActivity
import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * Fragment notifying the end of the first setup.
 */
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
