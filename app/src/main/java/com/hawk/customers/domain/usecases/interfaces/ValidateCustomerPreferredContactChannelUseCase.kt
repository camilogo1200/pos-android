package com.hawk.customers.domain.usecases.interfaces

import com.hawk.customers.domain.entities.CustomerFieldValidationResult

interface ValidateCustomerPreferredContactChannelUseCase {
    operator fun invoke(channel: String): CustomerFieldValidationResult
}
