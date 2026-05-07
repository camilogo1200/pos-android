package com.hawk.transactions.ui.viewstates

sealed interface CreateTransactionViewState {
    data class Form(
        val operationType: String = "SUPPLIER_PAYMENT",
        val branchFinancialDayId: String = "",
        val medium: String = "BANK_TRANSFER",
        val amount: String = "",
        val currency: String = "COP",
        val reasonCode: String = "",
        val description: String = "",
        val sourceContainerType: String = "BANK",
        val sourceContainerId: String = "",
        val sourceFundingAccountId: String = "",
        val sourceCashSessionId: String = "",
        val destinationContainerType: String = "",
        val destinationContainerId: String = "",
        val destinationFundingAccountId: String = "",
        val destinationCashSessionId: String = "",
        val referenceKeyOne: String = "supplierId",
        val referenceValueOne: String = "",
        val referenceKeyTwo: String = "invoiceReference",
        val referenceValueTwo: String = "",
        val referenceKeyThree: String = "",
        val referenceValueThree: String = "",
        val evidenceAssetIds: String = "",
        val isSubmitting: Boolean = false,
        val isSubmitEnabled: Boolean = false,
        val inlineMessage: String? = null
    ) : CreateTransactionViewState

    data class SubmissionResult(
        val isSuccess: Boolean,
        val title: String,
        val message: String
    ) : CreateTransactionViewState
}
