package com.peter.landing.ui.screen.help

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.peter.landing.R
import com.peter.landing.data.local.help.Help
import com.peter.landing.data.local.help.HelpCatalog

@Composable
fun HelpSection(
    helpCatalog: HelpCatalog,
    helpList: List<Help>
) {
    // 将整个 Column 包裹在 Box 中，设置边框和透明度
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp) // 调整外边距
            .background(MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(16.dp)) // 背景色和圆角效果
            .border(2.dp, MaterialTheme.colorScheme.outline, shape = RoundedCornerShape(16.dp)) // 设置更粗的边框和圆角
            .alpha(0.6f) // 设置透明度
    ) {
        Column(
            modifier = Modifier
                .padding(top = 16.dp, bottom = 8.dp)
                .fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically, // 垂直居中对齐
                modifier = Modifier.fillMaxWidth() // 让整个 Row 占满宽度
            ) {
                Spacer(modifier = Modifier.width(8.dp)) // 图标和文本之间的间隔
                Icon(
                    painter = painterResource(R.drawable.ic_help_yazi_24dp),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier.size(25.dp)
                )
                Spacer(modifier = Modifier.width(6.dp)) // 图标和文本之间的间隔
                Text(
                    text = helpCatalog.name,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f) // 确保 Text 占据剩余空间
                )
            }
                Spacer(modifier = Modifier.padding(vertical = 1.dp))
                Text(
                    text = helpCatalog.description,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.outline,
                    modifier = Modifier.padding(horizontal = 15.dp)
                )
            Spacer(modifier = Modifier.padding(bottom = 16.dp))
            Divider(thickness = 1.5.dp, color = MaterialTheme.colorScheme.outline)
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                helpList.forEach {
                    Column(
                        modifier = Modifier
                            .padding(top = 16.dp, bottom = 8.dp)
                            .fillMaxWidth()
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically, // 垂直居中对齐
                            modifier = Modifier.fillMaxWidth() // 让整个 Row 占满宽度
                        ) {
                            Spacer(modifier = Modifier.width(8.dp)) // 图标和文本之间的间隔
                            Icon(
                                painter = painterResource(R.drawable.ic_help_xigua_24dp),
                                contentDescription = null,
                                tint = Color.Unspecified,
                                modifier = Modifier.size(25.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp)) // 图标和文本之间的间隔
                            Text(
                                text = it.title,
                                color = MaterialTheme.colorScheme.secondary,
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        // for alphabet list
                        val content: String = if (it.content.contains("@")) {
                            it.content.replace("@", "\n")
                        } else {
                            it.content
                        }
                        Spacer(modifier = Modifier.padding(vertical = 1.dp))
                        Text(
                            text = content,
                            color = MaterialTheme.colorScheme.tertiary,
                            style = MaterialTheme.typography.titleSmall,
                            modifier = Modifier.padding(horizontal = 15.dp)
                        )
                    }
                    Divider(thickness = 1.5.dp, color = MaterialTheme.colorScheme.outline)
                }
            }

        }
    }

}
@Preview(showBackground = true)
@Composable
fun HelpSectionPreview() {
    // 示例 HelpCatalog 对象
    val sampleCatalog = HelpCatalog(
        id = 1L,
        name = "常见问题",
        description = "功能与使用说明"
    )

    // 示例 Help 列表，catalogId 对应 sampleCatalog.id
    val sampleHelpList = listOf(
        Help(
            catalogId = 1L,
            title = "如何添加新单词？",
            content = "点击右上角的 + 按钮@填写单词与释义@点击保存"
        ),
        Help(
            catalogId = 1L,
            title = "如何查看学习进度？",
            content = "进入个人中心页面@选择‘学习统计’查看每日学习情况"
        )
    )

    MaterialTheme {
        HelpSection(
            helpCatalog = sampleCatalog,
            helpList = sampleHelpList
        )
    }
}


