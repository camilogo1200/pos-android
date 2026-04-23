package com.hawk.authentication.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class KeycloakTokenDto(
    @SerialName("access_token")
    val accessToken: String,
    @SerialName("expires_in")
    val expiresInSeconds: Long,
    @SerialName("refresh_expires_in")
    val refreshExpiresInSeconds: Long?,
    @SerialName("refresh_token")
    val refreshToken: String?,
    @SerialName("token_type")
    val tokenType: String,
    val scope: String?
)
