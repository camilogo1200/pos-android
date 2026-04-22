package com.hawk.authentication.data.datasources.local.interfaces

import com.hawk.authentication.domain.entities.AuthenticationSession

interface AuthenticationLocalDataSource {
    suspend fun saveSession(session: AuthenticationSession)
    suspend fun clearSession()
}
