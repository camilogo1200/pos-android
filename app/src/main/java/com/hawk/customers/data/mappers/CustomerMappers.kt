package com.hawk.customers.data.mappers

import com.hawk.customers.data.datasources.local.CustomerEntity
import com.hawk.customers.data.dto.CreateCustomerClassificationDto
import com.hawk.customers.data.dto.CreateCustomerConsentsDto
import com.hawk.customers.data.dto.CreateCustomerEmailDto
import com.hawk.customers.data.dto.CreateCustomerContactDto
import com.hawk.customers.data.dto.CreateCustomerDocumentDto
import com.hawk.customers.data.dto.CreateCustomerDto
import com.hawk.customers.data.dto.CreateCustomerNotesDto
import com.hawk.customers.data.dto.CreateCustomerPhoneDto
import com.hawk.customers.data.dto.CreateCustomerPersonProfileDto
import com.hawk.customers.data.dto.CreateCustomerPreferredTimeWindowDto
import com.hawk.customers.data.dto.CreateCustomerPreferencesDto
import com.hawk.customers.data.dto.CreateCustomerAddressDto
import com.hawk.customers.data.dto.CreateCustomersRequestDto
import com.hawk.customers.data.dto.CustomerDto
import com.hawk.customers.data.dto.CustomersListResponseDto
import com.hawk.customers.domain.entities.CreateCustomerDraft
import com.hawk.customers.domain.entities.Customer
import com.hawk.customers.domain.entities.CustomerDirectory
import com.hawk.customers.domain.entities.CustomerGender
import com.hawk.customers.domain.entities.CustomerPreferredTimeWindow
import com.hawk.customers.domain.entities.CustomerStatus
import com.hawk.customers.domain.entities.CustomerType

fun CustomersListResponseDto.toDomain(): CustomerDirectory = CustomerDirectory(
    customers = data.map(CustomerDto::toDomain),
    loadedCount = page?.count ?: data.size,
    nextCursor = page?.nextCursor
)

fun CustomersListResponseDto.toEntities(): List<CustomerEntity> = data.map(CustomerDto::toEntity)

fun List<CustomerEntity>.toCustomerDirectory(): CustomerDirectory = CustomerDirectory(
    customers = map(CustomerEntity::toDomain),
    loadedCount = size,
    nextCursor = null
)

fun CustomerDto.toDomain(): Customer = Customer(
    customerId = customerId,
    customerCode = customerCode,
    customerType = customerType.toCustomerType(),
    status = status.toCustomerStatus(),
    displayName = displayName,
    source = source.orEmpty(),
    primaryEmail = contact.emails.firstOrNull { it.isPrimary }?.address ?: contact.emails.firstOrNull()?.address,
    primaryPhone = contact.phones.firstOrNull { it.isPrimary }?.number ?: contact.phones.firstOrNull()?.number,
    isPrimaryPhoneWhatsAppEnabled = contact.phones.firstOrNull { it.isPrimary }?.isWhatsAppEnabled
        ?: contact.phones.firstOrNull()?.isWhatsAppEnabled
        ?: false,
    primaryCity = addresses.firstOrNull { it.isPrimary }?.city ?: addresses.firstOrNull()?.city,
    businessTradeName = businessProfile?.tradeName,
    businessLegalName = businessProfile?.legalName,
    primaryBusinessContactRole = businessProfile?.contactPersons
        ?.firstOrNull { it.isPrimary }
        ?.role
        ?: businessProfile?.contactPersons?.firstOrNull()?.role,
    classificationGroup = classificationSummary?.customerGroup
)

fun CustomerEntity.toDomain(): Customer = Customer(
    customerId = customerId,
    customerCode = customerCode,
    customerType = customerType.toCustomerType(),
    status = status.toCustomerStatus(),
    displayName = displayName,
    source = source,
    primaryEmail = primaryEmail,
    primaryPhone = primaryPhone,
    isPrimaryPhoneWhatsAppEnabled = isPrimaryPhoneWhatsAppEnabled,
    primaryCity = primaryCity,
    businessTradeName = businessTradeName,
    businessLegalName = businessLegalName,
    primaryBusinessContactRole = primaryBusinessContactRole,
    classificationGroup = classificationGroup
)

fun List<CreateCustomerDraft>.toCreateCustomersRequestDto(): CreateCustomersRequestDto =
    CreateCustomersRequestDto(
        customers = map(CreateCustomerDraft::toCreateCustomerDto)
    )

