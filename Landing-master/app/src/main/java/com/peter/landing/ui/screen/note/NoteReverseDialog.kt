package com.peter.landing.ui.screen.note

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.peter.landing.R

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun NoteReverseDialog(
    removedNoteList: List<Pair<String, Long>>,
    addNote: (String, Long) -> Unit,
    onDismiss: () -> Unit
) {
    val scrollState = rememberScrollState()

    Dialog(
        onDismissRequest = onDismiss
    ) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(16.dp)) // 背景色和圆角效果
                    .border(2.dp, MaterialTheme.colorScheme.outline, shape = RoundedCornerShape(16.dp)) // 设置更粗的边框和圆角
                    .alpha(0.8f) // 设置透明度
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically, // 垂直对齐方式，使图标和文本在垂直方向对齐
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Spacer(modifier = Modifier.width(10.dp)) // 图标和文本之间的间隔
                    Icon(
                        painter = painterResource(R.drawable.ic_note_zhangyuge_24dp),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.tertiary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp)) // 图标和文本之间的间隔
                    Text(
                        text = "临时记录",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.tertiary,
                    )
                    Spacer(modifier = Modifier.padding(vertical = 4.dp))
                }
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(288.dp)
                        .padding(8.dp)
                ) {
                    if (removedNoteList.isNotEmpty()) {
                        Column(
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Text(
                                text = "点击对应的单词可以恢复生词记录",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.outline,
                            )
                            FlowRow(
                                modifier = Modifier
                                    .padding(4.dp)
                                    .weight(1f)
                                    .fillMaxWidth()
                                    .verticalScroll(scrollState)
                            ){ removedNoteList.forEach {
                                    OutlinedButton(
                                        onClick = { addNote(it.first, it.second) },
                                        modifier = Modifier.padding(4.dp)
                                    ) {
                                        Icon(
                                            painter = painterResource(R.drawable.ic_note_xiaowo_24dp),
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.outline,
                                            modifier = Modifier.size(20.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp)) // 图标和文本之间的间隔
                                        Text(text = it.first)
                                    }
                                }
                            }
                        }

                    } else {
                        Text(
                            text = "已删除生词的会出现在这里",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onBackground,
                        )
                    }
                }
                Spacer(modifier = Modifier.padding(vertical = 16.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp) // 可以适当调整 Row 内的 padding
                    ) {
                        Spacer(modifier = Modifier.weight(1f))
                        OutlinedButton(
                            onClick = onDismiss,
                            modifier = Modifier.padding(4.dp)
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_note_pilaoban_24dp),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.outline,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp)) // 图标和文本之间的间隔
                            Text(text = stringResource(R.string.cancel))
                        }
                }
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun NoteReverseDialogPreview() {
    // 提供一些假数据
    val removedNoteList = listOf(
        "单词1" to 1L,
        "单词2" to 2L,
        "单词3" to 3L
    )

    // 预览时不需要实际的 addNote 和 onDismiss 实现，可以传入空的 lambda 函数
    NoteReverseDialog(
        removedNoteList = removedNoteList,
        addNote = { _, _ -> },
        onDismiss = {}
    )
}
