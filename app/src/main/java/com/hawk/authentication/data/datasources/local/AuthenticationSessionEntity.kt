package com.hawk.authentication.data.datasources.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "authentication_session")
data class AuthenticationSessionEntity(
    @PrimaryKey
    @ColumnInfo(name = "session_id")
    val sessionId: Int = 1,
    @ColumnInfo(name = "access_token")
    val accessToken: String,
    @ColumnInfo(name = "refresh_token")
    val refreshToken: String?,
    @ColumnInfo(name = "token_type")
    val tokenType: String,
    @ColumnInfo(name = "expires_in_seconds")
    val expiresInSeconds: Long,
    @ColumnInfo(name = "refresh_expires_in_seconds")
    val refreshExpiresInSeconds: Long?,
    @ColumnInfo(name = "scope")
    val scope: String?,
    @ColumnInfo(name = "username")
    val username: String
)
