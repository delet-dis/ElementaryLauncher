package com.delet_dis.elementarylauncher.presentation.activities.onboardingActivity.recyclerViewAdapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.delet_dis.elementarylauncher.data.models.SettingsActionType
import com.delet_dis.elementarylauncher.databinding.RecyclerviewItemActionBinding

class SettingsActionPickingAdapter(
    private val values: Array<SettingsActionType>,
    val clickListener: (SettingsActionType) -> Unit
) : RecyclerView.Adapter<SettingsActionPickingAdapter.ToggleHolder>() {

    private lateinit var binding: RecyclerviewItemActionBinding

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ToggleHolder {
        binding = RecyclerviewItemActionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ToggleHolder(binding)
    }

    override fun onBindViewHolder(holder: ToggleHolder, position: Int) {
        with(values[position]) {
            with(binding) {
                actionImage.setImageDrawable(
                    ContextCompat.getDrawable(
                        holder.itemView.context,
                        imageId
                    )
                )

                actionName.text = holder.itemView.context.getString(stringId)

            }
        }
        binding.actionCard.setOnClickListener { clickListener(values[position]) }
    }

    override fun getItemCount(): Int = values.size

    inner class ToggleHolder(binding: RecyclerviewItemActionBinding) :
        RecyclerView.ViewHolder(binding.root)
}
