package com.hawk.authentication.data.datasources.local

import com.hawk.authentication.data.datasources.local.interfaces.AuthenticationLocalDataSource
import com.hawk.authentication.data.mappers.toEntity
import com.hawk.authentication.domain.entities.AuthenticationSession
import javax.inject.Inject

class RoomAuthenticationLocalDataSource @Inject constructor(
    private val authenticationSessionDao: AuthenticationSessionDao
) : AuthenticationLocalDataSource {

    override suspend fun saveSession(session: AuthenticationSession) {
        authenticationSessionDao.upsert(session.toEntity())
    }

    override suspend fun clearSession() {
        authenticationSessionDao.clear()
    }
}
