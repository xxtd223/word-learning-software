package com.peter.landing.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun CalendarWithDots(
        yearMonth: YearMonth = YearMonth.now(),
        markedDates: Set<LocalDate> = emptySet(),
        onDateClick: (LocalDate) -> Unit = {}
) {
    val daysInMonth = yearMonth.lengthOfMonth()
    val firstDayOfMonth = yearMonth.atDay(1).dayOfWeek
    val startOffset = (firstDayOfMonth.value + 6) % 7 // 对齐：周一=0

    val currentMonthDates = (1..daysInMonth).map { yearMonth.atDay(it) }

    // 新增容器效果
    Surface(
            modifier = Modifier
                    .padding(8.dp)
                    .shadow(
                            elevation = 2.dp,
                            shape = RoundedCornerShape(16.dp),
                            ambientColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                    )
                    .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                            shape = RoundedCornerShape(16.dp)
                    )
                    .clip(RoundedCornerShape(16.dp))
                    .background(
                            MaterialTheme.colorScheme.surface
                                    .copy(alpha = 0.9f) // 使用标准surface颜色+透明度
                    ),
            color = Color.Transparent
    ) {
        Column(
                modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
        ) {
            Text(
                    text = "本月学习情况",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Left,
                    color = MaterialTheme.colorScheme.primary, // 改用主题色
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.SansSerif
            )

            Text(
                    text = "${yearMonth.month.getDisplayName(TextStyle.FULL, Locale.getDefault())} ${yearMonth.year}",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurface // 适配主题文字色
            )

            // 新增星期标题容器
            Surface(
                    modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f)),
                    color = Color.Transparent
            ) {
                Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    DayOfWeek.values().forEach { dayOfWeek ->
                        Text(
                                text = dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault()),
                                modifier = Modifier.weight(1f),
                                textAlign = TextAlign.Center,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant // 适配主题文字色
                        )
                    }
                }
            }
            // 固定高度的日历区域
            Box(
                    modifier = Modifier
                            .height(168.dp) // 6行x28dp
                            .fillMaxWidth()
            ) {
                LazyColumn {
                    items((0..5).map { weekOffset ->
                        (0..6).map { dayOffset ->
                            val dateIndex = weekOffset * 7 + dayOffset
                            if (dateIndex >= startOffset && dateIndex < startOffset + daysInMonth) {
                                yearMonth.atDay(dateIndex - startOffset + 1)
                            } else null
                        }
                    }) { week ->
                        Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            week.forEach { date ->
                                Box(
                                        modifier = Modifier
                                                .weight(1f)
                                                .height(28.dp), // 固定行高
                                        contentAlignment = Alignment.Center
                                ) {
                                    if (date != null) {
                                        Column(
                                                horizontalAlignment = Alignment.CenterHorizontally,
                                                verticalArrangement = Arrangement.Center,
                                                modifier = Modifier
                                                        .clickable { onDateClick(date) }
                                                        .padding(2.dp)
                                        ) {
                                            Text(
                                                    text = date.dayOfMonth.toString(),
                                                    fontSize = 14.sp, // 缩小字体
                                                    color = when {
                                                        date !in currentMonthDates ->
                                                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                                                        date in markedDates ->
                                                            MaterialTheme.colorScheme.primary
                                                        else ->
                                                            MaterialTheme.colorScheme.onSurface
                                                    }
                                            )

                                            if (date in currentMonthDates && date in markedDates) {
                                                Box(
                                                        modifier = Modifier
                                                                .size(4.dp) // 缩小标记点
                                                                .clip(CircleShape)
                                                                .background(MaterialTheme.colorScheme.primary)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun CalendarExample(markedDates: List<LocalDate>) {
    val currentMonth = YearMonth.now()
    //markedDates list转set
    CalendarWithDots(
        yearMonth = currentMonth,
        markedDates = markedDates.toSet(),
        onDateClick = { date ->
            println("Clicked on: $date")
        }
    )
}