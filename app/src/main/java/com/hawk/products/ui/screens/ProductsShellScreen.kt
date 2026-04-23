package com.hawk.products.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hawk.R
import com.hawk.common.ui.KeyboardAwareScrollContainer
import com.hawk.designsystem.composables.layout.HawkWorkspaceScaffold
import com.hawk.designsystem.composables.layout.HawkWorkspaceSection
import com.hawk.home.theme.HawkBrandBlue
import com.hawk.home.theme.HawkTheme
import com.hawk.home.theme.HawkWorkspaceBackground
import com.hawk.products.ui.uimodels.ProductCardUiModel
import com.hawk.products.ui.uimodels.ProductStatusTone
import com.hawk.products.ui.viewmodels.CreateProductViewModel
import com.hawk.products.ui.viewmodels.ProductsViewModel
import com.hawk.products.ui.viewstates.CreateProductViewState
import com.hawk.products.ui.viewstates.ProductsFilter
import com.hawk.products.ui.viewstates.ProductsViewState

private val ProductsCardBackground = Color(0xFFFFFFFF)
private val ProductsBorder = Color(0xFFE3E7EE)
private val ProductsMutedText = Color(0xFF6B7280)
private val ProductsPrimaryText = Color(0xFF111827)
private val ProductsPositive = Color(0xFF15803D)
private val ProductsWarning = Color(0xFFC2410C)
private val ProductsNeutral = Color(0xFF475569)

@Composable
fun ProductsRoute(
    onCreateProductClicked: () -> Unit,
    onWorkspaceSectionSelected: (HawkWorkspaceSection) -> Unit = {},
    viewModel: ProductsViewModel = hiltViewModel()
) {
    val viewState by viewModel.viewState.collectAsStateWithLifecycle()

    ProductsScreen(
        viewState = viewState,
        onQueryChanged = viewModel::onQueryChanged,
        onFilterSelected = viewModel::onFilterSelected,
        onCreateProductClicked = onCreateProductClicked,
        onRetryRequested = viewModel::onRetryRequested,
        onDismissErrorDialog = viewModel::onDismissErrorDialog,
        onWorkspaceSectionSelected = onWorkspaceSectionSelected
    )
}

@Composable
fun CreateProductRoute(
    onBackToProducts: () -> Unit,
    onWorkspaceSectionSelected: (HawkWorkspaceSection) -> Unit = {},
    viewModel: CreateProductViewModel = hiltViewModel()
) {
    val viewState by viewModel.viewState.collectAsStateWithLifecycle()

    CreateProductScreen(
        viewState = viewState,
        onWorkspaceSectionSelected = onWorkspaceSectionSelected,
        onContinueToProducts = onBackToProducts,
        onCancelClicked = {
            viewModel.resetForm()
            onBackToProducts()
        },
        onCreateProductClicked = viewModel::onCreateProductClicked,
        onNameChanged = viewModel::onNameChanged,
        onSkuChanged = viewModel::onSkuChanged,
        onSellPriceChanged = viewModel::onSellPriceChanged,
        onCostPriceChanged = viewModel::onCostPriceChanged,
        onAvailableQuantityChanged = viewModel::onAvailableQuantityChanged,
        onStatusChanged = viewModel::onStatusChanged
    )
}

