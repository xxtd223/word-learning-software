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
    val startOffset = firstDayOfMonth.value % 7 // 0 for Sunday, 1 for Monday, etc.

    // Generate all days in month with empty slots for offset
    val days = List(42) { index ->
        if (index >= startOffset && index < startOffset + daysInMonth) {
            yearMonth.atDay(index - startOffset + 1)
        } else {
            null
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "本月学习情况",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Left,
            color = Color(0xFF6200EE), // 直接指定颜色
            fontSize = 20.sp, // 字体大小
            fontWeight = FontWeight.Bold, // 字体粗细
            fontFamily = FontFamily.SansSerif // 字体家族
        )
        // Month and year header
        Text(
            text = "${yearMonth.month.getDisplayName(TextStyle.FULL, Locale.getDefault())} ${yearMonth.year}",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            textAlign = TextAlign.Center
        )

        // Weekday headers
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

        // Calendar grid
        LazyColumn {
            items(days.chunked(7)) { week ->
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
                                    // Date number
                                    Text(
                                        text = date.dayOfMonth.toString(),
                                        modifier = Modifier
                                            .clickable { onDateClick(date) }
                                            .padding(8.dp),
                                        textAlign = TextAlign.Center
                                    )

                                    // Green dot if date is marked
                                    if (markedDates.contains(date)) {
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