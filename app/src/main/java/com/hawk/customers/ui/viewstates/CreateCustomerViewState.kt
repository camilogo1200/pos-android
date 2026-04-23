package com.hawk.customers.ui.viewstates

import androidx.annotation.StringRes
import com.hawk.customers.ui.uimodels.CustomerFormFieldUiModel

enum class CreateCustomerStep {
    PERSONAL_INFO,
    CONTACT_ADDRESS,
    PREFERENCES_NOTES;

    fun next(): CreateCustomerStep = entries.getOrElse(ordinal + 1) { this }

    fun previous(): CreateCustomerStep = entries.getOrElse(ordinal - 1) { this }
}

sealed interface CreateCustomerViewState {
    data class Form(
        val currentStep: CreateCustomerStep = CreateCustomerStep.PERSONAL_INFO,
        val firstName: CustomerFormFieldUiModel = CustomerFormFieldUiModel(),
        val lastName: CustomerFormFieldUiModel = CustomerFormFieldUiModel(),
        val birthDate: CustomerFormFieldUiModel = CustomerFormFieldUiModel(),
        val documentNumber: CustomerFormFieldUiModel = CustomerFormFieldUiModel(),
        val nationalityCode: CustomerFormFieldUiModel = CustomerFormFieldUiModel(),
        val genderCode: String = "",
        val preferredLanguageCode: String = "es-CO",
        val internalTags: List<String> = emptyList(),
        val emailAddress: CustomerFormFieldUiModel = CustomerFormFieldUiModel(),
        val phoneNumber: CustomerFormFieldUiModel = CustomerFormFieldUiModel(),
        val whatsAppNumber: CustomerFormFieldUiModel = CustomerFormFieldUiModel(),
        val countryCode: CustomerFormFieldUiModel = CustomerFormFieldUiModel(value = "CO"),
        val stateCode: String = "",
        val city: CustomerFormFieldUiModel = CustomerFormFieldUiModel(),
        val addressLineOne: CustomerFormFieldUiModel = CustomerFormFieldUiModel(),
        val addressLineTwo: CustomerFormFieldUiModel = CustomerFormFieldUiModel(),
        val postalCode: CustomerFormFieldUiModel = CustomerFormFieldUiModel(),
        val timeZoneCode: String = "America/Bogota",
        val preferredContactChannelCode: CustomerFormFieldUiModel = CustomerFormFieldUiModel(),
        val preferredContactTimeCode: String = "",
        val newsletterOptIn: Boolean = false,
        val customerGroupCode: String = "RETAIL",
        val acquisitionSourceCode: CustomerFormFieldUiModel = CustomerFormFieldUiModel(),
        val publicNote: CustomerFormFieldUiModel = CustomerFormFieldUiModel(),
        val internalNote: CustomerFormFieldUiModel = CustomerFormFieldUiModel(),
        val isSubmitting: Boolean = false,
        val isPrimaryActionEnabled: Boolean = false,
        @param:StringRes val bannerMessageRes: Int? = null
    ) : CreateCustomerViewState

    data class SubmissionResult(
        val isSuccess: Boolean,
        @param:StringRes val titleRes: Int,
        @param:StringRes val messageRes: Int
    ) : CreateCustomerViewState
}
