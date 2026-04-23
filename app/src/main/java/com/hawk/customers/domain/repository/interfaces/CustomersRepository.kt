package com.hawk.customers.domain.repository.interfaces

import com.hawk.customers.domain.entities.CreateCustomerDraft
import com.hawk.customers.domain.entities.CustomerDirectory
import kotlinx.coroutines.flow.Flow

interface CustomersRepository {
    fun getCustomers(): Flow<Result<CustomerDirectory>>

    fun createCustomers(customers: List<CreateCustomerDraft>): Flow<Result<Unit>>
}
