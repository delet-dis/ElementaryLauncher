package com.delet_dis.elementarylauncher.presentation.activities.launcherActivity.recyclerViewAdapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.delet_dis.elementarylauncher.data.models.HomescreenActionType
import com.delet_dis.elementarylauncher.databinding.RecyclerviewItemActionBinding

class OnHomescreenActionsPickingAdapter(
    private val values: Array<HomescreenActionType>,
    val clickListener: (HomescreenActionType) -> Unit
) : RecyclerView.Adapter<OnHomescreenActionsPickingAdapter.ActionHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ActionHolder =
        ActionHolder(
            RecyclerviewItemActionBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ActionHolder, position: Int) {
        with(values[position]) {
            with(holder.binding) {
                actionImage.setImageDrawable(
                    ContextCompat.getDrawable(
                        holder.itemView.context,
                        imageId
                    )
                )

                actionName.text = holder.itemView.context.getString(stringId)
            }
        }
        holder.binding.actionCard.setOnClickListener { clickListener(values[position]) }
    }

    override fun getItemCount(): Int = values.size

    class ActionHolder(val binding: RecyclerviewItemActionBinding) :
        RecyclerView.ViewHolder(binding.root)
}
