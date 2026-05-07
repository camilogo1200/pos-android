package com.hawk.transactions.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.hawk.transactions.domain.entities.Transaction
import com.hawk.transactions.domain.entities.TransactionsFeed
import com.hawk.transactions.domain.errors.TransactionReadException
import com.hawk.transactions.domain.usecases.interfaces.GetTransactionsUseCase
import com.hawk.transactions.ui.uimodels.TransactionListItemUiModel
import com.hawk.transactions.ui.uimodels.TransactionStatusTabUiModel
import com.hawk.transactions.ui.uimodels.TransactionSummaryCardUiModel
import com.hawk.transactions.ui.uimodels.TransactionTone
import com.hawk.transactions.ui.viewstates.TransactionsViewState
import com.hawk.utils.coroutines.IoDispatcher
import com.hawk.utils.coroutines.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import java.text.NumberFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update

@HiltViewModel
class TransactionsViewModel @Inject constructor(
    private val getTransactionsUseCase: GetTransactionsUseCase,
    @param:IoDispatcher private val coroutineDispatcher: CoroutineDispatcher
) : ViewModel() {

    companion object {
        private const val BankTransferMedium = "BANK_TRANSFER"
        private const val WalletMedium = "WALLET"
        private const val CashMedium = "CASH"
    }

    private val currencyFormatter = NumberFormat.getCurrencyInstance(
        Locale.forLanguageTag("es-CO")
    ).apply {
        maximumFractionDigits = 0
    }

    private val inputDateFormatter = DateTimeFormatter.ISO_LOCAL_DATE
    private val outputDateFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy", Locale.ENGLISH)

    private val _viewState = MutableStateFlow(TransactionsViewState())
    val viewState: StateFlow<TransactionsViewState> = _viewState.asStateFlow()

    private var allTransactions: List<Transaction> = emptyList()
    private var loadedCount: Int = 0
    private var hasMoreResults: Boolean = false

    init {
        loadTransactions()
    }

    fun onQueryChanged(query: String) {
        _viewState.update { state -> state.copy(query = query) }
        applyFilters()
    }

    fun onStatusSelected(status: String) {
        _viewState.update { state -> state.copy(selectedStatus = status) }
        applyFilters()
    }

    fun onMediumSelected(medium: String) {
        _viewState.update { state -> state.copy(selectedMedium = medium) }
        applyFilters()
    }

    fun onTypeSelected(type: String) {
        _viewState.update { state -> state.copy(selectedType = type) }
        applyFilters()
    }

    fun onRetryRequested() {
        _viewState.update { state ->
            state.copy(
                isLoading = true,
                statusTitle = null,
                statusBody = null
            )
        }
        loadTransactions()
    }

    fun onClearFiltersRequested() {
        _viewState.update { state ->
            state.copy(
                query = "",
                selectedStatus = TransactionsViewState.ALL_FILTER,
                selectedMedium = TransactionsViewState.ALL_FILTER,
                selectedType = TransactionsViewState.ALL_FILTER
            )
        }
        applyFilters()
    }

    private fun loadTransactions() {
        launch(coroutineDispatcher) {
            getTransactionsUseCase().collectLatest { result ->
                result
                    .onSuccess { feed ->
                        onTransactionsLoaded(feed)
                    }
                    .onFailure { throwable ->
                        allTransactions = emptyList()
                        loadedCount = 0
                        hasMoreResults = false
                        _viewState.update { state ->
                            state.copy(
                                isLoading = false,
                                transactions = emptyList(),
                                summaryCards = emptyList(),
                                statusTabs = emptyList(),
                                availableMedia = listOf(TransactionsViewState.ALL_FILTER),
                                availableTypes = listOf(TransactionsViewState.ALL_FILTER),
                                loadedCount = 0,
                                hasMoreResults = false,
                                statusTitle = when (throwable) {
                                    is TransactionReadException.NoConnection -> "No internet connection"
                                    else -> "Transactions unavailable"
                                },
                                statusBody = when (throwable) {
                                    is TransactionReadException.NoConnection ->
                                        "The transactions module could not reach the service. Reconnect and retry."

                                    else -> throwable.message
                                        ?.takeIf(String::isNotBlank)
                                        ?: "The transactions service returned an empty or invalid response."
                                }
                            )
                        }
                    }
            }
        }
    }

    private fun onTransactionsLoaded(feed: TransactionsFeed) {
        allTransactions = feed.transactions
        loadedCount = feed.loadedCount
        hasMoreResults = !feed.nextCursor.isNullOrBlank()

        _viewState.update { state ->
            state.copy(
                isLoading = false,
                statusTitle = null,
                statusBody = null,
                summaryCards = buildSummaryCards(allTransactions),
                statusTabs = buildStatusTabs(allTransactions),
                availableMedia = listOf(TransactionsViewState.ALL_FILTER) +
                    allTransactions.map(Transaction::medium).distinct().sorted(),
                availableTypes = listOf(TransactionsViewState.ALL_FILTER) +
                    allTransactions.map(Transaction::operationType).distinct().sorted(),
                loadedCount = loadedCount,
                hasMoreResults = hasMoreResults
            )
        }
        applyFilters()
    }

    private fun applyFilters() {
        val state = _viewState.value
        val query = state.query.trim()

        val filteredTransactions = allTransactions.filter { transaction ->
            val matchesQuery = query.isBlank() ||
                transaction.moneyOperationId.contains(query, ignoreCase = true) ||
                transaction.description.contains(query, ignoreCase = true) ||
                transaction.operationType.contains(query, ignoreCase = true) ||
                transaction.reasonCode.contains(query, ignoreCase = true) ||
                transaction.branchId.contains(query, ignoreCase = true) ||
                transaction.sourceContainer.containerId.contains(query, ignoreCase = true)

            val matchesStatus = state.selectedStatus == TransactionsViewState.ALL_FILTER ||
                transaction.operationStatus == state.selectedStatus

            val matchesMedium = state.selectedMedium == TransactionsViewState.ALL_FILTER ||
                transaction.medium == state.selectedMedium

            val matchesType = state.selectedType == TransactionsViewState.ALL_FILTER ||
                transaction.operationType == state.selectedType

            matchesQuery && matchesStatus && matchesMedium && matchesType
        }

        _viewState.update { current ->
            current.copy(
                transactions = filteredTransactions.map(::toUiModel),
                summaryCards = buildSummaryCards(allTransactions),
                statusTabs = buildStatusTabs(allTransactions),
                loadedCount = loadedCount,
                hasMoreResults = hasMoreResults
            )
        }
    }

    private fun buildSummaryCards(transactions: List<Transaction>): List<TransactionSummaryCardUiModel> {
        val totalAmount = transactions.sumOf(Transaction::amount)
        val pendingCount = transactions.count { it.operationStatus.contains("PENDING", ignoreCase = true) }

        val cashTransactions = transactions.filter { it.medium.equals(CashMedium, ignoreCase = true) }
        val walletTransactions = transactions.filter { it.medium.equals(WalletMedium, ignoreCase = true) }
        val bankTransactions = transactions.filter { it.medium.equals(BankTransferMedium, ignoreCase = true) }

        return listOf(
            TransactionSummaryCardUiModel(
                key = "total",
                badge = "$",
                title = "Balance",
                value = currencyFormatter.format(totalAmount),
                supportingText = "${transactions.size} loaded · $pendingCount pending approval",
                tone = TransactionTone.PRIMARY
            ),
            TransactionSummaryCardUiModel(
                key = "cash",
                badge = "C",
                title = "Cash",
                value = currencyFormatter.format(cashTransactions.sumOf(Transaction::amount)),
                supportingText = "${cashTransactions.size} cash movements",
                tone = TransactionTone.WARNING
            ),
            TransactionSummaryCardUiModel(
                key = "wallet",
                badge = "W",
                title = "Wallet",
                value = currencyFormatter.format(walletTransactions.sumOf(Transaction::amount)),
                supportingText = "${walletTransactions.size} wallet movements",
                tone = TransactionTone.POSITIVE
            ),
            TransactionSummaryCardUiModel(
                key = "bank",
                badge = "B",
                title = "Bank Transfer",
                value = currencyFormatter.format(bankTransactions.sumOf(Transaction::amount)),
                supportingText = "${bankTransactions.size} bank movements",
                tone = TransactionTone.NEUTRAL
            )
        )
    }

    private fun buildStatusTabs(transactions: List<Transaction>): List<TransactionStatusTabUiModel> {
        val groupedStatuses = transactions
            .groupingBy(Transaction::operationStatus)
            .eachCount()
            .toList()
            .sortedByDescending { (_, count) -> count }

        return buildList {
            add(
                TransactionStatusTabUiModel(
                    key = TransactionsViewState.ALL_FILTER,
                    label = "All",
                    count = transactions.size
                )
            )
            groupedStatuses.forEach { (status, count) ->
                add(
                    TransactionStatusTabUiModel(
                        key = status,
                        label = status.toHumanLabel(),
                        count = count
                    )
                )
            }
        }
    }

    private fun toUiModel(transaction: Transaction): TransactionListItemUiModel = TransactionListItemUiModel(
        moneyOperationId = transaction.moneyOperationId,
        operationTypeLabel = transaction.operationType.toHumanLabel(),
        branchLabel = transaction.branchId.uppercase(),
        businessDateLabel = transaction.businessDate.toDisplayDate(),
        mediumLabel = transaction.medium.toHumanLabel(),
        amountLabel = currencyFormatter.format(transaction.amount),
        operationStatusLabel = transaction.operationStatus.toHumanLabel(),
        operationStatusTone = transaction.operationStatus.toOperationTone(),
        accountingStatusLabel = transaction.accountingStatus.toHumanLabel(),
        accountingStatusTone = transaction.accountingStatus.toAccountingTone(),
        description = transaction.description,
        reasonLabel = transaction.reasonCode.toHumanLabel(),
        sourceContainerLabel = "${transaction.sourceContainer.containerType.toHumanLabel()} · ${transaction.sourceContainer.containerId}",
        referencesSummary = transaction.references.entries
            .joinToString(separator = " · ") { (key, value) -> "${key.toHumanLabel()}: $value" }
            .ifBlank { "No references" }
    )

    private fun String.toHumanLabel(): String = lowercase()
        .replace('_', ' ')
        .split(' ')
        .joinToString(separator = " ") { part ->
            part.replaceFirstChar { character -> character.titlecase() }
        }

    private fun String.toDisplayDate(): String = runCatching {
        LocalDate.parse(this, inputDateFormatter).format(outputDateFormatter)
    }.getOrElse { this }

    private fun String.toOperationTone(): TransactionTone = when {
        contains("EXECUTED", ignoreCase = true) -> TransactionTone.POSITIVE
        contains("PENDING", ignoreCase = true) -> TransactionTone.WARNING
        contains("FAILED", ignoreCase = true) || contains("CANCEL", ignoreCase = true) ->
            TransactionTone.NEUTRAL

        else -> TransactionTone.PRIMARY
    }

    private fun String.toAccountingTone(): TransactionTone = when {
        contains("LEDGER", ignoreCase = true) && contains("PENDING", ignoreCase = true) ->
            TransactionTone.WARNING

        contains("POSTED", ignoreCase = true) || contains("SYNCED", ignoreCase = true) ->
            TransactionTone.POSITIVE

        else -> TransactionTone.NEUTRAL
    }
}
