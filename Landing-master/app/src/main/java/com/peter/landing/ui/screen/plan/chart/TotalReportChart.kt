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
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.peter.landing.data.local.vocabulary.Vocabulary
import com.peter.landing.ui.theme.LandingAppTheme

@Composable
fun TotalReportChart(
        vocabularyName: Vocabulary.Name,
        totalProgress: List<Float>
) {
    // 保持原有Layout结构不变
    val todayChart = @Composable {
        TotalChart(vocabularyName, totalProgress)
    }

    Layout(
            contents = listOf(todayChart),
    ) { (todayChartMeasure), constraints ->

        val todayChartPlaceable = todayChartMeasure.first().measure(constraints)
        val totalHeight = todayChartPlaceable.height
        val totalWidth = todayChartPlaceable.width

        layout(totalWidth, totalHeight) {
            todayChartPlaceable.place(0, 0, 1f)
        }

    }
}

@OptIn(ExperimentalTextApi::class)
@Composable
private fun TotalChart(
        vocabularyName: Vocabulary.Name,
        totalProgress: List<Float>
) {
    val height = 268.dp
    val density = LocalDensity.current
    val heightPx = with(density) { height.toPx() }
    val frameColor = MaterialTheme.colorScheme.onSurfaceVariant
    val strokeWidth = 8f

    val textMeasurer = rememberTextMeasurer()
    val sectionTextStyle = MaterialTheme.typography.headlineLarge.copy(
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 1f)
    )
    val sectionTextLayout: (String) -> TextLayoutResult = {
        textMeasurer.measure(
                text = AnnotatedString(it),
                style = sectionTextStyle,
        )
    }

    val titleTextLayoutResult = textMeasurer.measure(
            text = AnnotatedString("整体学习进度"),
            style = MaterialTheme.typography.headlineLarge.copy(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
            ),
    )
    val titleTextSize = titleTextLayoutResult.size

    val learnedNum = totalProgress[0]
    val totalNum = totalProgress[1]
    val learnedAngle = learnedNum / totalNum * 360f

    val dictionaryTextLayout = sectionTextLayout("词库：${vocabularyName.cnValue}")
    val dictionaryTextSize = dictionaryTextLayout.size
    val learnedTextLayout = sectionTextLayout("√完成：${learnedNum.toInt()} 个")
    val learnedTextSize = learnedTextLayout.size
    val totalTextLayout = sectionTextLayout("◉总数：${totalNum.toInt()} 个")
    val totalTextSize = totalTextLayout.size

    val mottoTextLayout = textMeasurer.measure(
            text = AnnotatedString("Constant Effort Bring Success"),
            style = MaterialTheme.typography.headlineLarge.copy(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF5C94E8).copy(alpha = 0.8f)
            ),
    )
    val mottoTextSize = mottoTextLayout.size

    val radius = heightPx / 3.2f

    val dictionaryColor = Color(0xFF5C94E8)
    val learnedColor = Color(0xFF2C7F3E).copy(alpha = 0.8f)
    val totalColor = Color(0xFF2C7F3E).copy(alpha = 0.8f)
    val totalbgColor = Color(0xFF2C7F3E).copy(alpha = 0.5f)

    val dp16Px = with(density) { 16.dp.toPx() }
    val dp48Px = with(density) { 48.dp.toPx() }

    Canvas(
            modifier = Modifier
                    .fillMaxWidth()
                    .height(height)
    ) {
        val circleCenterOffset = Offset(
                x = this.size.width * 0.3f, // 修改点：相对宽度
                y = this.size.height / 2f   // 垂直居中
        )

        drawRoundRect(
                color = frameColor,
                cornerRadius = CornerRadius(8.dp.toPx()),
                style = Stroke(width = 1.dp.toPx())
        )

        drawCircle(
                color = totalbgColor.copy(alpha = 0.1f),
                radius = radius,
                center = circleCenterOffset,
                style = Fill
        )

        drawCircle(
                color = totalColor,
                radius = radius,
                center = circleCenterOffset,
                style = Stroke(width = strokeWidth)
        )

        drawArc(
                color = learnedColor,
                startAngle = -90f,
                sweepAngle = learnedAngle,
                useCenter = true,
                style = Fill,
                size = Size(radius * 2f, radius * 2f),
                topLeft = Offset(
                        x = circleCenterOffset.x - radius,
                        y = circleCenterOffset.y - radius
                )
        )

        drawText(
                textLayoutResult = titleTextLayoutResult,
                topLeft = Offset(
                        (this.size.width / 2f) - (titleTextSize.width / 2f),
                        dp16Px * 1.2f - (titleTextSize.height / 2f)
                ),
        )

        drawText(
                textLayoutResult = dictionaryTextLayout,
                color = dictionaryColor,
                topLeft = Offset(
                        this.size.width - dp48Px * 1.6f - (dictionaryTextSize.width / 2f),
                        dp48Px * 1.4f - (dictionaryTextSize.height / 2f)
                ),
        )

        val counterStartX = this.size.width - dp48Px * 2.8f
        drawText(
                textLayoutResult = learnedTextLayout,
                color = learnedColor.copy(alpha = 0.8f),
                topLeft = Offset(
                        counterStartX,
                        heightPx - dp48Px * 1.8f - (learnedTextSize.height / 2f)
                ),
        )

        drawText(
                textLayoutResult = totalTextLayout,
                color = totalColor.copy(alpha = 0.8f),
                topLeft = Offset(
                        counterStartX,
                        heightPx - dp48Px * 1.2f - (totalTextSize.height / 2f)
                ),
        )

        drawText(
                textLayoutResult = mottoTextLayout,
                topLeft = Offset(
                        (this.size.width / 2f) - (mottoTextSize.width / 2f),
                        heightPx - dp16Px * 1.1f - (mottoTextSize.height / 2f)
                ),
        )
    }
}

@Preview(name = "Total Progress Chart", showBackground = true)
@Composable
fun TotalProgressChartPreview() {
    LandingAppTheme {
        Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
        ) {
            Box(modifier = Modifier.padding(8.dp)) {
                TotalReportChart(Vocabulary.Name.BEGINNER, listOf(688f, 2688f))
            }
        }
    }
}