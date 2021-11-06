package com.delet_dis.elementarylauncher.presentation.views.shortcutCardView

import android.animation.ValueAnimator
import android.appwidget.AppWidgetHost
import android.appwidget.AppWidgetHostView
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProviderInfo
import android.content.Context
import android.graphics.drawable.Drawable
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


class ShortcutCardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private lateinit var widgetHost: AppWidgetHost
    private lateinit var widgetManager: AppWidgetManager

    private val binding: CardItemBigBinding

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

    private fun applyCard(card: Card) =
        with(binding) {
            cardView.visibility = View.VISIBLE

            if (card.isWidget) {
                cardConstraint.visibility = View.GONE

                card.widgetId?.let { widgetId ->
                    addWidget(widgetId)
                }
            } else {
                card.name?.let { name ->
                    cardText.text = name
                }

                card.icon?.let { drawable ->
                    cardImage.setImageDrawable(drawable)

                    setCardBackgroundColorBasedOnDrawable(drawable)
                }

                if (card.icon == null) {
                    cardSubText.text = card.text
                }

                card.onClickAction?.let { action ->
                    cardView.setOnClickListener {
                        action()
                    }
                }
            }
        }

    private fun setCardBackgroundColorBasedOnDrawable(
        drawable: Drawable
    ) = with(binding) {
        Palette.Builder(drawable.toBitmap())
            .generate { palette ->
                if (palette != null) {
                    cardView.setCardBackgroundColor(
                        palette.getLightVibrantColor(
                            context.getColor(R.color.cardBackgroundColor)
                        )
                    )
                }
            }
    }

    private fun addWidget(widgetId: Int) = with(binding) {
        widgetHost = AppWidgetHost(context.applicationContext, binding.cardView.id)
        widgetManager = AppWidgetManager.getInstance(context.applicationContext)

        val appWidgetInfo: AppWidgetProviderInfo =
            widgetManager.getAppWidgetInfo(widgetId)

        val hostView: AppWidgetHostView =
            widgetHost.createView(context.applicationContext, widgetId, appWidgetInfo)

        hostView.setAppWidget(widgetId, appWidgetInfo)

        cardView.addView(hostView)

        widgetHost.startListening()
    }

    private fun applySize(sizeType: SizeType) = with(binding) {
        val scale = sizeType.scaleCoefficient

        animateGuidelines(sizeType)

        animateCardText(scale)
    }

    private fun animateGuidelines(sizeType: SizeType) = with(binding) {

        val valueAnimatorForFirstGuideline = ValueAnimator.ofFloat(
            (firstGuideline.layoutParams as LayoutParams).guidePercent,
            sizeType.firstGuidelinePercentage
        )

        with(valueAnimatorForFirstGuideline) {
            duration = AppCardViewConstantsRepository.guidelineAnimationDuration

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

        val valueAnimatorForSecondGuideline = ValueAnimator.ofFloat(
            (secondGuideline.layoutParams as LayoutParams).guidePercent,
            sizeType.secondGuidelinePercentage
        )

        with(valueAnimatorForSecondGuideline) {
            duration = AppCardViewConstantsRepository.guidelineAnimationDuration

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
                AppCardViewConstantsRepository.defaultIconSize * scale /
                        AppCardViewConstantsRepository.scaleCorrectionCoefficient
            )

        with(valueAnimator) {
            duration = AppCardViewConstantsRepository.textAnimationDuration

            this.addUpdateListener {
                cardText.setTextSize(TypedValue.COMPLEX_UNIT_PX, animatedValue as Float)
            }

            start()
        }
    }

    private companion object AppCardViewConstantsRepository {
        const val guidelineAnimationDuration: Long = 300
        const val textAnimationDuration: Long = 200
        const val defaultIconSize = 55
        const val scaleCorrectionCoefficient = 1.5f
    }
}
