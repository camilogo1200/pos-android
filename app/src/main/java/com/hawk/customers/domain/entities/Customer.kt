package com.hawk.customers.domain.entities

data class Customer(
    val customerId: String,
    val customerCode: String,
    val customerType: CustomerType,
    val status: CustomerStatus,
    val displayName: String,
    val source: String,
    val primaryEmail: String?,
    val primaryPhone: String?,
    val isPrimaryPhoneWhatsAppEnabled: Boolean,
    val primaryCity: String?,
    val businessTradeName: String?,
    val businessLegalName: String?,
    val primaryBusinessContactRole: String?,
    val classificationGroup: String?
)

data class CustomerDirectory(
    val customers: List<Customer>,
    val loadedCount: Int,
    val nextCursor: String?
)

enum class CustomerType {
    PERSON,
    BUSINESS,
    UNKNOWN
}

enum class CustomerStatus {
    ACTIVE,
    INACTIVE,
    UNKNOWN
}
