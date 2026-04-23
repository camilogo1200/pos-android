package com.hawk.customers.domain.usecases.interfaces

import com.hawk.customers.domain.entities.CustomerDirectory
import kotlinx.coroutines.flow.Flow

interface GetCustomersUseCase {
    operator fun invoke(): Flow<Result<CustomerDirectory>>
}
