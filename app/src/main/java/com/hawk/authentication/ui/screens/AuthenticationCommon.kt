package com.hawk.authentication.ui.screens

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.window.core.layout.WindowSizeClass
import com.hawk.common.ui.KeyboardAwareScrollContainer
import com.hawk.home.theme.HawkBackground
import com.hawk.home.theme.HawkBorder
import com.hawk.home.theme.HawkPrimary
import com.hawk.home.theme.HawkShowcaseAccent
import com.hawk.home.theme.HawkShowcaseBottom
import com.hawk.home.theme.HawkShowcaseHighlight
import com.hawk.home.theme.HawkShowcaseTop
import com.hawk.home.theme.HawkSurface
import com.hawk.home.theme.HawkSurfaceMuted
import com.hawk.home.theme.HawkText
import com.hawk.home.theme.HawkTextMuted

@Composable
fun AuthenticationAdaptiveLayout(
    content: @Composable (Modifier) -> Unit
) {
    val windowSizeClass = currentWindowAdaptiveInfo(
        supportLargeAndXLargeWidth = true
    ).windowSizeClass

    val useTwoPaneLayout = windowSizeClass.isWidthAtLeastBreakpoint(
        WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND
    ) && windowSizeClass.isHeightAtLeastBreakpoint(
        WindowSizeClass.HEIGHT_DP_MEDIUM_LOWER_BOUND
    )

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = HawkBackground
    ) {
        if (useTwoPaneLayout) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .safeDrawingPadding()
                    .padding(20.dp)
                    .clip(RoundedCornerShape(32.dp))
                    .background(HawkSurface)
            ) {
                LoginShowcasePane(
                    modifier = Modifier
                        .weight(1.18f)
                        .fillMaxHeight()
                )
                Box(
                    modifier = Modifier
                        .weight(0.82f)
                        .fillMaxHeight()
                        .background(HawkSurface),
                    contentAlignment = Alignment.Center
                ) {
                    KeyboardAwareScrollContainer(
                        modifier = Modifier
                            .fillMaxHeight()
                            .padding(horizontal = 36.dp, vertical = 32.dp)
                            .widthIn(max = 420.dp),
                        scrollbarColor = HawkPrimary.copy(alpha = 0.9f),
                        scrollbarTrackColor = HawkBorder.copy(alpha = 0.55f)
                    ) { scrollModifier ->
                        content(scrollModifier)
                    }
                }
            }
        } else {
            KeyboardAwareScrollContainer(
                modifier = Modifier
                    .fillMaxSize()
                    .background(HawkBackground)
                    .safeDrawingPadding()
                    .padding(horizontal = 20.dp, vertical = 18.dp),
                scrollbarColor = HawkPrimary.copy(alpha = 0.9f),
                scrollbarTrackColor = HawkBorder.copy(alpha = 0.55f)
            ) { scrollModifier ->
                Column(
                    modifier = scrollModifier,
                    verticalArrangement = Arrangement.spacedBy(18.dp)
                ) {
                    LoginShowcaseHeader()
                    Surface(
                        shape = RoundedCornerShape(28.dp),
                        color = HawkSurface,
                        tonalElevation = 0.dp,
                        shadowElevation = 0.dp
                    ) {
                        content(
                            Modifier
                                .fillMaxWidth()
                                .padding(24.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AuthenticationFormScaffold(
    modifier: Modifier = Modifier,
    @StringRes titleRes: Int,
    @StringRes subtitleRes: Int,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                text = stringResource(titleRes),
                style = MaterialTheme.typography.headlineMedium,
                color = HawkText
            )
            Text(
                text = stringResource(subtitleRes),
                style = MaterialTheme.typography.bodyLarge,
                color = HawkTextMuted
            )
        }

        Spacer(modifier = Modifier.height(4.dp))
        content()
    }
}

@Composable
fun AuthTextField(
    value: String,
    onValueChange: (String) -> Unit,
    @StringRes labelRes: Int,
    @StringRes placeholderRes: Int,
    @StringRes errorRes: Int?,
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Next
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth(),
        textStyle = MaterialTheme.typography.bodyLarge,
        label = { Text(text = stringResource(labelRes)) },
        placeholder = { Text(text = stringResource(placeholderRes)) },
        isError = errorRes != null,
        singleLine = true,
        shape = RoundedCornerShape(18.dp),
        keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
            keyboardType = keyboardType,
            imeAction = imeAction
        ),
        supportingText = {
            errorRes?.let { errorMessageRes ->
                Text(
                    text = stringResource(errorMessageRes),
                    color = MaterialTheme.colorScheme.error
                )
            }
        },
        colors = authTextFieldColors()
    )
}

@Composable
fun AuthPasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    @StringRes labelRes: Int,
    @StringRes placeholderRes: Int,
    @StringRes errorRes: Int?,
    modifier: Modifier = Modifier,
    imeAction: ImeAction = ImeAction.Done
) {
    var isPasswordVisible by rememberSaveable { mutableStateOf(false) }
    val toggleIconRes = if (isPasswordVisible) {
        com.hawk.R.drawable.ic_visibility_off
    } else {
        com.hawk.R.drawable.ic_visibility
    }
    val toggleContentDescriptionRes = if (isPasswordVisible) {
        com.hawk.R.string.auth_hide_password
    } else {
        com.hawk.R.string.auth_show_password
    }

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.fillMaxWidth(),
        textStyle = MaterialTheme.typography.bodyLarge,
        label = { Text(text = stringResource(labelRes)) },
        placeholder = { Text(text = stringResource(placeholderRes)) },
        isError = errorRes != null,
        singleLine = true,
        shape = RoundedCornerShape(18.dp),
        visualTransformation = if (isPasswordVisible) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        },
        keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = imeAction
        ),
        trailingIcon = {
            IconButton(
                onClick = { isPasswordVisible = !isPasswordVisible }
            ) {
                Icon(
                    painter = painterResource(toggleIconRes),
                    contentDescription = stringResource(toggleContentDescriptionRes),
                    tint = HawkTextMuted
                )
            }
        },
        supportingText = {
            errorRes?.let { errorMessageRes ->
                Text(
                    text = stringResource(errorMessageRes),
                    color = MaterialTheme.colorScheme.error
                )
            }
        },
        colors = authTextFieldColors()
    )
}