@Composable
fun ProductsScreen(
    viewState: ProductsViewState,
    onQueryChanged: (String) -> Unit,
    onFilterSelected: (ProductsFilter) -> Unit,
    onCreateProductClicked: () -> Unit,
    onRetryRequested: () -> Unit,
    onDismissErrorDialog: () -> Unit,
    onWorkspaceSectionSelected: (HawkWorkspaceSection) -> Unit
) {
    HawkWorkspaceScaffold(
        title = stringResource(R.string.products_title),
        subtitle = stringResource(R.string.products_header_subtitle),
        selectedSection = HawkWorkspaceSection.Products,
        onSectionSelected = onWorkspaceSectionSelected,
        actionContent = {
            Button(
                onClick = onCreateProductClicked,
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = ProductsPrimaryText,
                    contentColor = Color.White
                )
            ) {
                Text(text = stringResource(R.string.products_create_button))
            }
        }
    ) {
        SearchBar(
            query = viewState.query,
            onQueryChanged = onQueryChanged
        )

        ProductsFilterRow(
            selectedFilter = viewState.selectedFilter,
            onFilterSelected = onFilterSelected
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            when {
                viewState.isLoading -> ProductsLoadingState()
                viewState.products.isEmpty() && viewState.query.isBlank() && viewState.selectedFilter == ProductsFilter.ALL -> {
                    ProductsEmptyState(
                        title = stringResource(R.string.products_no_products_title),
                        body = stringResource(R.string.products_no_products_body),
                        actionLabel = stringResource(R.string.products_create_button),
                        onActionClick = onCreateProductClicked
                    )
                }

                viewState.products.isEmpty() -> {
                    ProductsEmptyState(
                        title = stringResource(R.string.products_no_results_title),
                        body = stringResource(R.string.products_no_results_body),
                        actionLabel = stringResource(R.string.products_create_button),
                        onActionClick = onCreateProductClicked
                    )
                }

                else -> {
                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(minSize = 250.dp),
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        items(viewState.products, key = { it.productId }) { product ->
                            ProductCard(product = product)
                        }
                    }
                }
            }
        }
    }

    if (viewState.isErrorDialogVisible) {
        AlertDialog(
            onDismissRequest = onDismissErrorDialog,
            title = {
                Text(text = stringResource(R.string.products_error_title))
            },
            text = {
                Text(
                    text = viewState.errorMessage.ifBlank {
                        stringResource(R.string.products_error_title)
                    }
                )
            },
            confirmButton = {
                TextButton(onClick = onRetryRequested) {
                    Text(text = stringResource(R.string.products_error_confirm))
                }
            },
            dismissButton = {
                TextButton(onClick = onDismissErrorDialog) {
                    Text(text = stringResource(R.string.products_error_dismiss))
                }
            }
        )
    }
}

@Composable
fun CreateProductScreen(
    viewState: CreateProductViewState,
    onWorkspaceSectionSelected: (HawkWorkspaceSection) -> Unit,
    onContinueToProducts: () -> Unit,
    onCancelClicked: () -> Unit,
    onCreateProductClicked: () -> Unit,
    onNameChanged: (String) -> Unit,
    onSkuChanged: (String) -> Unit,
    onSellPriceChanged: (String) -> Unit,
    onCostPriceChanged: (String) -> Unit,
    onAvailableQuantityChanged: (String) -> Unit,
    onStatusChanged: (String) -> Unit
) {
    HawkWorkspaceScaffold(
        title = stringResource(R.string.products_create_title),
        subtitle = stringResource(R.string.products_create_subtitle),
        selectedSection = HawkWorkspaceSection.Products,
        onSectionSelected = onWorkspaceSectionSelected
    ) {
        when (viewState) {
            is CreateProductViewState.Form -> {
                KeyboardAwareScrollContainer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    scrollbarColor = HawkBrandBlue.copy(alpha = 0.92f),
                    scrollbarTrackColor = ProductsBorder
                ) { scrollModifier ->
                    Column(
                        modifier = scrollModifier,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        CreateProductFormCard(
                            viewState = viewState,
                            onCancelClicked = onCancelClicked,
                            onCreateProductClicked = onCreateProductClicked,
                            onNameChanged = onNameChanged,
                            onSkuChanged = onSkuChanged,
                            onSellPriceChanged = onSellPriceChanged,
                            onCostPriceChanged = onCostPriceChanged,
                            onAvailableQuantityChanged = onAvailableQuantityChanged,
                            onStatusChanged = onStatusChanged
                        )
                    }
                }
            }

            is CreateProductViewState.SubmissionResult -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    CreateProductResultCard(
                        viewState = viewState,
                        onContinueToProducts = onContinueToProducts
                    )
                }
            }
        }
    }
}

@Composable
private fun SearchBar(
    query: String,
    onQueryChanged: (String) -> Unit
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChanged,
        modifier = Modifier.fillMaxWidth(),
        placeholder = {
            Text(
                text = stringResource(R.string.products_search_placeholder)
            )
        },
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = ProductsCardBackground,
            unfocusedContainerColor = ProductsCardBackground,
            focusedBorderColor = HawkBrandBlue,
            unfocusedBorderColor = ProductsBorder,
            cursorColor = HawkBrandBlue
        )
    )
}

@Composable
private fun ProductsFilterRow(
    selectedFilter: ProductsFilter,
    onFilterSelected: (ProductsFilter) -> Unit
) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ProductsFilter.entries.forEach { filter ->
            FilterChip(
                selected = selectedFilter == filter,
                onClick = { onFilterSelected(filter) },
                label = {
                    Text(
                        text = when (filter) {
                            ProductsFilter.ALL -> stringResource(R.string.products_filter_all)
                            ProductsFilter.ACTIVE -> stringResource(R.string.products_filter_active)
                            ProductsFilter.LOW_STOCK -> stringResource(R.string.products_filter_low_stock)
                        }
                    )
                }
            )
        }
    }
}

