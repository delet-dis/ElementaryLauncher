package com.delet_dis.elementarylauncher.ui.fragments

import android.content.Intent
import android.content.pm.ApplicationInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.delet_dis.elementarylauncher.databinding.FragmentAppsListScreenBinding
import com.delet_dis.elementarylauncher.recyclerViewAdapters.AppsPickingAdapter
import com.delet_dis.elementarylauncher.viewmodels.AppsListFragmentViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class AppsListFragment : Fragment() {
    private lateinit var binding: FragmentAppsListScreenBinding

    private lateinit var appListFragmentViewModel: AppsListFragmentViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return if (savedInstanceState == null) {
            binding = FragmentAppsListScreenBinding.inflate(layoutInflater)

            appListFragmentViewModel = AppsListFragmentViewModel(requireActivity().application)

            binding.root
        } else {
            view
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            initBackButtonOnClickListener()

            initItemPickRecycler()

            initIsLoadingObserver()

            with(appListFragmentViewModel) {
                loadApplicationsPackages()
                applicationsPackagesLiveData
                    .observe(viewLifecycleOwner, { mutableList ->

                        itemPickRecycler.adapter =
                            AppsPickingAdapter(mutableList as MutableList<ApplicationInfo>) {
                                requireActivity().startActivity(
                                    Intent(
                                        requireContext().packageManager.getLaunchIntentForPackage(
                                            it.packageName
                                        )
                                    )
                                )
                            }
                    })
            }
        }
    }

    private fun initIsLoadingObserver() {
        appListFragmentViewModel.isLoading.observe(viewLifecycleOwner, {
            if (it) {
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

    private fun FragmentAppsListScreenBinding.initItemPickRecycler() {
        itemPickRecycler.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.VERTICAL,
            false
        )
    }


    private fun FragmentAppsListScreenBinding.initBackButtonOnClickListener() {
        backButton.setOnClickListener {
            requireActivity().finish()
        }
    }
}