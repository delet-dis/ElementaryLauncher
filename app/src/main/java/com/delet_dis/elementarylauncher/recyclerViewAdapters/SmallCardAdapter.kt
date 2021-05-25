package com.delet_dis.elementarylauncher.recyclerViewAdapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.drawable.toBitmap
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.RecyclerView
import com.delet_dis.elementarylauncher.R
import com.delet_dis.elementarylauncher.common.models.Card
import com.delet_dis.elementarylauncher.databinding.CardItemSmallBinding

class SmallCardAdapter(private val values: List<Card>, val clickListener: (Int) -> Unit) :
    RecyclerView.Adapter<SmallCardAdapter.SmallCardHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SmallCardHolder {
        return SmallCardHolder(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.card_item_small, parent, false)
        )
    }

    override fun onBindViewHolder(holder: SmallCardHolder, position: Int) {
        with(values[position]) {
            holder.bind(this)
        }
    }

    override fun getItemCount(): Int = values.size

    inner class SmallCardHolder(view: View) :
        RecyclerView.ViewHolder(view) {
        private val binding: CardItemSmallBinding =
            CardItemSmallBinding.bind(view)

        private val defaultColor = view.context.getColor(R.color.white)

        fun bind(data: Card) = with(binding) {
            cardView.setOnClickListener {
                clickListener(layoutPosition + 1)
            }

            if (data.icon == null) {
                data.text?.let {
                    cardText.text = it
                }
            }

            if (data.isWidget) {
                widgetCard.visibility = View.VISIBLE
            }

            data.icon?.let { drawable ->
                cardImage.setImageDrawable(drawable)

                Palette.Builder(drawable.toBitmap())
                    .generate {
                        if (it != null) {
                            cardView.setCardBackgroundColor(
                                it.getLightVibrantColor(
                                    defaultColor
                                )
                            )
                        }
                    }
            }
        }
    }
}