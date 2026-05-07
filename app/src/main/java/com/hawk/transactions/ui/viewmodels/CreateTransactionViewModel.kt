package com.hawk.transactions.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.hawk.transactions.domain.entities.CreateTransactionDraft
import com.hawk.transactions.domain.entities.TransactionContainer
import com.hawk.transactions.domain.errors.TransactionWriteException
import com.hawk.transactions.domain.usecases.interfaces.CreateTransactionUseCase
import com.hawk.transactions.ui.viewstates.CreateTransactionViewState
import com.hawk.transactions.ui.viewstates.CreateTransactionViewState.Form
import com.hawk.transactions.ui.viewstates.CreateTransactionViewState.SubmissionResult
import com.hawk.utils.coroutines.IoDispatcher
import com.hawk.utils.coroutines.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update

@HiltViewModel
class CreateTransactionViewModel @Inject constructor(
    private val createTransactionUseCase: CreateTransactionUseCase,
    @param:IoDispatcher private val coroutineDispatcher: CoroutineDispatcher
) : ViewModel() {

    companion object {
        private const val ValidationMessage =
            "Complete the required transaction fields and fix the partial reference pairs before creating the transaction."
        private const val SuccessTitle = "Transaction created"
        private const val SuccessMessage =
            "The money operation request was sent successfully to the Hawk transactions service."
        private const val ErrorTitle = "Transaction creation failed"
        private const val GenericErrorMessage =
            "The transaction request could not be completed."
        private const val NoConnectionMessage =
            "No internet connection. Check your connection and try again."
    }

    private val _viewState = MutableStateFlow<CreateTransactionViewState>(Form())
    val viewState: StateFlow<CreateTransactionViewState> = _viewState.asStateFlow()

    fun onOperationTypeChanged(value: String) = updateForm { it.copy(operationType = value).syncSubmitEnabled() }
    fun onBranchFinancialDayIdChanged(value: String) = updateForm {
        it.copy(branchFinancialDayId = value).syncSubmitEnabled()
    }
    fun onMediumChanged(value: String) = updateForm { it.copy(medium = value).syncSubmitEnabled() }
    fun onAmountChanged(value: String) = updateForm { it.copy(amount = value).syncSubmitEnabled() }
    fun onCurrencyChanged(value: String) = updateForm { it.copy(currency = value).syncSubmitEnabled() }
    fun onReasonCodeChanged(value: String) = updateForm { it.copy(reasonCode = value).syncSubmitEnabled() }
    fun onDescriptionChanged(value: String) = updateForm { it.copy(description = value).syncSubmitEnabled() }
    fun onSourceContainerTypeChanged(value: String) = updateForm {
        it.copy(sourceContainerType = value).syncSubmitEnabled()
    }
    fun onSourceContainerIdChanged(value: String) = updateForm {
        it.copy(sourceContainerId = value).syncSubmitEnabled()
    }
    fun onSourceFundingAccountIdChanged(value: String) = updateForm {
        it.copy(sourceFundingAccountId = value).syncSubmitEnabled()
    }
    fun onSourceCashSessionIdChanged(value: String) = updateForm {
        it.copy(sourceCashSessionId = value).syncSubmitEnabled()
    }
    fun onDestinationContainerTypeChanged(value: String) = updateForm {
        it.copy(destinationContainerType = value).syncSubmitEnabled()
    }
    fun onDestinationContainerIdChanged(value: String) = updateForm {
        it.copy(destinationContainerId = value).syncSubmitEnabled()
    }
    fun onDestinationFundingAccountIdChanged(value: String) = updateForm {
        it.copy(destinationFundingAccountId = value).syncSubmitEnabled()
    }
    fun onDestinationCashSessionIdChanged(value: String) = updateForm {
        it.copy(destinationCashSessionId = value).syncSubmitEnabled()
    }
    fun onReferenceKeyOneChanged(value: String) = updateForm {
        it.copy(referenceKeyOne = value).syncSubmitEnabled()
    }
    fun onReferenceValueOneChanged(value: String) = updateForm {
        it.copy(referenceValueOne = value).syncSubmitEnabled()
    }
    fun onReferenceKeyTwoChanged(value: String) = updateForm {
        it.copy(referenceKeyTwo = value).syncSubmitEnabled()
    }
    fun onReferenceValueTwoChanged(value: String) = updateForm {
        it.copy(referenceValueTwo = value).syncSubmitEnabled()
    }
    fun onReferenceKeyThreeChanged(value: String) = updateForm {
        it.copy(referenceKeyThree = value).syncSubmitEnabled()
    }
    fun onReferenceValueThreeChanged(value: String) = updateForm {
        it.copy(referenceValueThree = value).syncSubmitEnabled()
    }
    fun onEvidenceAssetIdsChanged(value: String) = updateForm {
        it.copy(evidenceAssetIds = value).syncSubmitEnabled()
    }

    fun resetForm() {
        _viewState.value = Form()
    }

    fun onCreateTransactionClicked() {
        val formState = _viewState.value as? Form ?: return
        if (formState.isSubmitting) {
            return
        }

        val request = formState.toDraft()
        if (request == null) {
            _viewState.update { state ->
                (state as? Form)?.copy(
                    inlineMessage = ValidationMessage,
                    isSubmitEnabled = false
                ) ?: state
            }
            return
        }

        launch(coroutineDispatcher) {
            _viewState.update { state ->
                (state as? Form)?.copy(
                    isSubmitting = true,
                    isSubmitEnabled = false,
                    inlineMessage = null
                ) ?: state
            }

            createTransactionUseCase(request).collectLatest { result ->
                result
                    .onSuccess {
                        _viewState.value = SubmissionResult(
                            isSuccess = true,
                            title = SuccessTitle,
                            message = SuccessMessage
                        )
                    }
                    .onFailure { throwable ->
                        val message = when (throwable) {
                            is TransactionWriteException.NoConnection -> NoConnectionMessage
                            else -> throwable.message
                                ?.takeIf(String::isNotBlank)
                                ?: GenericErrorMessage
                        }

                        _viewState.value = SubmissionResult(
                            isSuccess = false,
                            title = ErrorTitle,
                            message = message
                        )
                    }
            }
        }
    }

    private fun updateForm(transform: (Form) -> Form) {
        _viewState.update { state ->
            val formState = state as? Form ?: return@update state
            transform(formState).copy(inlineMessage = null)
        }
    }

    private fun Form.syncSubmitEnabled(): Form = copy(
        isSubmitEnabled = canSubmit(),
        isSubmitting = false
    )

    private fun Form.canSubmit(): Boolean =
        operationType.isNotBlank() &&
            branchFinancialDayId.isNotBlank() &&
            medium.isNotBlank() &&
            amount.toDoubleOrNull() != null &&
            currency.isNotBlank() &&
            reasonCode.isNotBlank() &&
            description.isNotBlank() &&
            sourceContainerType.isNotBlank() &&
            sourceContainerId.isNotBlank() &&
            hasValidSourceContainerDetails() &&
            hasValidDestinationContainerDetails() &&
            hasValidReferencePairs()

    private fun Form.hasValidSourceContainerDetails(): Boolean = when {
        sourceContainerType.contains("CASH", ignoreCase = true) -> sourceCashSessionId.isNotBlank()
        else -> sourceFundingAccountId.isNotBlank()
    }

    private fun Form.hasValidDestinationContainerDetails(): Boolean {
        val hasAnyDestinationValue = destinationContainerType.isNotBlank() ||
            destinationContainerId.isNotBlank() ||
            destinationFundingAccountId.isNotBlank() ||
            destinationCashSessionId.isNotBlank()

        if (!hasAnyDestinationValue) {
            return true
        }

        if (destinationContainerType.isBlank() || destinationContainerId.isBlank()) {
            return false
        }

        return if (destinationContainerType.contains("CASH", ignoreCase = true)) {
            destinationCashSessionId.isNotBlank()
        } else {
            destinationFundingAccountId.isNotBlank()
        }
    }

    private fun Form.hasValidReferencePairs(): Boolean = listOf(
        referenceKeyOne to referenceValueOne,
        referenceKeyTwo to referenceValueTwo,
        referenceKeyThree to referenceValueThree
    ).all { (key, value) ->
        key.isBlank() == value.isBlank()
    }

    private fun Form.toDraft(): CreateTransactionDraft? {
        if (!canSubmit()) {
            return null
        }

        val amountValue = amount.toDoubleOrNull() ?: return null
        val references = buildMap {
            addReference(referenceKeyOne, referenceValueOne)
            addReference(referenceKeyTwo, referenceValueTwo)
            addReference(referenceKeyThree, referenceValueThree)
        }

        return CreateTransactionDraft(
            operationType = operationType.trim(),
            branchFinancialDayId = branchFinancialDayId.trim(),
            medium = medium.trim(),
            amount = amountValue,
            currency = currency.trim().uppercase(),
            reasonCode = reasonCode.trim(),
            description = description.trim(),
            sourceContainer = TransactionContainer(
                containerType = sourceContainerType.trim(),
                containerId = sourceContainerId.trim(),
                fundingAccountId = sourceFundingAccountId.trim().ifBlank { null },
                cashSessionId = sourceCashSessionId.trim().ifBlank { null }
            ),
            destinationContainer = buildDestinationContainer(),
            references = references,
            evidenceAssetIds = evidenceAssetIds.split(',')
                .map(String::trim)
                .filter(String::isNotBlank)
        )
    }

    private fun Form.buildDestinationContainer(): TransactionContainer? {
        val hasAnyDestinationValue = destinationContainerType.isNotBlank() ||
            destinationContainerId.isNotBlank() ||
            destinationFundingAccountId.isNotBlank() ||
            destinationCashSessionId.isNotBlank()

        if (!hasAnyDestinationValue) {
            return null
        }

        return TransactionContainer(
            containerType = destinationContainerType.trim(),
            containerId = destinationContainerId.trim(),
            fundingAccountId = destinationFundingAccountId.trim().ifBlank { null },
            cashSessionId = destinationCashSessionId.trim().ifBlank { null }
        )
    }

    private fun MutableMap<String, String>.addReference(key: String, value: String) {
        val trimmedKey = key.trim()
        val trimmedValue = value.trim()
        if (trimmedKey.isNotBlank() && trimmedValue.isNotBlank()) {
            put(trimmedKey, trimmedValue)
        }
    }
}
