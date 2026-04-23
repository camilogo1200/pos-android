package com.hawk.customers.domain.errors

sealed class CustomerReadException(
    message: String
) : IllegalStateException(message) {
    data object NoConnection : CustomerReadException(
        message = "No internet connection detected. Customers will load when the device reconnects."
    )

    data class RequestFailed(
        val statusCode: Int? = null
    ) : CustomerReadException(
        message = "Customers could not be loaded from the service."
    )
}
