package com.delet_dis.elementarylauncher.presentation.activities.launcherActivity.fragments.twoByThreeLayoutFragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.delet_dis.elementarylauncher.databinding.FragmentTwoByThreeLayoutBinding
import com.delet_dis.elementarylauncher.domain.helpers.createIntro
import com.delet_dis.elementarylauncher.presentation.activities.launcherActivity.fragments.twoByThreeLayoutFragment.viewModel.TwoByThreeLayoutFragmentViewModel
import com.delet_dis.elementarylauncher.presentation.views.clockView.ClockView
import com.delet_dis.elementarylauncher.presentation.views.clockView.viewModel.ClockViewViewModel
import com.delet_dis.elementarylauncher.presentation.views.shortcutCardView.ShortcutCardView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * Fragment used to display a two-by-three grid of apps.
 */
@AndroidEntryPoint
@ExperimentalCoroutinesApi
class TwoByThreeLayoutFragment : Fragment() {
    private var _binding: FragmentTwoByThreeLayoutBinding? = null
    private val binding get() = checkNotNull(_binding)

    private val viewModel: TwoByThreeLayoutFragmentViewModel by viewModels()
    private val clockViewModel: ClockViewViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentTwoByThreeLayoutBinding.inflate(inflater, container, false)
        .also { _binding = it }
        .root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.clockView?.let { clockView ->
            clockView.bind(
                viewLifecycleOwner,
                clockViewModel,
                requireActivity() as ClockView.ParentActivityCallback
            )
            createIntro(this, requireActivity(), clockView)
        }

        viewModel.databaseRecordingsLiveData.observe(viewLifecycleOwner) { list ->
            binding.cardsGroup.referencedIds.forEachIndexed { index, id ->
                list.getOrNull(index)?.let { card ->
                    view.findViewById<ShortcutCardView>(id).card = card
                }
            }
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}
