package com.delet_dis.elementarylauncher.presentation.views.appCardView

import android.animation.ValueAnimator
import android.appwidget.AppWidgetHost
import android.appwidget.AppWidgetHostView
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProviderInfo
import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.graphics.drawable.toBitmap
import androidx.palette.graphics.Palette
import com.delet_dis.elementarylauncher.R
import com.delet_dis.elementarylauncher.data.models.Card
import com.delet_dis.elementarylauncher.data.models.SizeType
import com.delet_dis.elementarylauncher.databinding.CardItemBigBinding
import com.delet_dis.elementarylauncher.domain.repositories.SharedPreferencesRepository


class AppCardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private lateinit var widgetHost: AppWidgetHost
    private lateinit var widgetManager: AppWidgetManager

    private val binding: CardItemBigBinding

    private val defaultIconSize = 55

    private val scaleCorrectionCoefficient = 1.5f

    var size: SizeType = SizeType.MEDIUM
        set(value) {
            applySize(value)
        }

    var card: Card = Card()
        set(value) {
            applyCard(value)
        }

    init {
        inflate(
            context,
            R.layout.card_item_big,
            this
        ).also { view ->
            binding = CardItemBigBinding.bind(view)
        }

        applySize(SharedPreferencesRepository(context).getSizeType())
    }

    private fun applyCard(card: Card) {
        val defaultColor = context.getColor(R.color.white)
        with(card) {
            with(binding) {
                cardView.visibility = View.VISIBLE
                if (card.isWidget) {
                    widgetHost = AppWidgetHost(context, binding.cardView.id)
                    widgetManager = AppWidgetManager.getInstance(context)

                    card.widgetId?.let { widgetId ->
                        val appWidgetInfo: AppWidgetProviderInfo =
                            widgetManager.getAppWidgetInfo(widgetId)

                        val hostView: AppWidgetHostView =
                            widgetHost.createView(context, widgetId, appWidgetInfo)

                        hostView.setAppWidget(widgetId, appWidgetInfo)

                        cardView.addView(hostView)

                        widgetHost.startListening()
                    }

                    cardConstraint.visibility = View.GONE

                } else {
                    name?.let { name ->
                        cardText.text = name
                    }

                    if (icon == null) {
                        cardSubText.text = text
                    }

                    icon?.let { drawable ->
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

                    onClickAction?.let { action ->
                        cardView.setOnClickListener {
                            action()
                        }
                    }
                }
            }
        }
    }

    private fun applySize(sizeType: SizeType) = with(binding) {
        val scale = sizeType.scaleCoefficient

        animateFirstGuideline(sizeType)

        animateSecondGuideline(sizeType)

        animateCardText(scale)
    }

    private fun animateFirstGuideline(
        sizeType: SizeType
    ) = with(binding) {
        val valueAnimatorForFirstGuideline = ValueAnimator.ofFloat(
            (firstGuideline.layoutParams as LayoutParams).guidePercent,
            sizeType.firstGuidelinePercentage
        )

        with(valueAnimatorForFirstGuideline) {
            duration = 300

            interpolator = AccelerateDecelerateInterpolator()

            addUpdateListener { valueAnimator ->
                val layoutParams = firstGuideline.layoutParams as LayoutParams

                with(layoutParams) {
                    guidePercent = valueAnimator.animatedValue as Float

                    firstGuideline.layoutParams = this
                }
            }
            start()
        }
    }

    private fun animateSecondGuideline(
        sizeType: SizeType
    ) = with(binding) {
        val valueAnimatorForSecondGuideline = ValueAnimator.ofFloat(
            (secondGuideline.layoutParams as LayoutParams).guidePercent,
            sizeType.secondGuidelinePercentage
        )

        with(valueAnimatorForSecondGuideline) {
            duration = 300

            interpolator = AccelerateDecelerateInterpolator()

            addUpdateListener { valueAnimator ->
                val layoutParams = secondGuideline.layoutParams as LayoutParams

                with(layoutParams) {
                    guidePercent = valueAnimator.animatedValue as Float

                    secondGuideline.layoutParams = this
                }
            }
            start()
        }
    }

    private fun animateCardText(scale: Float) = with(binding) {
        val valueAnimator =
            ValueAnimator.ofFloat(
                cardText.textSize,
                defaultIconSize * scale / scaleCorrectionCoefficient
            )

        with(valueAnimator) {
            duration = 200

            this.addUpdateListener {
                cardText.setTextSize(TypedValue.COMPLEX_UNIT_PX, animatedValue as Float)
            }

            start()
        }
    }
}