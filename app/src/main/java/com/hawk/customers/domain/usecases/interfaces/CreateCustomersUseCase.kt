package com.hawk.customers.domain.usecases.interfaces

import com.hawk.customers.domain.entities.CreateCustomerDraft
import kotlinx.coroutines.flow.Flow

interface CreateCustomersUseCase {
    operator fun invoke(customers: List<CreateCustomerDraft>): Flow<Result<Unit>>
}
