package com.hawk.products.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.window.core.layout.WindowSizeClass
import com.hawk.R
import com.hawk.products.ui.uimodels.ProductCardUiModel
import com.hawk.products.ui.uimodels.ProductStatusTone
import com.hawk.products.ui.viewmodels.CreateProductViewModel
import com.hawk.products.ui.viewmodels.ProductsViewModel
import com.hawk.products.ui.viewstates.CreateProductViewState
import com.hawk.products.ui.viewstates.ProductsFilter
import com.hawk.products.ui.viewstates.ProductsViewState

private val ProductsSidebarBackground = Color(0xFF1A2333)
private val ProductsSidebarActive = Color(0xFF364152)
private val ProductsSidebarText = Color(0xFF9AA3B0)
private val ProductsAccent = Color(0xFF3B82F6)
private val ProductsPageBackground = Color(0xFFF0F0F0)
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
    viewModel: ProductsViewModel = hiltViewModel()
) {
    val viewState by viewModel.viewState.collectAsStateWithLifecycle()

    ProductsScreen(
        viewState = viewState,
        onQueryChanged = viewModel::onQueryChanged,
        onFilterSelected = viewModel::onFilterSelected,
        onCreateProductClicked = onCreateProductClicked,
        onRetryRequested = viewModel::onRetryRequested,
        onDismissErrorDialog = viewModel::onDismissErrorDialog
    )
}

@Composable
fun CreateProductRoute(
    onBackToProducts: () -> Unit,
    viewModel: CreateProductViewModel = hiltViewModel()
) {
    val viewState by viewModel.viewState.collectAsStateWithLifecycle()

    CreateProductScreen(
        viewState = viewState,
        onBackToProducts = onBackToProducts,
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
    onDismissErrorDialog: () -> Unit
) {
    ProductsModuleScaffold(
        title = stringResource(R.string.products_title),
        subtitle = stringResource(R.string.products_header_subtitle),
        actionLabel = stringResource(R.string.products_create_button),
        onActionClick = onCreateProductClicked
    ) {
        SearchBar(
            query = viewState.query,
            onQueryChanged = onQueryChanged
        )

        ProductsFilterRow(
            selectedFilter = viewState.selectedFilter,
            onFilterSelected = onFilterSelected
        )

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
    onBackToProducts: () -> Unit,
    onNameChanged: (String) -> Unit,
    onSkuChanged: (String) -> Unit,
    onSellPriceChanged: (String) -> Unit,
    onCostPriceChanged: (String) -> Unit,
    onAvailableQuantityChanged: (String) -> Unit,
    onStatusChanged: (String) -> Unit
) {
    ProductsModuleScaffold(
        title = stringResource(R.string.products_create_title),
        subtitle = stringResource(R.string.products_create_subtitle),
        actionLabel = stringResource(R.string.products_back_button),
        onActionClick = onBackToProducts
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
                    placeholder = stringResource(R.string.products_name_placeholder)
                )
                ProductTextField(
                    value = viewState.sku,
                    onValueChange = onSkuChanged,
                    label = stringResource(R.string.products_sku_label),
                    placeholder = stringResource(R.string.products_sku_placeholder)
                )
                ProductTextField(
                    value = viewState.sellPrice,
                    onValueChange = onSellPriceChanged,
                    label = stringResource(R.string.products_sell_price_label),
                    placeholder = stringResource(R.string.products_sell_price_placeholder),
                    keyboardType = KeyboardType.Number
                )
                ProductTextField(
                    value = viewState.costPrice,
                    onValueChange = onCostPriceChanged,
                    label = stringResource(R.string.products_cost_price_label),
                    placeholder = stringResource(R.string.products_cost_price_placeholder),
                    keyboardType = KeyboardType.Number
                )
                ProductTextField(
                    value = viewState.availableQuantity,
                    onValueChange = onAvailableQuantityChanged,
                    label = stringResource(R.string.products_quantity_label),
                    placeholder = stringResource(R.string.products_quantity_placeholder),
                    keyboardType = KeyboardType.Number
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
                                label = { Text(text = status) }
                            )
                        }
                    }
                }

                Text(
                    text = stringResource(R.string.products_form_note),
                    style = MaterialTheme.typography.bodyMedium,
                    color = ProductsMutedText
                )
            }
        }
    }
}

@Composable
private fun ProductsModuleScaffold(
    title: String,
    subtitle: String,
    actionLabel: String,
    onActionClick: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    val windowSizeClass = currentWindowAdaptiveInfo(
        supportLargeAndXLargeWidth = true
    ).windowSizeClass

    val useWideShell = windowSizeClass.isWidthAtLeastBreakpoint(
        WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND
    )

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = ProductsPageBackground
    ) {
        if (useWideShell) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .safeDrawingPadding()
                    .padding(20.dp)
                    .clip(RoundedCornerShape(28.dp))
                    .background(ProductsCardBackground)
            ) {
                ProductsMenuPane(
                    modifier = Modifier
                        .width(243.dp)
                        .fillMaxHeight()
                )
                ProductsContentPane(
                    title = title,
                    subtitle = subtitle,
                    actionLabel = actionLabel,
                    onActionClick = onActionClick,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    content = content
                )
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .safeDrawingPadding()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                ProductsMenuPane(modifier = Modifier.fillMaxWidth())
                ProductsContentPane(
                    title = title,
                    subtitle = subtitle,
                    actionLabel = actionLabel,
                    onActionClick = onActionClick,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    content = content
                )
            }
        }
    }
}

@Composable
private fun ProductsContentPane(
    title: String,
    subtitle: String,
    actionLabel: String,
    onActionClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Surface(
        modifier = modifier,
        color = ProductsPageBackground
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.headlineMedium,
                        color = ProductsPrimaryText
                    )
                    Text(
                        text = subtitle,
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
            content()
        }
    }
}

@Composable
private fun ProductsMenuPane(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .background(ProductsSidebarBackground)
            .padding(horizontal = 16.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp)
        ) {
            Surface(
                modifier = Modifier.size(28.dp),
                shape = RoundedCornerShape(8.dp),
                color = ProductsAccent
            ) {}
            Text(
                text = "Hawk",
                style = MaterialTheme.typography.titleMedium,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }

        Surface(
            shape = RoundedCornerShape(8.dp),
            color = ProductsSidebarActive
        ) {
            Text(
                text = stringResource(R.string.auth_products_nav),
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White,
                fontWeight = FontWeight.Medium
            )
        }

        SidebarLabel(stringResource(R.string.auth_sales_nav))
        SidebarLabel(stringResource(R.string.auth_inventory_nav))
        SidebarLabel(stringResource(R.string.auth_customers_nav))
    }
}

@Composable
private fun SidebarLabel(text: String) {
    Text(
        text = text,
        modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
        style = MaterialTheme.typography.bodyLarge,
        color = ProductsSidebarText
    )
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
            focusedBorderColor = ProductsAccent,
            unfocusedBorderColor = ProductsBorder,
            cursorColor = ProductsAccent
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
        CircularProgressIndicator(color = ProductsAccent)
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
                color = ProductsPageBackground
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
private fun ProductTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
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
            focusedBorderColor = ProductsAccent,
            unfocusedBorderColor = ProductsBorder,
            cursorColor = ProductsAccent
        )
    )
}
