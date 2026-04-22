package com.hawk.products.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.hawk.products.ui.viewstates.CreateProductViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class CreateProductViewModel @Inject constructor() : ViewModel() {

    private val _viewState = MutableStateFlow(CreateProductViewState())
    val viewState: StateFlow<CreateProductViewState> = _viewState.asStateFlow()

    fun onNameChanged(value: String) {
        _viewState.update { state -> state.copy(name = value) }
    }

    fun onSkuChanged(value: String) {
        _viewState.update { state -> state.copy(sku = value) }
    }

    fun onSellPriceChanged(value: String) {
        _viewState.update { state -> state.copy(sellPrice = value) }
    }

    fun onCostPriceChanged(value: String) {
        _viewState.update { state -> state.copy(costPrice = value) }
    }

    fun onAvailableQuantityChanged(value: String) {
        _viewState.update { state -> state.copy(availableQuantity = value) }
    }

    fun onStatusChanged(value: String) {
        _viewState.update { state -> state.copy(status = value) }
    }
}
