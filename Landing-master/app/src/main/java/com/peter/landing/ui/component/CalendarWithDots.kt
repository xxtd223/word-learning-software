package com.peter.landing.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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

    // 生成当前月份所有日期（用于过滤）
    val currentMonthDates = (1..daysInMonth).map { yearMonth.atDay(it) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "本月学习情况",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Left,
            color = Color(0xFF6200EE),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.SansSerif
        )

        Text(
            text = "${yearMonth.month.getDisplayName(TextStyle.FULL, Locale.getDefault())} ${yearMonth.year}",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            textAlign = TextAlign.Center
        )

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
                    fontSize = 14.sp
                )
            }
        }

        LazyColumn {
            items((0..5).map { weekOffset -> // 固定6周行
                (0..6).map { dayOffset ->  // 每周7天
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
                            modifier = Modifier.weight(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            if (date != null) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = date.dayOfMonth.toString(),
                                        modifier = Modifier
                                            .clickable { onDateClick(date) }
                                            .padding(8.dp),
                                        textAlign = TextAlign.Center
                                    )

                                    // 确保日期在当前月且被标记
                                    if (date in currentMonthDates && date in markedDates) {
                                        Box(
                                            modifier = Modifier
                                                .size(6.dp)
                                                .clip(CircleShape)
                                                .background(Color.Green)
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

@Composable
fun CalendarExample() {
    val currentMonth = YearMonth.now()
    val markedDates = setOf(
        currentMonth.atDay(5),
        currentMonth.atDay(10),
        currentMonth.atDay(15),
        currentMonth.atDay(20),
        currentMonth.atDay(25)
    )

    CalendarWithDots(
        yearMonth = currentMonth,
        markedDates = markedDates,
        onDateClick = { date ->
            println("Clicked on: $date")
        }
    )
}