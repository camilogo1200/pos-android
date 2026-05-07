package com.hawk.transactions.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hawk.common.ui.KeyboardAwareScrollContainer
import com.hawk.designsystem.composables.layout.HawkWorkspaceScaffold
import com.hawk.designsystem.composables.layout.HawkWorkspaceSection
import com.hawk.home.theme.HawkBorder
import com.hawk.home.theme.HawkBrandBlue
import com.hawk.home.theme.HawkDanger
import com.hawk.home.theme.HawkPrimary
import com.hawk.home.theme.HawkSidebarActive
import com.hawk.home.theme.HawkSurface
import com.hawk.home.theme.HawkText
import com.hawk.home.theme.HawkTextMuted
import com.hawk.home.theme.HawkTheme
import com.hawk.home.theme.HawkWorkspaceBackground
import com.hawk.transactions.ui.uimodels.TransactionListItemUiModel
import com.hawk.transactions.ui.uimodels.TransactionStatusTabUiModel
import com.hawk.transactions.ui.uimodels.TransactionSummaryCardUiModel
import com.hawk.transactions.ui.uimodels.TransactionTone
import com.hawk.transactions.ui.viewmodels.CreateTransactionViewModel
import com.hawk.transactions.ui.viewmodels.TransactionsViewModel
import com.hawk.transactions.ui.viewstates.CreateTransactionViewState
import com.hawk.transactions.ui.viewstates.TransactionsViewState

private val TransactionsCardBackground = Color(0xFFFFFFFF)
private val TransactionsSoftBlue = Color(0xFFE7F1FF)
private val TransactionsSoftGreen = Color(0xFFE6F6EC)
private val TransactionsSoftAmber = Color(0xFFFEF3DF)
private val TransactionsSoftGray = Color(0xFFF3F4F6)
private val TransactionsPrimaryText = Color(0xFF111827)
private val TransactionsMutedText = Color(0xFF6B7280)
private val TransactionsPositive = Color(0xFF16A34A)
private val TransactionsWarning = Color(0xFFD97706)
private val TransactionsNeutral = Color(0xFF64748B)

private val operationTypeOptions = listOf(
    "SUPPLIER_PAYMENT",
    "URGENT_REPAIR_EXPENSE",
    "BRANCH_TRANSFER_SENT"
)

private val mediumOptions = listOf(
    "BANK_TRANSFER",
    "CASH",
    "WALLET"
)

private val containerTypeOptions = listOf(
    "BANK",
    "CASH_DRAWER",
    "WALLET"
)

@Composable
fun TransactionsRoute(
    onCreateTransactionClicked: () -> Unit,
    onWorkspaceSectionSelected: (HawkWorkspaceSection) -> Unit = {},
    viewModel: TransactionsViewModel = hiltViewModel()
) {
    val viewState by viewModel.viewState.collectAsStateWithLifecycle()

    TransactionsScreen(
        viewState = viewState,
        onQueryChanged = viewModel::onQueryChanged,
        onStatusSelected = viewModel::onStatusSelected,
        onMediumSelected = viewModel::onMediumSelected,
        onTypeSelected = viewModel::onTypeSelected,
        onClearFiltersRequested = viewModel::onClearFiltersRequested,
        onRetryRequested = viewModel::onRetryRequested,
        onCreateTransactionClicked = onCreateTransactionClicked,
        onWorkspaceSectionSelected = onWorkspaceSectionSelected
    )
}

@Composable
fun CreateTransactionRoute(
    onBackToTransactions: () -> Unit,
    onWorkspaceSectionSelected: (HawkWorkspaceSection) -> Unit = {},
    viewModel: CreateTransactionViewModel = hiltViewModel()
) {
    val viewState by viewModel.viewState.collectAsStateWithLifecycle()

    CreateTransactionScreen(
        viewState = viewState,
        onWorkspaceSectionSelected = onWorkspaceSectionSelected,
        onBackToTransactions = onBackToTransactions,
        onCancelClicked = {
            viewModel.resetForm()
            onBackToTransactions()
        },
        onCreateTransactionClicked = viewModel::onCreateTransactionClicked,
        onOperationTypeChanged = viewModel::onOperationTypeChanged,
        onBranchFinancialDayIdChanged = viewModel::onBranchFinancialDayIdChanged,
        onMediumChanged = viewModel::onMediumChanged,
        onAmountChanged = viewModel::onAmountChanged,
        onCurrencyChanged = viewModel::onCurrencyChanged,
        onReasonCodeChanged = viewModel::onReasonCodeChanged,
        onDescriptionChanged = viewModel::onDescriptionChanged,
        onSourceContainerTypeChanged = viewModel::onSourceContainerTypeChanged,
        onSourceContainerIdChanged = viewModel::onSourceContainerIdChanged,
        onSourceFundingAccountIdChanged = viewModel::onSourceFundingAccountIdChanged,
        onSourceCashSessionIdChanged = viewModel::onSourceCashSessionIdChanged,
        onDestinationContainerTypeChanged = viewModel::onDestinationContainerTypeChanged,
        onDestinationContainerIdChanged = viewModel::onDestinationContainerIdChanged,
        onDestinationFundingAccountIdChanged = viewModel::onDestinationFundingAccountIdChanged,
        onDestinationCashSessionIdChanged = viewModel::onDestinationCashSessionIdChanged,
        onReferenceKeyOneChanged = viewModel::onReferenceKeyOneChanged,
        onReferenceValueOneChanged = viewModel::onReferenceValueOneChanged,
        onReferenceKeyTwoChanged = viewModel::onReferenceKeyTwoChanged,
        onReferenceValueTwoChanged = viewModel::onReferenceValueTwoChanged,
        onReferenceKeyThreeChanged = viewModel::onReferenceKeyThreeChanged,
        onReferenceValueThreeChanged = viewModel::onReferenceValueThreeChanged,
        onEvidenceAssetIdsChanged = viewModel::onEvidenceAssetIdsChanged
    )
}

