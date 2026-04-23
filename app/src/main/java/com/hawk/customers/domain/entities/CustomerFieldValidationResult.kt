package com.hawk.customers.domain.entities

data class CustomerFieldValidationResult(
    val error: CustomerFieldValidationError? = null
) {
    val isValid: Boolean
        get() = error == null
}

enum class CustomerFieldValidationError {
    REQUIRED,
    LETTERS_ONLY,
    INVALID_DATE,
    INVALID_DOCUMENT,
    INVALID_EMAIL,
    INVALID_PHONE
}
