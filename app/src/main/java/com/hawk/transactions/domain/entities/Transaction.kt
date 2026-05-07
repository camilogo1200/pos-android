package com.hawk.transactions.domain.entities

data class TransactionsFeed(
    val transactions: List<Transaction>,
    val loadedCount: Int,
    val nextCursor: String?
)

data class Transaction(
    val moneyOperationId: String,
    val operationType: String,
    val branchId: String,
    val branchFinancialDayId: String,
    val businessDate: String,
    val medium: String,
    val amount: Double,
    val currency: String,
    val reasonCode: String,
    val description: String,
    val operationStatus: String,
    val accountingStatus: String,
    val approvalRequired: Boolean,
    val approvalMode: String?,
    val sourceContainer: TransactionContainer,
    val destinationContainer: TransactionContainer?,
    val references: Map<String, String>,
    val evidenceAssetIds: List<String>,
    val requestedByUserId: String?,
    val approvedByUserId: String?,
    val createdAt: String?,
    val approvedAt: String?,
    val executedAt: String?
)

data class TransactionContainer(
    val containerType: String,
    val containerId: String,
    val fundingAccountId: String? = null,
    val cashSessionId: String? = null
)

data class CreateTransactionDraft(
    val operationType: String,
    val branchFinancialDayId: String,
    val medium: String,
    val amount: Double,
    val currency: String,
    val reasonCode: String,
    val description: String,
    val sourceContainer: TransactionContainer,
    val destinationContainer: TransactionContainer?,
    val references: Map<String, String>,
    val evidenceAssetIds: List<String>
)
