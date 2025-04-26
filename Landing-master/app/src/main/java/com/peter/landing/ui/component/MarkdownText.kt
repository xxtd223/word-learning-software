package com.peter.landing.ui.component

import android.text.method.LinkMovementMethod
import android.widget.TextView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import io.noties.markwon.Markwon
import io.noties.markwon.html.HtmlPlugin
import io.noties.markwon.image.ImagesPlugin

@Composable
fun MarkdownRenderer(
    markdown: String,
    textColor: Color,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val markwon = remember {
        Markwon.builder(context)
            .usePlugin(HtmlPlugin.create())                    // 支持 HTML
            .usePlugin(ImagesPlugin.create())                  // 支持图片
            .build()
    }

    // 用 AndroidView 嵌入原生 TextView（支持 Markwon 渲染）
    AndroidView(
        factory = { ctx ->
            TextView(ctx).apply {
                setTextColor(textColor.toArgb())               // 设置文字颜色
                movementMethod = LinkMovementMethod.getInstance() // 支持链接点击
            }
        },
        update = { textView ->
            markwon.setMarkdown(textView, markdown)            // 渲染 Markdown
        },
        modifier = modifier
    )
}