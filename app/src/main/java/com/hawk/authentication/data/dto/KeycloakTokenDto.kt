package com.hawk.authentication.data.dto

data class KeycloakTokenDto(
    val accessToken: String,
    val expiresInSeconds: Long,
    val refreshExpiresInSeconds: Long?,
    val refreshToken: String?,
    val tokenType: String,
    val scope: String?
)