@Composable
private fun ProductCard(product: ProductCardUiModel) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = androidx.compose.material3.CardDefaults.cardColors(
            containerColor = ProductsCardBackground
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = product.name,
                        style = MaterialTheme.typography.titleMedium,
                        color = ProductsPrimaryText
                    )
                    Text(
                        text = product.sku,
                        style = MaterialTheme.typography.bodyMedium,
                        color = ProductsMutedText
                    )
                }
                StatusBadge(
                    label = product.statusLabel,
                    tone = product.statusTone
                )
            }

            ProductDetailLine(
                label = stringResource(R.string.products_sell_price),
                value = product.sellPriceLabel
            )
            ProductDetailLine(
                label = stringResource(R.string.products_cost_price),
                value = product.costPriceLabel
            )
            ProductDetailLine(
                label = stringResource(R.string.products_quantity_label),
                value = product.stockLabel
            )
        }
    }
}

@Composable
private fun StatusBadge(
    label: String,
    tone: ProductStatusTone
) {
    val backgroundColor = when (tone) {
        ProductStatusTone.POSITIVE -> ProductsPositive.copy(alpha = 0.12f)
        ProductStatusTone.WARNING -> ProductsWarning.copy(alpha = 0.12f)
        ProductStatusTone.NEUTRAL -> ProductsNeutral.copy(alpha = 0.12f)
    }
    val contentColor = when (tone) {
        ProductStatusTone.POSITIVE -> ProductsPositive
        ProductStatusTone.WARNING -> ProductsWarning
        ProductStatusTone.NEUTRAL -> ProductsNeutral
    }

    Surface(
        shape = RoundedCornerShape(999.dp),
        color = backgroundColor
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            style = MaterialTheme.typography.labelMedium,
            color = contentColor
        )
    }
}

@Composable
private fun ProductDetailLine(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = ProductsMutedText
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = ProductsPrimaryText,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun ProductsLoadingState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = HawkBrandBlue)
    }
}

@Composable
private fun ProductsEmptyState(
    title: String,
    body: String,
    actionLabel: String,
    onActionClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxSize(),
        shape = RoundedCornerShape(16.dp),
        colors = androidx.compose.material3.CardDefaults.cardColors(
            containerColor = ProductsCardBackground
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Surface(
                modifier = Modifier.size(80.dp),
                shape = CircleShape,
                color = HawkWorkspaceBackground
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = "P",
                        style = MaterialTheme.typography.headlineMedium,
                        color = ProductsMutedText
                    )
                }
            }
            Column(
                modifier = Modifier.padding(top = 20.dp, bottom = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = ProductsPrimaryText
                )
                Text(
                    text = body,
                    style = MaterialTheme.typography.bodyMedium,
                    color = ProductsMutedText
                )
            }
            Button(
                onClick = onActionClick,
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = ProductsPrimaryText,
                    contentColor = Color.White
                )
            ) {
                Text(text = actionLabel)
            }
        }
    }
}

