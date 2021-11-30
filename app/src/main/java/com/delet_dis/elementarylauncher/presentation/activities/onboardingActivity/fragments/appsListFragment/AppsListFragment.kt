package com.delet_dis.elementarylauncher.presentation.activities.onboardingActivity.fragments.appsListFragment

import android.content.Intent
import android.content.pm.ApplicationInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.delet_dis.elementarylauncher.R
import com.delet_dis.elementarylauncher.data.interfaces.FragmentParentInterface
import com.delet_dis.elementarylauncher.databinding.FragmentAppsListScreenBinding
import com.delet_dis.elementarylauncher.presentation.activities.onboardingActivity.fragments.appsListFragment.viewModel.AppsListFragmentViewModel
import com.delet_dis.elementarylauncher.presentation.activities.onboardingActivity.recyclerViewAdapters.AppsPickingAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * Fragment used to display the list of installed applications available to launch.
 */
@AndroidEntryPoint
@ExperimentalCoroutinesApi
class AppsListFragment : Fragment(), FragmentParentInterface {
    private lateinit var binding: FragmentAppsListScreenBinding

    private val viewModel: AppsListFragmentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return if (savedInstanceState == null) {
            binding = FragmentAppsListScreenBinding.inflate(layoutInflater)


            binding.root
        } else {
            view
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initBackButtonOnClickListener()

        initItemPickRecycler()

        initIsLoadingObserver()

        with(viewModel) {
            loadApplicationsPackages()
            applicationsPackagesLiveData
                .observe(viewLifecycleOwner, { mutableList ->

                    binding.itemPickRecycler.adapter =
                        AppsPickingAdapter(
                            mutableList as MutableList<ApplicationInfo>
                        ) { applicationInfo ->

                            with(requireActivity()) {
                                startActivity(
                                    Intent(
                                        requireContext().packageManager.getLaunchIntentForPackage(
                                            applicationInfo.packageName
                                        )
                                    )
                                )
                                finish()
                            }
                        }
                })
        }
    }

    private fun initIsLoadingObserver() {
        viewModel.isLoading.observe(viewLifecycleOwner, { isLoading ->
            if (isLoading) {
                showListLoading()
            } else {
                hideListLoading()
            }
        })
    }

    private fun hideListLoading() {
        binding.itemPickRecycler.visibility = View.VISIBLE

        binding.progressBar.visibility = View.INVISIBLE
    }

    private fun showListLoading() {
        binding.itemPickRecycler.visibility = View.INVISIBLE

        binding.progressBar.visibility = View.VISIBLE
    }

    private fun initItemPickRecycler() =
        with(binding) {
            itemPickRecycler.layoutManager = LinearLayoutManager(
                requireContext(),
                LinearLayoutManager.VERTICAL,
                false
            )
        }

    private fun initBackButtonOnClickListener() =
        with(binding) {
            backButton.setOnClickListener {
                requireActivity().finish()
            }
        }

    override fun getFragmentId(): Int {
        return R.id.appsListFragment
    }
}
