package com.peter.landing.ui.screen.plan.chart

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.peter.landing.ui.theme.LandingAppTheme
import java.util.*
import kotlin.random.Random

@OptIn(ExperimentalTextApi::class)
@Composable
fun StudyHistoryChart(
        historyData: List<Pair<String, Int>>
) {
    val density = LocalDensity.current
    val height = 228.dp
    val heightPx = with(density) { height.toPx() }
    val textMeasurer = rememberTextMeasurer()

    val frameColor = MaterialTheme.colorScheme.onBackground
    val textColor = MaterialTheme.colorScheme.onBackground
    val strokeWidth = 6f

    val titleStyle = MaterialTheme.typography.headlineLarge.copy(
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
    )
    val labelStyle = MaterialTheme.typography.bodyLarge.copy(
            fontSize = 14.sp,
            color = textColor
    )

    val titleLayout = textMeasurer.measure("学习历史", titleStyle)

    val dp16 = with(density) { 16.dp.toPx() }
    val dp24 = with(density) { 24.dp.toPx() }
    val dp36 = with(density) { 36.dp.toPx() }
    val dp48 = with(density) { 48.dp.toPx() }
    val chartPadding = dp48 * 1.2f
    val chartPaddingV = dp48 * 0.8f

    Canvas(
            modifier = Modifier
                    .fillMaxWidth()
                    .height(height)
    ) {
        drawRoundRect(
                color = frameColor,
                cornerRadius = CornerRadius(8.dp.toPx()),
                style = Stroke(width = 1.dp.toPx())
        )

        drawText(
                textLayoutResult = titleLayout,
                topLeft = Offset(
                        (size.width / 2) - (titleLayout.size.width / 2),
                        dp16 * 1.2f
                )
        )

        val chartArea = Size(
                width = size.width - chartPadding * 2,
                height = heightPx - chartPaddingV * 2 - dp24
        )

        val maxValue = historyData.maxOfOrNull { it.second }?.toFloat() ?: 1f

        // 计算最小日期标签宽度
        val minLabelWidth = historyData.maxOf {
            textMeasurer.measure(it.first, labelStyle).size.width.toFloat()
        }

        // 计算每个柱子的宽度和间隔
        val totalRequiredWidth = (historyData.size * minLabelWidth * 1.2f)
        val spacingScale = if (totalRequiredWidth > chartArea.width) {
            chartArea.width / totalRequiredWidth
        } else {
            1f
        }

        val baseSpacing = chartArea.width / historyData.size
        val barWidth = baseSpacing * 0.8f * spacingScale
        val barSpacing = baseSpacing * 0.2f * spacingScale

        // 增加一个固定的间距值，避免日期重叠
        val extraSpacing = 9.dp.toPx() // 新增的固定间距

        // 计算柱状图起始的 x 位置，居中显示
        val startX = (size.width - (barWidth * historyData.size + barSpacing * (historyData.size - 1) + extraSpacing * (historyData.size - 1))) / 2f

        historyData.forEachIndexed { index, (date, value) ->
            val barHeight = (value / maxValue) * chartArea.height
            val xPos = startX + (barWidth + barSpacing + extraSpacing) * index
            val yPos = heightPx - dp36 - barHeight

            val barColor = if (value % 10 == 0) {
                Color(0xFF2C7F3E).copy(alpha = 0.7f)
            } else {
                Color(0xFF5C94E8).copy(alpha = 0.7f)
            }

            drawRoundRect(
                    color = barColor,
                    topLeft = Offset(xPos, yPos),
                    size = Size(barWidth, barHeight),
                    cornerRadius = CornerRadius(4.dp.toPx()),
                    style = Stroke(width = strokeWidth)
            )

            val dateLayout = textMeasurer.measure(date, labelStyle)
            drawText(
                    textLayoutResult = dateLayout,
                    topLeft = Offset(
                            xPos + barWidth / 2 - dateLayout.size.width / 2,
                            heightPx - chartPaddingV + 8.dp.toPx()
                    )
            )

            val valueLayout = textMeasurer.measure("$value", labelStyle)
            drawText(
                    textLayoutResult = valueLayout,
                    topLeft = Offset(
                            xPos + barWidth / 2 - valueLayout.size.width / 2,
                            yPos - valueLayout.size.height - 4.dp.toPx()
                    )
            )
        }
    }
}

@Preview(name = "Study History Chart", showBackground = true)
@Composable
fun StudyHistoryChartPreview() {
    LandingAppTheme {
        Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
        ) {
            // 生成模拟数据
            val calendar = Calendar.getInstance()
            val simulatedHistory = List(7) { index ->
                calendar.timeInMillis = System.currentTimeMillis()
                calendar.add(Calendar.DATE, -index)
                val date = "${calendar.get(Calendar.MONTH) + 1}-${calendar.get(Calendar.DAY_OF_MONTH)}"
                date to if (Random.nextFloat() < 0.5) {
                    listOf(30).random()
                } else {
                    Random.nextInt(30)
                } //还没接数据库，此为模拟数据
            }.reversed()

            StudyHistoryChart(
                    historyData = simulatedHistory
            )
        }
    }
}