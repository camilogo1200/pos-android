package com.hawk.transactions.ui.uimodels

enum class TransactionTone {
    PRIMARY,
    POSITIVE,
    WARNING,
    NEUTRAL
}

data class TransactionSummaryCardUiModel(
    val key: String,
    val badge: String,
    val title: String,
    val value: String,
    val supportingText: String,
    val tone: TransactionTone
)

data class TransactionStatusTabUiModel(
    val key: String,
    val label: String,
    val count: Int
)

data class TransactionListItemUiModel(
    val moneyOperationId: String,
    val operationTypeLabel: String,
    val branchLabel: String,
    val businessDateLabel: String,
    val mediumLabel: String,
    val amountLabel: String,
    val operationStatusLabel: String,
    val operationStatusTone: TransactionTone,
    val accountingStatusLabel: String,
    val accountingStatusTone: TransactionTone,
    val description: String,
    val reasonLabel: String,
    val sourceContainerLabel: String,
    val referencesSummary: String
)