@Composable
fun TransactionsScreen(
    viewState: TransactionsViewState,
    onQueryChanged: (String) -> Unit,
    onStatusSelected: (String) -> Unit,
    onMediumSelected: (String) -> Unit,
    onTypeSelected: (String) -> Unit,
    onClearFiltersRequested: () -> Unit,
    onRetryRequested: () -> Unit,
    onCreateTransactionClicked: () -> Unit,
    onWorkspaceSectionSelected: (HawkWorkspaceSection) -> Unit
) {
    HawkWorkspaceScaffold(
        title = "Transactions",
        subtitle = "Monitor branch money operations, approvals, and funding flows.",
        selectedSection = HawkWorkspaceSection.Orders,
        onSectionSelected = onWorkspaceSectionSelected,
        actionContent = {
            Button(
                onClick = onCreateTransactionClicked,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = TransactionsPrimaryText,
                    contentColor = Color.White
                )
            ) {
                Text(text = "New Transaction")
            }
        }
    ) {
        SummaryCardsRow(summaryCards = viewState.summaryCards)
        TransactionsStatusTabs(
            tabs = viewState.statusTabs,
            selectedStatus = viewState.selectedStatus,
            onStatusSelected = onStatusSelected
        )
        TransactionsFilterBar(
            query = viewState.query,
            availableMedia = viewState.availableMedia,
            availableTypes = viewState.availableTypes,
            selectedMedium = viewState.selectedMedium,
            selectedType = viewState.selectedType,
            onQueryChanged = onQueryChanged,
            onMediumSelected = onMediumSelected,
            onTypeSelected = onTypeSelected
        )

        when {
            viewState.isLoading -> {
                TransactionsStatusCard(
                    title = "Loading transactions...",
                    body = "The latest money operations are being retrieved from the service.",
                    actionLabel = null,
                    onActionClick = null,
                    showLoader = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
            }

            viewState.statusTitle != null && viewState.statusBody != null -> {
                TransactionsStatusCard(
                    title = viewState.statusTitle,
                    body = viewState.statusBody,
                    actionLabel = "Retry",
                    onActionClick = onRetryRequested,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
            }

            viewState.transactions.isEmpty() &&
                viewState.query.isBlank() &&
                viewState.selectedStatus == TransactionsViewState.ALL_FILTER &&
                viewState.selectedMedium == TransactionsViewState.ALL_FILTER &&
                viewState.selectedType == TransactionsViewState.ALL_FILTER -> {
                TransactionsStatusCard(
                    title = "No transactions yet",
                    body = "Create the first money operation to populate the transactions workspace.",
                    actionLabel = "New Transaction",
                    onActionClick = onCreateTransactionClicked,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
            }

            viewState.transactions.isEmpty() -> {
                TransactionsStatusCard(
                    title = "No matching transactions",
                    body = "Try another search, status, medium, or operation type.",
                    actionLabel = "Clear filters",
                    onActionClick = onClearFiltersRequested,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
            }

            else -> {
                TransactionsTableCard(
                    transactions = viewState.transactions,
                    loadedCount = viewState.loadedCount,
                    hasMoreResults = viewState.hasMoreResults,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
            }
        }
    }
}

@Composable
fun CreateTransactionScreen(
    viewState: CreateTransactionViewState,
    onWorkspaceSectionSelected: (HawkWorkspaceSection) -> Unit,
    onBackToTransactions: () -> Unit,
    onCancelClicked: () -> Unit,
    onCreateTransactionClicked: () -> Unit,
    onOperationTypeChanged: (String) -> Unit,
    onBranchFinancialDayIdChanged: (String) -> Unit,
    onMediumChanged: (String) -> Unit,
    onAmountChanged: (String) -> Unit,
    onCurrencyChanged: (String) -> Unit,
    onReasonCodeChanged: (String) -> Unit,
    onDescriptionChanged: (String) -> Unit,
    onSourceContainerTypeChanged: (String) -> Unit,
    onSourceContainerIdChanged: (String) -> Unit,
    onSourceFundingAccountIdChanged: (String) -> Unit,
    onSourceCashSessionIdChanged: (String) -> Unit,
    onDestinationContainerTypeChanged: (String) -> Unit,
    onDestinationContainerIdChanged: (String) -> Unit,
    onDestinationFundingAccountIdChanged: (String) -> Unit,
    onDestinationCashSessionIdChanged: (String) -> Unit,
    onReferenceKeyOneChanged: (String) -> Unit,
    onReferenceValueOneChanged: (String) -> Unit,
    onReferenceKeyTwoChanged: (String) -> Unit,
    onReferenceValueTwoChanged: (String) -> Unit,
    onReferenceKeyThreeChanged: (String) -> Unit,
    onReferenceValueThreeChanged: (String) -> Unit,
    onEvidenceAssetIdsChanged: (String) -> Unit
) {
    HawkWorkspaceScaffold(
        title = "Create Transaction",
        subtitle = "Compose the money operation request using the current backend contract.",
        selectedSection = HawkWorkspaceSection.Orders,
        onSectionSelected = onWorkspaceSectionSelected
    ) {
        when (viewState) {
            is CreateTransactionViewState.Form -> {
                KeyboardAwareScrollContainer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    scrollbarColor = HawkBrandBlue.copy(alpha = 0.92f),
                    scrollbarTrackColor = HawkBorder
                ) { scrollModifier ->
                    Column(
                        modifier = scrollModifier,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        TransactionFormCard(
                            viewState = viewState,
                            onCancelClicked = onCancelClicked,
                            onCreateTransactionClicked = onCreateTransactionClicked,
                            onOperationTypeChanged = onOperationTypeChanged,
                            onBranchFinancialDayIdChanged = onBranchFinancialDayIdChanged,
                            onMediumChanged = onMediumChanged,
                            onAmountChanged = onAmountChanged,
                            onCurrencyChanged = onCurrencyChanged,
                            onReasonCodeChanged = onReasonCodeChanged,
                            onDescriptionChanged = onDescriptionChanged,
                            onSourceContainerTypeChanged = onSourceContainerTypeChanged,
                            onSourceContainerIdChanged = onSourceContainerIdChanged,
                            onSourceFundingAccountIdChanged = onSourceFundingAccountIdChanged,
                            onSourceCashSessionIdChanged = onSourceCashSessionIdChanged,
                            onDestinationContainerTypeChanged = onDestinationContainerTypeChanged,
                            onDestinationContainerIdChanged = onDestinationContainerIdChanged,
                            onDestinationFundingAccountIdChanged = onDestinationFundingAccountIdChanged,
                            onDestinationCashSessionIdChanged = onDestinationCashSessionIdChanged,
                            onReferenceKeyOneChanged = onReferenceKeyOneChanged,
                            onReferenceValueOneChanged = onReferenceValueOneChanged,
                            onReferenceKeyTwoChanged = onReferenceKeyTwoChanged,
                            onReferenceValueTwoChanged = onReferenceValueTwoChanged,
                            onReferenceKeyThreeChanged = onReferenceKeyThreeChanged,
                            onReferenceValueThreeChanged = onReferenceValueThreeChanged,
                            onEvidenceAssetIdsChanged = onEvidenceAssetIdsChanged
                        )
                    }
                }
            }

            is CreateTransactionViewState.SubmissionResult -> {
                TransactionsResultCard(
                    viewState = viewState,
                    onBackToTransactions = onBackToTransactions,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
            }
        }
    }
}

@Composable
private fun SummaryCardsRow(
    summaryCards: List<TransactionSummaryCardUiModel>
) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        summaryCards.forEach { summaryCard ->
            SummaryCard(summaryCard = summaryCard)
        }
    }
}

@Composable
private fun SummaryCard(
    summaryCard: TransactionSummaryCardUiModel
) {
    val (accentBackground, accentColor) = summaryCard.tone.toAccentColors()

    Card(
        modifier = Modifier.width(250.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = TransactionsCardBackground),
        border = BorderStroke(1.dp, HawkBorder.copy(alpha = 0.5f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            Surface(
                modifier = Modifier.size(52.dp),
                shape = RoundedCornerShape(16.dp),
                color = accentBackground
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = summaryCard.badge,
                        style = MaterialTheme.typography.titleMedium,
                        color = accentColor,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = summaryCard.value,
                    style = MaterialTheme.typography.headlineMedium,
                    color = TransactionsPrimaryText
                )
                Text(
                    text = summaryCard.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = TransactionsPrimaryText
                )
                Text(
                    text = summaryCard.supportingText,
                    style = MaterialTheme.typography.bodyMedium,
                    color = TransactionsMutedText
                )
            }
        }
    }
}

@Composable
private fun TransactionsStatusTabs(
    tabs: List<TransactionStatusTabUiModel>,
    selectedStatus: String,
    onStatusSelected: (String) -> Unit
) {
    if (tabs.isEmpty()) {
        return
    }

    val selectedIndex = tabs.indexOfFirst { it.key == selectedStatus }.coerceAtLeast(0)

    ScrollableTabRow(
        selectedTabIndex = selectedIndex,
        edgePadding = 0.dp,
        containerColor = TransactionsCardBackground,
        divider = {}
    ) {
        tabs.forEach { tab ->
            val isSelected = tab.key == selectedStatus
            Tab(
                selected = isSelected,
                onClick = { onStatusSelected(tab.key) },
                text = {
                    Text(
                        text = "${tab.label} (${tab.count})",
                        color = if (isSelected) HawkSurface else TransactionsMutedText,
                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                    )
                },
                selectedContentColor = HawkSurface,
                unselectedContentColor = TransactionsMutedText,
                modifier = Modifier
                    .padding(horizontal = 4.dp, vertical = 8.dp)
                    .background(
                        color = if (isSelected) HawkBrandBlue else Color.Transparent,
                        shape = RoundedCornerShape(12.dp)
                    )
            )
        }
    }
}

@Composable
private fun TransactionsFilterBar(
    query: String,
    availableMedia: List<String>,
    availableTypes: List<String>,
    selectedMedium: String,
    selectedType: String,
    onQueryChanged: (String) -> Unit,
    onMediumSelected: (String) -> Unit,
    onTypeSelected: (String) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TransactionTextField(
            value = query,
            onValueChange = onQueryChanged,
            label = "Search",
            placeholder = "Search by id, branch, type or description...",
            modifier = Modifier.weight(1.5f)
        )
        TransactionDropdownField(
            label = "Medium",
            value = selectedMedium.toFilterLabel(),
            options = availableMedia,
            onOptionSelected = onMediumSelected,
            modifier = Modifier.weight(1f)
        )
        TransactionDropdownField(
            label = "Operation Type",
            value = selectedType.toFilterLabel(),
            options = availableTypes,
            onOptionSelected = onTypeSelected,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun TransactionsTableCard(
    transactions: List<TransactionListItemUiModel>,
    loadedCount: Int,
    hasMoreResults: Boolean,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = TransactionsCardBackground),
        border = BorderStroke(1.dp, HawkBorder.copy(alpha = 0.5f))
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            TransactionHeaderRow()
            HorizontalDivider(color = HawkBorder.copy(alpha = 0.6f))
            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                items(transactions, key = { it.moneyOperationId }) { transaction ->
                    TransactionDataRow(transaction = transaction)
                    HorizontalDivider(color = HawkBorder.copy(alpha = 0.45f))
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = if (hasMoreResults) {
                        "Showing $loadedCount loaded transactions. More results are available."
                    } else {
                        "Showing $loadedCount loaded transactions."
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = TransactionsMutedText
                )
                Text(
                    text = "${transactions.size} visible",
                    style = MaterialTheme.typography.bodyMedium,
                    color = HawkBrandBlue,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
private fun TransactionHeaderRow() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 18.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        HeaderCell(label = "OPERATION", weight = 1.2f)
        HeaderCell(label = "TYPE", weight = 1.4f)
        HeaderCell(label = "BRANCH", weight = 0.9f)
        HeaderCell(label = "DATE", weight = 1f)
        HeaderCell(label = "MEDIUM", weight = 1f)
        HeaderCell(label = "AMOUNT", weight = 0.9f)
        HeaderCell(label = "STATUS", weight = 1.1f)
        HeaderCell(label = "ACCOUNTING", weight = 1.1f)
    }
}

@Composable
private fun RowScope.HeaderCell(
    label: String,
    weight: Float
) {
    Text(
        text = label,
        modifier = Modifier.weight(weight),
        style = MaterialTheme.typography.labelLarge,
        color = TransactionsMutedText
    )
}

@Composable
private fun TransactionDataRow(
    transaction: TransactionListItemUiModel
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1.2f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = transaction.moneyOperationId,
                style = MaterialTheme.typography.titleMedium,
                color = HawkBrandBlue
            )
            Text(
                text = transaction.reasonLabel,
                style = MaterialTheme.typography.bodyMedium,
                color = TransactionsMutedText,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        Column(
            modifier = Modifier.weight(1.4f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = transaction.operationTypeLabel,
                style = MaterialTheme.typography.bodyLarge,
                color = TransactionsPrimaryText,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = transaction.description,
                style = MaterialTheme.typography.bodyMedium,
                color = TransactionsMutedText,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        Text(
            text = transaction.branchLabel,
            modifier = Modifier.weight(0.9f),
            style = MaterialTheme.typography.bodyMedium,
            color = TransactionsPrimaryText
        )
        Text(
            text = transaction.businessDateLabel,
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.bodyMedium,
            color = TransactionsPrimaryText
        )
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = transaction.mediumLabel,
                style = MaterialTheme.typography.bodyMedium,
                color = TransactionsPrimaryText
            )
            Text(
                text = transaction.sourceContainerLabel,
                style = MaterialTheme.typography.bodyMedium,
                color = TransactionsMutedText,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        Text(
            text = transaction.amountLabel,
            modifier = Modifier.weight(0.9f),
            style = MaterialTheme.typography.bodyLarge,
            color = TransactionsPrimaryText,
            fontWeight = FontWeight.SemiBold
        )
        StatusPill(
            label = transaction.operationStatusLabel,
            tone = transaction.operationStatusTone,
            modifier = Modifier.weight(1.1f)
        )
        StatusPill(
            label = transaction.accountingStatusLabel,
            tone = transaction.accountingStatusTone,
            modifier = Modifier.weight(1.1f)
        )
    }
}

@Composable
private fun StatusPill(
    label: String,
    tone: TransactionTone,
    modifier: Modifier = Modifier
) {
    val (backgroundColor, contentColor) = tone.toPillColors()

    Box(
        modifier = modifier,
        contentAlignment = Alignment.CenterStart
    ) {
        Surface(
            shape = RoundedCornerShape(999.dp),
            color = backgroundColor
        ) {
            Text(
                text = label,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                style = MaterialTheme.typography.labelMedium,
                color = contentColor,
                maxLines = 1
            )
        }
    }
}

@Composable
private fun TransactionsStatusCard(
    title: String,
    body: String,
    actionLabel: String?,
    onActionClick: (() -> Unit)?,
    modifier: Modifier = Modifier,
    showLoader: Boolean = false
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = TransactionsCardBackground),
        border = BorderStroke(1.dp, HawkBorder.copy(alpha = 0.5f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Surface(
                modifier = Modifier.size(84.dp),
                shape = CircleShape,
                color = HawkWorkspaceBackground
            ) {
                Box(contentAlignment = Alignment.Center) {
                    if (showLoader) {
                        CircularProgressIndicator(color = HawkBrandBlue)
                    } else {
                        Text(
                            text = "T",
                            style = MaterialTheme.typography.headlineMedium,
                            color = HawkBrandBlue,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(18.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.headlineMedium,
                color = TransactionsPrimaryText
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = body,
                style = MaterialTheme.typography.bodyLarge,
                color = TransactionsMutedText
            )
            if (actionLabel != null && onActionClick != null) {
                Spacer(modifier = Modifier.height(22.dp))
                Button(
                    onClick = onActionClick,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = TransactionsPrimaryText,
                        contentColor = Color.White
                    )
                ) {
                    Text(text = actionLabel)
                }
            }
        }
    }
}

@Composable
private fun TransactionFormCard(
    viewState: CreateTransactionViewState.Form,
    onCancelClicked: () -> Unit,
    onCreateTransactionClicked: () -> Unit,
    onOperationTypeChanged: (String) -> Unit,
    onBranchFinancialDayIdChanged: (String) -> Unit,
    onMediumChanged: (String) -> Unit,
    onAmountChanged: (String) -> Unit,
    onCurrencyChanged: (String) -> Unit,
    onReasonCodeChanged: (String) -> Unit,
    onDescriptionChanged: (String) -> Unit,
    onSourceContainerTypeChanged: (String) -> Unit,
    onSourceContainerIdChanged: (String) -> Unit,
    onSourceFundingAccountIdChanged: (String) -> Unit,
    onSourceCashSessionIdChanged: (String) -> Unit,
    onDestinationContainerTypeChanged: (String) -> Unit,
    onDestinationContainerIdChanged: (String) -> Unit,
    onDestinationFundingAccountIdChanged: (String) -> Unit,
    onDestinationCashSessionIdChanged: (String) -> Unit,
    onReferenceKeyOneChanged: (String) -> Unit,
    onReferenceValueOneChanged: (String) -> Unit,
    onReferenceKeyTwoChanged: (String) -> Unit,
    onReferenceValueTwoChanged: (String) -> Unit,
    onReferenceKeyThreeChanged: (String) -> Unit,
    onReferenceValueThreeChanged: (String) -> Unit,
    onEvidenceAssetIdsChanged: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = TransactionsCardBackground),
        border = BorderStroke(1.dp, HawkBorder.copy(alpha = 0.5f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            SectionTitle(
                title = "Core details",
                body = "The fields below are taken directly from the current POST example."
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                TransactionDropdownField(
                    label = "Operation Type",
                    value = viewState.operationType.toFilterLabel(),
                    options = operationTypeOptions,
                    onOptionSelected = onOperationTypeChanged,
                    modifier = Modifier.weight(1f),
                    enabled = !viewState.isSubmitting
                )
                TransactionTextField(
                    value = viewState.branchFinancialDayId,
                    onValueChange = onBranchFinancialDayIdChanged,
                    label = "Branch Financial Day ID",
                    placeholder = "bfd_2026_05_07_br_010",
                    modifier = Modifier.weight(1f),
                    enabled = !viewState.isSubmitting
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                TransactionDropdownField(
                    label = "Medium",
                    value = viewState.medium.toFilterLabel(),
                    options = mediumOptions,
                    onOptionSelected = onMediumChanged,
                    modifier = Modifier.weight(1f),
                    enabled = !viewState.isSubmitting
                )
                TransactionTextField(
                    value = viewState.amount,
                    onValueChange = onAmountChanged,
                    label = "Amount",
                    placeholder = "150000",
                    modifier = Modifier.weight(1f),
                    keyboardType = KeyboardType.Decimal,
                    enabled = !viewState.isSubmitting
                )
                TransactionTextField(
                    value = viewState.currency,
                    onValueChange = onCurrencyChanged,
                    label = "Currency",
                    placeholder = "COP",
                    modifier = Modifier.weight(0.6f),
                    enabled = !viewState.isSubmitting
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                TransactionTextField(
                    value = viewState.reasonCode,
                    onValueChange = onReasonCodeChanged,
                    label = "Reason Code",
                    placeholder = "SUPPLIES_PAYMENT",
                    modifier = Modifier.weight(1f),
                    enabled = !viewState.isSubmitting
                )
                TransactionTextField(
                    value = viewState.description,
                    onValueChange = onDescriptionChanged,
                    label = "Description",
                    placeholder = "Invoice 7781 for branch paper and toner restock",
                    modifier = Modifier.weight(1.4f),
                    enabled = !viewState.isSubmitting
                )
            }

            HorizontalDivider(color = HawkBorder.copy(alpha = 0.6f))
            SectionTitle(
                title = "Source container",
                body = "Provide the origin wallet, bank, or cash drawer details."
            )
            ContainerFields(
                containerType = viewState.sourceContainerType,
                containerId = viewState.sourceContainerId,
                fundingAccountId = viewState.sourceFundingAccountId,
                cashSessionId = viewState.sourceCashSessionId,
                onContainerTypeChanged = onSourceContainerTypeChanged,
                onContainerIdChanged = onSourceContainerIdChanged,
                onFundingAccountIdChanged = onSourceFundingAccountIdChanged,
                onCashSessionIdChanged = onSourceCashSessionIdChanged,
                enabled = !viewState.isSubmitting
            )

            HorizontalDivider(color = HawkBorder.copy(alpha = 0.6f))
            SectionTitle(
                title = "Destination container",
                body = "Leave this section empty when the operation has no destination container."
            )
            ContainerFields(
                containerType = viewState.destinationContainerType,
                containerId = viewState.destinationContainerId,
                fundingAccountId = viewState.destinationFundingAccountId,
                cashSessionId = viewState.destinationCashSessionId,
                onContainerTypeChanged = onDestinationContainerTypeChanged,
                onContainerIdChanged = onDestinationContainerIdChanged,
                onFundingAccountIdChanged = onDestinationFundingAccountIdChanged,
                onCashSessionIdChanged = onDestinationCashSessionIdChanged,
                enabled = !viewState.isSubmitting,
                optional = true
            )

            HorizontalDivider(color = HawkBorder.copy(alpha = 0.6f))
            SectionTitle(
                title = "References and evidence",
                body = "Use key-value pairs to preserve the flexible references object from the backend contract."
            )
            ReferenceRow(
                keyValue = viewState.referenceKeyOne,
                valueValue = viewState.referenceValueOne,
                onKeyChanged = onReferenceKeyOneChanged,
                onValueChanged = onReferenceValueOneChanged,
                enabled = !viewState.isSubmitting
            )
            ReferenceRow(
                keyValue = viewState.referenceKeyTwo,
                valueValue = viewState.referenceValueTwo,
                onKeyChanged = onReferenceKeyTwoChanged,
                onValueChanged = onReferenceValueTwoChanged,
                enabled = !viewState.isSubmitting
            )
            ReferenceRow(
                keyValue = viewState.referenceKeyThree,
                valueValue = viewState.referenceValueThree,
                onKeyChanged = onReferenceKeyThreeChanged,
                onValueChanged = onReferenceValueThreeChanged,
                enabled = !viewState.isSubmitting
            )
            TransactionTextField(
                value = viewState.evidenceAssetIds,
                onValueChange = onEvidenceAssetIdsChanged,
                label = "Evidence Asset IDs",
                placeholder = "asset_1, asset_2",
                enabled = !viewState.isSubmitting
            )

            if (viewState.isSubmitting) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        strokeWidth = 2.dp,
                        color = HawkBrandBlue
                    )
                    Text(
                        text = "Creating transaction...",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TransactionsMutedText
                    )
                }
            }

            Text(
                text = viewState.inlineMessage
                    ?: "Creating a transaction will send the request body to the Hawk transactions service.",
                style = MaterialTheme.typography.bodyMedium,
                color = if (viewState.inlineMessage == null) HawkTextMuted else HawkDanger
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(
                    onClick = onCancelClicked,
                    enabled = !viewState.isSubmitting
                ) {
                    Text(text = "Cancel")
                }
                Button(
                    onClick = onCreateTransactionClicked,
                    enabled = viewState.isSubmitEnabled && !viewState.isSubmitting,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = TransactionsPrimaryText,
                        contentColor = Color.White
                    )
                ) {
                    Text(text = "Create Transaction")
                }
            }
        }
    }
}

@Composable
private fun SectionTitle(
    title: String,
    body: String
) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            color = TransactionsPrimaryText
        )
        Text(
            text = body,
            style = MaterialTheme.typography.bodyMedium,
            color = TransactionsMutedText
        )
    }
}

@Composable
private fun ContainerFields(
    containerType: String,
    containerId: String,
    fundingAccountId: String,
    cashSessionId: String,
    onContainerTypeChanged: (String) -> Unit,
    onContainerIdChanged: (String) -> Unit,
    onFundingAccountIdChanged: (String) -> Unit,
    onCashSessionIdChanged: (String) -> Unit,
    enabled: Boolean,
    optional: Boolean = false
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        TransactionDropdownField(
            label = if (optional) "Container Type (optional)" else "Container Type",
            value = containerType.toFilterLabel(),
            options = containerTypeOptions,
            onOptionSelected = onContainerTypeChanged,
            modifier = Modifier.weight(1f),
            enabled = enabled
        )
        TransactionTextField(
            value = containerId,
            onValueChange = onContainerIdChanged,
            label = if (optional) "Container ID (optional)" else "Container ID",
            placeholder = "wallet_nequi_br_010",
            modifier = Modifier.weight(1.2f),
            enabled = enabled
        )
    }
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        TransactionTextField(
            value = fundingAccountId,
            onValueChange = onFundingAccountIdChanged,
            label = if (optional) "Funding Account ID (optional)" else "Funding Account ID",
            placeholder = "fa_bank_010",
            modifier = Modifier.weight(1f),
            enabled = enabled
        )
        TransactionTextField(
            value = cashSessionId,
            onValueChange = onCashSessionIdChanged,
            label = if (optional) "Cash Session ID (optional)" else "Cash Session ID",
            placeholder = "cs_1001",
            modifier = Modifier.weight(1f),
            enabled = enabled
        )
    }
}

