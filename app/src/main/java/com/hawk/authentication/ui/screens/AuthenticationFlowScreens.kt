package com.hawk.authentication.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hawk.R
import com.hawk.authentication.ui.viewmodels.ForgotPasswordViewModel
import com.hawk.authentication.ui.viewmodels.SetNewPasswordViewModel
import com.hawk.authentication.ui.viewstates.ForgotPasswordViewState
import com.hawk.authentication.ui.viewstates.SetNewPasswordViewState
import com.hawk.home.theme.HawkPrimary
import com.hawk.home.theme.HawkShowcaseAccent
import com.hawk.home.theme.HawkTextMuted
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.tooling.preview.Preview
import com.hawk.home.theme.HawkTheme

@Composable
fun ForgotPasswordRoute(
    onBackToLogin: () -> Unit,
    onNavigateToCheckEmail: (String) -> Unit,
    viewModel: ForgotPasswordViewModel = hiltViewModel()
) {
    val viewState by viewModel.viewState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        viewModel.effects.collect { effect ->
            when (effect) {
                is ForgotPasswordViewModel.ForgotPasswordEffect.NavigateToCheckEmail -> {
                    onNavigateToCheckEmail(effect.email)
                }
            }
        }
    }

    ForgotPasswordScreen(
        viewState = viewState,
        onEmailChanged = viewModel::onEmailChanged,
        onContinueClicked = viewModel::onContinueClicked,
        onBackToLogin = onBackToLogin
    )
}

@Composable
fun ForgotPasswordScreen(
    viewState: ForgotPasswordViewState,
    onEmailChanged: (String) -> Unit,
    onContinueClicked: () -> Unit,
    onBackToLogin: () -> Unit
) {
    AuthenticationAdaptiveLayout { modifier ->
        AuthenticationFormScaffold(
            modifier = modifier,
            titleRes = R.string.auth_forgot_password_title,
            subtitleRes = R.string.auth_forgot_password_body
        ) {
            AuthTextField(
                value = viewState.email.value,
                onValueChange = onEmailChanged,
                labelRes = R.string.auth_email_label,
                placeholderRes = R.string.auth_email_placeholder,
                errorRes = viewState.email.errorMessageRes,
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Done
            )
            AuthPrimaryButton(
                textRes = R.string.auth_continue_button,
                onClick = onContinueClicked,
                enabled = viewState.isSubmitEnabled
            )
            AuthCenteredTextButton(
                textRes = R.string.auth_back_to_login,
                onClick = onBackToLogin
            )
        }
    }
}

@Composable
fun CheckEmailScreen(
    email: String,
    onOpenEmailApp: () -> Unit,
    onBackToLogin: () -> Unit
) {
    AuthenticationAdaptiveLayout { modifier ->
        Column(
            modifier = modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AuthStatusIcon(
                symbol = "@",
                containerColor = HawkShowcaseAccent.copy(alpha = 0.42f),
                contentColor = HawkPrimary
            )
            Text(
                text = androidx.compose.ui.res.stringResource(R.string.auth_check_email_title),
                style = MaterialTheme.typography.headlineMedium
            )
            Text(
                text = androidx.compose.ui.res.stringResource(
                    R.string.auth_check_email_body,
                    email
                ),
                style = MaterialTheme.typography.bodyLarge,
                color = HawkTextMuted
            )
            AuthPrimaryButton(
                textRes = R.string.auth_open_email_app,
                onClick = onOpenEmailApp,
                enabled = true
            )
            AuthCenteredTextButton(
                textRes = R.string.auth_back_to_login,
                onClick = onBackToLogin
            )
        }
    }
}

@Composable
fun SetNewPasswordRoute(
    onPasswordResetSuccess: () -> Unit,
    onBackToLogin: () -> Unit,
    viewModel: SetNewPasswordViewModel = hiltViewModel()
) {
    val viewState by viewModel.viewState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel) {
        viewModel.effects.collect { effect ->
            when (effect) {
                SetNewPasswordViewModel.SetNewPasswordEffect.NavigateToSuccess -> onPasswordResetSuccess()
            }
        }
    }

    SetNewPasswordScreen(
        viewState = viewState,
        onPasswordChanged = viewModel::onPasswordChanged,
        onConfirmPasswordChanged = viewModel::onConfirmPasswordChanged,
        onResetPasswordClicked = viewModel::onResetPasswordClicked,
        onBackToLogin = onBackToLogin
    )
}

