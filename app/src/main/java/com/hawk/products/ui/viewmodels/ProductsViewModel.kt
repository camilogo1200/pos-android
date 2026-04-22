package com.hawk.products.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.hawk.products.domain.entities.Product
import com.hawk.products.domain.entities.ProductStatus
import com.hawk.products.domain.usecases.interfaces.GetProductsUseCase
import com.hawk.products.ui.uimodels.ProductCardUiModel
import com.hawk.products.ui.uimodels.ProductStatusTone
import com.hawk.products.ui.viewstates.ProductsFilter
import com.hawk.products.ui.viewstates.ProductsViewState
import com.hawk.utils.coroutines.IoDispatcher
import com.hawk.utils.coroutines.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import java.text.NumberFormat
import java.util.Locale
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update

@HiltViewModel
class ProductsViewModel @Inject constructor(
    private val getProductsUseCase: GetProductsUseCase,
    @param:IoDispatcher private val coroutineDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val currencyFormatter = NumberFormat.getCurrencyInstance(
        Locale.forLanguageTag("es-CO")
    ).apply {
        maximumFractionDigits = 0
    }

    private val _viewState = MutableStateFlow(ProductsViewState())
    val viewState: StateFlow<ProductsViewState> = _viewState.asStateFlow()

    private var allProducts: List<ProductCardUiModel> = emptyList()

    init {
        loadProducts()
    }

    fun onQueryChanged(query: String) {
        _viewState.update { state -> state.copy(query = query) }
        applyFilters()
    }

    fun onFilterSelected(filter: ProductsFilter) {
        _viewState.update { state -> state.copy(selectedFilter = filter) }
        applyFilters()
    }

    fun onRetryRequested() {
        _viewState.update { state ->
            state.copy(
                isErrorDialogVisible = false,
                errorMessage = "",
                isLoading = true
            )
        }
        loadProducts()
    }

    fun onDismissErrorDialog() {
        _viewState.update { state ->
            state.copy(
                isErrorDialogVisible = false
            )
        }
    }

    private fun loadProducts() {
        launch(coroutineDispatcher) {
            getProductsUseCase().collectLatest { result ->
                result
                    .onSuccess { products ->
                        allProducts = products.map(::toUiModel)
                        _viewState.update { state -> state.copy(isLoading = false) }
                        applyFilters()
                    }
                    .onFailure { throwable ->
                        allProducts = emptyList()
                        _viewState.update { state ->
                            state.copy(
                                isLoading = false,
                                products = emptyList(),
                                isErrorDialogVisible = true,
                                errorMessage = throwable.message ?: "Unable to load products."
                            )
                        }
                    }
            }
        }
    }

    private fun applyFilters() {
        val state = _viewState.value
        val query = state.query.trim()

        val filteredProducts = allProducts.filter { product ->
            val matchesQuery = query.isBlank() ||
                product.name.contains(query, ignoreCase = true) ||
                product.sku.contains(query, ignoreCase = true)

            val matchesFilter = when (state.selectedFilter) {
                ProductsFilter.ALL -> true
                ProductsFilter.ACTIVE -> product.statusLabel.equals("Active", ignoreCase = true)
                ProductsFilter.LOW_STOCK -> product.availableQuantity <= 10
            }

            matchesQuery && matchesFilter
        }

        _viewState.update { current ->
            current.copy(products = filteredProducts)
        }
    }

    private fun toUiModel(product: Product): ProductCardUiModel = ProductCardUiModel(
        productId = product.productId,
        sku = product.sku,
        name = product.name,
        statusLabel = product.status.name.lowercase()
            .replaceFirstChar { it.titlecase() },
        statusTone = when (product.status) {
            ProductStatus.ACTIVE -> ProductStatusTone.POSITIVE
            ProductStatus.INACTIVE, ProductStatus.ARCHIVED -> ProductStatusTone.WARNING
            ProductStatus.DRAFT, ProductStatus.UNKNOWN -> ProductStatusTone.NEUTRAL
        },
        sellPriceLabel = currencyFormatter.format(product.sellPriceAmount),
        costPriceLabel = currencyFormatter.format(product.costAmount),
        stockLabel = "${product.availableQuantity} in stock",
        availableQuantity = product.availableQuantity
    )
}