@Composable
private fun ReferenceRow(
    keyValue: String,
    valueValue: String,
    onKeyChanged: (String) -> Unit,
    onValueChanged: (String) -> Unit,
    enabled: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        TransactionTextField(
            value = keyValue,
            onValueChange = onKeyChanged,
            label = "Reference Key",
            placeholder = "supplierId",
            modifier = Modifier.weight(1f),
            enabled = enabled
        )
        TransactionTextField(
            value = valueValue,
            onValueChange = onValueChanged,
            label = "Reference Value",
            placeholder = "sup_2001",
            modifier = Modifier.weight(1.2f),
            enabled = enabled
        )
    }
}

@Composable
private fun TransactionsResultCard(
    viewState: CreateTransactionViewState.SubmissionResult,
    onBackToTransactions: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = TransactionsCardBackground),
        border = BorderStroke(1.dp, HawkBorder.copy(alpha = 0.5f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Surface(
                modifier = Modifier.size(88.dp),
                shape = CircleShape,
                color = if (viewState.isSuccess) TransactionsSoftGreen else TransactionsSoftAmber
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = if (viewState.isSuccess) "OK" else "!",
                        style = MaterialTheme.typography.headlineMedium,
                        color = if (viewState.isSuccess) TransactionsPositive else TransactionsWarning,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = viewState.title,
                style = MaterialTheme.typography.headlineMedium,
                color = TransactionsPrimaryText
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = viewState.message,
                style = MaterialTheme.typography.bodyLarge,
                color = TransactionsMutedText
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = onBackToTransactions,
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = TransactionsPrimaryText,
                    contentColor = Color.White
                )
            ) {
                Text(text = "Back to transactions")
            }
        }
    }
}

