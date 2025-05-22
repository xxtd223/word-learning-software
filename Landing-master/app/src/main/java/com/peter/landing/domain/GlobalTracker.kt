package com.peter.landing.domain

import android.util.Log
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

//静态变量管理栈
object GlobalTracker {
    private const val TAG = "GlobalTracker"

    // 初始化为空字符串
    private var _globalContent: String = ""

    // 线程安全锁
    private val mutex = Mutex()

    // 提供对外访问的属性
    val globalContent: String
        get() = _globalContent

    suspend fun appendContent(newContent: String) {
        mutex.withLock {
            _globalContent = newContent
            Log.d(TAG, "globalContent 发生变化: $_globalContent")
        }
    }
}