package com.hawk.customers.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class CustomersListResponseDto(
    val requestContext: CustomersRequestContextDto? = null,
    val data: List<CustomerDto> = emptyList(),
    val page: CustomersPageDto? = null
)

@Serializable
data class CustomersRequestContextDto(
    val requestId: String? = null,
    val correlationId: String? = null,
    val servedAt: String? = null
)

@Serializable
data class CustomersPageDto(
    val limit: Int? = null,
    val count: Int? = null,
    val nextCursor: String? = null
)

@Serializable
data class CustomerDto(
    val customerId: String,
    val customerCode: String,
    val companyId: String? = null,
    val customerType: String,
    val status: String,
    val displayName: String,
    val source: String? = null,
    val contact: CustomerContactDto = CustomerContactDto(),
    val addresses: List<CustomerAddressDto> = emptyList(),
    val businessProfile: CustomerBusinessProfileDto? = null,
    val classificationSummary: CustomerClassificationSummaryDto? = null
)

@Serializable
data class CustomerContactDto(
    val phones: List<CustomerPhoneDto> = emptyList(),
    val emails: List<CustomerEmailDto> = emptyList()
)

@Serializable
data class CustomerPhoneDto(
    val phoneId: String? = null,
    val type: String,
    val number: String,
    val isPrimary: Boolean = false,
    val isWhatsAppEnabled: Boolean = false
)

@Serializable
data class CustomerEmailDto(
    val emailId: String? = null,
    val type: String,
    val address: String,
    val isPrimary: Boolean = false
)

@Serializable
data class CustomerAddressDto(
    val addressId: String? = null,
    val label: String,
    val type: String,
    val line1: String,
    val line2: String? = null,
    val city: String,
    val state: String? = null,
    val country: String,
    val postalCode: String? = null,
    val isPrimary: Boolean = false
)

@Serializable
data class CustomerBusinessProfileDto(
    val legalName: String? = null,
    val tradeName: String? = null,
    val taxId: String? = null,
    val industry: String? = null,
    val contactPersons: List<CustomerBusinessContactPersonDto> = emptyList()
)

@Serializable
data class CustomerBusinessContactPersonDto(
    val contactPersonId: String? = null,
    val fullName: String,
    val role: String? = null,
    val phone: String? = null,
    val email: String? = null,
    val isPrimary: Boolean = false
)

@Serializable
data class CustomerClassificationSummaryDto(
    val customerGroup: String? = null
)
