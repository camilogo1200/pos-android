package com.hawk.customers.data.datasources.remote.interfaces

import com.hawk.customers.data.dto.CreateCustomersRequestDto
import com.hawk.customers.data.dto.CustomersListResponseDto
import kotlinx.coroutines.flow.Flow

interface CustomersRemoteDataSource {
    fun getCustomers(): Flow<Result<CustomersListResponseDto>>

    fun createCustomers(request: CreateCustomersRequestDto): Flow<Result<Unit>>
}
