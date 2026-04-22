package com.hawk.authentication.domain.entities

data class AuthenticationSession(
    val accessToken: String,
    val refreshToken: String?,
    val tokenType: String,
    val expiresInSeconds: Long,
    val refreshExpiresInSeconds: Long?,
    val scope: String?,
    val username: String
) {
    val hasBearerToken: Boolean
        get() = accessToken.isNotBlank() && tokenType.equals("bearer", ignoreCase = true)
}
