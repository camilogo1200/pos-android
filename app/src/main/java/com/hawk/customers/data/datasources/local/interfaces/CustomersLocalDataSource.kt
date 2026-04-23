package com.hawk.customers.data.datasources.local.interfaces

import com.hawk.customers.data.datasources.local.CustomerEntity
import kotlinx.coroutines.flow.Flow

interface CustomersLocalDataSource {
    fun getCustomers(): Flow<Result<List<CustomerEntity>>>

    suspend fun upsertCustomers(customers: List<CustomerEntity>)
}
