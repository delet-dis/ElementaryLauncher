package com.delet_dis.elementarylauncher.common.view

import android.app.Application
import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.LifecycleOwner
import com.delet_dis.elementarylauncher.R
import com.delet_dis.elementarylauncher.databinding.ClockViewBinding
import com.delet_dis.elementarylauncher.viewmodels.ClockViewViewModel

class ClockView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    private val binding: ClockViewBinding

    private val clockViewViewModel: ClockViewViewModel

    private var parentActivityCallback: ParentActivityCallback

    init {
        inflate(context, R.layout.clock_view, this).also {
            binding = ClockViewBinding.bind(it)
        }

        parentActivityCallback = context as ParentActivityCallback

        clockViewViewModel = ClockViewViewModel(context.applicationContext as Application)

        initCardViewOnClicksCallbacks()

        initDateObserver()
        initTimeObserver()
        initIsAlarmEnabledObserver()
        initAlarmTimeObserver()
    }

    private fun initCardViewOnClicksCallbacks() {
        with(binding.cardView) {
            setOnClickListener {
                parentActivityCallback.callToHideHomescreenBottomSheet()
            }
            setOnLongClickListener {
                parentActivityCallback.callHomescreenBottomSheet()
                true
            }
        }
    }

    private fun initDateObserver() {
        clockViewViewModel.dateLiveData.observe(context as LifecycleOwner, {
            binding.dateStamp.text = it
        })
    }

    private fun initTimeObserver() {
        clockViewViewModel.timeLiveData.observe(context as LifecycleOwner, {
            binding.timeStamp.text = it
        })
    }

    private fun initIsAlarmEnabledObserver() {
        clockViewViewModel.isAlarmEnabled.observe(context as LifecycleOwner, {
            binding.alarmImage.visibility = if (it) {
                View.VISIBLE
            } else {
                View.GONE
            }

            binding.alarmStamp.visibility = if (it) {
                View.VISIBLE
            } else {
                View.GONE
            }
        })
    }

    private fun initAlarmTimeObserver() {
        clockViewViewModel.nextAlarmTriggerTime.observe(context as LifecycleOwner, {
            binding.alarmStamp.text = it
        })
    }

    interface ParentActivityCallback {
        fun callHomescreenBottomSheet()
        fun callToHideHomescreenBottomSheet()
    }
}