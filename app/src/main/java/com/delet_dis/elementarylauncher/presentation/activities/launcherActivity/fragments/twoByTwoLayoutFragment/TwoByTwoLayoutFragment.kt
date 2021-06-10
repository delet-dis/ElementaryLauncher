package com.delet_dis.elementarylauncher.presentation.activities.launcherActivity.fragments.twoByTwoLayoutFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.delet_dis.elementarylauncher.databinding.FragmentTwoByTwoLayoutBinding
import com.delet_dis.elementarylauncher.domain.helpers.createIntro
import com.delet_dis.elementarylauncher.presentation.activities.launcherActivity.fragments.twoByTwoLayoutFragment.viewModel.TwoByTwoLayoutFragmentViewModel
import com.delet_dis.elementarylauncher.presentation.views.appCardView.AppCardView
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
class TwoByTwoLayoutFragment : Fragment() {
    private lateinit var binding: FragmentTwoByTwoLayoutBinding

    private lateinit var twoByTwoLayoutFragmentViewModel: TwoByTwoLayoutFragmentViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return if (savedInstanceState == null) {
            binding = FragmentTwoByTwoLayoutBinding.inflate(layoutInflater)

            twoByTwoLayoutFragmentViewModel =
                TwoByTwoLayoutFragmentViewModel(requireActivity().application)

            binding.root
        } else {
            view
        }
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
}