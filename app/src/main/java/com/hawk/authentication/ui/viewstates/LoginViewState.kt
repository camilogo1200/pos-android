package com.hawk.authentication.ui.viewstates

import androidx.annotation.StringRes
import com.hawk.authentication.ui.uimodels.LoginFieldUiModel

data class LoginViewState(
    val email: LoginFieldUiModel = LoginFieldUiModel(),
    val password: LoginFieldUiModel = LoginFieldUiModel(),
    val rememberMe: Boolean = false,
    val isLoading: Boolean = false,
    val isSubmitEnabled: Boolean = false,
    @param:StringRes val generalErrorMessageRes: Int? = null
) {
    val hasGeneralError: Boolean
        get() = generalErrorMessageRes != null
}
