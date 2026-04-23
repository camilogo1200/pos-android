package com.hawk.customers.domain.errors

sealed class CustomerWriteException(
    message: String
) : IllegalStateException(message) {
    data object NoConnection : CustomerWriteException(
        message = "No internet connection. Check your connection and try again."
    )

    data class RequestFailed(
        val statusCode: Int? = null
    ) : CustomerWriteException(
        message = "Customer creation failed."
    )
}