@Composable
private fun TransactionTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Text,
    enabled: Boolean = true
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        enabled = enabled,
        modifier = modifier,
        label = { Text(text = label) },
        placeholder = { Text(text = placeholder) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = TransactionsCardBackground,
            unfocusedContainerColor = TransactionsCardBackground,
            focusedBorderColor = HawkBrandBlue,
            unfocusedBorderColor = HawkBorder,
            cursorColor = HawkBrandBlue
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TransactionDropdownField(
    label: String,
    value: String,
    options: List<String>,
    onOptionSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    var isExpanded by rememberSaveable { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = isExpanded,
        onExpandedChange = {
            if (enabled) {
                isExpanded = !isExpanded
            }
        },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = {},
            readOnly = true,
            enabled = enabled,
            label = { Text(text = label) },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
            },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = TransactionsCardBackground,
                unfocusedContainerColor = TransactionsCardBackground,
                focusedBorderColor = HawkBrandBlue,
                unfocusedBorderColor = HawkBorder
            )
        )
        DropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(text = option.toFilterLabel()) },
                    onClick = {
                        isExpanded = false
                        onOptionSelected(option)
                    }
                )
            }
        }
    }
}

private fun TransactionTone.toAccentColors(): Pair<Color, Color> = when (this) {
    TransactionTone.PRIMARY -> TransactionsSoftBlue to HawkBrandBlue
    TransactionTone.POSITIVE -> TransactionsSoftGreen to TransactionsPositive
    TransactionTone.WARNING -> TransactionsSoftAmber to TransactionsWarning
    TransactionTone.NEUTRAL -> TransactionsSoftGray to TransactionsNeutral
}

