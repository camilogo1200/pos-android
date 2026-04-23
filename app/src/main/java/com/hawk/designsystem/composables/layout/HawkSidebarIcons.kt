package com.hawk.designsystem.composables.layout

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke

enum class HawkSidebarIconToken {
    Dashboard,
    Products,
    Orders,
    Customers,
    Sales,
    Cash,
    Staff,
    Settings,
    Logout
}

@Composable
internal fun HawkSidebarIcon(
    icon: HawkSidebarIconToken,
    tint: Color,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        when (icon) {
            HawkSidebarIconToken.Dashboard -> drawDashboardIcon(tint)
            HawkSidebarIconToken.Products -> drawProductsIcon(tint)
            HawkSidebarIconToken.Orders -> drawOrdersIcon(tint)
            HawkSidebarIconToken.Customers -> drawCustomersIcon(tint)
            HawkSidebarIconToken.Sales -> drawSalesIcon(tint)
            HawkSidebarIconToken.Cash -> drawCashIcon(tint)
            HawkSidebarIconToken.Staff -> drawStaffIcon(tint)
            HawkSidebarIconToken.Settings -> drawSettingsIcon(tint)
            HawkSidebarIconToken.Logout -> drawLogoutIcon(tint)
        }
    }
}

private fun DrawScope.drawDashboardIcon(tint: Color) {
    val cell = size.minDimension * 0.24f
    val gap = size.minDimension * 0.1f
    val startX = size.width * 0.16f
    val startY = size.height * 0.16f

    repeat(2) { row ->
        repeat(2) { column ->
            drawRoundRect(
                color = tint,
                topLeft = Offset(
                    x = startX + column * (cell + gap),
                    y = startY + row * (cell + gap)
                ),
                size = Size(cell, cell),
                cornerRadius = CornerRadius(cell * 0.18f, cell * 0.18f),
                style = outlineStroke()
            )
        }
    }
}

private fun DrawScope.drawProductsIcon(tint: Color) {
    val left = size.width * 0.18f
    val right = size.width * 0.82f
    val top = size.height * 0.22f
    val middleY = size.height * 0.42f
    val lowerShoulder = size.height * 0.34f
    val bottom = size.height * 0.8f
    val centerX = size.width * 0.5f

    val packagePath = Path().apply {
        moveTo(centerX, top)
        lineTo(right, lowerShoulder)
        lineTo(right, size.height * 0.68f)
        lineTo(centerX, bottom)
        lineTo(left, size.height * 0.68f)
        lineTo(left, lowerShoulder)
        close()
    }

    drawPath(
        path = packagePath,
        color = tint,
        style = outlineStroke()
    )
    drawLine(
        color = tint,
        start = Offset(centerX, top),
        end = Offset(centerX, bottom),
        strokeWidth = strokeWidth(),
        cap = StrokeCap.Round
    )
    drawLine(
        color = tint,
        start = Offset(left, lowerShoulder),
        end = Offset(centerX, middleY),
        strokeWidth = strokeWidth(),
        cap = StrokeCap.Round
    )
    drawLine(
        color = tint,
        start = Offset(right, lowerShoulder),
        end = Offset(centerX, middleY),
        strokeWidth = strokeWidth(),
        cap = StrokeCap.Round
    )
}

private fun DrawScope.drawOrdersIcon(tint: Color) {
    val bodyTopLeft = Offset(size.width * 0.22f, size.height * 0.18f)
    val bodySize = Size(size.width * 0.56f, size.height * 0.64f)

    drawRoundRect(
        color = tint,
        topLeft = bodyTopLeft,
        size = bodySize,
        cornerRadius = CornerRadius(size.minDimension * 0.12f, size.minDimension * 0.12f),
        style = outlineStroke()
    )
    drawRoundRect(
        color = tint,
        topLeft = Offset(size.width * 0.36f, size.height * 0.1f),
        size = Size(size.width * 0.28f, size.height * 0.16f),
        cornerRadius = CornerRadius(size.minDimension * 0.08f, size.minDimension * 0.08f),
        style = outlineStroke()
    )

    val lineStartX = size.width * 0.34f
    val lineEndX = size.width * 0.66f
    listOf(0.44f, 0.58f, 0.72f).forEach { ratio ->
        drawLine(
            color = tint,
            start = Offset(lineStartX, size.height * ratio),
            end = Offset(lineEndX, size.height * ratio),
            strokeWidth = strokeWidth(),
            cap = StrokeCap.Round
        )
    }
}

