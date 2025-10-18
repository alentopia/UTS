package com.example.testing.ui.screens

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.testing.JurnalModel
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.*

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun JournalListScreen(
    navController: NavController,
    listJurnal: MutableList<JurnalModel> = remember { mutableStateListOf() },
    onAddClick: () -> Unit = {},
    onDelete: (Int) -> Unit = {}
) {
    val dummyData = remember {
        listOf(
            JurnalModel(
                emoji = "😊",
                mood = "Happy",
                title = "Felt great this morning!",
                content = "Habis menang saham 1 Milliar",
                date = "10:25 AM",
                location = "Jakarta"
            ),
            JurnalModel(
                emoji = "😔",
                mood = "Sad",
                title = "Had a rough day...",
                content = "Habis Kejedot Dinding",
                date = "09:42 PM",
                location = "Tangerang"
            )
        )
    }


    val displayedList = if (listJurnal.isEmpty()) dummyData else listJurnal
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var searchQuery by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 8.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 12.dp)
        ) {

            //  Header: Journal + Icon Calendar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Journal",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF442D92)
                )

                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .clickable {
                            // Arahkan ke layar kalender mood
                            navController.navigate("mood_calendar")
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Calendar Icon",
                        tint = Color(0xFF8B4CFC)
                    )
                }
            }

            //  Kalender Mingguan (Scrollable Horizontal)
            CalendarJournal(
                selectedDate = selectedDate,
                onDateSelected = { selectedDate = it }
            )

            //  Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search", color = Color.Gray) },
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                trailingIcon = {
                    IconButton(onClick = { }) {
                        Icon(
                            imageVector = Icons.Default.FilterList,
                            contentDescription = "Filter Journals",
                            tint = Color(0xFF8B4CFC)
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedBorderColor = Color(0xFF8B4CFC),
                    unfocusedBorderColor = Color(0xFFDADADA)
                )
            )

            //  Daftar Journal (scrollable)
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                itemsIndexed(displayedList) { index, journal ->
                    JournalItemCard(
                        journal = journal,
                        onClick = {
                            navController.navigate(
                                "journal_detail/${journal.title}/${journal.content}/${journal.date}/${journal.emoji}/${journal.location}"
                            )
                        },
                        onLongPress = { onDelete(index) }
                    )
                }
            }
        }

        //  Tombol tambah di kanan bawah
        FloatingActionButton(
            onClick = onAddClick,
            containerColor = Color(0xFF8B4CFC),
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(bottom = 20.dp, end = 8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add Journal",
                tint = Color.White
            )
        }
    }
}

@Composable
fun CalendarJournal(
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit
) {
    val today = LocalDate.now()
    var currentStart by remember { mutableStateOf(today.minusDays(3)) }

    val visibleDates = remember(currentStart) {
        (0..13).map { currentStart.plusDays(it.toLong()) }
    }

    Surface(
        shape = RoundedCornerShape(24.dp),
        color = Color.White,
        shadowElevation = 3.dp,
        tonalElevation = 2.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 14.dp, vertical = 10.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(visibleDates) { date: LocalDate ->
                val isSelected = date == selectedDate
                val isToday = date == today
                val dayNumber = date.dayOfMonth
                val dayName = date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.ENGLISH)

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(
                            when {
                                isSelected -> Color(0xFF8B4CFC)
                                isToday -> Color(0xFFE7DBFF)
                                else -> Color.Transparent
                            }
                        )
                        .clickable { onDateSelected(date) }
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = dayNumber.toString(),
                        color = if (isSelected) Color.White else Color(0xFF2F195F),
                        fontSize = 16.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                    Text(
                        text = dayName,
                        color = if (isSelected) Color.White else Color(0xFF6A6A8E),
                        fontSize = 13.sp
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun JournalItemCard(
    journal: JurnalModel,
    onClick: () -> Unit,
    onLongPress: () -> Unit
) {
    //isi acak dari tips
    val tipsList = listOf(
        "Connect with nature" to "Spend time outdoors, surrounded by greenery and fresh air",
        "Take deep breaths" to "Pause for a moment and breathe deeply to clear your mind",
        "Write it down" to "Jot your thoughts to understand your emotions better",
        "Stay hydrated" to "Drink a glass of water and give yourself a quick reset",
        "Move your body" to "A short walk or stretch can improve your mood instantly"
    )
    val randomTip = remember { tipsList.random() }

    Surface(
        shape = RoundedCornerShape(20.dp),
        tonalElevation = 2.dp,
        shadowElevation = 3.dp,
        color = Color.White,
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongPress,
                indication = rememberRipple(color = Color(0xFF8B4CFC)),
                interactionSource = remember { MutableInteractionSource() }
            )
    ) {
        Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .background(Color(0xFFF4EEFF), RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = journal.emoji, fontSize = 22.sp)
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = if (journal.title.isNotBlank()) journal.title else "No Title",
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF333333),
                            fontSize = 15.sp
                        )
                        Text(
                            text = if (journal.content.isNotBlank()) {
                                val preview = journal.content.take(40)
                                preview + if (journal.content.length > 40) "..." else ""
                            } else {
                                "No Content"
                            },
                            color = Color(0xFF777777),
                            fontSize = 13.sp
                        )
                    }

                }

                Text(
                    text = journal.date,
                    color = Color(0xFF999999),
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))
            Divider(color = Color(0xFFF0E6FF), thickness = 1.dp)
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "💡 Tip",
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF8B4CFC),
                fontSize = 14.sp
            )
            Text(
                text = randomTip.first,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF333333),
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 4.dp)
            )
            Text(
                text = randomTip.second,
                color = Color(0xFF777777),
                fontSize = 13.sp,
                modifier = Modifier.padding(top = 2.dp, bottom = 4.dp)
            )
        }
    }
}
