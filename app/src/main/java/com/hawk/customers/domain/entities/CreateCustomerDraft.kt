package com.hawk.customers.domain.entities

data class CreateCustomerDraft(
    val clientReferenceId: String,
    val customerType: CustomerType,
    val firstName: String,
    val lastName: String,
    val birthDateIso: String,
    val gender: CustomerGender?,
    val documentNumber: String,
    val documentType: String,
    val nationalityCountryCode: String,
    val preferredLanguage: String,
    val primaryEmail: String,
    val primaryPhone: String,
    val whatsAppPhone: String?,
    val addressCountryCode: String,
    val addressState: String?,
    val addressCity: String,
    val addressLineOne: String,
    val addressLineTwo: String?,
    val postalCode: String?,
    val preferredContactChannel: String,
    val preferredContactTimeWindow: CustomerPreferredTimeWindow?,
    val newsletterOptIn: Boolean,
    val customerGroup: String,
    val acquisitionSource: String,
    val publicNote: String?,
    val internalNote: String?,
    val internalTags: List<String>
) {
    val displayName: String
        get() = listOf(firstName.trim(), lastName.trim())
            .filter(String::isNotBlank)
            .joinToString(separator = " ")
}

data class CustomerPreferredTimeWindow(
    val start: String,
    val end: String,
    val timeZone: String
)

enum class CustomerGender {
    MALE,
    FEMALE,
    OTHER,
    PREFER_NOT_TO_SAY
}
