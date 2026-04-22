package com.hawk.authentication.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hawk.R
import com.hawk.authentication.ui.uimodels.LoginFieldUiModel
import com.hawk.authentication.ui.viewmodels.LoginViewModel
import com.hawk.authentication.ui.viewstates.LoginViewState
import com.hawk.home.theme.HawkSecondary
import com.hawk.home.theme.HawkSurface
import com.hawk.home.theme.HawkTheme
import com.hawk.home.theme.HawkTextMuted
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text

@Composable
fun AuthenticationRoute(
    onForgotPasswordClicked: () -> Unit,
    onProductsAuthenticated: () -> Unit,
    onConnectionFailure: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val viewState by viewModel.viewState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        viewModel.effects.collect { effect ->
            when (effect) {
                LoginViewModel.LoginEffect.NavigateToProducts -> onProductsAuthenticated()
                LoginViewModel.LoginEffect.NavigateToConnectionFailure -> onConnectionFailure()
            }
        }
    }

    LoginScreen(
        viewState = viewState,
        onEmailChanged = viewModel::onEmailChanged,
        onPasswordChanged = viewModel::onPasswordChanged,
        onRememberMeChanged = viewModel::onRememberMeChanged,
        onForgotPasswordClicked = onForgotPasswordClicked,
        onLoginClicked = viewModel::onLoginClicked
    )
}

@Composable
fun LoginScreen(
    viewState: LoginViewState,
    onEmailChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onRememberMeChanged: (Boolean) -> Unit,
    onForgotPasswordClicked: () -> Unit,
    onLoginClicked: () -> Unit
) {
    AuthenticationAdaptiveLayout { modifier ->
        AuthenticationFormScaffold(
            modifier = modifier,
            titleRes = R.string.auth_login_title,
            subtitleRes = R.string.auth_login_subtitle
        ) {
            if (viewState.hasGeneralError) {
                AuthBanner(messageRes = viewState.generalErrorMessageRes!!)
            }

            AuthTextField(
                value = viewState.email.value,
                onValueChange = onEmailChanged,
                labelRes = R.string.auth_email_label,
                placeholderRes = R.string.auth_email_placeholder,
                errorRes = viewState.email.errorMessageRes,
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            )

            AuthPasswordField(
                value = viewState.password.value,
                onValueChange = onPasswordChanged,
                labelRes = R.string.auth_password_label,
                placeholderRes = R.string.auth_password_placeholder,
                errorRes = viewState.password.errorMessageRes,
                imeAction = ImeAction.Done
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = viewState.rememberMe,
                        onCheckedChange = onRememberMeChanged,
                        colors = CheckboxDefaults.colors(
                            checkedColor = com.hawk.home.theme.HawkPrimary,
                            uncheckedColor = HawkSecondary,
                            checkmarkColor = HawkSurface
                        )
                    )
                    Text(
                        text = androidx.compose.ui.res.stringResource(R.string.auth_remember_me),
                        style = MaterialTheme.typography.bodyMedium,
                        color = HawkTextMuted
                    )
                }
                AuthInlineTextButton(
                    textRes = R.string.auth_forgot_password,
                    onClick = onForgotPasswordClicked
                )
            }

            AuthPrimaryButton(
                textRes = R.string.auth_login_button,
                onClick = onLoginClicked,
                enabled = viewState.isSubmitEnabled && !viewState.isLoading
            )
        }
    }
}

@Preview(
    name = "Authentication Tablet",
    widthDp = 1280,
    heightDp = 800,
    showBackground = true
)
@Composable
private fun AuthenticationTabletPreview() {
    HawkTheme {
        LoginScreen(
            viewState = LoginViewState(
                email = LoginFieldUiModel(
                    value = "cashier@hawk.com",
                    isTouched = true
                ),
                password = LoginFieldUiModel(
                    value = "Password123",
                    isTouched = true
                ),
                rememberMe = true,
                isSubmitEnabled = true
            ),
            onEmailChanged = {},
            onPasswordChanged = {},
            onRememberMeChanged = {},
            onForgotPasswordClicked = {},
            onLoginClicked = {}
        )
    }
}

@Preview(
    name = "Authentication Phone",
    widthDp = 412,
    heightDp = 915,
    showBackground = true
)
@Composable
private fun AuthenticationPhonePreview() {
    HawkTheme {
        LoginScreen(
            viewState = LoginViewState(),
            onEmailChanged = {},
            onPasswordChanged = {},
            onRememberMeChanged = {},
            onForgotPasswordClicked = {},
            onLoginClicked = {}
        )
    }
}