@Composable
fun SetNewPasswordScreen(
    viewState: SetNewPasswordViewState,
    onPasswordChanged: (String) -> Unit,
    onConfirmPasswordChanged: (String) -> Unit,
    onResetPasswordClicked: () -> Unit,
    onBackToLogin: () -> Unit
) {
    AuthenticationAdaptiveLayout { modifier ->
        AuthenticationFormScaffold(
            modifier = modifier,
            titleRes = R.string.auth_set_new_password_title,
            subtitleRes = R.string.auth_set_new_password_body
        ) {
            AuthPasswordField(
                value = viewState.password.value,
                onValueChange = onPasswordChanged,
                labelRes = R.string.auth_new_password_label,
                placeholderRes = R.string.auth_new_password_placeholder,
                errorRes = viewState.password.errorMessageRes,
                imeAction = ImeAction.Next
            )
            AuthPasswordField(
                value = viewState.confirmPassword.value,
                onValueChange = onConfirmPasswordChanged,
                labelRes = R.string.auth_confirm_password_label,
                placeholderRes = R.string.auth_confirm_password_placeholder,
                errorRes = viewState.confirmPassword.errorMessageRes,
                imeAction = ImeAction.Done
            )
            AuthPrimaryButton(
                textRes = R.string.auth_reset_password_button,
                onClick = onResetPasswordClicked,
                enabled = viewState.isSubmitEnabled
            )
            AuthCenteredTextButton(
                textRes = R.string.auth_back_to_login,
                onClick = onBackToLogin
            )
        }
    }
}

@Composable
fun PasswordResetSuccessScreen(
    onBackToLogin: () -> Unit
) {
    AuthenticationAdaptiveLayout { modifier ->
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AuthStatusIcon(
                symbol = "OK",
                containerColor = HawkShowcaseAccent.copy(alpha = 0.42f),
                contentColor = HawkPrimary
            )
            Text(
                text = androidx.compose.ui.res.stringResource(R.string.auth_password_reset_success_title),
                style = MaterialTheme.typography.headlineMedium
            )
            Text(
                text = androidx.compose.ui.res.stringResource(R.string.auth_password_reset_success_body),
                style = MaterialTheme.typography.bodyLarge,
                color = HawkTextMuted
            )
            AuthPrimaryButton(
                textRes = R.string.auth_back_to_login,
                onClick = onBackToLogin,
                enabled = true
            )
        }
    }
}

@Composable
fun ConnectionErrorScreen(
    onRetry: () -> Unit,
    onBackToLogin: () -> Unit
) {
    AuthenticationAdaptiveLayout { modifier ->
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AuthStatusIcon(
                symbol = "!",
                containerColor = MaterialTheme.colorScheme.error.copy(alpha = 0.12f),
                contentColor = MaterialTheme.colorScheme.error
            )
            Text(
                text = androidx.compose.ui.res.stringResource(R.string.auth_connection_failed_title),
                style = MaterialTheme.typography.headlineMedium
            )
            Text(
                text = androidx.compose.ui.res.stringResource(R.string.auth_connection_failed_body),
                style = MaterialTheme.typography.bodyLarge,
                color = HawkTextMuted
            )
            AuthPrimaryButton(
                textRes = R.string.auth_try_again,
                onClick = onRetry,
                enabled = true
            )
            AuthCenteredTextButton(
                textRes = R.string.auth_back_to_login,
                onClick = onBackToLogin
            )
        }
    }
}


@Preview(
    name = "Connection Failed Tablet",
    widthDp = 1280,
    heightDp = 800,
    showBackground = true
)
@Composable
fun previewConnectionErrorScreen() {
    HawkTheme {
        ConnectionErrorScreen({}, {})
    }
}