private fun DrawScope.drawCustomersIcon(tint: Color) {
    drawCircle(
        color = tint,
        radius = size.minDimension * 0.12f,
        center = Offset(size.width * 0.34f, size.height * 0.34f),
        style = outlineStroke()
    )
    drawCircle(
        color = tint,
        radius = size.minDimension * 0.1f,
        center = Offset(size.width * 0.66f, size.height * 0.4f),
        style = outlineStroke()
    )
    drawArc(
        color = tint,
        startAngle = 198f,
        sweepAngle = 144f,
        useCenter = false,
        topLeft = Offset(size.width * 0.12f, size.height * 0.48f),
        size = Size(size.width * 0.46f, size.height * 0.34f),
        style = outlineStroke()
    )
    drawArc(
        color = tint,
        startAngle = 210f,
        sweepAngle = 130f,
        useCenter = false,
        topLeft = Offset(size.width * 0.48f, size.height * 0.56f),
        size = Size(size.width * 0.26f, size.height * 0.2f),
        style = outlineStroke()
    )
}

private fun DrawScope.drawSalesIcon(tint: Color) {
    drawRoundRect(
        color = tint,
        topLeft = Offset(size.width * 0.24f, size.height * 0.16f),
        size = Size(size.width * 0.52f, size.height * 0.68f),
        cornerRadius = CornerRadius(size.minDimension * 0.1f, size.minDimension * 0.1f),
        style = outlineStroke()
    )
    listOf(0.36f, 0.5f, 0.64f).forEach { ratio ->
        drawLine(
            color = tint,
            start = Offset(size.width * 0.36f, size.height * ratio),
            end = Offset(size.width * 0.64f, size.height * ratio),
            strokeWidth = strokeWidth(),
            cap = StrokeCap.Round
        )
    }
    drawLine(
        color = tint,
        start = Offset(size.width * 0.32f, size.height * 0.16f),
        end = Offset(size.width * 0.32f, size.height * 0.84f),
        strokeWidth = strokeWidth(),
        cap = StrokeCap.Round
    )
}

private fun DrawScope.drawCashIcon(tint: Color) {
    drawRoundRect(
        color = tint,
        topLeft = Offset(size.width * 0.14f, size.height * 0.28f),
        size = Size(size.width * 0.72f, size.height * 0.44f),
        cornerRadius = CornerRadius(size.minDimension * 0.08f, size.minDimension * 0.08f),
        style = outlineStroke()
    )
    drawCircle(
        color = tint,
        radius = size.minDimension * 0.1f,
        center = Offset(size.width * 0.5f, size.height * 0.5f),
        style = outlineStroke()
    )
    drawLine(
        color = tint,
        start = Offset(size.width * 0.24f, size.height * 0.5f),
        end = Offset(size.width * 0.3f, size.height * 0.5f),
        strokeWidth = strokeWidth(),
        cap = StrokeCap.Round
    )
    drawLine(
        color = tint,
        start = Offset(size.width * 0.7f, size.height * 0.5f),
        end = Offset(size.width * 0.76f, size.height * 0.5f),
        strokeWidth = strokeWidth(),
        cap = StrokeCap.Round
    )
}

