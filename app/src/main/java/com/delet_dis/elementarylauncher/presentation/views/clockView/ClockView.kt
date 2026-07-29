package com.delet_dis.elementarylauncher.presentation.views.clockView

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.LifecycleOwner
import com.delet_dis.elementarylauncher.R
import com.delet_dis.elementarylauncher.databinding.ClockViewBinding
import com.delet_dis.elementarylauncher.presentation.views.clockView.viewModel.ClockViewViewModel

/**
 * Custom view used to display the clock widget. Its Fragment supplies lifecycle ownership.
 */
class ClockView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    private val binding: ClockViewBinding

    init {
        inflate(context, R.layout.clock_view, this).also { view ->
            binding = ClockViewBinding.bind(view)
        }
    }

    fun bind(
        lifecycleOwner: LifecycleOwner,
        viewModel: ClockViewViewModel,
        parentActivityCallback: ParentActivityCallback
    ) {
        binding.cardView.setOnClickListener {
            parentActivityCallback.callToHideHomescreenBottomSheet()
        }
        binding.cardView.setOnLongClickListener {
            parentActivityCallback.callHomescreenBottomSheet()
            true
        }

        viewModel.dateLiveData.observe(lifecycleOwner) { binding.dateStamp.text = it }
        viewModel.timeLiveData.observe(lifecycleOwner) { binding.timeStamp.text = it }
        viewModel.isAlarmEnabled.observe(lifecycleOwner) { isAlarmEnabled ->
            val visibility = if (isAlarmEnabled) View.VISIBLE else View.GONE
            binding.alarmImage.visibility = visibility
            binding.alarmStamp.visibility = visibility
        }
        viewModel.nextAlarmTriggerTime.observe(lifecycleOwner) {
            binding.alarmStamp.text = it.orEmpty()
        }
    }

    interface ParentActivityCallback {
        fun callHomescreenBottomSheet()
        fun callToHideHomescreenBottomSheet()
    }
}
