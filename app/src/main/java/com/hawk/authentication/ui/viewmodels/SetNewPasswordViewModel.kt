package com.hawk.authentication.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hawk.R
import com.hawk.authentication.domain.usecases.interfaces.ValidateLoginPasswordUseCase
import com.hawk.authentication.domain.usecases.interfaces.ValidatePasswordConfirmationUseCase
import com.hawk.authentication.ui.uimodels.LoginFieldUiModel
import com.hawk.authentication.ui.viewstates.SetNewPasswordViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class SetNewPasswordViewModel @Inject constructor(
    private val validateLoginPasswordUseCase: ValidateLoginPasswordUseCase,
    private val validatePasswordConfirmationUseCase: ValidatePasswordConfirmationUseCase
) : ViewModel() {

    private val _viewState = MutableStateFlow(SetNewPasswordViewState())
    val viewState: StateFlow<SetNewPasswordViewState> = _viewState.asStateFlow()

    private val _effects = MutableSharedFlow<SetNewPasswordEffect>()
    val effects: SharedFlow<SetNewPasswordEffect> = _effects.asSharedFlow()

    fun onPasswordChanged(password: String) {
        _viewState.update { current ->
            val updatedPassword = current.password.validatePassword(password)
            val updatedConfirm = current.confirmPassword.validateConfirmation(
                password = updatedPassword.value,
                confirmPassword = current.confirmPassword.value
            )
            current.copy(
                password = updatedPassword,
                confirmPassword = updatedConfirm,
                isSubmitEnabled = isFormValid(
                    password = updatedPassword.value,
                    confirmPassword = updatedConfirm.value
                )
            )
        }
    }

    fun onConfirmPasswordChanged(confirmPassword: String) {
        _viewState.update { current ->
            val updatedConfirm = current.confirmPassword.validateConfirmation(
                password = current.password.value,
                confirmPassword = confirmPassword
            )
            current.copy(
                confirmPassword = updatedConfirm,
                isSubmitEnabled = isFormValid(
                    password = current.password.value,
                    confirmPassword = updatedConfirm.value
                )
            )
        }
    }

    fun onResetPasswordClicked() {
        val updatedState = _viewState.value.let { current ->
            val validatedPassword = current.password.validatePassword(
                password = current.password.value,
                forceTouched = true
            )
            val validatedConfirm = current.confirmPassword.validateConfirmation(
                password = validatedPassword.value,
                confirmPassword = current.confirmPassword.value,
                forceTouched = true
            )
            current.copy(
                password = validatedPassword,
                confirmPassword = validatedConfirm,
                isSubmitEnabled = isFormValid(
                    password = validatedPassword.value,
                    confirmPassword = validatedConfirm.value
                )
            )
        }

        _viewState.value = updatedState
        if (updatedState.isSubmitEnabled) {
            viewModelScope.launch {
                _effects.emit(SetNewPasswordEffect.NavigateToSuccess)
            }
        }
    }

    private fun LoginFieldUiModel.validatePassword(
        password: String,
        forceTouched: Boolean = false
    ): LoginFieldUiModel {
        val isTouched = forceTouched || this.isTouched || password.isNotEmpty()
        val errorMessageRes = when {
            !isTouched -> null
            password.isEmpty() -> R.string.auth_password_required
            validateLoginPasswordUseCase(password) -> null
            else -> R.string.auth_password_invalid
        }

        return copy(
            value = password,
            errorMessageRes = errorMessageRes,
            isTouched = isTouched
        )
    }

    private fun LoginFieldUiModel.validateConfirmation(
        password: String,
        confirmPassword: String,
        forceTouched: Boolean = false
    ): LoginFieldUiModel {
        val isTouched = forceTouched || this.isTouched || confirmPassword.isNotEmpty()
        val errorMessageRes = when {
            !isTouched -> null
            confirmPassword.isEmpty() -> R.string.auth_confirm_password_required
            validatePasswordConfirmationUseCase(password, confirmPassword) -> null
            else -> R.string.auth_confirm_password_invalid
        }

        return copy(
            value = confirmPassword,
            errorMessageRes = errorMessageRes,
            isTouched = isTouched
        )
    }

    private fun isFormValid(password: String, confirmPassword: String): Boolean =
        validateLoginPasswordUseCase(password) &&
            validatePasswordConfirmationUseCase(password, confirmPassword)

    sealed interface SetNewPasswordEffect {
        data object NavigateToSuccess : SetNewPasswordEffect
    }
}
