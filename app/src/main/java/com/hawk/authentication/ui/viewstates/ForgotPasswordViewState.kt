package com.hawk.authentication.ui.viewstates

import com.hawk.authentication.ui.uimodels.LoginFieldUiModel

data class ForgotPasswordViewState(
    val email: LoginFieldUiModel = LoginFieldUiModel(),
    val isSubmitEnabled: Boolean = false
)
