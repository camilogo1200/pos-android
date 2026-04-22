package com.hawk.authentication.data.mappers

import com.hawk.authentication.data.datasources.local.AuthenticationSessionEntity
import com.hawk.authentication.data.dto.KeycloakTokenDto
import com.hawk.authentication.domain.entities.AuthenticationSession
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonPrimitive

fun JsonObject.toKeycloakTokenDto(): KeycloakTokenDto = KeycloakTokenDto(
    accessToken = getValue("access_token").jsonPrimitive.content,
    expiresInSeconds = getValue("expires_in").jsonPrimitive.content.toLong(),
    refreshExpiresInSeconds = get("refresh_expires_in")?.jsonPrimitive?.contentOrNull?.toLongOrNull(),
    refreshToken = get("refresh_token")?.jsonPrimitive?.contentOrNull,
    tokenType = getValue("token_type").jsonPrimitive.content,
    scope = get("scope")?.jsonPrimitive?.contentOrNull
)

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
