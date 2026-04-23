package com.hawk.customers.data.datasources.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface CustomerDao {
    @Query("SELECT * FROM customers ORDER BY display_name ASC")
    suspend fun getAll(): List<CustomerEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(customers: List<CustomerEntity>)
}
