package com.hawk.authentication.data.datasources.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface AuthenticationSessionDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(session: AuthenticationSessionEntity)

    @Query("DELETE FROM authentication_session")
    suspend fun clear()
}
