package kr.jhp.purchtrac.ui.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneId
import java.util.*

/**
 * 년/월만 선택할 수 있는 커스텀 날짜 선택 다이얼로그
 */
@Composable
fun YearMonthPickerDialog(
    initialDate: Long,
    onDateSelected: (Long) -> Unit,
    onDismissRequest: () -> Unit
) {
    // 초기 날짜에서 년/월 추출
    val calendar = Calendar.getInstance().apply {
        timeInMillis = initialDate
    }

    var selectedYear by remember { mutableStateOf(calendar.get(Calendar.YEAR)) }
    var selectedMonth by remember { mutableStateOf(calendar.get(Calendar.MONTH)) }

    Dialog(onDismissRequest = onDismissRequest) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "날짜 선택",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // 년도 선택
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    IconButton(onClick = { selectedYear-- }) {
                        Text("-", style = MaterialTheme.typography.headlineMedium)
                    }

                    Text(
                        text = "$selectedYear 년",
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )

                    IconButton(onClick = { selectedYear++ }) {
                        Text("+", style = MaterialTheme.typography.headlineMedium)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 월 선택
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    IconButton(onClick = {
                        if (selectedMonth > 0) selectedMonth--
                        else {
                            selectedMonth = 11
                            selectedYear--
                        }
                    }) {
                        Text("-", style = MaterialTheme.typography.headlineMedium)
                    }

                    // 월은 0-11 범위이지만 표시는 1-12로 함
                    Text(
                        text = "${selectedMonth + 1}월",
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )

                    IconButton(onClick = {
                        if (selectedMonth < 11) selectedMonth++
                        else {
                            selectedMonth = 0
                            selectedYear++
                        }
                    }) {
                        Text("+", style = MaterialTheme.typography.headlineMedium)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // 버튼
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismissRequest) {
                        Text("취소")
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    TextButton(
                        onClick = {
                            // 선택한 연/월로 캘린더 설정
                            val resultCalendar = Calendar.getInstance().apply {
                                set(Calendar.YEAR, selectedYear)
                                set(Calendar.MONTH, selectedMonth)
                                set(Calendar.DAY_OF_MONTH, 1) // 월의 첫날
                                set(Calendar.HOUR_OF_DAY, 0)
                                set(Calendar.MINUTE, 0)
                                set(Calendar.SECOND, 0)
                                set(Calendar.MILLISECOND, 0)
                            }
                            onDateSelected(resultCalendar.timeInMillis)
                            onDismissRequest()
                        }
                    ) {
                        Text("확인")
                    }
                }
            }
        }
    }
}