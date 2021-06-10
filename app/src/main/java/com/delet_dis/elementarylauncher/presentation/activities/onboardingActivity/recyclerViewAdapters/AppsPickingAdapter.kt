package com.delet_dis.elementarylauncher.presentation.activities.onboardingActivity.recyclerViewAdapters

import android.content.pm.ApplicationInfo
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.delet_dis.elementarylauncher.R
import com.delet_dis.elementarylauncher.databinding.RecyclerviewItemActionBinding


class AppsPickingAdapter(
    private val values: MutableList<ApplicationInfo>,
    val clickListener: (ApplicationInfo) -> Unit
) : RecyclerView.Adapter<AppsPickingAdapter.AppHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AppHolder {
        return AppHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.recyclerview_item_action, parent, false)
        )
    }

    override fun onBindViewHolder(holder: AppHolder, position: Int) {
        with(values[position]) {
            holder.bind(this)
        }
    }

    override fun getItemCount(): Int = values.size

    inner class AppHolder(view: View) :
        RecyclerView.ViewHolder(view) {
        private val binding: RecyclerviewItemActionBinding =
            RecyclerviewItemActionBinding.bind(view)

        private val packageManager = view.context.packageManager

        fun bind(data: ApplicationInfo) = with(binding) {
            actionCard.setOnClickListener {
                clickListener(data)
            }

            actionImage.setImageDrawable(data.loadIcon(packageManager))

            actionName.text = data.loadLabel(packageManager)
        }
    }
}
