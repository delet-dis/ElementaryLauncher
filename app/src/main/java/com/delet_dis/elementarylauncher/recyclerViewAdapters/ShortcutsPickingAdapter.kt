package com.delet_dis.elementarylauncher.recyclerViewAdapters

import android.content.pm.ResolveInfo
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.delet_dis.elementarylauncher.R
import com.delet_dis.elementarylauncher.databinding.RecyclerviewItemActionBinding


class ShortcutsPickingAdapter(
    private val values: MutableList<ResolveInfo>,
    val clickListener: (ResolveInfo) -> Unit
) :
    RecyclerView.Adapter<ShortcutsPickingAdapter.ShortcutHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ShortcutHolder {
        return ShortcutHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.recyclerview_item_action, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ShortcutHolder, position: Int) {
        with(values[position]) {
            holder.bind(this)
        }
    }

    override fun getItemCount(): Int = values.size

    inner class ShortcutHolder(view: View) :
        RecyclerView.ViewHolder(view) {
        private val binding: RecyclerviewItemActionBinding =
            RecyclerviewItemActionBinding.bind(view)

        private val packageManager = view.context.packageManager

        fun bind(data: ResolveInfo) = with(binding) {
            actionCard.setOnClickListener {
                clickListener(data)
            }

            actionImage.setImageDrawable(data.loadIcon(packageManager))

            actionName.text = data.loadLabel(packageManager)
        }
    }
}
