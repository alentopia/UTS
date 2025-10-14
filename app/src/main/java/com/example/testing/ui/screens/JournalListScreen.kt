package com.example.testing.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.testing.JurnalModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JournalListScreen(
    listJurnal: MutableList<JurnalModel>,
    onAddClick: () -> Unit,
    onDelete: (Int) -> Unit,
    onEdit: (Int) -> Unit = {} // callback edit (opsional)
) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    var deleteIndex by remember { mutableStateOf(-1) }

    Scaffold(
        containerColor = Color.Transparent,
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddClick,
                containerColor = Color(0xFF8B4CFC)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Journal",
                    tint = Color.White
                )
            }
        }
    ) { padding ->

        if (listJurnal.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No journals yet.\nTap + to add one!",
                    color = Color.Gray,
                    fontSize = 16.sp,
                    lineHeight = 22.sp,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                itemsIndexed(listJurnal) { index, journal ->
                    var expanded by remember { mutableStateOf(false) }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            // 🔹 Header (emoji, title, edit/delete)
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(text = journal.emoji, fontSize = 28.sp)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = journal.title,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF333333)
                                    )
                                }

                                Row {
                                    IconButton(onClick = { onEdit(index) }) {
                                        Icon(
                                            imageVector = Icons.Default.Edit,
                                            contentDescription = "Edit",
                                            tint = Color(0xFF6E6E6E)
                                        )
                                    }
                                    IconButton(onClick = {
                                        deleteIndex = index
                                        showDeleteDialog = true
                                    }) {
                                        Icon(
                                            imageVector = Icons.Default.Delete,
                                            contentDescription = "Delete",
                                            tint = Color(0xFFD32F2F)
                                        )
                                    }
                                }
                            }

                            // 🕒 Date
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = journal.date,
                                color = Color.Gray,
                                fontSize = 13.sp
                            )

                            // 📝 Content
                            if (journal.content.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = journal.content,
                                    maxLines = if (expanded) Int.MAX_VALUE else 3,
                                    overflow = TextOverflow.Ellipsis,
                                    fontSize = 15.sp,
                                    color = Color(0xFF444444)
                                )

                                TextButton(
                                    onClick = { expanded = !expanded },
                                    contentPadding = PaddingValues(0.dp)
                                ) {
                                    Text(
                                        text = if (expanded) "Show Less" else "Read More",
                                        color = Color(0xFF8B4CFC),
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // 🗑️ Dialog konfirmasi delete
        if (showDeleteDialog && deleteIndex >= 0) {
            AlertDialog(
                onDismissRequest = { showDeleteDialog = false },
                title = { Text("Delete Confirmation") },
                text = { Text("Are you sure you want to delete this journal?") },
                confirmButton = {
                    TextButton(onClick = {
                        onDelete(deleteIndex)
                        showDeleteDialog = false
                    }) {
                        Text("Delete", color = Color(0xFFD32F2F))
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteDialog = false }) {
                        Text("Cancel", color = Color.Gray)
                    }
                }
            )
        }
    }
}
