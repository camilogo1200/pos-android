package com.hawk.common.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.max

@Composable
fun KeyboardAwareScrollContainer(
    modifier: Modifier = Modifier,
    scrollbarColor: Color,
    scrollbarTrackColor: Color,
    scrollbarWidth: Dp = 4.dp,
    minThumbHeight: Dp = 36.dp,
    content: @Composable (Modifier) -> Unit
) {
    val scrollState = rememberScrollState()
    val density = LocalDensity.current
    val imeVisible = WindowInsets.ime.getBottom(density) > 0
    val viewportHeightPx = scrollState.viewportSize
    val canScroll = scrollState.maxValue > 0 && viewportHeightPx > 0
    val showScrollbar = imeVisible && canScroll

    Box(
        modifier = modifier.imePadding()
    ) {
        content(
            Modifier
                .fillMaxWidth()
                .verticalScroll(scrollState)
                .padding(end = if (showScrollbar) 14.dp else 0.dp)
        )

        if (showScrollbar) {
            val scrollbarMetrics = remember(
                scrollState.value,
                scrollState.maxValue,
                viewportHeightPx,
                density
            ) {
                calculateScrollbarMetrics(
                    scrollValue = scrollState.value,
                    scrollMaxValue = scrollState.maxValue,
                    viewportHeightPx = viewportHeightPx,
                    density = density,
                    minThumbHeight = minThumbHeight
                )
            }

            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .fillMaxHeight()
                    .padding(top = 8.dp, bottom = 8.dp, end = 2.dp)
                    .width(scrollbarWidth)
                    .clip(RoundedCornerShape(999.dp))
                    .background(scrollbarTrackColor)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(y = scrollbarMetrics.thumbOffset)
                        .height(scrollbarMetrics.thumbHeight)
                        .clip(RoundedCornerShape(999.dp))
                        .background(scrollbarColor)
                )
            }
        }
    }
}

private data class ScrollbarMetrics(
    val thumbHeight: Dp,
    val thumbOffset: Dp
)

private fun calculateScrollbarMetrics(
    scrollValue: Int,
    scrollMaxValue: Int,
    viewportHeightPx: Int,
    density: Density,
    minThumbHeight: Dp
): ScrollbarMetrics {
    if (viewportHeightPx <= 0) {
        return ScrollbarMetrics(
            thumbHeight = minThumbHeight,
            thumbOffset = 0.dp
        )
    }

    val contentHeightPx = viewportHeightPx + scrollMaxValue
    val proportionalThumbHeightPx =
        (viewportHeightPx.toFloat() / contentHeightPx.toFloat()) * viewportHeightPx.toFloat()
    val minThumbHeightPx = with(density) { minThumbHeight.toPx() }
    val thumbHeightPx = max(proportionalThumbHeightPx, minThumbHeightPx)
    val maxThumbOffsetPx = max(viewportHeightPx.toFloat() - thumbHeightPx, 0f)
    val scrollFraction = if (scrollMaxValue == 0) {
        0f
    } else {
        scrollValue.toFloat() / scrollMaxValue.toFloat()
    }

    return ScrollbarMetrics(
        thumbHeight = with(density) { thumbHeightPx.toDp() },
        thumbOffset = with(density) { (maxThumbOffsetPx * scrollFraction).toDp() }
    )
}
