package com.hawk.customers.data.dto

import kotlinx.serialization.Serializable





@Serializable
data class CreateCustomersRequestDto(
    val customers: List<CreateCustomerDto>
)

@Serializable
data class CreateCustomerDto(
    val customerId: String? = null,
    val customerCode: String? = null,
    val companyId: String? = null,
    val customerType: String,
    val status: String? = null,
    val displayName: String,
    val source: String? = null,
    val clientReferenceId: String? = null,
    val preferredName: String? = null,
    val personProfile: CreateCustomerPersonProfileDto? = null,
    val businessProfile: CreateCustomerBusinessProfileDto? = null,
    val contact: CreateCustomerContactDto = CreateCustomerContactDto(),
    val documents: List<CreateCustomerDocumentDto> = emptyList(),
    val billingProfile: CreateCustomerBillingProfileDto? = null,
    val addresses: List<CreateCustomerAddressDto> = emptyList(),
    val classification: CreateCustomerClassificationDto? = null,
    val preferences: CreateCustomerPreferencesDto? = null,
    val consents: CreateCustomerConsentsDto? = null,
    val notes: CreateCustomerNotesDto? = null
)

@Serializable
data class CreateCustomerPersonProfileDto(
    val firstName: String,
    val middleName: String? = null,
    val lastName: String,
    val secondLastName: String? = null,
    val dateOfBirth: String? = null,
    val gender: String? = null
)

@Serializable
data class CreateCustomerBusinessProfileDto(
    val legalName: String? = null,
    val tradeName: String? = null,
    val taxId: String? = null,
    val industry: String? = null,
    val contactPersons: List<CreateCustomerBusinessContactPersonDto> = emptyList()
)

@Serializable
data class CreateCustomerBusinessContactPersonDto(
    val fullName: String,
    val role: String? = null,
    val phone: String? = null,
    val email: String? = null,
    val isPrimary: Boolean = false
)

@Serializable
data class CreateCustomerContactDto(
    val phones: List<CreateCustomerPhoneDto> = emptyList(),
    val emails: List<CreateCustomerEmailDto> = emptyList(),
    val preferredContactChannel: String? = null,
    val preferredLanguage: String? = null
)

@Serializable
data class CreateCustomerPhoneDto(
    val phoneId: String? = null,
    val type: String,
    val number: String,
    val isPrimary: Boolean = false,
    val isWhatsAppEnabled: Boolean = false
)

@Serializable
data class CreateCustomerEmailDto(
    val emailId: String? = null,
    val type: String,
    val address: String,
    val isPrimary: Boolean = false
)

@Serializable
data class CreateCustomerDocumentDto(
    val type: String,
    val number: String,
    val country: String,
    val isPrimary: Boolean = false
)

@Serializable
data class CreateCustomerBillingProfileDto(
    val documentType: String? = null,
    val invoiceRequired: Boolean = false,
    val legalName: String? = null,
    val taxId: String? = null,
    val taxRegime: String? = null,
    val billingEmail: String? = null,
    val electronicInvoiceEmail: String? = null
)

@Serializable
data class CreateCustomerAddressDto(
    val addressId: String? = null,
    val label: String,
    val type: String,
    val usage: List<String> = emptyList(),
    val line1: String,
    val line2: String? = null,
    val city: String,
    val state: String? = null,
    val country: String,
    val postalCode: String? = null,
    val instructions: String? = null,
    val isPrimary: Boolean = false,
    val isBillingDefault: Boolean = false
)

@Serializable
data class CreateCustomerClassificationDto(
    val tags: List<String> = emptyList(),
    val customerGroup: String? = null
)

@Serializable
data class CreateCustomerPreferencesDto(
    val invoiceDeliveryMethod: String? = null,
    val receiptDeliveryMethod: String? = null,
    val marketingOptIn: Boolean = false,
    val allowWhatsAppNotifications: Boolean = false,
    val doNotCall: Boolean = false,
    val doNotEmail: Boolean = false,
    val doNotWhatsApp: Boolean = false,
    val preferredContactTimeWindow: CreateCustomerPreferredTimeWindowDto? = null
)

@Serializable
data class CreateCustomerPreferredTimeWindowDto(
    val start: String,
    val end: String,
    val timeZone: String
)

@Serializable
data class CreateCustomerConsentsDto(
    val privacyAccepted: Boolean,
    val marketingOptIn: Boolean
)

@Serializable
data class CreateCustomerNotesDto(
    val internalNote: String? = null
)
