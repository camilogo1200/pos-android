package com.hawk.products.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.hawk.products.domain.entities.CreateProductDraft
import com.hawk.products.domain.entities.ProductStatus
import com.hawk.products.domain.errors.ProductWriteException
import com.hawk.products.domain.usecases.interfaces.CreateProductUseCase
import com.hawk.products.ui.viewstates.CreateProductViewState.Form
import com.hawk.products.ui.viewstates.CreateProductViewState.SubmissionResult
import com.hawk.products.ui.viewstates.CreateProductViewState
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
class CreateProductViewModel @Inject constructor(
    private val createProductUseCase: CreateProductUseCase,
    @param:IoDispatcher private val coroutineDispatcher: CoroutineDispatcher
) : ViewModel() {

    companion object {
        private const val ValidationMessage =
            "Complete all required fields with valid values before creating the product."
        private const val SuccessTitle = "Product created"
        private const val SuccessMessage =
            "The product was created successfully in the Hawk catalog."
        private const val GenericErrorTitle = "Product creation failed"
        private const val GenericErrorMessage =
            "Something failed while creating the product."
        private const val NoConnectionMessage =
            "No internet connection. Check your connection and try again."
        private const val DefaultCurrency = "COP"
    }

    private val _viewState = MutableStateFlow<CreateProductViewState>(Form())
    val viewState: StateFlow<CreateProductViewState> = _viewState.asStateFlow()

    fun onNameChanged(value: String) {
        updateForm { state -> state.copy(name = value).syncSubmitEnabled() }
    }

    fun onSkuChanged(value: String) {
        updateForm { state -> state.copy(sku = value).syncSubmitEnabled() }
    }

    fun onSellPriceChanged(value: String) {
        updateForm { state -> state.copy(sellPrice = value).syncSubmitEnabled() }
    }

    fun onCostPriceChanged(value: String) {
        updateForm { state -> state.copy(costPrice = value).syncSubmitEnabled() }
    }

    fun onAvailableQuantityChanged(value: String) {
        updateForm { state -> state.copy(availableQuantity = value).syncSubmitEnabled() }
    }

    fun onStatusChanged(value: String) {
        updateForm { state -> state.copy(status = value).syncSubmitEnabled() }
    }

    fun resetForm() {
        _viewState.value = Form()
    }

    fun onCreateProductClicked() {
        val currentState = _viewState.value as? Form ?: return
        if (currentState.isSubmitting) {
            return
        }
        val request = currentState.toCreateProductDraft()

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

            createProductUseCase(request).collectLatest { result ->
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
                            is ProductWriteException.NoConnection -> NoConnectionMessage
                            else -> throwable.message
                                ?.takeIf { it.isNotBlank() }
                                ?: GenericErrorMessage
                        }

                        _viewState.value = SubmissionResult(
                            isSuccess = false,
                            title = GenericErrorTitle,
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
        name.isNotBlank() &&
            sku.isNotBlank() &&
            sellPrice.toLongOrNull() != null &&
            costPrice.toLongOrNull() != null &&
            availableQuantity.toIntOrNull() != null

    private fun Form.toCreateProductDraft(): CreateProductDraft? {
        val trimmedName = name.trim()
        val trimmedSku = sku.trim()
        val sellPriceValue = sellPrice.toLongOrNull()
        val costPriceValue = costPrice.toLongOrNull()
        val quantityValue = availableQuantity.toIntOrNull()

        if (
            trimmedName.isBlank() ||
            trimmedSku.isBlank() ||
            sellPriceValue == null ||
            costPriceValue == null ||
            quantityValue == null
        ) {
            return null
        }

        return CreateProductDraft(
            productId = buildProductId(trimmedSku, trimmedName),
            sku = trimmedSku,
            name = trimmedName,
            status = status.toProductStatus(),
            sellPriceAmount = sellPriceValue,
            costAmount = costPriceValue,
            currency = DefaultCurrency,
            availableQuantity = quantityValue
        )
    }

    private fun buildProductId(sku: String, name: String): String {
        val skuSlug = sku.lowercase().replace(Regex("[^a-z0-9]+"), "-").trim('-')
        if (skuSlug.isNotBlank()) {
            return skuSlug
        }

        return name.lowercase().replace(Regex("[^a-z0-9]+"), "-").trim('-')
    }

    private fun String.toProductStatus(): ProductStatus = when (uppercase()) {
        "ACTIVE" -> ProductStatus.ACTIVE
        "INACTIVE" -> ProductStatus.INACTIVE
        "DRAFT" -> ProductStatus.DRAFT
        "ARCHIVED" -> ProductStatus.ARCHIVED
        else -> ProductStatus.UNKNOWN
    }
}