private fun DrawScope.drawStaffIcon(tint: Color) {
    drawCircle(
        color = tint,
        radius = size.minDimension * 0.12f,
        center = Offset(size.width * 0.34f, size.height * 0.34f),
        style = outlineStroke()
    )
    drawArc(
        color = tint,
        startAngle = 198f,
        sweepAngle = 144f,
        useCenter = false,
        topLeft = Offset(size.width * 0.12f, size.height * 0.5f),
        size = Size(size.width * 0.46f, size.height * 0.32f),
        style = outlineStroke()
    )
    drawLine(
        color = tint,
        start = Offset(size.width * 0.58f, size.height * 0.64f),
        end = Offset(size.width * 0.66f, size.height * 0.72f),
        strokeWidth = strokeWidth(),
        cap = StrokeCap.Round
    )
    drawLine(
        color = tint,
        start = Offset(size.width * 0.66f, size.height * 0.72f),
        end = Offset(size.width * 0.82f, size.height * 0.5f),
        strokeWidth = strokeWidth(),
        cap = StrokeCap.Round
    )
}

private fun DrawScope.drawSettingsIcon(tint: Color) {
    val center = Offset(size.width * 0.5f, size.height * 0.5f)
    val innerRadius = size.minDimension * 0.1f
    val outerRadius = size.minDimension * 0.28f

    repeat(8) { index ->
        val angle = Math.toRadians((index * 45.0) - 90.0)
        val start = Offset(
            x = center.x + kotlin.math.cos(angle).toFloat() * (outerRadius - size.minDimension * 0.05f),
            y = center.y + kotlin.math.sin(angle).toFloat() * (outerRadius - size.minDimension * 0.05f)
        )
        val end = Offset(
            x = center.x + kotlin.math.cos(angle).toFloat() * outerRadius,
            y = center.y + kotlin.math.sin(angle).toFloat() * outerRadius
        )
        drawLine(
            color = tint,
            start = start,
            end = end,
            strokeWidth = strokeWidth(),
            cap = StrokeCap.Round
        )
    }

    drawCircle(
        color = tint,
        radius = outerRadius - size.minDimension * 0.06f,
        center = center,
        style = outlineStroke()
    )
    drawCircle(
        color = tint,
        radius = innerRadius,
        center = center,
        style = outlineStroke()
    )
}

private fun DrawScope.drawLogoutIcon(tint: Color) {
    val left = size.width * 0.2f
    val top = size.height * 0.24f
    val bottom = size.height * 0.76f
    val middleY = size.height * 0.5f

    drawLine(
        color = tint,
        start = Offset(left, top),
        end = Offset(left, bottom),
        strokeWidth = strokeWidth(),
        cap = StrokeCap.Round
    )
    drawLine(
        color = tint,
        start = Offset(left, top),
        end = Offset(size.width * 0.38f, top),
        strokeWidth = strokeWidth(),
        cap = StrokeCap.Round
    )
    drawLine(
        color = tint,
        start = Offset(left, bottom),
        end = Offset(size.width * 0.38f, bottom),
        strokeWidth = strokeWidth(),
        cap = StrokeCap.Round
    )
    drawLine(
        color = tint,
        start = Offset(size.width * 0.38f, middleY),
        end = Offset(size.width * 0.8f, middleY),
        strokeWidth = strokeWidth(),
        cap = StrokeCap.Round
    )
    drawLine(
        color = tint,
        start = Offset(size.width * 0.64f, size.height * 0.34f),
        end = Offset(size.width * 0.8f, middleY),
        strokeWidth = strokeWidth(),
        cap = StrokeCap.Round
    )
    drawLine(
        color = tint,
        start = Offset(size.width * 0.64f, size.height * 0.66f),
        end = Offset(size.width * 0.8f, middleY),
        strokeWidth = strokeWidth(),
        cap = StrokeCap.Round
    )
}

private fun DrawScope.outlineStroke(): Stroke = Stroke(
    width = strokeWidth(),
    cap = StrokeCap.Round,
    join = StrokeJoin.Round
)

private fun DrawScope.strokeWidth(): Float = size.minDimension * 0.11f
