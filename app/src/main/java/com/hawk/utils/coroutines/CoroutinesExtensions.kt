package com.hawk.utils.coroutines

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

fun ViewModel.launch(
    coroutineDispatcher: CoroutineDispatcher,
    block: suspend CoroutineScope.() -> Unit
): Job = viewModelScope.launch(context = coroutineDispatcher, block = block)

suspend fun <T> networkCall(
    coroutineDispatcher: CoroutineDispatcher,
    block: suspend () -> Result<T>
): Result<T> {
    return withContext(coroutineDispatcher) {
        ensureActive()
        try {
            block.invoke()
        } catch (ex: Exception) {
            return@withContext Result.failure<T>(ex)
        }
    }
}
