package com.delet_dis.elementarylauncher.recyclerViewAdapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.delet_dis.elementarylauncher.common.models.HomescreenActionType
import com.delet_dis.elementarylauncher.databinding.RecyclerviewItemActionBinding

class OnHomescreenActionsPickingAdapter(
    private val values: Array<HomescreenActionType>,
    val clickListener: (HomescreenActionType) -> Unit
) :
    RecyclerView.Adapter<OnHomescreenActionsPickingAdapter.ActionHolder>() {

    private lateinit var binding: RecyclerviewItemActionBinding

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ActionHolder {
        binding = RecyclerviewItemActionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return ActionHolder(binding)
    }

    override fun onBindViewHolder(holder: ActionHolder, position: Int) {
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

    inner class ActionHolder(binding: RecyclerviewItemActionBinding) :
        RecyclerView.ViewHolder(binding.root)
}
