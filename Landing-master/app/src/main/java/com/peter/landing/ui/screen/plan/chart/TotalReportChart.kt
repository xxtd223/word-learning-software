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
    val frameColor = MaterialTheme.colorScheme.onSurfaceVariant // 新增边框颜色
    val strokeWidth = 8f  // 新增线条宽度参数

    // 保持原有文字测量逻辑不变
    val textMeasurer = rememberTextMeasurer()
    val sectionTextStyle = MaterialTheme.typography.headlineLarge.copy(
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 1f) // 添加透明度
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
                    color = MaterialTheme.colorScheme.primary, // 动态主题色// 添加透明度
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
                    color = Color(0xFF5C94E8).copy(alpha = 0.8f) // 添加透明度
            ),
    )
    val mottoTextSize = mottoTextLayout.size

    val radius = heightPx / 3.2f

    // 保持原有颜色变量不变，仅在使用时调整透明度
    val dictionaryColor = Color(0xFF5C94E8)
    val learnedColor = Color(0xFF2C7F3E).copy(alpha = 0.8f)
    val totalColor = Color(0xFF2C7F3E).copy(alpha = 0.8f) // 基底线透明度调整
    val totalbgColor = Color(0xFF2C7F3E).copy(alpha = 0.5f) // 基底线透明度调整

    val dp16Px = with(density) { 16.dp.toPx() }
    val dp48Px = with(density) { 48.dp.toPx() }

    Canvas(
            modifier = Modifier
                    .fillMaxWidth()
                    .height(height)
    ) {
        val circleCenterOffset = Offset(
                x = this.size.width / 2f - dp48Px * 1.8f,
                y = heightPx / 2f + dp16Px * 0.4f
        )

        // 移除渐变背景
        /* 删除原始背景绘制
        drawRoundRect(
            brush = Brush.linearGradient(...),
            ...
        )
        */

        // 新增边框绘制
        drawRoundRect(
                color = frameColor,
                cornerRadius = CornerRadius(8.dp.toPx()),
                style = Stroke(width = 1.dp.toPx())
        )

        // 修改饼图绘制部分
        drawCircle(
                color = totalbgColor.copy(alpha = 0.1f), // 新增半透明背景层
                radius = radius,
                center = circleCenterOffset,
                style = Fill
        )

        drawCircle(  // 原有基底线
                color = totalColor,
                radius = radius,
                center = circleCenterOffset,
                style = Stroke(width = strokeWidth)
        )
        // 空心扇形绘制（关键改动）
        drawArc(
                color = learnedColor,
                startAngle = -90f,
                sweepAngle = learnedAngle,
                useCenter = true,  // 保持连接圆心
                style = Fill,
                size = Size(radius * 2f, radius * 2f),
                topLeft = Offset(
                        x = circleCenterOffset.x - radius,
                        y = circleCenterOffset.y - radius
                )
        )

        // 保持原有文字绘制逻辑，仅调整颜色透明度
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