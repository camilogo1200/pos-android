package com.hawk.transactions.ui.viewstates

import com.hawk.transactions.ui.uimodels.TransactionListItemUiModel
import com.hawk.transactions.ui.uimodels.TransactionStatusTabUiModel
import com.hawk.transactions.ui.uimodels.TransactionSummaryCardUiModel

data class TransactionsViewState(
    val isLoading: Boolean = true,
    val query: String = "",
    val selectedStatus: String = ALL_FILTER,
    val selectedMedium: String = ALL_FILTER,
    val selectedType: String = ALL_FILTER,
    val transactions: List<TransactionListItemUiModel> = emptyList(),
    val summaryCards: List<TransactionSummaryCardUiModel> = emptyList(),
    val statusTabs: List<TransactionStatusTabUiModel> = emptyList(),
    val availableMedia: List<String> = listOf(ALL_FILTER),
    val availableTypes: List<String> = listOf(ALL_FILTER),
    val loadedCount: Int = 0,
    val hasMoreResults: Boolean = false,
    val statusTitle: String? = null,
    val statusBody: String? = null
) {
    companion object {
        const val ALL_FILTER = "ALL"
    }
}
