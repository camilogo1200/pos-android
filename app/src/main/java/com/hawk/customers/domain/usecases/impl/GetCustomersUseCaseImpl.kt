package com.hawk.customers.domain.usecases.impl

import com.hawk.customers.domain.entities.CustomerDirectory
import com.hawk.customers.domain.repository.interfaces.CustomersRepository
import com.hawk.customers.domain.usecases.interfaces.GetCustomersUseCase
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetCustomersUseCaseImpl @Inject constructor(
    private val customersRepository: CustomersRepository
) : GetCustomersUseCase {

    override fun invoke(): Flow<Result<CustomerDirectory>> = customersRepository.getCustomers()
}