private fun TransactionTone.toPillColors(): Pair<Color, Color> = when (this) {
    TransactionTone.PRIMARY -> TransactionsSoftBlue to HawkBrandBlue
    TransactionTone.POSITIVE -> TransactionsSoftGreen to TransactionsPositive
    TransactionTone.WARNING -> TransactionsSoftAmber to TransactionsWarning
    TransactionTone.NEUTRAL -> TransactionsSoftGray to TransactionsNeutral
}

private fun String.toFilterLabel(): String = when (this) {
    TransactionsViewState.ALL_FILTER -> "All"
    else -> lowercase()
        .replace('_', ' ')
        .split(' ')
        .joinToString(separator = " ") { part ->
            part.replaceFirstChar { character -> character.titlecase() }
        }
}

@Preview(widthDp = 1366, heightDp = 900, showBackground = true)
@Composable
private fun PreviewTransactionsScreen() {
    HawkTheme {
        TransactionsScreen(
            viewState = TransactionsViewState(
                isLoading = false,
                transactions = listOf(
                    TransactionListItemUiModel(
                        moneyOperationId = "mop_9001",
                        operationTypeLabel = "Supplier Payment",
                        branchLabel = "BR_010",
                        businessDateLabel = "May 07, 2026",
                        mediumLabel = "Bank Transfer",
                        amountLabel = "\$150,000",
                        operationStatusLabel = "Executed",
                        operationStatusTone = TransactionTone.POSITIVE,
                        accountingStatusLabel = "Pending Ledger",
                        accountingStatusTone = TransactionTone.WARNING,
                        description = "Invoice 7781 for branch paper and toner restock",
                        reasonLabel = "Supplies Payment",
                        sourceContainerLabel = "Bank · bank_bancolombia_ops_010",
                        referencesSummary = "Supplier Id: sup_2001 · Invoice Reference: INV-7781"
                    )
                ),
                summaryCards = listOf(
                    TransactionSummaryCardUiModel(
                        key = "total",
                        badge = "$",
                        title = "Balance",
                        value = "\$350,000",
                        supportingText = "3 loaded · 2 pending approval",
                        tone = TransactionTone.PRIMARY
                    )
                ),
                statusTabs = listOf(
                    TransactionStatusTabUiModel("ALL", "All", 3),
                    TransactionStatusTabUiModel("PENDING_APPROVAL", "Pending Approval", 2),
                    TransactionStatusTabUiModel("EXECUTED", "Executed", 1)
                ),
                availableMedia = listOf("ALL", "BANK_TRANSFER", "CASH", "WALLET"),
                availableTypes = listOf("ALL", "SUPPLIER_PAYMENT", "BRANCH_TRANSFER_SENT"),
                loadedCount = 3
            ),
            onQueryChanged = {},
            onStatusSelected = {},
            onMediumSelected = {},
            onTypeSelected = {},
            onClearFiltersRequested = {},
            onRetryRequested = {},
            onCreateTransactionClicked = {},
            onWorkspaceSectionSelected = {}
        )
    }
}

