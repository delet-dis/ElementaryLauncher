package com.delet_dis.elementarylauncher.presentation.views.clockView

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.LifecycleOwner
import com.delet_dis.elementarylauncher.R
import com.delet_dis.elementarylauncher.databinding.ClockViewBinding
import com.delet_dis.elementarylauncher.presentation.views.clockView.viewModel.ClockViewViewModel
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@AndroidEntryPoint
class ClockView @JvmOverloads constructor(
    @ApplicationContext context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    private val binding: ClockViewBinding

    @Inject
    lateinit var clockViewViewModel:ClockViewViewModel

    private var parentFragmentCallback: ParentFragmentCallback

    init {
        inflate(context.applicationContext, R.layout.clock_view, this).also { view ->
            binding = ClockViewBinding.bind(view)
        }

        parentFragmentCallback = context as ParentFragmentCallback

        initCardViewOnClicksCallbacks()

        initDateObserver()
        initTimeObserver()
        initIsAlarmEnabledObserver()
        initAlarmTimeObserver()
    }

    private fun initCardViewOnClicksCallbacks() =
        with(binding.cardView) {
            setOnClickListener {
                parentFragmentCallback.callToHideHomescreenBottomSheet()
            }
            setOnLongClickListener {
                parentFragmentCallback.callHomescreenBottomSheet()
                true
            }
        }
    
    private fun initDateObserver() =
        clockViewViewModel.dateLiveData.observe(context as LifecycleOwner, { dateStamp ->
            binding.dateStamp.text = dateStamp
        })

    private fun initTimeObserver() =
        clockViewViewModel.timeLiveData.observe(context as LifecycleOwner, { timeStamp ->
            binding.timeStamp.text = timeStamp
        })

    private fun initIsAlarmEnabledObserver() =
        clockViewViewModel.isAlarmEnabled.observe(context as LifecycleOwner, { isAlarmEnabled ->
            binding.alarmImage.visibility = if (isAlarmEnabled) {
                View.VISIBLE
            } else {
                View.GONE
            }

            binding.alarmStamp.visibility = if (isAlarmEnabled) {
                View.VISIBLE
            } else {
                View.GONE
            }
        })

    private fun initAlarmTimeObserver() =
        clockViewViewModel.nextAlarmTriggerTime.observe(context as LifecycleOwner, { alarmStamp ->
            binding.alarmStamp.text = alarmStamp
        })

    interface ParentFragmentCallback {
        fun callHomescreenBottomSheet()
        fun callToHideHomescreenBottomSheet()
    }
}
