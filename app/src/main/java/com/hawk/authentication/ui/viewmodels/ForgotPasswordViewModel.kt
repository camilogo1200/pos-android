package com.hawk.authentication.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hawk.R
import com.hawk.authentication.domain.usecases.interfaces.ValidateLoginEmailUseCase
import com.hawk.authentication.ui.uimodels.LoginFieldUiModel
import com.hawk.authentication.ui.viewstates.ForgotPasswordViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private val validateLoginEmailUseCase: ValidateLoginEmailUseCase
) : ViewModel() {

    private val _viewState = MutableStateFlow(ForgotPasswordViewState())
    val viewState: StateFlow<ForgotPasswordViewState> = _viewState.asStateFlow()

    private val _effects = MutableSharedFlow<ForgotPasswordEffect>()
    val effects: SharedFlow<ForgotPasswordEffect> = _effects.asSharedFlow()

    fun onEmailChanged(email: String) {
        _viewState.update { current ->
            val updatedEmail = current.email.validateEmail(email)
            current.copy(
                email = updatedEmail,
                isSubmitEnabled = validateLoginEmailUseCase(updatedEmail.value.trim())
            )
        }
    }

    fun onContinueClicked() {
        val updatedState = _viewState.value.let { current ->
            val validatedEmail = current.email.validateEmail(current.email.value, forceTouched = true)
            current.copy(
                email = validatedEmail,
                isSubmitEnabled = validateLoginEmailUseCase(validatedEmail.value.trim())
            )
        }

        _viewState.value = updatedState
        if (updatedState.isSubmitEnabled) {
            viewModelScope.launch {
                _effects.emit(ForgotPasswordEffect.NavigateToCheckEmail(updatedState.email.value.trim()))
            }
        }
    }

    private fun LoginFieldUiModel.validateEmail(
        email: String,
        forceTouched: Boolean = false
    ): LoginFieldUiModel {
        val isTouched = forceTouched || this.isTouched || email.isNotEmpty()
        val trimmedEmail = email.trim()
        val errorMessageRes = when {
            !isTouched -> null
            trimmedEmail.isEmpty() -> R.string.auth_email_required
            validateLoginEmailUseCase(trimmedEmail) -> null
            else -> R.string.auth_email_invalid
        }

        return copy(
            value = email,
            errorMessageRes = errorMessageRes,
            isTouched = isTouched
        )
    }

    sealed interface ForgotPasswordEffect {
        data class NavigateToCheckEmail(val email: String) : ForgotPasswordEffect
    }
}
