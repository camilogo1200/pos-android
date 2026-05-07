package com.hawk.transactions.domain.errors

import java.io.IOException

sealed class TransactionWriteException(message: String) : IOException(message) {
    data object NoConnection : TransactionWriteException(
        message = "No internet connection. Check your connection and try again."
    )

    data class RequestFailed(
        val code: Int? = null
    ) : TransactionWriteException(
        message = if (code == null) {
            "The transaction could not be created right now."
        } else {
            "Transaction request failed with code $code."
        }
    )
}
