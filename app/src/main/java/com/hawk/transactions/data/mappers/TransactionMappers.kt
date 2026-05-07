package com.hawk.transactions.data.mappers

import com.hawk.transactions.data.dto.CreateTransactionRequestDto
import com.hawk.transactions.data.dto.TransactionContainerDto
import com.hawk.transactions.data.dto.TransactionDto
import com.hawk.transactions.data.dto.TransactionsListResponseDto
import com.hawk.transactions.domain.entities.CreateTransactionDraft
import com.hawk.transactions.domain.entities.Transaction
import com.hawk.transactions.domain.entities.TransactionContainer
import com.hawk.transactions.domain.entities.TransactionsFeed

fun TransactionsListResponseDto.toDomain(): TransactionsFeed = TransactionsFeed(
    transactions = data.map(TransactionDto::toDomain),
    loadedCount = page?.count ?: data.size,
    nextCursor = page?.nextCursor
)

fun TransactionDto.toDomain(): Transaction = Transaction(
    moneyOperationId = moneyOperationId,
    operationType = operationType,
    branchId = branchId,
    branchFinancialDayId = branchFinancialDayId,
    businessDate = businessDate,
    medium = medium,
    amount = amount,
    currency = currency,
    reasonCode = reasonCode,
    description = description,
    operationStatus = operationStatus,
    accountingStatus = accountingStatus,
    approvalRequired = approvalRequired,
    approvalMode = approvalMode,
    sourceContainer = sourceContainer.toDomain(),
    destinationContainer = destinationContainer?.toDomain(),
    references = references,
    evidenceAssetIds = evidenceAssetIds,
    requestedByUserId = requestedByUserId,
    approvedByUserId = approvedByUserId,
    createdAt = createdAt,
    approvedAt = approvedAt,
    executedAt = executedAt
)

fun CreateTransactionDraft.toRequestDto(): CreateTransactionRequestDto = CreateTransactionRequestDto(
    operationType = operationType,
    branchFinancialDayId = branchFinancialDayId,
    medium = medium,
    amount = amount,
    currency = currency,
    reasonCode = reasonCode,
    description = description,
    sourceContainer = sourceContainer.toDto(),
    destinationContainer = destinationContainer?.toDto(),
    references = references,
    evidenceAssetIds = evidenceAssetIds
)

private fun TransactionContainerDto.toDomain(): TransactionContainer = TransactionContainer(
    containerType = containerType,
    containerId = containerId,
    fundingAccountId = fundingAccountId,
    cashSessionId = cashSessionId
)

private fun TransactionContainer.toDto(): TransactionContainerDto = TransactionContainerDto(
    containerType = containerType,
    containerId = containerId,
    fundingAccountId = fundingAccountId,
    cashSessionId = cashSessionId
)
