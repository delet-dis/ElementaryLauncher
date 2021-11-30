package com.delet_dis.elementarylauncher.presentation.activities.onboardingActivity.recyclerViewAdapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.drawable.toBitmap
import androidx.palette.graphics.Palette
import androidx.recyclerview.widget.RecyclerView
import com.delet_dis.elementarylauncher.R
import com.delet_dis.elementarylauncher.data.models.Card
import com.delet_dis.elementarylauncher.databinding.CardItemSmallBinding


/**
 * Recycler view adapter used to display a mini-list of selected shortcuts on their config screen.
 */
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

        private val defaultColor = view.context.getColor(R.color.paletteDefaultColor)

        fun bind(data: Card) = with(binding) {
            cardView.setOnClickListener {
                clickListener(layoutPosition + 1)
            }

            if (data.icon == null) {
                data.text?.let { text ->
                    cardText.text = text
                }
            }

            if (data.isWidget) {
                widgetCard.visibility = View.VISIBLE
            }

            data.icon?.let { drawable ->
                cardImage.setImageDrawable(drawable)

                Palette.Builder(drawable.toBitmap())
                    .generate { palette ->
                        if (palette != null) {
                            cardView.setCardBackgroundColor(
                                palette.getLightVibrantColor(
                                    defaultColor
                                )
                            )
                        }
                    }
            }
        }
    }
}