@Composable
fun AuthPrimaryButton(
    @StringRes textRes: Int,
    onClick: () -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(20.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = HawkPrimary,
            contentColor = HawkSurface,
            disabledContainerColor = HawkSurfaceMuted,
            disabledContentColor = HawkTextMuted
        ),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp),
        enabled = enabled
    ) {
        Text(
            text = stringResource(textRes),
            style = MaterialTheme.typography.labelLarge
        )
    }
}

@Composable
fun AuthInlineTextButton(
    @StringRes textRes: Int,
    onClick: () -> Unit
) {
    TextButton(onClick = onClick) {
        Text(
            text = stringResource(textRes),
            style = MaterialTheme.typography.labelMedium,
            color = HawkPrimary
        )
    }
}

@Composable
fun AuthCenteredTextButton(
    @StringRes textRes: Int,
    onClick: () -> Unit
) {
    TextButton(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(textRes),
            style = MaterialTheme.typography.labelMedium,
            color = HawkPrimary
        )
    }
}

@Composable
fun AuthBanner(
    @StringRes messageRes: Int,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = MaterialTheme.colorScheme.error.copy(alpha = 0.08f)
    ) {
        Text(
            text = stringResource(messageRes),
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.error
        )
    }
}

@Composable
fun AuthStatusIcon(
    symbol: String,
    containerColor: Color,
    contentColor: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.size(64.dp),
        shape = RoundedCornerShape(18.dp),
        color = containerColor
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = symbol,
                style = MaterialTheme.typography.headlineMedium,
                color = contentColor
            )
        }
    }
}

@Composable
private fun LoginShowcasePane(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.background(
            brush = Brush.linearGradient(
                colors = listOf(HawkShowcaseHighlight, HawkShowcaseTop, HawkShowcaseBottom)
            )
        )
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 40.dp, end = 56.dp)
                .size(240.dp)
                .clip(CircleShape)
                .background(HawkShowcaseAccent.copy(alpha = 0.48f))
        )
        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 48.dp, bottom = 64.dp)
                .size(320.dp)
                .clip(CircleShape)
                .background(HawkShowcaseAccent.copy(alpha = 0.26f))
        )
        Column(
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(horizontal = 56.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            Surface(
                shape = CircleShape,
                color = HawkSurface.copy(alpha = 0.82f)
            ) {
                Text(
                    text = stringResource(com.hawk.R.string.auth_showcase_badge),
                    modifier = Modifier.padding(horizontal = 18.dp, vertical = 10.dp),
                    style = MaterialTheme.typography.labelMedium,
                    color = HawkPrimary
                )
            }
            Text(
                text = "HAWK",
                style = MaterialTheme.typography.labelLarge,
                color = HawkPrimary,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = stringResource(com.hawk.R.string.auth_showcase_title),
                style = MaterialTheme.typography.headlineLarge,
                color = HawkText
            )
            Text(
                text = stringResource(com.hawk.R.string.auth_showcase_subtitle),
                style = MaterialTheme.typography.bodyLarge,
                color = HawkTextMuted,
                modifier = Modifier.widthIn(max = 420.dp)
            )
        }
    }
}

@Composable
private fun LoginShowcaseHeader() {
    Surface(
        shape = RoundedCornerShape(28.dp),
        color = Color.Transparent
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(28.dp))
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(HawkShowcaseHighlight, HawkShowcaseTop, HawkShowcaseBottom)
                    )
                )
                .padding(24.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    text = "HAWK",
                    style = MaterialTheme.typography.labelLarge,
                    color = HawkPrimary,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = stringResource(com.hawk.R.string.auth_showcase_title),
                    style = MaterialTheme.typography.headlineMedium,
                    color = HawkText
                )
                Text(
                    text = stringResource(com.hawk.R.string.auth_showcase_subtitle),
                    style = MaterialTheme.typography.bodyMedium,
                    color = HawkTextMuted
                )
            }
        }
    }
}

@Composable
private fun authTextFieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = HawkPrimary,
    unfocusedBorderColor = HawkBorder,
    focusedLabelColor = HawkPrimary,
    unfocusedLabelColor = HawkTextMuted,
    focusedTextColor = HawkText,
    unfocusedTextColor = HawkText,
    focusedContainerColor = HawkSurface,
    unfocusedContainerColor = HawkSurface,
    cursorColor = HawkPrimary,
    focusedPlaceholderColor = HawkTextMuted,
    unfocusedPlaceholderColor = HawkTextMuted,
    focusedSupportingTextColor = MaterialTheme.colorScheme.error,
    unfocusedSupportingTextColor = MaterialTheme.colorScheme.error
)
