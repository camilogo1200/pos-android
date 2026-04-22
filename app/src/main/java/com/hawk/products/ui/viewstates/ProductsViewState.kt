package com.hawk.products.ui.viewstates

import com.hawk.products.ui.uimodels.ProductCardUiModel

data class ProductsViewState(
    val isLoading: Boolean = true,
    val query: String = "",
    val selectedFilter: ProductsFilter = ProductsFilter.ALL,
    val products: List<ProductCardUiModel> = emptyList(),
    val isErrorDialogVisible: Boolean = false,
    val errorMessage: String = ""
)

enum class ProductsFilter {
    ALL,
    ACTIVE,
    LOW_STOCK
}
