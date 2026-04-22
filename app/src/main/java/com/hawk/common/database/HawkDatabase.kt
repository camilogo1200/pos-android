package com.hawk.common.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hawk.authentication.data.datasources.local.AuthenticationSessionDao
import com.hawk.authentication.data.datasources.local.AuthenticationSessionEntity

@Database(
    entities = [AuthenticationSessionEntity::class],
    version = 1,
    exportSchema = false
)
abstract class HawkDatabase : RoomDatabase() {
    abstract fun authenticationSessionDao(): AuthenticationSessionDao
}
