package com.hawk.authentication.data.mappers

import com.hawk.authentication.data.datasources.local.AuthenticationSessionEntity
import com.hawk.authentication.data.dto.KeycloakTokenDto
import com.hawk.authentication.domain.entities.AuthenticationSession

fun KeycloakTokenDto.toDomain(username: String): AuthenticationSession = AuthenticationSession(
    accessToken = accessToken,
    refreshToken = refreshToken,
    tokenType = tokenType,
    expiresInSeconds = expiresInSeconds,
    refreshExpiresInSeconds = refreshExpiresInSeconds,
    scope = scope,
    username = username
)

fun AuthenticationSession.toEntity(): AuthenticationSessionEntity = AuthenticationSessionEntity(
    accessToken = accessToken,
    refreshToken = refreshToken,
    tokenType = tokenType,
    expiresInSeconds = expiresInSeconds,
    refreshExpiresInSeconds = refreshExpiresInSeconds,
    scope = scope,
    username = username
)