private fun CreateCustomerDraft.toCreateCustomerDto(): CreateCustomerDto = CreateCustomerDto(
    clientReferenceId = clientReferenceId,
    customerType = customerType.toTransportValue(),
    status = "ACTIVE",
    displayName = displayName,
    source = acquisitionSource.ifBlank { "POS" },
    preferredName = firstName,
    personProfile = CreateCustomerPersonProfileDto(
        firstName = firstName,
        lastName = lastName,
        dateOfBirth = birthDateIso,
        gender = gender?.toTransportValue()
    ),
    contact = CreateCustomerContactDto(
        phones = buildList {
            add(
                CreateCustomerPhoneDto(
                    type = "MOBILE",
                    number = primaryPhone,
                    isPrimary = true,
                    isWhatsAppEnabled = preferredContactChannel == "WHATSAPP" || !whatsAppPhone.isNullOrBlank()
                )
            )
            whatsAppPhone
                ?.takeIf(String::isNotBlank)
                ?.takeIf { it != primaryPhone }
                ?.let { number ->
                    add(
                        CreateCustomerPhoneDto(
                            type = "MOBILE",
                            number = number,
                            isPrimary = false,
                            isWhatsAppEnabled = true
                        )
                    )
                }
        },
        emails = listOf(
            CreateCustomerEmailDto(
                type = "PERSONAL",
                address = primaryEmail,
                isPrimary = true
            )
        ),
        preferredContactChannel = preferredContactChannel,
        preferredLanguage = preferredLanguage
    ),
    documents = listOf(
        CreateCustomerDocumentDto(
            type = documentType,
            number = documentNumber,
            country = nationalityCountryCode,
            isPrimary = true
        )
    ),
    addresses = listOf(
        CreateCustomerAddressDto(
            label = "Primary address",
            type = "HOME",
            line1 = addressLineOne,
            line2 = addressLineTwo.takeIf { !it.isNullOrBlank() },
            city = addressCity,
            state = addressState.takeIf { !it.isNullOrBlank() },
            country = addressCountryCode,
            postalCode = postalCode.takeIf { !it.isNullOrBlank() },
            isPrimary = true
        )
    ),
    classification = CreateCustomerClassificationDto(
        customerGroup = customerGroup.ifBlank { "RETAIL" }
    ),
    preferences = CreateCustomerPreferencesDto(
        invoiceDeliveryMethod = "EMAIL",
        receiptDeliveryMethod = preferredContactChannel.toReceiptDeliveryMethod(),
        marketingOptIn = newsletterOptIn,
        allowWhatsAppNotifications = preferredContactChannel == "WHATSAPP" || !whatsAppPhone.isNullOrBlank(),
        preferredContactTimeWindow = preferredContactTimeWindow?.toDto()
    ),
    consents = CreateCustomerConsentsDto(
        privacyAccepted = true,
        marketingOptIn = newsletterOptIn
    ),
    notes = CreateCustomerNotesDto(
        internalNote = buildInternalNote()
    )
)

private fun CustomerDto.toEntity(): CustomerEntity = CustomerEntity(
    customerId = customerId,
    customerCode = customerCode,
    customerType = customerType,
    status = status,
    displayName = displayName,
    source = source.orEmpty(),
    primaryEmail = contact.emails.firstOrNull { it.isPrimary }?.address ?: contact.emails.firstOrNull()?.address,
    primaryPhone = contact.phones.firstOrNull { it.isPrimary }?.number ?: contact.phones.firstOrNull()?.number,
    isPrimaryPhoneWhatsAppEnabled = contact.phones.firstOrNull { it.isPrimary }?.isWhatsAppEnabled
        ?: contact.phones.firstOrNull()?.isWhatsAppEnabled
        ?: false,
    primaryCity = addresses.firstOrNull { it.isPrimary }?.city ?: addresses.firstOrNull()?.city,
    businessTradeName = businessProfile?.tradeName,
    businessLegalName = businessProfile?.legalName,
    primaryBusinessContactRole = businessProfile?.contactPersons
        ?.firstOrNull { it.isPrimary }
        ?.role
        ?: businessProfile?.contactPersons?.firstOrNull()?.role,
    classificationGroup = classificationSummary?.customerGroup
)

private fun String.toCustomerType(): CustomerType = when (uppercase()) {
    "PERSON" -> CustomerType.PERSON
    "BUSINESS" -> CustomerType.BUSINESS
    else -> CustomerType.UNKNOWN
}

private fun String.toCustomerStatus(): CustomerStatus = when (uppercase()) {
    "ACTIVE" -> CustomerStatus.ACTIVE
    "INACTIVE" -> CustomerStatus.INACTIVE
    else -> CustomerStatus.UNKNOWN
}

private fun CustomerType.toTransportValue(): String = when (this) {
    CustomerType.PERSON -> "PERSON"
    CustomerType.BUSINESS -> "BUSINESS"
    CustomerType.UNKNOWN -> "PERSON"
}

private fun CustomerGender.toTransportValue(): String = when (this) {
    CustomerGender.MALE -> "MALE"
    CustomerGender.FEMALE -> "FEMALE"
    CustomerGender.OTHER -> "OTHER"
    CustomerGender.PREFER_NOT_TO_SAY -> "PREFER_NOT_TO_SAY"
}

private fun String.toReceiptDeliveryMethod(): String = when (uppercase()) {
    "WHATSAPP" -> "WHATSAPP"
    "CALL" -> "PRINT"
    else -> "EMAIL"
}

private fun CustomerPreferredTimeWindow.toDto(): CreateCustomerPreferredTimeWindowDto =
    CreateCustomerPreferredTimeWindowDto(
        start = start,
        end = end,
        timeZone = timeZone
    )

private fun CreateCustomerDraft.buildInternalNote(): String? {
    val sections = buildList {
        publicNote
            ?.trim()
            ?.takeIf(String::isNotBlank)
            ?.let { add("Customer note: $it") }
        internalNote
            ?.trim()
            ?.takeIf(String::isNotBlank)
            ?.let { add("Internal note: $it") }
    }

    return sections
        .takeIf { it.isNotEmpty() }
        ?.joinToString(separator = "\n\n")
}
