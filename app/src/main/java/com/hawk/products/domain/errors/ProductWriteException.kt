package com.hawk.products.domain.errors

sealed class ProductWriteException(
    message: String
) : IllegalStateException(message) {
    data object NoConnection : ProductWriteException(
        message = "No internet connection. Check your connection and try again."
    )

    data class RequestFailed(
        val statusCode: Int? = null
    ) : ProductWriteException(
        message = "Something failed while creating the product."
    )
}
