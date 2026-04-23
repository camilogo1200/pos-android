package com.hawk.products.ui.viewstates

sealed interface CreateProductViewState {
    data class Form(
        val name: String = "",
        val sku: String = "",
        val sellPrice: String = "",
        val costPrice: String = "",
        val availableQuantity: String = "",
        val status: String = "ACTIVE",
        val isSubmitting: Boolean = false,
        val isSubmitEnabled: Boolean = false,
        val inlineMessage: String? = null
    ) : CreateProductViewState

    data class SubmissionResult(
        val isSuccess: Boolean,
        val title: String,
        val message: String
    ) : CreateProductViewState
}
