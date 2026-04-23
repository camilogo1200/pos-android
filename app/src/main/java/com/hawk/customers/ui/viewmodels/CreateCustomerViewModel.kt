package com.hawk.customers.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.hawk.R
import com.hawk.customers.domain.entities.CreateCustomerDraft
import com.hawk.customers.domain.entities.CustomerFieldValidationError
import com.hawk.customers.domain.entities.CustomerGender
import com.hawk.customers.domain.entities.CustomerPreferredTimeWindow
import com.hawk.customers.domain.entities.CustomerType
import com.hawk.customers.domain.errors.CustomerWriteException
import com.hawk.customers.domain.usecases.interfaces.CreateCustomersUseCase
import com.hawk.customers.domain.usecases.interfaces.ValidateCustomerAcquisitionSourceUseCase
import com.hawk.customers.domain.usecases.interfaces.ValidateCustomerAddressLineOneUseCase
import com.hawk.customers.domain.usecases.interfaces.ValidateCustomerBirthDateUseCase
import com.hawk.customers.domain.usecases.interfaces.ValidateCustomerCityUseCase
import com.hawk.customers.domain.usecases.interfaces.ValidateCustomerCountryUseCase
import com.hawk.customers.domain.usecases.interfaces.ValidateCustomerDocumentNumberUseCase
import com.hawk.customers.domain.usecases.interfaces.ValidateCustomerEmailUseCase
import com.hawk.customers.domain.usecases.interfaces.ValidateCustomerFirstNameUseCase
import com.hawk.customers.domain.usecases.interfaces.ValidateCustomerLastNameUseCase
import com.hawk.customers.domain.usecases.interfaces.ValidateCustomerNationalityUseCase
import com.hawk.customers.domain.usecases.interfaces.ValidateCustomerPhoneUseCase
import com.hawk.customers.domain.usecases.interfaces.ValidateCustomerPreferredContactChannelUseCase
import com.hawk.customers.ui.uimodels.CustomerFormFieldUiModel
import com.hawk.customers.ui.viewstates.CreateCustomerStep
import com.hawk.customers.ui.viewstates.CreateCustomerViewState
import com.hawk.customers.ui.viewstates.CreateCustomerViewState.Form
import com.hawk.customers.ui.viewstates.CreateCustomerViewState.SubmissionResult
import com.hawk.utils.coroutines.IoDispatcher
import com.hawk.utils.coroutines.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.ResolverStyle
import java.util.UUID
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update