@Composable
private fun CreateProductFormCard(
    viewState: CreateProductViewState.Form,
    onCancelClicked: () -> Unit,
    onCreateProductClicked: () -> Unit,
    onNameChanged: (String) -> Unit,
    onSkuChanged: (String) -> Unit,
    onSellPriceChanged: (String) -> Unit,
    onCostPriceChanged: (String) -> Unit,
    onAvailableQuantityChanged: (String) -> Unit,
    onStatusChanged: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = androidx.compose.material3.CardDefaults.cardColors(
            containerColor = ProductsCardBackground
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ProductTextField(
                value = viewState.name,
                onValueChange = onNameChanged,
                label = stringResource(R.string.products_name_label),
                placeholder = stringResource(R.string.products_name_placeholder),
                enabled = !viewState.isSubmitting
            )
            ProductTextField(
                value = viewState.sku,
                onValueChange = onSkuChanged,
                label = stringResource(R.string.products_sku_label),
                placeholder = stringResource(R.string.products_sku_placeholder),
                enabled = !viewState.isSubmitting
            )
            ProductTextField(
                value = viewState.sellPrice,
                onValueChange = onSellPriceChanged,
                label = stringResource(R.string.products_sell_price_label),
                placeholder = stringResource(R.string.products_sell_price_placeholder),
                keyboardType = KeyboardType.Number,
                enabled = !viewState.isSubmitting
            )
            ProductTextField(
                value = viewState.costPrice,
                onValueChange = onCostPriceChanged,
                label = stringResource(R.string.products_cost_price_label),
                placeholder = stringResource(R.string.products_cost_price_placeholder),
                keyboardType = KeyboardType.Number,
                enabled = !viewState.isSubmitting
            )
            ProductTextField(
                value = viewState.availableQuantity,
                onValueChange = onAvailableQuantityChanged,
                label = stringResource(R.string.products_quantity_label),
                placeholder = stringResource(R.string.products_quantity_placeholder),
                keyboardType = KeyboardType.Number,
                enabled = !viewState.isSubmitting
            )

            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = stringResource(R.string.products_status_label),
                    style = MaterialTheme.typography.labelLarge,
                    color = ProductsPrimaryText
                )
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf(
                        stringResource(R.string.products_status_active),
                        stringResource(R.string.products_status_inactive),
                        stringResource(R.string.products_status_draft)
                    ).forEach { status ->
                        FilterChip(
                            selected = viewState.status == status,
                            onClick = { onStatusChanged(status) },
                            enabled = !viewState.isSubmitting,
                            label = { Text(text = status) }
                        )
                    }
                }
            }

            if (viewState.isSubmitting) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        strokeWidth = 2.dp,
                        color = HawkBrandBlue
                    )
                    Text(
                        text = stringResource(R.string.products_create_loading),
                        style = MaterialTheme.typography.bodyMedium,
                        color = ProductsMutedText
                    )
                }
            }

            Text(
                text = viewState.inlineMessage ?: stringResource(R.string.products_form_note),
                style = MaterialTheme.typography.bodyMedium,
                color = if (viewState.inlineMessage == null) {
                    ProductsMutedText
                } else {
                    MaterialTheme.colorScheme.error
                }
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
                    Text(text = stringResource(R.string.products_cancel_button))
                }
                Button(
                    onClick = onCreateProductClicked,
                    enabled = viewState.isSubmitEnabled && !viewState.isSubmitting,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ProductsPrimaryText,
                        contentColor = Color.White
                    )
                ) {
                    Text(text = stringResource(R.string.products_create_button))
                }
            }
        }
    }
}

@Composable
private fun CreateProductResultCard(
    viewState: CreateProductViewState.SubmissionResult,
    onContinueToProducts: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxSize(),
        shape = RoundedCornerShape(16.dp),
        colors = androidx.compose.material3.CardDefaults.cardColors(
            containerColor = ProductsCardBackground
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            ResultStatusIcon(isSuccess = viewState.isSuccess)
            Column(
                modifier = Modifier.padding(top = 20.dp, bottom = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = viewState.title,
                    style = MaterialTheme.typography.headlineSmall,
                    color = ProductsPrimaryText
                )
                Text(
                    text = viewState.message,
                    style = MaterialTheme.typography.bodyLarge,
                    color = ProductsMutedText
                )
            }
            Button(
                onClick = onContinueToProducts,
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = ProductsPrimaryText,
                    contentColor = Color.White
                )
            ) {
                Text(text = stringResource(R.string.products_continue_button))
            }
        }
    }
}

@Composable
private fun ResultStatusIcon(isSuccess: Boolean) {
    val backgroundColor = if (isSuccess) {
        ProductsPositive.copy(alpha = 0.14f)
    } else {
        ProductsWarning.copy(alpha = 0.14f)
    }
    val contentColor = if (isSuccess) {
        ProductsPositive
    } else {
        ProductsWarning
    }
    val symbol = if (isSuccess) "✓" else "!"

    Surface(
        modifier = Modifier.size(84.dp),
        shape = CircleShape,
        color = backgroundColor
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = symbol,
                style = MaterialTheme.typography.headlineMedium,
                color = contentColor,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun ProductTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    enabled: Boolean = true
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        enabled = enabled,
        modifier = Modifier.fillMaxWidth(),
        label = { Text(text = label) },
        placeholder = { Text(text = placeholder) },
        singleLine = true,
        keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
            keyboardType = keyboardType
        ),
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = ProductsCardBackground,
            unfocusedContainerColor = ProductsCardBackground,
            focusedBorderColor = HawkBrandBlue,
            unfocusedBorderColor = ProductsBorder,
            cursorColor = HawkBrandBlue
        )
    )
}

@Preview
@Composable
fun PreviewProductsScreen() {
    HawkTheme() {
        ProductsScreen(ProductsViewState(), {}, {}, {}, {}, {}, {})
    }
}

@Preview
@Composable
fun PreviewCreateProductFormCard() {
    HawkTheme {
        CreateProductFormCard(
            viewState = CreateProductViewState.Form(),
            onCancelClicked = {},
            onCreateProductClicked = {},
            onNameChanged = {},
            onSkuChanged = {},
            onSellPriceChanged = {},
            onCostPriceChanged = {},
            onAvailableQuantityChanged = {},
            onStatusChanged = {}
        )
    }
}
