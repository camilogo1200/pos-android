package com.hawk.customers.data.datasources.local

import com.hawk.customers.data.datasources.local.interfaces.CustomersLocalDataSource
import java.io.IOException
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RoomCustomersLocalDataSource @Inject constructor(
    private val customerDao: CustomerDao
) : CustomersLocalDataSource {

    override fun getCustomers(): Flow<Result<List<CustomerEntity>>> = flow {
        try {
            emit(Result.success(customerDao.getAll()))
        } catch (exception: Exception) {
            emit(Result.failure(IOException("Unable to read customers from local storage.", exception)))
        }
    }

    override suspend fun upsertCustomers(customers: List<CustomerEntity>) {
        customerDao.upsertAll(customers)
    }
}
