package com.hawk.customers.domain.usecases.impl

import com.hawk.customers.domain.entities.CreateCustomerDraft
import com.hawk.customers.domain.repository.interfaces.CustomersRepository
import com.hawk.customers.domain.usecases.interfaces.CreateCustomersUseCase
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class CreateCustomersUseCaseImpl @Inject constructor(
    private val customersRepository: CustomersRepository
) : CreateCustomersUseCase {

    override fun invoke(customers: List<CreateCustomerDraft>): Flow<Result<Unit>> =
        customersRepository.createCustomers(customers)
}
