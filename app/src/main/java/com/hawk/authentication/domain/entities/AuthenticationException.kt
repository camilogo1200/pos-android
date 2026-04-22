package com.hawk.authentication.domain.entities

sealed class AuthenticationException(
    message: String,
    cause: Throwable? = null
) : Exception(message, cause) {
    class InvalidCredentials : AuthenticationException(
        message = "The provided credentials are invalid."
    )

    class ConnectionFailed(cause: Throwable? = null) : AuthenticationException(
        message = "Unable to connect to the authentication server.",
        cause = cause
    )

    class InvalidPayload : AuthenticationException(
        message = "The authentication response payload was invalid."
    )
}
