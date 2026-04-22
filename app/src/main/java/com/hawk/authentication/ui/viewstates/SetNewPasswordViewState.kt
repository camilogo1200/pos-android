package com.hawk.authentication.ui.viewstates

import com.hawk.authentication.ui.uimodels.LoginFieldUiModel

data class SetNewPasswordViewState(
    val password: LoginFieldUiModel = LoginFieldUiModel(),
    val confirmPassword: LoginFieldUiModel = LoginFieldUiModel(),
    val isSubmitEnabled: Boolean = false
)
