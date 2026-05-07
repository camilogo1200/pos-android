package com.hawk.transactions.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class TransactionsListResponseDto(
    val requestContext: TransactionsRequestContextDto? = null,
    val data: List<TransactionDto> = emptyList(),
    val page: TransactionsPageDto? = null
)

@Serializable
data class TransactionsRequestContextDto(
    val requestId: String? = null,
    val correlationId: String? = null,
    val servedAt: String? = null
)

@Serializable
data class TransactionsPageDto(
    val limit: Int? = null,
    val count: Int? = null,
    val nextCursor: String? = null
)

@Serializable
data class TransactionDto(
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
    val approvalRequired: Boolean = false,
    val approvalMode: String? = null,
    val sourceContainer: TransactionContainerDto = TransactionContainerDto(),
    val destinationContainer: TransactionContainerDto? = null,
    val references: Map<String, String> = emptyMap(),
    val evidenceAssetIds: List<String> = emptyList(),
    val requestedByUserId: String? = null,
    val approvedByUserId: String? = null,
    val createdAt: String? = null,
    val approvedAt: String? = null,
    val executedAt: String? = null
)

@Serializable
data class TransactionContainerDto(
    val containerType: String = "",
    val containerId: String = "",
    val fundingAccountId: String? = null,
    val cashSessionId: String? = null
)

@Serializable
data class CreateTransactionRequestDto(
    val operationType: String,
    val branchFinancialDayId: String,
    val medium: String,
    val amount: Double,
    val currency: String,
    val reasonCode: String,
    val description: String,
    val sourceContainer: TransactionContainerDto,
    val destinationContainer: TransactionContainerDto? = null,
    val references: Map<String, String> = emptyMap(),
    val evidenceAssetIds: List<String> = emptyList()
)