@HiltViewModel
class CreateCustomerViewModel @Inject constructor(
    private val createCustomersUseCase: CreateCustomersUseCase,
    private val validateCustomerFirstNameUseCase: ValidateCustomerFirstNameUseCase,
    private val validateCustomerLastNameUseCase: ValidateCustomerLastNameUseCase,
    private val validateCustomerBirthDateUseCase: ValidateCustomerBirthDateUseCase,
    private val validateCustomerDocumentNumberUseCase: ValidateCustomerDocumentNumberUseCase,
    private val validateCustomerNationalityUseCase: ValidateCustomerNationalityUseCase,
    private val validateCustomerEmailUseCase: ValidateCustomerEmailUseCase,
    private val validateCustomerPhoneUseCase: ValidateCustomerPhoneUseCase,
    private val validateCustomerCountryUseCase: ValidateCustomerCountryUseCase,
    private val validateCustomerCityUseCase: ValidateCustomerCityUseCase,
    private val validateCustomerAddressLineOneUseCase: ValidateCustomerAddressLineOneUseCase,
    private val validateCustomerPreferredContactChannelUseCase: ValidateCustomerPreferredContactChannelUseCase,
    private val validateCustomerAcquisitionSourceUseCase: ValidateCustomerAcquisitionSourceUseCase,
    @param:IoDispatcher private val coroutineDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val birthDateFormatter = DateTimeFormatter.ofPattern("MM/dd/uuuu")
        .withResolverStyle(ResolverStyle.STRICT)

    private val _viewState = MutableStateFlow<CreateCustomerViewState>(Form())
    val viewState: StateFlow<CreateCustomerViewState> = _viewState.asStateFlow()

    fun onFirstNameChanged(value: String) {
        updateForm { state ->
            state.copy(
                firstName = validateFirstNameField(
                    state.firstName.copy(value = value),
                    forceTouched = state.firstName.isTouched
                )
            )
        }
    }

    fun onLastNameChanged(value: String) {
        updateForm { state ->
            state.copy(
                lastName = validateLastNameField(
                    state.lastName.copy(value = value),
                    forceTouched = state.lastName.isTouched
                )
            )
        }
    }

    fun onBirthDateChanged(value: String) {
        updateForm { state ->
            state.copy(
                birthDate = validateBirthDateField(
                    state.birthDate.copy(value = value),
                    forceTouched = state.birthDate.isTouched
                )
            )
        }
    }

    fun onDocumentNumberChanged(value: String) {
        updateForm { state ->
            state.copy(
                documentNumber = validateDocumentNumberField(
                    state.documentNumber.copy(value = value),
                    forceTouched = state.documentNumber.isTouched
                )
            )
        }
    }

    fun onNationalityChanged(value: String) {
        updateForm { state ->
            state.copy(
                nationalityCode = validateNationalityField(
                    state.nationalityCode.copy(value = value),
                    forceTouched = state.nationalityCode.isTouched
                )
            )
        }
    }

    fun onGenderChanged(value: String) {
        updateForm { state ->
            state.copy(genderCode = value)
        }
    }

    fun onPreferredLanguageChanged(value: String) {
        updateForm { state ->
            state.copy(preferredLanguageCode = value)
        }
    }

    fun onAddInternalTag(tag: String) {
        val normalizedTag = tag.trim()
        if (normalizedTag.isBlank()) {
            return
        }

        updateForm { state ->
            if (state.internalTags.any { it.equals(normalizedTag, ignoreCase = true) }) {
                state
            } else {
                state.copy(internalTags = state.internalTags + normalizedTag)
            }
        }
    }

    fun onRemoveInternalTag(tag: String) {
        updateForm { state ->
            state.copy(
                internalTags = state.internalTags.filterNot { it.equals(tag, ignoreCase = true) }
            )
        }
    }

    fun onEmailAddressChanged(value: String) {
        updateForm { state ->
            state.copy(
                emailAddress = validateEmailField(
                    state.emailAddress.copy(value = value),
                    forceTouched = state.emailAddress.isTouched
                )
            )
        }
    }

    fun onPhoneNumberChanged(value: String) {
        updateForm { state ->
            state.copy(
                phoneNumber = validatePhoneField(
                    state.phoneNumber.copy(value = value),
                    forceTouched = state.phoneNumber.isTouched
                )
            )
        }
    }

    fun onWhatsAppNumberChanged(value: String) {
        updateForm { state ->
            state.copy(
                whatsAppNumber = validateOptionalPhoneField(
                    state.whatsAppNumber.copy(value = value),
                    forceTouched = state.whatsAppNumber.isTouched
                )
            )
        }
    }

    fun onCountryChanged(value: String) {
        updateForm { state ->
            state.copy(
                countryCode = validateCountryField(
                    state.countryCode.copy(value = value),
                    forceTouched = state.countryCode.isTouched
                )
            )
        }
    }

    fun onStateChanged(value: String) {
        updateForm { state ->
            state.copy(stateCode = value)
        }
    }

    fun onCityChanged(value: String) {
        updateForm { state ->
            state.copy(
                city = validateCityField(
                    state.city.copy(value = value),
                    forceTouched = state.city.isTouched
                )
            )
        }
    }

    fun onAddressLineOneChanged(value: String) {
        updateForm { state ->
            state.copy(
                addressLineOne = validateAddressLineOneField(
                    state.addressLineOne.copy(value = value),
                    forceTouched = state.addressLineOne.isTouched
                )
            )
        }
    }

    fun onAddressLineTwoChanged(value: String) {
        updateForm { state ->
            state.copy(addressLineTwo = state.addressLineTwo.copy(value = value))
        }
    }

    fun onPostalCodeChanged(value: String) {
        updateForm { state ->
            state.copy(postalCode = state.postalCode.copy(value = value))
        }
    }

    fun onTimeZoneChanged(value: String) {
        updateForm { state ->
            state.copy(timeZoneCode = value)
        }
    }

    fun onPreferredContactChannelChanged(value: String) {
        updateForm { state ->
            state.copy(
                preferredContactChannelCode = validatePreferredContactChannelField(
                    state.preferredContactChannelCode.copy(value = value),
                    forceTouched = state.preferredContactChannelCode.isTouched
                )
            )
        }
    }

    fun onPreferredContactTimeChanged(value: String) {
        updateForm { state ->
            state.copy(preferredContactTimeCode = value)
        }
    }

    fun onNewsletterOptInChanged(isChecked: Boolean) {
        updateForm { state ->
            state.copy(newsletterOptIn = isChecked)
        }
    }

    fun onCustomerGroupChanged(value: String) {
        updateForm { state ->
            state.copy(customerGroupCode = value)
        }
    }

    fun onAcquisitionSourceChanged(value: String) {
        updateForm { state ->
            state.copy(
                acquisitionSourceCode = validateAcquisitionSourceField(
                    state.acquisitionSourceCode.copy(value = value),
                    forceTouched = state.acquisitionSourceCode.isTouched
                )
            )
        }
    }

    fun onPublicNoteChanged(value: String) {
        updateForm { state ->
            state.copy(publicNote = state.publicNote.copy(value = value))
        }
    }

    fun onInternalNoteChanged(value: String) {
        updateForm { state ->
            state.copy(internalNote = state.internalNote.copy(value = value))
        }
    }

    fun onPreviousClicked() {
        updateForm { state ->
            state.copy(currentStep = state.currentStep.previous())
        }
    }

    fun onNextClicked() {
        val formState = _viewState.value as? Form ?: return
        val validatedState = validateStep(formState, formState.currentStep, forceTouched = true)
            .syncPrimaryActionEnabled()

        if (!validatedState.isPrimaryActionEnabled) {
            _viewState.value = validatedState.copy(
                bannerMessageRes = R.string.customers_create_validation_banner
            )
            return
        }

        _viewState.value = validatedState.copy(
            currentStep = validatedState.currentStep.next(),
            bannerMessageRes = null
        ).syncPrimaryActionEnabled()
    }

    fun onSaveCustomerClicked() {
        val formState = forceValidateAll() ?: return
        if (formState.isSubmitting) {
            return
        }

        val firstInvalidStep = formState.firstInvalidStep()
        if (firstInvalidStep != null) {
            _viewState.value = formState.copy(
                currentStep = firstInvalidStep,
                bannerMessageRes = R.string.customers_create_validation_banner
            ).syncPrimaryActionEnabled()
            return
        }

        val request = formState.toCreateCustomerDraft() ?: return

        launch(coroutineDispatcher) {
            _viewState.update { state ->
                (state as? Form)?.copy(
                    isSubmitting = true,
                    bannerMessageRes = null
                ) ?: state
            }

            createCustomersUseCase(listOf(request)).collectLatest { result ->
                result
                    .onSuccess {
                        _viewState.value = SubmissionResult(
                            isSuccess = true,
                            titleRes = R.string.customers_create_success_title,
                            messageRes = R.string.customers_create_success_body
                        )
                    }
                    .onFailure { throwable ->
                        _viewState.value = SubmissionResult(
                            isSuccess = false,
                            titleRes = R.string.customers_create_failed_title,
                            messageRes = when (throwable) {
                                is CustomerWriteException.NoConnection ->
                                    R.string.customers_create_no_connection_body

                                else -> R.string.customers_create_failed_body
                            }
                        )
                    }
            }
        }
    }

    fun resetForm() {
        _viewState.value = Form()
    }

    private fun updateForm(transform: (Form) -> Form) {
        _viewState.update { state ->
            val formState = state as? Form ?: return@update state
            transform(formState).copy(
                bannerMessageRes = null,
                isSubmitting = false
            ).syncPrimaryActionEnabled()
        }
    }

    private fun forceValidateAll(): Form? {
        val formState = _viewState.value as? Form ?: return null
        val validatedState = validateStep(
            validateStep(
                validateStep(formState, CreateCustomerStep.PERSONAL_INFO, forceTouched = true),
                CreateCustomerStep.CONTACT_ADDRESS,
                forceTouched = true
            ),
            CreateCustomerStep.PREFERENCES_NOTES,
            forceTouched = true
        ).syncPrimaryActionEnabled()

        _viewState.value = validatedState
        return validatedState
    }

    private fun validateStep(
        state: Form,
        step: CreateCustomerStep,
        forceTouched: Boolean
    ): Form = when (step) {
        CreateCustomerStep.PERSONAL_INFO -> state.copy(
            firstName = validateFirstNameField(state.firstName, forceTouched),
            lastName = validateLastNameField(state.lastName, forceTouched),
            birthDate = validateBirthDateField(state.birthDate, forceTouched),
            documentNumber = validateDocumentNumberField(state.documentNumber, forceTouched),
            nationalityCode = validateNationalityField(state.nationalityCode, forceTouched)
        )

        CreateCustomerStep.CONTACT_ADDRESS -> state.copy(
            emailAddress = validateEmailField(state.emailAddress, forceTouched),
            phoneNumber = validatePhoneField(state.phoneNumber, forceTouched),
            whatsAppNumber = validateOptionalPhoneField(state.whatsAppNumber, forceTouched),
            countryCode = validateCountryField(state.countryCode, forceTouched),
            city = validateCityField(state.city, forceTouched),
            addressLineOne = validateAddressLineOneField(state.addressLineOne, forceTouched)
        )

        CreateCustomerStep.PREFERENCES_NOTES -> state.copy(
            preferredContactChannelCode = validatePreferredContactChannelField(
                state.preferredContactChannelCode,
                forceTouched
            ),
            acquisitionSourceCode = validateAcquisitionSourceField(
                state.acquisitionSourceCode,
                forceTouched
            )
        )
    }

    private fun validateFirstNameField(
        field: CustomerFormFieldUiModel,
        forceTouched: Boolean
    ): CustomerFormFieldUiModel {
        val isTouched = forceTouched || field.isTouched || field.value.isNotEmpty()
        val errorMessageRes = validateCustomerFirstNameUseCase(field.value)
            .error
            .toFirstNameErrorRes(isTouched)

        return field.copy(
            errorMessageRes = errorMessageRes,
            isTouched = isTouched
        )
    }

    private fun validateLastNameField(
        field: CustomerFormFieldUiModel,
        forceTouched: Boolean
    ): CustomerFormFieldUiModel {
        val isTouched = forceTouched || field.isTouched || field.value.isNotEmpty()
        val errorMessageRes = validateCustomerLastNameUseCase(field.value)
            .error
            .toLastNameErrorRes(isTouched)

        return field.copy(
            errorMessageRes = errorMessageRes,
            isTouched = isTouched
        )
    }

    private fun validateBirthDateField(
        field: CustomerFormFieldUiModel,
        forceTouched: Boolean
    ): CustomerFormFieldUiModel {
        val isTouched = forceTouched || field.isTouched || field.value.isNotEmpty()
        val errorMessageRes = validateCustomerBirthDateUseCase(field.value)
            .error
            .toBirthDateErrorRes(isTouched)

        return field.copy(
            errorMessageRes = errorMessageRes,
            isTouched = isTouched
        )
    }

    private fun validateDocumentNumberField(
        field: CustomerFormFieldUiModel,
        forceTouched: Boolean
    ): CustomerFormFieldUiModel {
        val isTouched = forceTouched || field.isTouched || field.value.isNotEmpty()
        val errorMessageRes = validateCustomerDocumentNumberUseCase(field.value)
            .error
            .toDocumentNumberErrorRes(isTouched)

        return field.copy(
            errorMessageRes = errorMessageRes,
            isTouched = isTouched
        )
    }

    private fun validateNationalityField(
        field: CustomerFormFieldUiModel,
        forceTouched: Boolean
    ): CustomerFormFieldUiModel {
        val isTouched = forceTouched || field.isTouched || field.value.isNotEmpty()
        val errorMessageRes = validateCustomerNationalityUseCase(field.value)
            .error
            .toNationalityErrorRes(isTouched)

        return field.copy(
            errorMessageRes = errorMessageRes,
            isTouched = isTouched
        )
    }

    private fun validateEmailField(
        field: CustomerFormFieldUiModel,
        forceTouched: Boolean
    ): CustomerFormFieldUiModel {
        val isTouched = forceTouched || field.isTouched || field.value.isNotEmpty()
        val errorMessageRes = validateCustomerEmailUseCase(field.value)
            .error
            .toEmailErrorRes(isTouched)

        return field.copy(
            errorMessageRes = errorMessageRes,
            isTouched = isTouched
        )
    }

    private fun validatePhoneField(
        field: CustomerFormFieldUiModel,
        forceTouched: Boolean
    ): CustomerFormFieldUiModel {
        val isTouched = forceTouched || field.isTouched || field.value.isNotEmpty()
        val errorMessageRes = validateCustomerPhoneUseCase(field.value)
            .error
            .toPhoneErrorRes(isTouched)

        return field.copy(
            errorMessageRes = errorMessageRes,
            isTouched = isTouched
        )
    }

    private fun validateOptionalPhoneField(
        field: CustomerFormFieldUiModel,
        forceTouched: Boolean
    ): CustomerFormFieldUiModel {
        val isTouched = forceTouched || field.isTouched || field.value.isNotEmpty()
        if (field.value.isBlank()) {
            return field.copy(
                errorMessageRes = null,
                isTouched = isTouched
            )
        }

        val errorMessageRes = validateCustomerPhoneUseCase(field.value)
            .error
            .let { error ->
                if (error == CustomerFieldValidationError.REQUIRED) {
                    null
                } else {
                    error.toPhoneErrorRes(isTouched)
                }
            }

        return field.copy(
            errorMessageRes = errorMessageRes,
            isTouched = isTouched
        )
    }

    private fun validateCountryField(
        field: CustomerFormFieldUiModel,
        forceTouched: Boolean
    ): CustomerFormFieldUiModel {
        val isTouched = forceTouched || field.isTouched || field.value.isNotEmpty()
        val errorMessageRes = validateCustomerCountryUseCase(field.value)
            .error
            .toCountryErrorRes(isTouched)

        return field.copy(
            errorMessageRes = errorMessageRes,
            isTouched = isTouched
        )
    }

    private fun validateCityField(
        field: CustomerFormFieldUiModel,
        forceTouched: Boolean
    ): CustomerFormFieldUiModel {
        val isTouched = forceTouched || field.isTouched || field.value.isNotEmpty()
        val errorMessageRes = validateCustomerCityUseCase(field.value)
            .error
            .toCityErrorRes(isTouched)

        return field.copy(
            errorMessageRes = errorMessageRes,
            isTouched = isTouched
        )
    }

    private fun validateAddressLineOneField(
        field: CustomerFormFieldUiModel,
        forceTouched: Boolean
    ): CustomerFormFieldUiModel {
        val isTouched = forceTouched || field.isTouched || field.value.isNotEmpty()
        val errorMessageRes = validateCustomerAddressLineOneUseCase(field.value)
            .error
            .toAddressLineOneErrorRes(isTouched)

        return field.copy(
            errorMessageRes = errorMessageRes,
            isTouched = isTouched
        )
    }

    private fun validatePreferredContactChannelField(
        field: CustomerFormFieldUiModel,
        forceTouched: Boolean
    ): CustomerFormFieldUiModel {
        val isTouched = forceTouched || field.isTouched || field.value.isNotEmpty()
        val errorMessageRes = validateCustomerPreferredContactChannelUseCase(field.value)
            .error
            .toPreferredContactChannelErrorRes(isTouched)

        return field.copy(
            errorMessageRes = errorMessageRes,
            isTouched = isTouched
        )
    }

    private fun validateAcquisitionSourceField(
        field: CustomerFormFieldUiModel,
        forceTouched: Boolean
    ): CustomerFormFieldUiModel {
        val isTouched = forceTouched || field.isTouched || field.value.isNotEmpty()
        val errorMessageRes = validateCustomerAcquisitionSourceUseCase(field.value)
            .error
            .toAcquisitionSourceErrorRes(isTouched)

        return field.copy(
            errorMessageRes = errorMessageRes,
            isTouched = isTouched
        )
    }

    private fun Form.syncPrimaryActionEnabled(): Form = copy(
        isPrimaryActionEnabled = when (currentStep) {
            CreateCustomerStep.PERSONAL_INFO -> isPersonalInfoValid()
            CreateCustomerStep.CONTACT_ADDRESS -> isContactAddressValid()
            CreateCustomerStep.PREFERENCES_NOTES -> isPreferencesAndNotesValid()
        }
    )

    private fun Form.isPersonalInfoValid(): Boolean =
        firstName.errorMessageRes == null &&
            lastName.errorMessageRes == null &&
            birthDate.errorMessageRes == null &&
            documentNumber.errorMessageRes == null &&
            nationalityCode.errorMessageRes == null &&
            firstName.value.isNotBlank() &&
            lastName.value.isNotBlank() &&
            birthDate.value.isNotBlank() &&
            documentNumber.value.isNotBlank() &&
            nationalityCode.value.isNotBlank()

    private fun Form.isContactAddressValid(): Boolean =
        emailAddress.errorMessageRes == null &&
            phoneNumber.errorMessageRes == null &&
            whatsAppNumber.errorMessageRes == null &&
            countryCode.errorMessageRes == null &&
            city.errorMessageRes == null &&
            addressLineOne.errorMessageRes == null &&
            emailAddress.value.isNotBlank() &&
            phoneNumber.value.isNotBlank() &&
            countryCode.value.isNotBlank() &&
            city.value.isNotBlank() &&
            addressLineOne.value.isNotBlank()

    private fun Form.isPreferencesAndNotesValid(): Boolean =
        preferredContactChannelCode.errorMessageRes == null &&
            acquisitionSourceCode.errorMessageRes == null &&
            preferredContactChannelCode.value.isNotBlank() &&
            acquisitionSourceCode.value.isNotBlank()

    private fun Form.firstInvalidStep(): CreateCustomerStep? = when {
        !isPersonalInfoValid() -> CreateCustomerStep.PERSONAL_INFO
        !isContactAddressValid() -> CreateCustomerStep.CONTACT_ADDRESS
        !isPreferencesAndNotesValid() -> CreateCustomerStep.PREFERENCES_NOTES
        else -> null
    }

    private fun Form.toCreateCustomerDraft(): CreateCustomerDraft? {
        if (firstInvalidStep() != null) {
            return null
        }

        val birthDateIso = runCatching {
            LocalDate.parse(birthDate.value.trim(), birthDateFormatter).toString()
        }.getOrNull() ?: return null

        val preferredTimeWindow = preferredContactTimeCode
            .takeIf(String::isNotBlank)
            ?.split("|", limit = 2)
            ?.takeIf { it.size == 2 }
            ?.let { (start, end) ->
                CustomerPreferredTimeWindow(
                    start = start,
                    end = end,
                    timeZone = timeZoneCode.ifBlank { "America/Bogota" }
                )
            }

        val normalizedNationality = nationalityCode.value.trim()
        val documentType = if (normalizedNationality.equals("CO", ignoreCase = true)) {
            "CC"
        } else {
            "PASSPORT"
        }

        return CreateCustomerDraft(
            clientReferenceId = "android-customer-${UUID.randomUUID()}",
            customerType = CustomerType.PERSON,
            firstName = firstName.value.trim(),
            lastName = lastName.value.trim(),
            birthDateIso = birthDateIso,
            gender = genderCode.toCustomerGender(),
            documentNumber = documentNumber.value.trim(),
            documentType = documentType,
            nationalityCountryCode = normalizedNationality,
            preferredLanguage = preferredLanguageCode.ifBlank { "es-CO" },
            primaryEmail = emailAddress.value.trim(),
            primaryPhone = phoneNumber.value.trim(),
            whatsAppPhone = whatsAppNumber.value.trim().takeIf(String::isNotBlank),
            addressCountryCode = countryCode.value.trim(),
            addressState = stateCode.trim().takeIf(String::isNotBlank),
            addressCity = city.value.trim(),
            addressLineOne = addressLineOne.value.trim(),
            addressLineTwo = addressLineTwo.value.trim().takeIf(String::isNotBlank),
            postalCode = postalCode.value.trim().takeIf(String::isNotBlank),
            preferredContactChannel = preferredContactChannelCode.value.trim(),
            preferredContactTimeWindow = preferredTimeWindow,
            newsletterOptIn = newsletterOptIn,
            customerGroup = customerGroupCode.ifBlank { "RETAIL" },
            acquisitionSource = acquisitionSourceCode.value.trim(),
            publicNote = publicNote.value.trim().takeIf(String::isNotBlank),
            internalNote = internalNote.value.trim().takeIf(String::isNotBlank),
            internalTags = internalTags
        )
    }

    private fun String.toCustomerGender(): CustomerGender? = when (uppercase()) {
        "MALE" -> CustomerGender.MALE
        "FEMALE" -> CustomerGender.FEMALE
        "OTHER" -> CustomerGender.OTHER
        "PREFER_NOT_TO_SAY" -> CustomerGender.PREFER_NOT_TO_SAY
        else -> null
    }

    private fun CustomerFieldValidationError?.toFirstNameErrorRes(isTouched: Boolean): Int? = when {
        !isTouched || this == null -> null
        this == CustomerFieldValidationError.REQUIRED -> R.string.customers_create_first_name_required
        else -> R.string.customers_create_first_name_letters_only
    }

    private fun CustomerFieldValidationError?.toLastNameErrorRes(isTouched: Boolean): Int? = when {
        !isTouched || this == null -> null
        this == CustomerFieldValidationError.REQUIRED -> R.string.customers_create_last_name_required
        else -> R.string.customers_create_last_name_letters_only
    }

    private fun CustomerFieldValidationError?.toBirthDateErrorRes(isTouched: Boolean): Int? = when {
        !isTouched || this == null -> null
        this == CustomerFieldValidationError.REQUIRED -> R.string.customers_create_birth_date_required
        else -> R.string.customers_create_birth_date_invalid
    }

    private fun CustomerFieldValidationError?.toDocumentNumberErrorRes(isTouched: Boolean): Int? = when {
        !isTouched || this == null -> null
        this == CustomerFieldValidationError.REQUIRED -> R.string.customers_create_document_number_required
        else -> R.string.customers_create_document_number_invalid
    }

    private fun CustomerFieldValidationError?.toNationalityErrorRes(isTouched: Boolean): Int? = when {
        !isTouched || this == null -> null
        else -> R.string.customers_create_nationality_required
    }

    private fun CustomerFieldValidationError?.toEmailErrorRes(isTouched: Boolean): Int? = when {
        !isTouched || this == null -> null
        this == CustomerFieldValidationError.REQUIRED -> R.string.customers_create_email_required
        else -> R.string.customers_create_email_invalid
    }

    private fun CustomerFieldValidationError?.toPhoneErrorRes(isTouched: Boolean): Int? = when {
        !isTouched || this == null -> null
        this == CustomerFieldValidationError.REQUIRED -> R.string.customers_create_phone_required
        else -> R.string.customers_create_phone_invalid
    }

    private fun CustomerFieldValidationError?.toCountryErrorRes(isTouched: Boolean): Int? = when {
        !isTouched || this == null -> null
        else -> R.string.customers_create_country_required
    }

    private fun CustomerFieldValidationError?.toCityErrorRes(isTouched: Boolean): Int? = when {
        !isTouched || this == null -> null
        else -> R.string.customers_create_city_required
    }

    private fun CustomerFieldValidationError?.toAddressLineOneErrorRes(isTouched: Boolean): Int? = when {
        !isTouched || this == null -> null
        else -> R.string.customers_create_address_required
    }

    private fun CustomerFieldValidationError?.toPreferredContactChannelErrorRes(
        isTouched: Boolean
    ): Int? = when {
        !isTouched || this == null -> null
        else -> R.string.customers_create_preferred_contact_required
    }

    private fun CustomerFieldValidationError?.toAcquisitionSourceErrorRes(isTouched: Boolean): Int? = when {
        !isTouched || this == null -> null
        else -> R.string.customers_create_acquisition_source_required
    }
}
