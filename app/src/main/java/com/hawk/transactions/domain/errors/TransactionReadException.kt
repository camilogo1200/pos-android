package com.hawk.transactions.domain.errors

import java.io.IOException

sealed class TransactionReadException(message: String) : IOException(message) {
    data object NoConnection : TransactionReadException(
        message = "No internet connection. Check your connection and try again."
    )

    data class RequestFailed(
        val code: Int? = null
    ) : TransactionReadException(
        message = if (code == null) {
            "Transactions service is unavailable right now."
        } else {
            "Transactions request failed with code $code."
        }
    )
}
