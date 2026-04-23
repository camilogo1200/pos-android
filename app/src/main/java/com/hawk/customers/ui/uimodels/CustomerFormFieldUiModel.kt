package com.hawk.customers.ui.uimodels

import androidx.annotation.StringRes

data class CustomerFormFieldUiModel(
    val value: String = "",
    @param:StringRes val errorMessageRes: Int? = null,
    val isTouched: Boolean = false
) {
    val hasError: Boolean
        get() = errorMessageRes != null
}
