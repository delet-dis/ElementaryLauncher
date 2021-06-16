package com.delet_dis.elementarylauncher.presentation.activities.launcherActivity.fragments.twoByTwoLayoutFragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.delet_dis.elementarylauncher.databinding.FragmentTwoByTwoLayoutBinding
import com.delet_dis.elementarylauncher.domain.helpers.createIntro
import com.delet_dis.elementarylauncher.presentation.activities.launcherActivity.fragments.twoByThreeLayoutFragment.viewModel.TwoByThreeLayoutFragmentViewModel
import com.delet_dis.elementarylauncher.presentation.views.appCardView.AppCardView
import com.delet_dis.elementarylauncher.presentation.views.clockView.ClockView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class TwoByTwoLayoutFragment : Fragment(), ClockView.ParentFragmentCallback {
    private lateinit var binding: FragmentTwoByTwoLayoutBinding

    private val twoByTwoLayoutFragmentViewModel by viewModels<TwoByThreeLayoutFragmentViewModel>()

    private lateinit var parentActivityCallback: ParentActivityCallback

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return if (savedInstanceState == null) {
            binding = FragmentTwoByTwoLayoutBinding.inflate(layoutInflater)

            binding.root
        } else {
            view
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        parentActivityCallback = context as ParentActivityCallback
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState == null) {
            binding.clockView?.let { clockView ->
                createIntro(this, requireActivity(), clockView)
            }

            twoByTwoLayoutFragmentViewModel.databaseRecordingsLiveData.observe(viewLifecycleOwner)
            { list ->
                binding.cardsGroup.referencedIds.forEachIndexed { index, i ->
                    list[index]?.let { card ->
                        requireView().findViewById<AppCardView>(i).card = card
                    }
                }
            }
        }
    }

    override fun callHomescreenBottomSheet() {
        parentActivityCallback.callHomescreenBottomSheetToParentActivity()
    }

    override fun callToHideHomescreenBottomSheet() {
        parentActivityCallback.callToHideHomescreenBottomSheetToParentActivity()
    }

    interface ParentActivityCallback {
        fun callHomescreenBottomSheetToParentActivity()
        fun callToHideHomescreenBottomSheetToParentActivity()
    }
}