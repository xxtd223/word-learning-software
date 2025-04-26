package com.peter.landing.ui.screen.affix

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.peter.landing.data.local.affix.Affix
import com.peter.landing.data.local.affix.AffixCatalog
import androidx.compose.material3.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.peter.landing.R


@Composable
fun AffixSection(
    affixCatalog: AffixCatalog,
    affixList: List<Affix>
) {
    // 将整个 Column 包裹在 Box 中，设置边框和透明度
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp) // 调整外边距
            .background(MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(16.dp)) // 背景色和圆角效果
            .border(2.dp, MaterialTheme.colorScheme.outline, shape = RoundedCornerShape(16.dp)) // 设置更粗的边框和圆角
            .alpha(0.8f) // 设置透明度
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ){
            Row(
                verticalAlignment = Alignment.CenterVertically, // 垂直居中对齐
                modifier = Modifier.fillMaxWidth() // 让整个 Row 占满宽度
            ) {
                Spacer(modifier = Modifier.width(8.dp)) // 图标和文本之间的间隔
                Icon(
                    painter = painterResource(R.drawable.ic_affix_jinpguo_24dp),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp)) // 图标和文本之间的间隔
                Text(
                    text = affixCatalog.description,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.tertiary,
                    modifier = Modifier.weight(1f) // 确保 Text 占据剩余空间
                )
            }
            Divider(
                color = MaterialTheme.colorScheme.outline,
                thickness = 1.5.dp // 修改为 2.dp
            )
            // 将整个 Row 包裹在 Box 中，设置边框和透明度
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
            ) {
                AffixItemTitle(
                    title = "词缀",
                    modifier = Modifier
                        .padding(vertical = 4.dp)
                        .weight(1f)
                )
                Divider(
                    color = MaterialTheme.colorScheme.outline,
                    modifier = Modifier
                        .width(1.5.dp) // 修改为 2.dp
                        .fillMaxHeight()
                )
                if (affixList.first().meaning.isNotBlank()) {
                    AffixItemTitle(
                        title = "含义",
                        modifier = Modifier
                            .padding(vertical = 4.dp)
                            .weight(2f)
                    )
                    Divider(
                        color = MaterialTheme.colorScheme.outline,
                        modifier = Modifier
                            .width(1.5.dp) // 修改为 2.dp
                            .fillMaxHeight()
                    )
                }
                AffixItemTitle(
                    title = "例子",
                    modifier = Modifier
                        .padding(vertical = 4.dp)
                        .weight(3f)
                )
            }

            Divider(
                color = MaterialTheme.colorScheme.outline,
                thickness = 1.5.dp // 修改为 2.dp
            )

            affixList.forEach {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(96.dp)
                ) {
                    AffixItemContent(
                        content = it.text,
                        modifier = Modifier.weight(1f)
                    )
                    Divider(
                        color = MaterialTheme.colorScheme.outline,
                        modifier = Modifier
                            .width(1.5.dp) // 修改为 2.dp
                            .fillMaxHeight()
                    )
                    if (it.meaning.isNotBlank()) {
                        AffixItemContent(
                            content = it.meaning,
                            modifier = Modifier
                                .padding(4.dp)
                                .weight(2f)
                        )
                        Divider(
                            color = MaterialTheme.colorScheme.outline,
                            modifier = Modifier
                                .width(1.5.dp) // 修改为 2.dp
                                .fillMaxHeight()
                        )
                    }
                    AffixItemContent(
                        content = it.example,
                        modifier = Modifier
                            .padding(4.dp)
                            .weight(3f)
                    )
                }
                Divider(
                    color = MaterialTheme.colorScheme.outline,
                    thickness = 1.5.dp // 修改为 2.dp
                )
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun PreviewAffixSection() {
    // 示例数据
    val affixCatalog = AffixCatalog(
        type = AffixCatalog.Type.PREFIX, // 这里你需要指定一个type，例如：前缀
        description = "词缀示例"
    )

    // 假设 AffixCatalog 的 id 是 1
    val affixCatalogId = 1L

    val affixList = listOf(
        Affix(text = "un-", meaning = "否定", example = "unhappy", catalogId = affixCatalogId),
        Affix(text = "re-", meaning = "重复", example = "revisit", catalogId = affixCatalogId),
        Affix(text = "pre-", meaning = "预先", example = "preview", catalogId = affixCatalogId)
    )

    AffixSection(
        affixCatalog = affixCatalog,
        affixList = affixList
    )
}


