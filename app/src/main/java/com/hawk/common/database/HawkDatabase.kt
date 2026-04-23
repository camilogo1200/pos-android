package com.hawk.common.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hawk.authentication.data.datasources.local.AuthenticationSessionDao
import com.hawk.authentication.data.datasources.local.AuthenticationSessionEntity
import com.hawk.customers.data.datasources.local.CustomerDao
import com.hawk.customers.data.datasources.local.CustomerEntity

@Database(
    entities = [AuthenticationSessionEntity::class, CustomerEntity::class],
    version = 2,
    exportSchema = false
)
abstract class HawkDatabase : RoomDatabase() {
    abstract fun authenticationSessionDao(): AuthenticationSessionDao
    abstract fun customerDao(): CustomerDao
}
