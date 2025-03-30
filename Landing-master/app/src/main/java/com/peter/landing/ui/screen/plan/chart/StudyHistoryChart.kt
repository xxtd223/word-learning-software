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
    historyData: List<Pair<String, Int>> // (日期, 单词数量)
) {
    val density = LocalDensity.current
    val height = 228.dp
    val heightPx = with(density) { height.toPx() }
    val textMeasurer = rememberTextMeasurer()

    // 颜色
    val frameColor = MaterialTheme.colorScheme.onBackground
    val textColor = MaterialTheme.colorScheme.onBackground
    val strokeWidth = 4f

    // 文本样式
    val titleStyle = MaterialTheme.typography.headlineLarge.copy(
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        color = textColor
    )
    val labelStyle = MaterialTheme.typography.bodyLarge.copy(
        fontSize = 14.sp,
        color = textColor
    )

    // 文本布局
    val titleLayout = textMeasurer.measure("学习历史", titleStyle)

    // 尺寸参数
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
        // 边框
        drawRoundRect(
            brush = Brush.linearGradient(
                colors = listOf(
                    Color(0xFFFFC2E9FB).copy(alpha = 0.5f),
                    Color(0xFFFAA1C4FD).copy(alpha = 0.5f)
                ),
                start = Offset(0f, 0f),
                end = Offset(size.width, size.height)
            ),
            topLeft = Offset.Zero,
            size = Size(size.width, heightPx),
            cornerRadius = CornerRadius(8.dp.toPx())
        )

        // 标题
        drawText(
            textLayoutResult = titleLayout,
            topLeft = Offset(
                (size.width / 2) - (titleLayout.size.width / 2),
                dp16 * 1.2f
            )
        )

        // 图表主体
        val chartArea = Size(
            width = size.width - chartPadding * 2,
            height = heightPx - chartPaddingV * 2 - dp24
        )

        // 柱状图
        val maxValue = historyData.maxOfOrNull { it.second }?.toFloat() ?: 1f
        val barWidth = (chartArea.width / historyData.size) * 0.9f
        val barSpacing = (chartArea.width / historyData.size) * 0.15f

        historyData.forEachIndexed { index, (date, value) ->
            val barHeight = (value / maxValue) * chartArea.height
            val xPos = chartPadding + (barWidth + barSpacing) * index
            val yPos = heightPx - dp36 - barHeight

            // 判断颜色，整十数为绿色，否则为黄色
            val barColor = if (value % 10 == 0) {
                Color(0xFF96E6A1)
            } else {
                Color(0xFFFCC687)
            }

            // 绘制柱状图
            drawRoundRect(
                color = barColor,
                topLeft = Offset(xPos, yPos),
                size = Size(barWidth, barHeight),
                cornerRadius = CornerRadius(4.dp.toPx())
            )

            // 绘制日期
            val dateLayout = textMeasurer.measure(date, labelStyle)
            drawText(
                textLayoutResult = dateLayout,
                topLeft = Offset(
                    xPos + barWidth / 2 - dateLayout.size.width / 2,
                    heightPx - chartPaddingV + 8.dp.toPx()
                )
            )

            // 绘制数值
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