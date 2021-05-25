package com.delet_dis.elementarylauncher.recyclerViewAdapters

import android.appwidget.AppWidgetProviderInfo
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.delet_dis.elementarylauncher.R
import com.delet_dis.elementarylauncher.databinding.RecyclerviewItemActionBinding


class WidgetsPickingAdapter(
    private val values: MutableList<AppWidgetProviderInfo>,
    val clickListener: (AppWidgetProviderInfo) -> Unit
) :
    RecyclerView.Adapter<WidgetsPickingAdapter.WidgetHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): WidgetHolder {
        return WidgetHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.recyclerview_item_widget, parent, false)
        )
    }

    override fun onBindViewHolder(holder: WidgetHolder, position: Int) {
        with(values[position]) {
            holder.bind(this)
        }
    }

    override fun getItemCount(): Int = values.size

    inner class WidgetHolder(view: View) :
        RecyclerView.ViewHolder(view) {
        private val binding: RecyclerviewItemActionBinding =
            RecyclerviewItemActionBinding.bind(view)

        private val packageManager = view.context.packageManager

        private val context = view.context

        fun bind(data: AppWidgetProviderInfo) = with(binding) {
            actionCard.setOnClickListener {
                clickListener(data)
            }

            actionImage.setImageDrawable(
                data.loadPreviewImage(
                    context,
                    DisplayMetrics.DENSITY_MEDIUM
                )
            )

            actionName.text = data.loadLabel(packageManager)
        }
    }
}
