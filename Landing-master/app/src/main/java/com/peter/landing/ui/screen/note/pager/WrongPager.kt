package com.peter.landing.ui.screen.note.pager

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import com.peter.landing.R
import com.peter.landing.ui.util.ImageNotice
import kotlinx.coroutines.flow.Flow

@Composable
fun WrongPager(
    isDarkMode: Boolean,
    wrongWordList: Flow<PagingData<WrongWordItem>>,
    openExplainDialog: (String, Map<String, List<String>>) -> Unit,
    playPron: (String) -> Unit,
) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(16.dp)) // 背景色和圆角效果
            .border(2.dp, MaterialTheme.colorScheme.outline, shape = RoundedCornerShape(16.dp)) // 设置更粗的边框和圆角
            .alpha(0.8f) // 设置透明度
    ){
        val pagingItems = wrongWordList.collectAsLazyPagingItems()
        if (pagingItems.itemCount != 0) {
            WrongList(
                pagingItems = pagingItems,
                openExplainDialog = openExplainDialog,
                playPron = playPron
            )
        } else {
            WrongListEmpty(isDarkMode)
        }
    }
}

@Composable
private fun WrongList(
    pagingItems: LazyPagingItems<WrongWordItem>,
    openExplainDialog: (String, Map<String, List<String>>) -> Unit,
    playPron: (String) -> Unit,
) {
    LazyColumn {
        items(
            items = pagingItems,
            key = { it.hashCode() }
        ) {wrongWordItem ->
            if (wrongWordItem != null) {
                when (wrongWordItem) {
                    is WrongWordItem.ItemWrongWord -> {
                        WrongWord(
                            progressWrongWord = wrongWordItem.progressWrongWord,
                            openExplainDialog = openExplainDialog,
                            playPron = playPron
                        )
                    }
                    is WrongWordItem.NumHeader -> {
                        WrongWordNumHeader(num = wrongWordItem.num)
                    }
                }
            }
        }
    }
}

@Composable
private fun WrongListEmpty(isDarkMode: Boolean) {
    val image = if (isDarkMode) {
        R.drawable.sleepkedaya
    } else {
        R.drawable.sleepkedaya
    }

    Column {
        Spacer(modifier = Modifier.weight(1.5f))
        ImageNotice(
            imageId = image,
            text = stringResource(R.string.wrong_list_empty_msg),
            Modifier
                .padding(16.dp)
                .weight(3.5f)
                .fillMaxWidth()
                    .height(280.dp) // 设置高度

        )
        Spacer(modifier = Modifier.weight(2f))
    }
}
