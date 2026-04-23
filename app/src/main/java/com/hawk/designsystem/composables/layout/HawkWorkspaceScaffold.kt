package com.hawk.designsystem.composables.layout

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowSizeClass
import com.hawk.R
import com.hawk.home.theme.HawkBrandBlue
import com.hawk.home.theme.HawkDanger
import com.hawk.home.theme.HawkSidebarActive
import com.hawk.home.theme.HawkSidebarBackground
import com.hawk.home.theme.HawkSidebarText
import com.hawk.home.theme.HawkSurface
import com.hawk.home.theme.HawkText
import com.hawk.home.theme.HawkTextMuted
import com.hawk.home.theme.HawkTheme
import com.hawk.home.theme.HawkWorkspaceBackground

enum class HawkWorkspaceSection(
    @StringRes val labelRes: Int,
    val icon: HawkSidebarIconToken,
    val isDestructive: Boolean = false
) {
    Dashboard(R.string.nav_dashboard, HawkSidebarIconToken.Dashboard),
    Products(R.string.nav_products, HawkSidebarIconToken.Products),
    Orders(R.string.nav_orders, HawkSidebarIconToken.Orders),
    Customers(R.string.nav_customers, HawkSidebarIconToken.Customers),
    Sales(R.string.nav_sales, HawkSidebarIconToken.Sales),
    Cash(R.string.nav_cash, HawkSidebarIconToken.Cash),
    Staff(R.string.nav_staff, HawkSidebarIconToken.Staff),
    Settings(R.string.nav_settings, HawkSidebarIconToken.Settings),
    Logout(R.string.nav_log_out, HawkSidebarIconToken.Logout, isDestructive = true);

    companion object {
        val primaryEntries: List<HawkWorkspaceSection>
            get() = entries.filterNot(HawkWorkspaceSection::isDestructive)
    }
}

@Composable
fun HawkWorkspaceScaffold(
    title: String,
    subtitle: String,
    selectedSection: HawkWorkspaceSection,
    onSectionSelected: (HawkWorkspaceSection) -> Unit,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(horizontal = 24.dp, vertical = 24.dp),
    actionContent: @Composable RowScope.() -> Unit = {},
    content: @Composable ColumnScope.() -> Unit
) {
    val windowSizeClass = currentWindowAdaptiveInfo(
        supportLargeAndXLargeWidth = true
    ).windowSizeClass

    val useWideShell = windowSizeClass.isWidthAtLeastBreakpoint(
        WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND
    )

    Surface(
        modifier = modifier.fillMaxSize(),
        color = HawkWorkspaceBackground
    ) {
        if (useWideShell) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .safeDrawingPadding()
            ) {
                HawkSidebar(
                    selectedSection = selectedSection,
                    onSectionSelected = onSectionSelected,
                    modifier = Modifier
                        .width(243.dp)
                        .fillMaxHeight()
                )
                HawkWorkspaceContentPane(
                    title = title,
                    subtitle = subtitle,
                    contentPadding = contentPadding,
                    actionContent = actionContent,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    content = content
                )
            }
        } else {
            HawkWorkspaceContentPane(
                title = title,
                subtitle = subtitle,
                contentPadding = contentPadding,
                actionContent = actionContent,
                modifier = Modifier
                    .fillMaxSize()
                    .safeDrawingPadding(),
                content = content
            )
        }
    }
}

@Composable
private fun HawkWorkspaceContentPane(
    title: String,
    subtitle: String,
    contentPadding: PaddingValues,
    actionContent: @Composable RowScope.() -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Surface(
        modifier = modifier,
        color = HawkWorkspaceBackground
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.headlineMedium,
                        color = HawkText
                    )
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodyMedium,
                        color = HawkTextMuted
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    content = actionContent
                )
            }
            content()
        }
    }
}

@Composable
private fun HawkSidebar(
    selectedSection: HawkWorkspaceSection,
    onSectionSelected: (HawkWorkspaceSection) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(HawkSidebarBackground)
            .padding(horizontal = 16.dp, vertical = 24.dp)
    ) {
        HawkSidebarLogo()
        Spacer(modifier = Modifier.height(16.dp))
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            HawkWorkspaceSection.primaryEntries.forEach { section ->
                HawkSidebarButton(
                    section = section,
                    isSelected = section == selectedSection,
                    onClick = { onSectionSelected(section) }
                )
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        HawkSidebarButton(
            section = HawkWorkspaceSection.Logout,
            isSelected = false,
            onClick = { onSectionSelected(HawkWorkspaceSection.Logout) }
        )
    }
}

@Composable
private fun HawkSidebarLogo() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            modifier = Modifier.size(28.dp),
            shape = RoundedCornerShape(8.dp),
            color = HawkBrandBlue
        ) {}
        Text(
            text = "Hawk",
            style = MaterialTheme.typography.titleMedium,
            color = HawkSurface,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun HawkSidebarButton(
    section: HawkWorkspaceSection,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (isSelected && !section.isDestructive) {
        HawkSidebarActive
    } else {
        Color.Transparent
    }
    val contentColor = when {
        section.isDestructive -> HawkDanger
        isSelected -> HawkSurface
        else -> HawkSidebarText
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor)
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        HawkSidebarIcon(
            icon = section.icon,
            tint = contentColor,
            modifier = Modifier.size(18.dp)
        )
        Text(
            text = androidx.compose.ui.res.stringResource(section.labelRes),
            style = MaterialTheme.typography.bodyMedium,
            color = contentColor,
            fontWeight = if (isSelected && !section.isDestructive) {
                FontWeight.Medium
            } else {
                FontWeight.Normal
            }
        )
    }
}

@Preview(name = "Sidebar Products", widthDp = 243, heightDp = 800, showBackground = true)
@Composable
private fun PreviewHawkSidebarProducts() {
    HawkTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            HawkSidebar(
                selectedSection = HawkWorkspaceSection.Products,
                onSectionSelected = {},
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@Preview(name = "Sidebar Customers", widthDp = 243, heightDp = 800, showBackground = true)
@Composable
private fun PreviewHawkSidebarCustomers() {
    HawkTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            HawkSidebar(
                selectedSection = HawkWorkspaceSection.Customers,
                onSectionSelected = {},
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