@Preview(widthDp = 1366, heightDp = 900, showBackground = true)
@Composable
private fun PreviewCreateTransactionScreen() {
    HawkTheme {
        CreateTransactionScreen(
            viewState = CreateTransactionViewState.Form(
                branchFinancialDayId = "bfd_2026_05_07_br_010",
                amount = "150000",
                reasonCode = "SUPPLIES_PAYMENT",
                description = "Invoice 7781 for branch paper and toner restock",
                sourceContainerId = "bank_bancolombia_ops_010",
                sourceFundingAccountId = "fa_bank_010",
                referenceValueOne = "sup_2001",
                referenceValueTwo = "INV-7781"
            ),
            onWorkspaceSectionSelected = {},
            onBackToTransactions = {},
            onCancelClicked = {},
            onCreateTransactionClicked = {},
            onOperationTypeChanged = {},
            onBranchFinancialDayIdChanged = {},
            onMediumChanged = {},
            onAmountChanged = {},
            onCurrencyChanged = {},
            onReasonCodeChanged = {},
            onDescriptionChanged = {},
            onSourceContainerTypeChanged = {},
            onSourceContainerIdChanged = {},
            onSourceFundingAccountIdChanged = {},
            onSourceCashSessionIdChanged = {},
            onDestinationContainerTypeChanged = {},
            onDestinationContainerIdChanged = {},
            onDestinationFundingAccountIdChanged = {},
            onDestinationCashSessionIdChanged = {},
            onReferenceKeyOneChanged = {},
            onReferenceValueOneChanged = {},
            onReferenceKeyTwoChanged = {},
            onReferenceValueTwoChanged = {},
            onReferenceKeyThreeChanged = {},
            onReferenceValueThreeChanged = {},
            onEvidenceAssetIdsChanged = {}
        )
    }
}
