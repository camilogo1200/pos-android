package com.hawk.authentication.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.hawk.R
import com.hawk.authentication.domain.entities.AuthenticationException
import com.hawk.authentication.domain.usecases.interfaces.AuthenticateUserUseCase
import com.hawk.authentication.domain.usecases.interfaces.ValidateLoginEmailUseCase
import com.hawk.authentication.domain.usecases.interfaces.ValidateLoginPasswordUseCase
import com.hawk.authentication.ui.uimodels.LoginFieldUiModel
import com.hawk.authentication.ui.viewstates.LoginViewState
import com.hawk.utils.coroutines.IoDispatcher
import com.hawk.utils.coroutines.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authenticateUserUseCase: AuthenticateUserUseCase,
    private val validateLoginEmailUseCase: ValidateLoginEmailUseCase,
    private val validateLoginPasswordUseCase: ValidateLoginPasswordUseCase,
    @param:IoDispatcher private val coroutineDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _viewState = MutableStateFlow(LoginViewState())
    val viewState: StateFlow<LoginViewState> = _viewState.asStateFlow()

    private val _effects = MutableSharedFlow<LoginEffect>()
    val effects: SharedFlow<LoginEffect> = _effects.asSharedFlow()

    fun onEmailChanged(email: String) {
        _viewState.update { currentState ->
            val updatedEmail = currentState.email.validateEmail(email)
            currentState.copy(
                email = updatedEmail,
                generalErrorMessageRes = null,
                isSubmitEnabled = isFormValid(
                    email = updatedEmail.value,
                    password = currentState.password.value
                )
            )
        }
    }

    fun onPasswordChanged(password: String) {
        _viewState.update { currentState ->
            val updatedPassword = currentState.password.validatePassword(password)
            currentState.copy(
                password = updatedPassword,
                generalErrorMessageRes = null,
                isSubmitEnabled = isFormValid(
                    email = currentState.email.value,
                    password = updatedPassword.value
                )
            )
        }
    }

    fun onRememberMeChanged(rememberMe: Boolean) {
        _viewState.update { currentState ->
            currentState.copy(rememberMe = rememberMe)
        }
    }

    fun onLoginClicked() {
        val currentState = _viewState.value
        val updatedState = currentState.run {
            val validatedEmail = currentState.email.validateEmail(currentState.email.value, forceTouched = true)
            val validatedPassword = currentState.password.validatePassword(
                currentState.password.value,
                forceTouched = true
            )

            currentState.copy(
                email = validatedEmail,
                password = validatedPassword,
                generalErrorMessageRes = null,
                isSubmitEnabled = isFormValid(
                    email = validatedEmail.value,
                    password = validatedPassword.value
                )
            )
        }

        _viewState.value = updatedState
        if (!updatedState.isSubmitEnabled) {
            return
        }

        launch(coroutineDispatcher) {
            _viewState.update { state ->
                state.copy(
                    isLoading = true,
                    generalErrorMessageRes = null
                )
            }

            authenticateUserUseCase(
                username = updatedState.email.value.trim(),
                password = updatedState.password.value
            ).collectLatest { result ->
                result
                    .onSuccess { session ->
                        _viewState.update { state ->
                            state.copy(
                                isLoading = false,
                                generalErrorMessageRes = null
                            )
                        }
                        if (session.hasBearerToken) {
                            _effects.emit(LoginEffect.NavigateToProducts)
                        } else {
                            _effects.emit(LoginEffect.NavigateToConnectionFailure)
                        }
                    }
                    .onFailure { throwable ->
                        when (throwable) {
                            is AuthenticationException.InvalidCredentials -> {
                                _viewState.update { state ->
                                    state.copy(
                                        isLoading = false,
                                        generalErrorMessageRes = R.string.auth_login_invalid_credentials
                                    )
                                }
                            }

                            else -> {
                                _viewState.update { state ->
                                    state.copy(isLoading = false)
                                }
                                _effects.emit(LoginEffect.NavigateToConnectionFailure)
                            }
                        }
                    }
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

    private fun isFormValid(email: String, password: String): Boolean =
        validateLoginEmailUseCase(email.trim()) && validateLoginPasswordUseCase(password)

    sealed interface LoginEffect {
        data object NavigateToProducts : LoginEffect
        data object NavigateToConnectionFailure : LoginEffect
    }
}
