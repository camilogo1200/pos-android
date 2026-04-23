package com.hawk.customers.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.hawk.R
import com.hawk.customers.ui.uimodels.CustomerBadgeTone
import com.hawk.customers.ui.uimodels.CustomerFormFieldUiModel
import com.hawk.customers.ui.uimodels.CustomerHealthTone
import com.hawk.customers.ui.uimodels.CustomerListItemUiModel
import com.hawk.customers.domain.entities.CustomerType
import com.hawk.customers.ui.viewmodels.CreateCustomerViewModel
import com.hawk.customers.ui.viewmodels.CustomersViewModel
import com.hawk.customers.ui.viewstates.CreateCustomerStep
import com.hawk.customers.ui.viewstates.CreateCustomerViewState
import com.hawk.customers.ui.viewstates.CustomerTypeFilter
import com.hawk.customers.ui.viewstates.CustomerWorkspaceTab
import com.hawk.customers.ui.viewstates.CustomersViewState
import com.hawk.designsystem.composables.layout.HawkWorkspaceScaffold
import com.hawk.designsystem.composables.layout.HawkWorkspaceSection
import com.hawk.home.theme.HawkBrandBlue
import com.hawk.home.theme.HawkTheme
import java.text.NumberFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

private val CustomersPageBackground = Color(0xFFF3F3F3)
private val CustomersCardBackground = Color(0xFFFFFFFF)
private val CustomersBorder = Color(0xFFE5E7EB)
private val CustomersMutedText = Color(0xFF6B7280)
private val CustomersPrimaryText = Color(0xFF111827)
private val CustomersDanger = Color(0xFFEF4444)
private val CustomersSuccess = Color(0xFF22C55E)
private val CustomersWarning = Color(0xFFF59E0B)
private val CustomersSidebarShadow = Color(0x14000000)

private data class CustomerOption(
    val code: String,
    val label: String
)

private val nationalityOptions = listOf(
    CustomerOption(code = "CO", label = "Colombia"),
    CustomerOption(code = "MX", label = "Mexico"),
    CustomerOption(code = "AR", label = "Argentina"),
    CustomerOption(code = "CL", label = "Chile")
)

private val languageOptions = listOf(
    CustomerOption(code = "es-CO", label = "Spanish"),
    CustomerOption(code = "en-US", label = "English")
)

private val genderOptions = listOf(
    CustomerOption(code = "MALE", label = "Male"),
    CustomerOption(code = "FEMALE", label = "Female"),
    CustomerOption(code = "OTHER", label = "Other"),
    CustomerOption(code = "PREFER_NOT_TO_SAY", label = "Prefer not to say")
)

private val stateOptions = listOf(
    CustomerOption(code = "CUNDINAMARCA", label = "Cundinamarca"),
    CustomerOption(code = "ANTIOQUIA", label = "Antioquia"),
    CustomerOption(code = "VALLE_DEL_CAUCA", label = "Valle del Cauca"),
    CustomerOption(code = "SANTANDER", label = "Santander")
)

private val timeZoneOptions = listOf(
    CustomerOption(code = "America/Bogota", label = "America/Bogota"),
    CustomerOption(code = "America/Mexico_City", label = "America/Mexico_City"),
    CustomerOption(code = "America/Santiago", label = "America/Santiago")
)

private val preferredContactMethodOptions = listOf(
    CustomerOption(code = "EMAIL", label = "Email"),
    CustomerOption(code = "CALL", label = "Call"),
    CustomerOption(code = "WHATSAPP", label = "WhatsApp")
)

private val preferredContactTimeOptions = listOf(
    CustomerOption(code = "08:00|12:00", label = "Morning (8 AM - 12 PM)"),
    CustomerOption(code = "12:00|18:00", label = "Afternoon (12 PM - 6 PM)"),
    CustomerOption(code = "18:00|21:00", label = "Evening (6 PM - 9 PM)")
)

private val customerGroupOptions = listOf(
    CustomerOption(code = "RETAIL", label = "Retail"),
    CustomerOption(code = "VIP", label = "VIP"),
    CustomerOption(code = "B2B", label = "B2B"),
    CustomerOption(code = "DISTRIBUTOR", label = "Distributor"),
    CustomerOption(code = "WHOLESALE", label = "Wholesale")
)

private val acquisitionSourceOptions = listOf(
    CustomerOption(code = "POS", label = "POS"),
    CustomerOption(code = "ADMIN", label = "Admin"),
    CustomerOption(code = "API", label = "API"),
    CustomerOption(code = "IMPORT", label = "Import")
)

@Composable
fun CustomersRoute(
    onCreateCustomerClicked: () -> Unit,
    onWorkspaceSectionSelected: (HawkWorkspaceSection) -> Unit = {},
    viewModel: CustomersViewModel = hiltViewModel()
) {
    val viewState by viewModel.viewState.collectAsStateWithLifecycle()

    CustomersScreen(
        viewState = viewState,
        onWorkspaceTabSelected = viewModel::onWorkspaceTabSelected,
        onQueryChanged = viewModel::onQueryChanged,
        onFilterSelected = viewModel::onFilterSelected,
        onCreateCustomerClicked = onCreateCustomerClicked,
        onRetryRequested = viewModel::onRetryRequested,
        onWorkspaceSectionSelected = onWorkspaceSectionSelected
    )
}

@Composable
fun CreateCustomerRoute(
    onBackToCustomers: () -> Unit,
    onWorkspaceSectionSelected: (HawkWorkspaceSection) -> Unit = {},
    viewModel: CreateCustomerViewModel = hiltViewModel()
) {
    val viewState by viewModel.viewState.collectAsStateWithLifecycle()

    CreateCustomerScreen(
        viewState = viewState,
        onWorkspaceSectionSelected = onWorkspaceSectionSelected,
        onBackToCustomers = onBackToCustomers,
        onCancelClicked = {
            viewModel.resetForm()
            onBackToCustomers()
        },
        onSaveCustomerClicked = viewModel::onSaveCustomerClicked,
        onNextClicked = viewModel::onNextClicked,
        onPreviousClicked = viewModel::onPreviousClicked,
        onFirstNameChanged = viewModel::onFirstNameChanged,
        onLastNameChanged = viewModel::onLastNameChanged,
        onBirthDateChanged = viewModel::onBirthDateChanged,
        onDocumentNumberChanged = viewModel::onDocumentNumberChanged,
        onNationalityChanged = viewModel::onNationalityChanged,
        onGenderChanged = viewModel::onGenderChanged,
        onPreferredLanguageChanged = viewModel::onPreferredLanguageChanged,
        onEmailAddressChanged = viewModel::onEmailAddressChanged,
        onPhoneNumberChanged = viewModel::onPhoneNumberChanged,
        onWhatsAppNumberChanged = viewModel::onWhatsAppNumberChanged,
        onCountryChanged = viewModel::onCountryChanged,
        onStateChanged = viewModel::onStateChanged,
        onCityChanged = viewModel::onCityChanged,
        onAddressLineOneChanged = viewModel::onAddressLineOneChanged,
        onAddressLineTwoChanged = viewModel::onAddressLineTwoChanged,
        onPostalCodeChanged = viewModel::onPostalCodeChanged,
        onTimeZoneChanged = viewModel::onTimeZoneChanged,
        onPreferredContactChannelChanged = viewModel::onPreferredContactChannelChanged,
        onPreferredContactTimeChanged = viewModel::onPreferredContactTimeChanged,
        onNewsletterOptInChanged = viewModel::onNewsletterOptInChanged,
        onCustomerGroupChanged = viewModel::onCustomerGroupChanged,
        onAcquisitionSourceChanged = viewModel::onAcquisitionSourceChanged,
        onPublicNoteChanged = viewModel::onPublicNoteChanged,
        onInternalNoteChanged = viewModel::onInternalNoteChanged,
        onAddInternalTag = viewModel::onAddInternalTag,
        onRemoveInternalTag = viewModel::onRemoveInternalTag
    )
}

@Composable
fun CustomersScreen(
    viewState: CustomersViewState,
    onWorkspaceTabSelected: (CustomerWorkspaceTab) -> Unit,
    onQueryChanged: (String) -> Unit,
    onFilterSelected: (CustomerTypeFilter) -> Unit,
    onCreateCustomerClicked: () -> Unit,
    onRetryRequested: () -> Unit,
    onWorkspaceSectionSelected: (HawkWorkspaceSection) -> Unit
) {
    HawkWorkspaceScaffold(
        title = "Customers",
        subtitle = customersDateLabel(),
        selectedSection = HawkWorkspaceSection.Customers,
        onSectionSelected = onWorkspaceSectionSelected,
        contentPadding = PaddingValues(horizontal = 28.dp, vertical = 24.dp)
    ) {
        CustomersTabs(
            selectedTab = viewState.selectedTab,
            onTabSelected = onWorkspaceTabSelected
        )

        when {
            viewState.selectedTab != CustomerWorkspaceTab.CONTACTS -> {
                CustomersStatusCard(
                    title = when (viewState.selectedTab) {
                        CustomerWorkspaceTab.PIPELINE -> stringResourceSafe(R.string.customers_pipeline_placeholder_title)
                        CustomerWorkspaceTab.ACTIVITIES -> stringResourceSafe(R.string.customers_activities_placeholder_title)
                        CustomerWorkspaceTab.WORKFLOWS -> stringResourceSafe(R.string.customers_workflows_placeholder_title)
                        CustomerWorkspaceTab.CONTACTS -> ""
                    },
                    body = when (viewState.selectedTab) {
                        CustomerWorkspaceTab.PIPELINE -> stringResourceSafe(R.string.customers_pipeline_placeholder_body)
                        CustomerWorkspaceTab.ACTIVITIES -> stringResourceSafe(R.string.customers_activities_placeholder_body)
                        CustomerWorkspaceTab.WORKFLOWS -> stringResourceSafe(R.string.customers_workflows_placeholder_body)
                        CustomerWorkspaceTab.CONTACTS -> ""
                    },
                    actionLabel = null,
                    onActionClick = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
            }

            viewState.isLoading -> {
                CustomersLoadingCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
            }

            viewState.statusTitleRes != null && viewState.statusBodyRes != null -> {
                CustomersStatusCard(
                    title = stringResourceSafe(viewState.statusTitleRes),
                    body = stringResourceSafe(viewState.statusBodyRes),
                    actionLabel = stringResourceSafe(R.string.customers_retry_button),
                    onActionClick = onRetryRequested,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
            }

            else -> {
                Column(
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    renderSummaryCards(viewState)
                    Spacer(Modifier.size(16.dp))
                    renderFilterbar(
                        viewState,
                        onQueryChanged,
                        onFilterSelected,
                        onCreateCustomerClicked
                    )
                    Spacer(Modifier.size(16.dp))
                    renderCustomersTable(
                        viewState,
                        onCreateCustomerClicked,
                        onQueryChanged,
                        onFilterSelected
                    )
                }
            }
        }
    }
}

@Composable
private fun ColumnScope.renderCustomersTable(
    viewState: CustomersViewState,
    onCreateCustomerClicked: () -> Unit,
    onQueryChanged: (String) -> Unit,
    onFilterSelected: (CustomerTypeFilter) -> Unit
) {
    when {
        viewState.customers.isEmpty() && viewState.query.isBlank() &&
                viewState.selectedFilter == CustomerTypeFilter.ALL -> {
            CustomersStatusCard(
                title = stringResourceSafe(R.string.customers_empty_title),
                body = stringResourceSafe(R.string.customers_empty_body),
                actionLabel = stringResourceSafe(R.string.customers_add_button),
                onActionClick = onCreateCustomerClicked,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
        }

        viewState.customers.isEmpty() -> {
            CustomersStatusCard(
                title = stringResourceSafe(R.string.customers_no_results_title),
                body = stringResourceSafe(R.string.customers_no_results_body),
                actionLabel = stringResourceSafe(R.string.customers_clear_filters_button),
                onActionClick = {
                    onQueryChanged("")
                    onFilterSelected(CustomerTypeFilter.ALL)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
        }

        else -> {
            CustomersTableCard(
                customers = viewState.customers,
                loadedCount = viewState.loadedCount,
                hasMoreResults = viewState.hasMoreResults,
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
            )
        }
    }
}

@Composable
private fun renderFilterbar(
    viewState: CustomersViewState,
    onQueryChanged: (String) -> Unit,
    onFilterSelected: (CustomerTypeFilter) -> Unit,
    onCreateCustomerClicked: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier
                .weight(8f),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CustomerSearchField(
                query = viewState.query,
                onQueryChanged = onQueryChanged,
                modifier = Modifier.fillMaxWidth(0.65f)
            )
            CustomerTypeDropdown(
                selectedFilter = viewState.selectedFilter,
                onFilterSelected = onFilterSelected,
                modifier = Modifier
                    .widthIn(min = 164.dp)
                    .fillMaxWidth(0.45f)
            )
        }
        Button(
            onClick = onCreateCustomerClicked,
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = CustomersPrimaryText,
                contentColor = Color.White
            ),
            modifier = Modifier.weight(2f)
        ) {
            Text(text = stringResourceSafe(R.string.customers_add_button))
        }
    }
}

@Composable
private fun renderSummaryCards(viewState: CustomersViewState) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        SummaryCard(
            title = stringResourceSafe(R.string.customers_total_summary_title),
            value = formatCount(viewState.totalCustomers),
            body = stringResourceSafe(R.string.customers_total_summary_body)
        )
        SummaryCard(
            title = stringResourceSafe(R.string.customers_b2b_summary_title),
            value = formatCount(viewState.businessAccounts),
            body = stringResourceSafe(R.string.customers_b2b_summary_body)
        )
        SummaryCard(
            title = stringResourceSafe(R.string.customers_risk_summary_title),
            value = formatCount(viewState.atRiskCustomers),
            body = stringResourceSafe(R.string.customers_risk_summary_body),
            valueColor = CustomersDanger
        )
    }
}

@Composable
fun CreateCustomerScreen(
    viewState: CreateCustomerViewState,
    onWorkspaceSectionSelected: (HawkWorkspaceSection) -> Unit,
    onBackToCustomers: () -> Unit,
    onCancelClicked: () -> Unit,
    onSaveCustomerClicked: () -> Unit,
    onNextClicked: () -> Unit,
    onPreviousClicked: () -> Unit,
    onFirstNameChanged: (String) -> Unit,
    onLastNameChanged: (String) -> Unit,
    onBirthDateChanged: (String) -> Unit,
    onDocumentNumberChanged: (String) -> Unit,
    onNationalityChanged: (String) -> Unit,
    onGenderChanged: (String) -> Unit,
    onPreferredLanguageChanged: (String) -> Unit,
    onEmailAddressChanged: (String) -> Unit,
    onPhoneNumberChanged: (String) -> Unit,
    onWhatsAppNumberChanged: (String) -> Unit,
    onCountryChanged: (String) -> Unit,
    onStateChanged: (String) -> Unit,
    onCityChanged: (String) -> Unit,
    onAddressLineOneChanged: (String) -> Unit,
    onAddressLineTwoChanged: (String) -> Unit,
    onPostalCodeChanged: (String) -> Unit,
    onTimeZoneChanged: (String) -> Unit,
    onPreferredContactChannelChanged: (String) -> Unit,
    onPreferredContactTimeChanged: (String) -> Unit,
    onNewsletterOptInChanged: (Boolean) -> Unit,
    onCustomerGroupChanged: (String) -> Unit,
    onAcquisitionSourceChanged: (String) -> Unit,
    onPublicNoteChanged: (String) -> Unit,
    onInternalNoteChanged: (String) -> Unit,
    onAddInternalTag: (String) -> Unit,
    onRemoveInternalTag: (String) -> Unit
) {
    HawkWorkspaceScaffold(
        title = "Customers",
        subtitle = customersDateLabel(),
        selectedSection = HawkWorkspaceSection.Customers,
        onSectionSelected = onWorkspaceSectionSelected,
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 24.dp)
    ) {
        when (viewState) {
            is CreateCustomerViewState.Form -> {
                CreateCustomerFormCard(
                    viewState = viewState,
                    onCancelClicked = onCancelClicked,
                    onSaveCustomerClicked = onSaveCustomerClicked,
                    onNextClicked = onNextClicked,
                    onPreviousClicked = onPreviousClicked,
                    onFirstNameChanged = onFirstNameChanged,
                    onLastNameChanged = onLastNameChanged,
                    onBirthDateChanged = onBirthDateChanged,
                    onDocumentNumberChanged = onDocumentNumberChanged,
                    onNationalityChanged = onNationalityChanged,
                    onGenderChanged = onGenderChanged,
                    onPreferredLanguageChanged = onPreferredLanguageChanged,
                    onEmailAddressChanged = onEmailAddressChanged,
                    onPhoneNumberChanged = onPhoneNumberChanged,
                    onWhatsAppNumberChanged = onWhatsAppNumberChanged,
                    onCountryChanged = onCountryChanged,
                    onStateChanged = onStateChanged,
                    onCityChanged = onCityChanged,
                    onAddressLineOneChanged = onAddressLineOneChanged,
                    onAddressLineTwoChanged = onAddressLineTwoChanged,
                    onPostalCodeChanged = onPostalCodeChanged,
                    onTimeZoneChanged = onTimeZoneChanged,
                    onPreferredContactChannelChanged = onPreferredContactChannelChanged,
                    onPreferredContactTimeChanged = onPreferredContactTimeChanged,
                    onNewsletterOptInChanged = onNewsletterOptInChanged,
                    onCustomerGroupChanged = onCustomerGroupChanged,
                    onAcquisitionSourceChanged = onAcquisitionSourceChanged,
                    onPublicNoteChanged = onPublicNoteChanged,
                    onInternalNoteChanged = onInternalNoteChanged,
                    onAddInternalTag = onAddInternalTag,
                    onRemoveInternalTag = onRemoveInternalTag,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
            }

            is CreateCustomerViewState.SubmissionResult -> {
                CustomersStatusCard(
                    title = stringResourceSafe(viewState.titleRes),
                    body = stringResourceSafe(viewState.messageRes),
                    actionLabel = stringResourceSafe(R.string.customers_back_to_list_button),
                    onActionClick = onBackToCustomers,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
            }
        }
    }
}

@Composable
private fun CustomersTabs(
    selectedTab: CustomerWorkspaceTab,
    onTabSelected: (CustomerWorkspaceTab) -> Unit
) {
    TabRow(
        selectedTabIndex = CustomerWorkspaceTab.entries.indexOf(selectedTab),
        containerColor = Color.Transparent,
        contentColor = HawkBrandBlue,
        divider = {}
    ) {
        CustomerWorkspaceTab.entries.forEach { tab ->
            Tab(
                selected = tab == selectedTab,
                onClick = { onTabSelected(tab) },
                text = {
                    Text(
                        text = when (tab) {
                            CustomerWorkspaceTab.CONTACTS -> stringResourceSafe(R.string.customers_tab_contacts)
                            CustomerWorkspaceTab.PIPELINE -> stringResourceSafe(R.string.customers_tab_pipeline)
                            CustomerWorkspaceTab.ACTIVITIES -> stringResourceSafe(R.string.customers_tab_activities)
                            CustomerWorkspaceTab.WORKFLOWS -> stringResourceSafe(R.string.customers_tab_workflows)
                        }
                    )
                }
            )
        }
    }
}

@Composable
private fun SummaryCard(
    title: String,
    value: String,
    body: String,
    valueColor: Color = CustomersPrimaryText
) {
    Card(
        modifier = Modifier
            .widthIn(min = 220.dp)
            .heightIn(min = 148.dp),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = CustomersCardBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier
                .wrapContentSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelLarge,
                color = CustomersMutedText
            )
            Text(
                text = value,
                style = MaterialTheme.typography.headlineLarge,
                color = valueColor,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = body,
                style = MaterialTheme.typography.bodyMedium,
                color = CustomersMutedText
            )
        }
    }
}

@Composable
private fun CustomerSearchField(
    query: String,
    onQueryChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChanged,
        modifier = modifier,
        placeholder = {
            Text(text = stringResourceSafe(R.string.customers_search_placeholder))
        },
        singleLine = true,
        shape = RoundedCornerShape(14.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = CustomersCardBackground,
            unfocusedContainerColor = CustomersCardBackground,
            focusedBorderColor = HawkBrandBlue,
            unfocusedBorderColor = CustomersBorder,
            cursorColor = HawkBrandBlue
        )
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun CustomerTypeDropdown(
    selectedFilter: CustomerTypeFilter,
    onFilterSelected: (CustomerTypeFilter) -> Unit,
    modifier: Modifier = Modifier
) {
    var isExpanded by rememberSaveable { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = isExpanded,
        onExpandedChange = { isExpanded = !isExpanded },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = when (selectedFilter) {
                CustomerTypeFilter.ALL -> stringResourceSafe(R.string.customers_filter_all)
                CustomerTypeFilter.PERSON -> stringResourceSafe(R.string.customers_filter_person)
                CustomerTypeFilter.BUSINESS -> stringResourceSafe(R.string.customers_filter_business)
            },
            onValueChange = {},
            readOnly = true,
            singleLine = true,
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = CustomersCardBackground,
                unfocusedContainerColor = CustomersCardBackground,
                focusedBorderColor = CustomersBorder,
                unfocusedBorderColor = CustomersBorder
            ),
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
            },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false }
        ) {
            CustomerTypeFilter.entries.forEach { filter ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = when (filter) {
                                CustomerTypeFilter.ALL -> stringResourceSafe(R.string.customers_filter_all)
                                CustomerTypeFilter.PERSON -> stringResourceSafe(R.string.customers_filter_person)
                                CustomerTypeFilter.BUSINESS -> stringResourceSafe(R.string.customers_filter_business)
                            }
                        )
                    },
                    onClick = {
                        isExpanded = false
                        onFilterSelected(filter)
                    }
                )
            }
        }
    }
}

@Composable
private fun CustomersLoadingCard(
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = CustomersCardBackground)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                CircularProgressIndicator(color = HawkBrandBlue)
                Text(
                    text = stringResourceSafe(R.string.customers_loading_label),
                    style = MaterialTheme.typography.bodyLarge,
                    color = CustomersMutedText
                )
            }
        }
    }
}

@Composable
private fun CustomersStatusCard(
    title: String,
    body: String,
    actionLabel: String?,
    onActionClick: (() -> Unit)?,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = CustomersCardBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Surface(
                modifier = Modifier.size(84.dp),
                shape = CircleShape,
                color = CustomersPageBackground
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = "C",
                        style = MaterialTheme.typography.headlineMedium,
                        color = CustomersMutedText,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            Text(
                text = title,
                modifier = Modifier.padding(top = 20.dp),
                style = MaterialTheme.typography.titleMedium,
                color = CustomersPrimaryText
            )
            Text(
                text = body,
                modifier = Modifier.padding(top = 8.dp),
                style = MaterialTheme.typography.bodyMedium,
                color = CustomersMutedText
            )
            if (actionLabel != null && onActionClick != null) {
                Button(
                    onClick = onActionClick,
                    modifier = Modifier.padding(top = 24.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CustomersPrimaryText,
                        contentColor = Color.White
                    )
                ) {
                    Text(text = actionLabel)
                }
            }
        }
    }
}

@Composable
private fun CustomersTableCard(
    customers: List<CustomerListItemUiModel>,
    loadedCount: Int,
    hasMoreResults: Boolean,
    modifier: Modifier = Modifier
) {
    val horizontalScrollState = rememberScrollState()

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = CustomersCardBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .horizontalScroll(horizontalScrollState)
                    .width(1200.dp)
                    .wrapContentSize()
            ) {
                CustomerTableHeader()
                LazyColumn(
                    modifier = Modifier
                        .width(1000.dp)
                        .heightIn(min = 320.dp),
                    verticalArrangement = Arrangement.spacedBy(0.dp)
                ) {
                    items(customers, key = { it.customerId }) { customer ->
                        CustomerTableRow(customer = customer)
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = stringResourceSafe(
                            R.string.customers_table_footer_loaded,
                            loadedCount.toString()
                        ),
                        style = MaterialTheme.typography.bodyMedium,
                        color = CustomersMutedText
                    )
                    if (hasMoreResults) {
                        Text(
                            text = stringResourceSafe(R.string.customers_table_footer_more_results),
                            style = MaterialTheme.typography.labelMedium,
                            color = HawkBrandBlue
                        )
                    }
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    PaginationBadge(label = "1", isSelected = true)
                    PaginationBadge(label = "2")
                    PaginationBadge(label = "3")
                    PaginationBadge(label = "...")
                }
            }
        }
    }
}

@Composable
private fun CustomerTableHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 56.dp)
            .background(Color(0xFFFAFAFA))
            .padding(horizontal = 20.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CustomerTableCell("NAME", 2.0f, CustomersMutedText, FontWeight.Medium)
        CustomerTableCell("COMPANY", 1.4f, CustomersMutedText, FontWeight.Medium)
        CustomerTableCell("JOB TITLE", 1.3f, CustomersMutedText, FontWeight.Medium)
        CustomerTableCell("CITY", 0.9f, CustomersMutedText, FontWeight.Medium)
        CustomerTableCell("ACQ. CHANNEL", 1.1f, CustomersMutedText, FontWeight.Medium)
        CustomerTableCell("PREF. CHANN", 1.0f, CustomersMutedText, FontWeight.Medium)
        CustomerTableCell("TYPE", 0.8f, CustomersMutedText, FontWeight.Medium)
        CustomerTableCell("HEALTH", 1.1f, CustomersMutedText, FontWeight.Medium)
        CustomerTableCell("LAST ACTIVITY", 1.0f, CustomersMutedText, FontWeight.Medium)
        CustomerTableCell("", 0.2f, CustomersMutedText, FontWeight.Medium)
    }
}

@Composable
private fun CustomerTableRow(customer: CustomerListItemUiModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 20.dp, vertical = 18.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(2.0f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AvatarCircle(customer = customer)
            Text(
                text = customer.displayName,
                style = MaterialTheme.typography.bodyLarge,
                color = CustomersPrimaryText,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        CustomerTableCell(customer.companyLabel, 1.4f)
        CustomerTableCell(customer.roleLabel, 1.3f)
        CustomerTableCell(customer.cityLabel, 0.9f)
        CustomerTableBadgeCell(
            label = customer.acquisitionChannelLabel,
            tone = customer.acquisitionChannelTone,
            weight = 1.1f
        )
        CustomerTableCell(customer.preferredChannelLabel, 1.0f)
        CustomerTableBadgeCell(
            label = customer.typeLabel,
            tone = customer.typeTone,
            weight = 0.8f
        )
        CustomerHealthCell(customer = customer, weight = 1.1f)
        CustomerTableCell(customer.lastActivityLabel, 1.0f)
        CustomerTableCell("...", 0.2f)
    }
}

@Composable
private fun RowScope.CustomerTableCell(
    text: String,
    weight: Float,
    color: Color = CustomersPrimaryText,
    fontWeight: FontWeight = FontWeight.Normal
) {
    Text(
        text = text,
        modifier = Modifier.weight(weight),
        style = MaterialTheme.typography.bodyMedium,
        color = color,
        fontWeight = fontWeight,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
private fun RowScope.CustomerTableBadgeCell(
    label: String,
    tone: CustomerBadgeTone,
    weight: Float
) {
    Row(
        modifier = Modifier.weight(weight),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ToneBadge(label = label, tone = tone)
    }
}

@Composable
private fun RowScope.CustomerHealthCell(
    customer: CustomerListItemUiModel,
    weight: Float
) {
    Row(
        modifier = Modifier.weight(weight),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .background(
                    color = when (customer.healthTone) {
                        CustomerHealthTone.HEALTHY -> CustomersSuccess
                        CustomerHealthTone.AT_RISK -> CustomersWarning
                        CustomerHealthTone.COLD -> CustomersDanger
                    },
                    shape = CircleShape
                )
        )
        Text(
            text = "${customer.healthScoreLabel} / ${customer.healthStatusLabel}",
            style = MaterialTheme.typography.bodyMedium,
            color = CustomersMutedText,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun ToneBadge(
    label: String,
    tone: CustomerBadgeTone
) {
    val backgroundColor = when (tone) {
        CustomerBadgeTone.BLUE -> Color(0xFFE8F0FF)
        CustomerBadgeTone.GREEN -> Color(0xFFE7F8EF)
        CustomerBadgeTone.ORANGE -> Color(0xFFFFF1E8)
        CustomerBadgeTone.PURPLE -> Color(0xFFF1EBFF)
        CustomerBadgeTone.RED -> Color(0xFFFFE8E8)
        CustomerBadgeTone.NEUTRAL -> Color(0xFFF3F4F6)
    }
    val contentColor = when (tone) {
        CustomerBadgeTone.BLUE -> Color(0xFF2563EB)
        CustomerBadgeTone.GREEN -> Color(0xFF059669)
        CustomerBadgeTone.ORANGE -> Color(0xFFEA580C)
        CustomerBadgeTone.PURPLE -> Color(0xFF7C3AED)
        CustomerBadgeTone.RED -> Color(0xFFDC2626)
        CustomerBadgeTone.NEUTRAL -> CustomersMutedText
    }

    Surface(
        shape = RoundedCornerShape(999.dp),
        color = backgroundColor
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            style = MaterialTheme.typography.labelMedium,
            color = contentColor,
            maxLines = 1
        )
    }
}

@Composable
private fun AvatarCircle(customer: CustomerListItemUiModel) {
    val avatarBackground = remember(customer.customerId) {
        when (customer.customerId.last().code % 4) {
            0 -> Color(0xFFE9F5FF)
            1 -> Color(0xFFFFF2E6)
            2 -> Color(0xFFEAF8ED)
            else -> Color(0xFFF4ECFF)
        }
    }

    Surface(
        modifier = Modifier.size(44.dp),
        shape = CircleShape,
        color = avatarBackground,
        shadowElevation = 1.dp
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = customer.avatarLabel,
                style = MaterialTheme.typography.labelLarge,
                color = CustomersPrimaryText,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun PaginationBadge(
    label: String,
    isSelected: Boolean = false
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = if (isSelected) CustomersPrimaryText else Color.White,
        border = androidx.compose.foundation.BorderStroke(
            width = 1.dp,
            color = if (isSelected) CustomersPrimaryText else CustomersBorder
        )
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
            style = MaterialTheme.typography.labelMedium,
            color = if (isSelected) Color.White else CustomersMutedText
        )
    }
}

@Composable
private fun CreateCustomerFormCard(
    viewState: CreateCustomerViewState.Form,
    onCancelClicked: () -> Unit,
    onSaveCustomerClicked: () -> Unit,
    onNextClicked: () -> Unit,
    onPreviousClicked: () -> Unit,
    onFirstNameChanged: (String) -> Unit,
    onLastNameChanged: (String) -> Unit,
    onBirthDateChanged: (String) -> Unit,
    onDocumentNumberChanged: (String) -> Unit,
    onNationalityChanged: (String) -> Unit,
    onGenderChanged: (String) -> Unit,
    onPreferredLanguageChanged: (String) -> Unit,
    onEmailAddressChanged: (String) -> Unit,
    onPhoneNumberChanged: (String) -> Unit,
    onWhatsAppNumberChanged: (String) -> Unit,
    onCountryChanged: (String) -> Unit,
    onStateChanged: (String) -> Unit,
    onCityChanged: (String) -> Unit,
    onAddressLineOneChanged: (String) -> Unit,
    onAddressLineTwoChanged: (String) -> Unit,
    onPostalCodeChanged: (String) -> Unit,
    onTimeZoneChanged: (String) -> Unit,
    onPreferredContactChannelChanged: (String) -> Unit,
    onPreferredContactTimeChanged: (String) -> Unit,
    onNewsletterOptInChanged: (Boolean) -> Unit,
    onCustomerGroupChanged: (String) -> Unit,
    onAcquisitionSourceChanged: (String) -> Unit,
    onPublicNoteChanged: (String) -> Unit,
    onInternalNoteChanged: (String) -> Unit,
    onAddInternalTag: (String) -> Unit,
    onRemoveInternalTag: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val footerActionLabel = when (viewState.currentStep) {
        CreateCustomerStep.PERSONAL_INFO -> stringResourceSafe(R.string.customers_next_button)
        CreateCustomerStep.CONTACT_ADDRESS -> stringResourceSafe(R.string.customers_next_preferences_button)
        CreateCustomerStep.PREFERENCES_NOTES -> stringResourceSafe(R.string.customers_footer_save_button)
    }
    val footerAction = when (viewState.currentStep) {
        CreateCustomerStep.PREFERENCES_NOTES -> onSaveCustomerClicked
        else -> onNextClicked
    }
    val formScrollState = rememberScrollState()

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = CustomersCardBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 5.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 24.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            text = stringResourceSafe(R.string.customers_create_title),
                            style = MaterialTheme.typography.headlineMedium,
                            color = CustomersPrimaryText
                        )
                        Text(
                            text = stringResourceSafe(R.string.customers_create_subtitle),
                            style = MaterialTheme.typography.bodyLarge,
                            color = CustomersMutedText
                        )
                    }
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextButton(
                            onClick = onCancelClicked,
                            enabled = !viewState.isSubmitting
                        ) {
                            Text(text = stringResourceSafe(R.string.customers_cancel_button))
                        }
                        Button(
                            onClick = onSaveCustomerClicked,
                            enabled = !viewState.isSubmitting,
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = CustomersPrimaryText,
                                contentColor = Color.White
                            )
                        ) {
                            Text(text = stringResourceSafe(R.string.customers_save_button))
                        }
                    }
                }

                if (viewState.bannerMessageRes != null) {
                    Text(
                        text = stringResourceSafe(viewState.bannerMessageRes),
                        style = MaterialTheme.typography.bodyMedium,
                        color = CustomersDanger
                    )
                }
            }

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(CustomersBorder)
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(formScrollState)
                    .padding(horizontal = 24.dp, vertical = 22.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                CustomerCreationStepTabs(currentStep = viewState.currentStep)

                BoxWithConstraints(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    when (viewState.currentStep) {
                        CreateCustomerStep.PERSONAL_INFO -> {
                            CreateCustomerPersonalInfoStep(
                                viewState = viewState,
                                onFirstNameChanged = onFirstNameChanged,
                                onLastNameChanged = onLastNameChanged,
                                onBirthDateChanged = onBirthDateChanged,
                                onDocumentNumberChanged = onDocumentNumberChanged,
                                onNationalityChanged = onNationalityChanged,
                                onGenderChanged = onGenderChanged,
                                onPreferredLanguageChanged = onPreferredLanguageChanged,
                                onAddInternalTag = onAddInternalTag,
                                onRemoveInternalTag = onRemoveInternalTag
                            )
                        }

                        CreateCustomerStep.CONTACT_ADDRESS -> {
                            CreateCustomerContactAddressStep(
                                viewState = viewState,
                                onEmailAddressChanged = onEmailAddressChanged,
                                onPhoneNumberChanged = onPhoneNumberChanged,
                                onWhatsAppNumberChanged = onWhatsAppNumberChanged,
                                onCountryChanged = onCountryChanged,
                                onStateChanged = onStateChanged,
                                onCityChanged = onCityChanged,
                                onAddressLineOneChanged = onAddressLineOneChanged,
                                onAddressLineTwoChanged = onAddressLineTwoChanged,
                                onPostalCodeChanged = onPostalCodeChanged,
                                onTimeZoneChanged = onTimeZoneChanged
                            )
                        }

                        CreateCustomerStep.PREFERENCES_NOTES -> {
                            CreateCustomerPreferencesStep(
                                viewState = viewState,
                                onPreferredContactChannelChanged = onPreferredContactChannelChanged,
                                onPreferredContactTimeChanged = onPreferredContactTimeChanged,
                                onNewsletterOptInChanged = onNewsletterOptInChanged,
                                onCustomerGroupChanged = onCustomerGroupChanged,
                                onAcquisitionSourceChanged = onAcquisitionSourceChanged,
                                onPublicNoteChanged = onPublicNoteChanged,
                                onInternalNoteChanged = onInternalNoteChanged
                            )
                        }
                    }
                }
            }

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(CustomersBorder)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 18.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    CreateCustomerStep.entries.forEachIndexed { index, step ->
                        Surface(
                            modifier = Modifier
                                .width(if (step == viewState.currentStep) 28.dp else 10.dp)
                                .height(10.dp),
                            shape = RoundedCornerShape(999.dp),
                            color = if (index <= viewState.currentStep.ordinal) {
                                CustomersPrimaryText
                            } else {
                                CustomersBorder
                            }
                        ) {}
                    }
                    Text(
                        text = stepCounterLabel(viewState.currentStep),
                        modifier = Modifier.padding(start = 8.dp),
                        style = MaterialTheme.typography.bodyMedium,
                        color = CustomersMutedText
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (viewState.currentStep != CreateCustomerStep.PERSONAL_INFO) {
                        TextButton(
                            onClick = onPreviousClicked,
                            enabled = !viewState.isSubmitting
                        ) {
                            Text(text = stringResourceSafe(R.string.customers_previous_button))
                        }
                    }
                    if (viewState.isSubmitting) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(18.dp),
                            strokeWidth = 2.dp,
                            color = HawkBrandBlue
                        )
                    }
                    Button(
                        onClick = footerAction,
                        enabled = viewState.isPrimaryActionEnabled && !viewState.isSubmitting,
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = CustomersPrimaryText,
                            contentColor = Color.White,
                            disabledContainerColor = CustomersBorder,
                            disabledContentColor = CustomersMutedText
                        )
                    ) {
                        Text(text = footerActionLabel)
                    }
                }
            }
        }
    }
}

@Composable
private fun CustomerCreationStepTabs(
    currentStep: CreateCustomerStep
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF1F1F1), RoundedCornerShape(16.dp))
            .padding(6.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        StepTab(
            label = stringResourceSafe(R.string.customers_step_personal_info),
            isSelected = currentStep == CreateCustomerStep.PERSONAL_INFO
        )
        StepTab(
            label = stringResourceSafe(R.string.customers_step_contact_address),
            isSelected = currentStep == CreateCustomerStep.CONTACT_ADDRESS
        )
        StepTab(
            label = stringResourceSafe(R.string.customers_step_preferences_notes),
            isSelected = currentStep == CreateCustomerStep.PREFERENCES_NOTES
        )
    }
}

@Composable
private fun RowScope.StepTab(
    label: String,
    isSelected: Boolean
) {
    Surface(
        modifier = Modifier.weight(1f),
        shape = RoundedCornerShape(12.dp),
        color = if (isSelected) Color.White else Color.Transparent,
        shadowElevation = if (isSelected) 2.dp else 0.dp
    ) {
        Box(
            modifier = Modifier.padding(vertical = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.titleMedium,
                color = if (isSelected) CustomersPrimaryText else CustomersMutedText
            )
        }
    }
}

@Composable
private fun CreateCustomerPersonalInfoStep(
    viewState: CreateCustomerViewState.Form,
    onFirstNameChanged: (String) -> Unit,
    onLastNameChanged: (String) -> Unit,
    onBirthDateChanged: (String) -> Unit,
    onDocumentNumberChanged: (String) -> Unit,
    onNationalityChanged: (String) -> Unit,
    onGenderChanged: (String) -> Unit,
    onPreferredLanguageChanged: (String) -> Unit,
    onAddInternalTag: (String) -> Unit,
    onRemoveInternalTag: (String) -> Unit
) {
    CreateCustomerTwoColumnStep(
        left = {
            PhotoUploadPlaceholder()

            Text(
                text = stringResourceSafe(R.string.customers_full_name_label),
                style = MaterialTheme.typography.titleMedium,
                color = CustomersPrimaryText
            )

            CustomerTextInput(
                label = stringResourceSafe(R.string.customers_first_name_label),
                placeholder = stringResourceSafe(R.string.customers_first_name_placeholder),
                field = viewState.firstName,
                onValueChange = onFirstNameChanged
            )

            CustomerTextInput(
                label = stringResourceSafe(R.string.customers_last_name_label),
                placeholder = stringResourceSafe(R.string.customers_last_name_placeholder),
                field = viewState.lastName,
                onValueChange = onLastNameChanged
            )

            CustomerTextInput(
                label = stringResourceSafe(R.string.customers_birth_date_label),
                placeholder = stringResourceSafe(R.string.customers_birth_date_placeholder),
                field = viewState.birthDate,
                onValueChange = onBirthDateChanged,
                keyboardType = KeyboardType.Number
            )

            CustomerSelectionInput(
                label = stringResourceSafe(R.string.customers_gender_label),
                placeholder = stringResourceSafe(R.string.customers_select_gender_placeholder),
                selectedCode = viewState.genderCode,
                options = genderOptions,
                onValueSelected = onGenderChanged,
                errorMessageRes = null
            )
        },
        right = {
            CustomerTextInput(
                label = stringResourceSafe(R.string.customers_document_number_label),
                placeholder = stringResourceSafe(R.string.customers_document_number_placeholder),
                field = viewState.documentNumber,
                onValueChange = onDocumentNumberChanged
            )

            CustomerSelectionInput(
                label = stringResourceSafe(R.string.customers_nationality_label),
                placeholder = stringResourceSafe(R.string.customers_select_nationality_placeholder),
                selectedCode = viewState.nationalityCode.value,
                options = nationalityOptions,
                onValueSelected = onNationalityChanged,
                errorMessageRes = viewState.nationalityCode.errorMessageRes
            )

            CustomerSelectionInput(
                label = stringResourceSafe(R.string.customers_preferred_language_label),
                placeholder = stringResourceSafe(R.string.customers_select_language_placeholder),
                selectedCode = viewState.preferredLanguageCode,
                options = languageOptions,
                onValueSelected = onPreferredLanguageChanged,
                errorMessageRes = null
            )

            InternalTagsEditor(
                internalTags = viewState.internalTags,
                onAddInternalTag = onAddInternalTag,
                onRemoveInternalTag = onRemoveInternalTag
            )
        }
    )
}

@Composable
private fun CreateCustomerContactAddressStep(
    viewState: CreateCustomerViewState.Form,
    onEmailAddressChanged: (String) -> Unit,
    onPhoneNumberChanged: (String) -> Unit,
    onWhatsAppNumberChanged: (String) -> Unit,
    onCountryChanged: (String) -> Unit,
    onStateChanged: (String) -> Unit,
    onCityChanged: (String) -> Unit,
    onAddressLineOneChanged: (String) -> Unit,
    onAddressLineTwoChanged: (String) -> Unit,
    onPostalCodeChanged: (String) -> Unit,
    onTimeZoneChanged: (String) -> Unit
) {
    CreateCustomerTwoColumnStep(
        left = {
            CustomerTextInput(
                label = stringResourceSafe(R.string.customers_email_address_label),
                placeholder = stringResourceSafe(R.string.customers_email_address_placeholder),
                field = viewState.emailAddress,
                onValueChange = onEmailAddressChanged,
                keyboardType = KeyboardType.Email
            )

            CustomerTextInput(
                label = stringResourceSafe(R.string.customers_phone_number_label),
                placeholder = stringResourceSafe(R.string.customers_phone_number_placeholder),
                field = viewState.phoneNumber,
                onValueChange = onPhoneNumberChanged,
                keyboardType = KeyboardType.Phone
            )

            CustomerTextInput(
                label = stringResourceSafe(R.string.customers_whatsapp_number_label),
                placeholder = stringResourceSafe(R.string.customers_whatsapp_number_placeholder),
                field = viewState.whatsAppNumber,
                onValueChange = onWhatsAppNumberChanged,
                keyboardType = KeyboardType.Phone
            )

            CustomerSelectionInput(
                label = stringResourceSafe(R.string.customers_country_label),
                placeholder = stringResourceSafe(R.string.customers_select_country_placeholder),
                selectedCode = viewState.countryCode.value,
                options = nationalityOptions,
                onValueSelected = onCountryChanged,
                errorMessageRes = viewState.countryCode.errorMessageRes
            )

            CustomerSelectionInput(
                label = stringResourceSafe(R.string.customers_state_label),
                placeholder = stringResourceSafe(R.string.customers_select_state_placeholder),
                selectedCode = viewState.stateCode,
                options = stateOptions,
                onValueSelected = onStateChanged,
                errorMessageRes = null
            )
        },
        right = {
            CustomerTextInput(
                label = stringResourceSafe(R.string.customers_city_label),
                placeholder = stringResourceSafe(R.string.customers_city_placeholder),
                field = viewState.city,
                onValueChange = onCityChanged
            )

            CustomerTextInput(
                label = stringResourceSafe(R.string.customers_address_line_one_label),
                placeholder = stringResourceSafe(R.string.customers_address_line_one_placeholder),
                field = viewState.addressLineOne,
                onValueChange = onAddressLineOneChanged
            )

            CustomerTextInput(
                label = stringResourceSafe(R.string.customers_address_line_two_label),
                placeholder = stringResourceSafe(R.string.customers_address_line_two_placeholder),
                field = viewState.addressLineTwo,
                onValueChange = onAddressLineTwoChanged
            )

            CustomerTextInput(
                label = stringResourceSafe(R.string.customers_postal_code_label),
                placeholder = stringResourceSafe(R.string.customers_postal_code_placeholder),
                field = viewState.postalCode,
                onValueChange = onPostalCodeChanged,
                keyboardType = KeyboardType.Number
            )

            CustomerSelectionInput(
                label = stringResourceSafe(R.string.customers_timezone_label),
                placeholder = stringResourceSafe(R.string.customers_select_timezone_placeholder),
                selectedCode = viewState.timeZoneCode,
                options = timeZoneOptions,
                onValueSelected = onTimeZoneChanged,
                errorMessageRes = null
            )
        }
    )
}

@Composable
private fun CreateCustomerPreferencesStep(
    viewState: CreateCustomerViewState.Form,
    onPreferredContactChannelChanged: (String) -> Unit,
    onPreferredContactTimeChanged: (String) -> Unit,
    onNewsletterOptInChanged: (Boolean) -> Unit,
    onCustomerGroupChanged: (String) -> Unit,
    onAcquisitionSourceChanged: (String) -> Unit,
    onPublicNoteChanged: (String) -> Unit,
    onInternalNoteChanged: (String) -> Unit
) {
    CreateCustomerTwoColumnStep(
        left = {
            CustomerSelectionInput(
                label = stringResourceSafe(R.string.customers_preferred_contact_method_label),
                placeholder = stringResourceSafe(R.string.customers_select_contact_method_placeholder),
                selectedCode = viewState.preferredContactChannelCode.value,
                options = preferredContactMethodOptions,
                onValueSelected = onPreferredContactChannelChanged,
                errorMessageRes = viewState.preferredContactChannelCode.errorMessageRes
            )

            CustomerSelectionInput(
                label = stringResourceSafe(R.string.customers_best_time_to_contact_label),
                placeholder = stringResourceSafe(R.string.customers_select_contact_time_placeholder),
                selectedCode = viewState.preferredContactTimeCode,
                options = preferredContactTimeOptions,
                onValueSelected = onPreferredContactTimeChanged,
                errorMessageRes = null
            )

            NewsletterOptInCard(
                isChecked = viewState.newsletterOptIn,
                onCheckedChange = onNewsletterOptInChanged
            )

            CustomerSelectionInput(
                label = stringResourceSafe(R.string.customers_customer_group_label),
                placeholder = stringResourceSafe(R.string.customers_select_customer_group_placeholder),
                selectedCode = viewState.customerGroupCode,
                options = customerGroupOptions,
                onValueSelected = onCustomerGroupChanged,
                errorMessageRes = null
            )

            CustomerSelectionInput(
                label = stringResourceSafe(R.string.customers_acquisition_source_label),
                placeholder = stringResourceSafe(R.string.customers_select_acquisition_source_placeholder),
                selectedCode = viewState.acquisitionSourceCode.value,
                options = acquisitionSourceOptions,
                onValueSelected = onAcquisitionSourceChanged,
                errorMessageRes = viewState.acquisitionSourceCode.errorMessageRes
            )
        },
        right = {
            CustomerTextInput(
                label = stringResourceSafe(R.string.customers_public_note_label),
                placeholder = stringResourceSafe(R.string.customers_public_note_placeholder),
                field = viewState.publicNote,
                onValueChange = onPublicNoteChanged,
                singleLine = false,
                minLines = 4
            )

            CustomerTextInput(
                label = stringResourceSafe(R.string.customers_internal_note_label),
                placeholder = stringResourceSafe(R.string.customers_internal_note_placeholder),
                field = viewState.internalNote,
                onValueChange = onInternalNoteChanged,
                singleLine = false,
                minLines = 4
            )
        }
    )
}

@Composable
private fun CreateCustomerTwoColumnStep(
    left: @Composable ColumnScope.() -> Unit,
    right: @Composable ColumnScope.() -> Unit
) {
    BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
        val useTwoColumns = maxWidth >= 920.dp

        if (useTwoColumns) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(36.dp)
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(18.dp),
                    content = left
                )
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(18.dp),
                    content = right
                )
            }
        } else {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(18.dp), content = left)
                Column(verticalArrangement = Arrangement.spacedBy(18.dp), content = right)
            }
        }
    }
}

@Composable
private fun NewsletterOptInCard(
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, CustomersBorder),
        color = CustomersCardBackground
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = stringResourceSafe(R.string.customers_newsletter_opt_in_label),
                    style = MaterialTheme.typography.bodyLarge,
                    color = CustomersPrimaryText
                )
                Text(
                    text = if (isChecked) {
                        stringResourceSafe(R.string.customers_newsletter_opt_in_enabled)
                    } else {
                        stringResourceSafe(R.string.customers_newsletter_opt_in_disabled)
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = CustomersMutedText
                )
            }
            Switch(
                checked = isChecked,
                onCheckedChange = onCheckedChange
            )
        }
    }
}

@Composable
private fun stepCounterLabel(step: CreateCustomerStep): String = when (step) {
    CreateCustomerStep.PERSONAL_INFO -> stringResourceSafe(R.string.customers_step_one_of_three)
    CreateCustomerStep.CONTACT_ADDRESS -> stringResourceSafe(R.string.customers_step_two_of_three)
    CreateCustomerStep.PREFERENCES_NOTES -> stringResourceSafe(R.string.customers_step_three_of_three)
}

@Composable
private fun PhotoUploadPlaceholder() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Surface(
            modifier = Modifier.size(120.dp),
            shape = CircleShape,
            color = Color(0xFFF7F7F7),
            border = androidx.compose.foundation.BorderStroke(1.dp, CustomersBorder)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = "Photo",
                    style = MaterialTheme.typography.labelLarge,
                    color = CustomersMutedText
                )
            }
        }
        Text(
            text = stringResourceSafe(R.string.customers_upload_photo_label),
            style = MaterialTheme.typography.bodyLarge,
            color = HawkBrandBlue
        )
    }
}

@Composable
private fun CustomerTextInput(
    label: String,
    placeholder: String,
    field: CustomerFormFieldUiModel,
    onValueChange: (String) -> Unit,
    keyboardType: KeyboardType = KeyboardType.Text,
    singleLine: Boolean = true,
    minLines: Int = 1
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = CustomersPrimaryText
        )
        OutlinedTextField(
            value = field.value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(text = placeholder) },
            singleLine = singleLine,
            minLines = minLines,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            isError = field.hasError,
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = CustomersCardBackground,
                unfocusedContainerColor = CustomersCardBackground,
                focusedBorderColor = if (field.hasError) CustomersDanger else HawkBrandBlue,
                unfocusedBorderColor = if (field.hasError) CustomersDanger else CustomersBorder,
                cursorColor = HawkBrandBlue
            )
        )
        if (field.errorMessageRes != null) {
            Text(
                text = stringResourceSafe(field.errorMessageRes),
                style = MaterialTheme.typography.bodyMedium,
                color = CustomersDanger
            )
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun CustomerSelectionInput(
    label: String,
    placeholder: String,
    selectedCode: String,
    options: List<CustomerOption>,
    onValueSelected: (String) -> Unit,
    errorMessageRes: Int?
) {
    var isExpanded by rememberSaveable(label) { mutableStateOf(false) }
    val selectedOption = options.firstOrNull { it.code == selectedCode }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = CustomersPrimaryText
        )
        ExposedDropdownMenuBox(
            expanded = isExpanded,
            onExpandedChange = { isExpanded = !isExpanded }
        ) {
            OutlinedTextField(
                value = selectedOption?.label.orEmpty(),
                onValueChange = {},
                readOnly = true,
                singleLine = true,
                isError = errorMessageRes != null,
                placeholder = { Text(text = placeholder) },
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = CustomersCardBackground,
                    unfocusedContainerColor = CustomersCardBackground,
                    focusedBorderColor = if (errorMessageRes != null) CustomersDanger else HawkBrandBlue,
                    unfocusedBorderColor = if (errorMessageRes != null) CustomersDanger else CustomersBorder
                ),
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
                },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = isExpanded,
                onDismissRequest = { isExpanded = false }
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(text = option.label) },
                        onClick = {
                            isExpanded = false
                            onValueSelected(option.code)
                        }
                    )
                }
            }
        }
        if (errorMessageRes != null) {
            Text(
                text = stringResourceSafe(errorMessageRes),
                style = MaterialTheme.typography.bodyMedium,
                color = CustomersDanger
            )
        }
    }
}

@Composable
private fun InternalTagsEditor(
    internalTags: List<String>,
    onAddInternalTag: (String) -> Unit,
    onRemoveInternalTag: (String) -> Unit
) {
    val suggestedTags = remember { listOf("VIP", "Wholesale", "Retail") }

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(
            text = stringResourceSafe(R.string.customers_internal_tags_label),
            style = MaterialTheme.typography.bodyLarge,
            color = CustomersPrimaryText
        )
        Surface(
            shape = RoundedCornerShape(16.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, CustomersBorder),
            color = CustomersCardBackground
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (internalTags.isNotEmpty()) {
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        internalTags.forEach { tag ->
                            Surface(
                                shape = RoundedCornerShape(999.dp),
                                color = Color(0xFFE7F8EF)
                            ) {
                                Row(
                                    modifier = Modifier.padding(
                                        horizontal = 12.dp,
                                        vertical = 8.dp
                                    ),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Text(
                                        text = tag,
                                        style = MaterialTheme.typography.labelMedium,
                                        color = Color(0xFF059669)
                                    )
                                    TextButton(
                                        onClick = { onRemoveInternalTag(tag) },
                                        contentPadding = PaddingValues(0.dp)
                                    ) {
                                        Text(text = "x")
                                    }
                                }
                            }
                        }
                    }
                }

                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    suggestedTags.forEach { tag ->
                        Surface(
                            shape = RoundedCornerShape(999.dp),
                            color = Color(0xFFF3F4F6)
                        ) {
                            TextButton(onClick = { onAddInternalTag(tag) }) {
                                Text(text = tag)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun customersDateLabel(): String {
    val formatter = remember {
        DateTimeFormatter.ofPattern("EEEE, d MMMM yyyy", Locale.ENGLISH)
    }
    return remember {
        LocalDate.now().format(formatter)
    }
}

@Composable
private fun stringResourceSafe(id: Int?, vararg args: String): String {
    if (id == null) {
        return ""
    }
    return if (args.isEmpty()) {
        androidx.compose.ui.res.stringResource(id)
    } else {
        androidx.compose.ui.res.stringResource(id, *args)
    }
}

private fun formatCount(value: Int): String =
    NumberFormat.getIntegerInstance(Locale.US).format(value)

@Preview(widthDp = 1366, heightDp = 900, showBackground = true)
@Composable
private fun PreviewCustomersScreen() {
    HawkTheme {
        CustomersScreen(
            viewState = CustomersViewState(
                isLoading = false,
                customers = listOf(
                    CustomerListItemUiModel(
                        customerId = "1",
                        customerType = CustomerType.PERSON,
                        avatarLabel = "AM",
                        displayName = "Alejandra Mora",
                        companyLabel = "Personal account",
                        roleLabel = "Retail customer",
                        cityLabel = "Bogota",
                        acquisitionChannelLabel = "POS",
                        acquisitionChannelTone = CustomerBadgeTone.GREEN,
                        preferredChannelLabel = "WhatsApp",
                        typeLabel = "B2C",
                        typeTone = CustomerBadgeTone.PURPLE,
                        healthScoreLabel = "92",
                        healthStatusLabel = "Healthy",
                        healthTone = CustomerHealthTone.HEALTHY,
                        lastActivityLabel = "Today"
                    )
                ),
                totalCustomers = 1,
                businessAccounts = 0,
                atRiskCustomers = 0,
                loadedCount = 1
            ),
            onWorkspaceTabSelected = {},
            onQueryChanged = {},
            onFilterSelected = {},
            onCreateCustomerClicked = {},
            onRetryRequested = {},
            onWorkspaceSectionSelected = {}
        )
    }
}

@Preview(widthDp = 1366, heightDp = 900, showBackground = true)
@Composable
private fun PreviewCreateCustomerScreen() {
    HawkTheme {
        CreateCustomerScreen(
            viewState = CreateCustomerViewState.Form(),
            onWorkspaceSectionSelected = {},
            onBackToCustomers = {},
            onCancelClicked = {},
            onSaveCustomerClicked = {},
            onNextClicked = {},
            onPreviousClicked = {},
            onFirstNameChanged = {},
            onLastNameChanged = {},
            onBirthDateChanged = {},
            onDocumentNumberChanged = {},
            onNationalityChanged = {},
            onGenderChanged = {},
            onPreferredLanguageChanged = {},
            onEmailAddressChanged = {},
            onPhoneNumberChanged = {},
            onWhatsAppNumberChanged = {},
            onCountryChanged = {},
            onStateChanged = {},
            onCityChanged = {},
            onAddressLineOneChanged = {},
            onAddressLineTwoChanged = {},
            onPostalCodeChanged = {},
            onTimeZoneChanged = {},
            onPreferredContactChannelChanged = {},
            onPreferredContactTimeChanged = {},
            onNewsletterOptInChanged = {},
            onCustomerGroupChanged = {},
            onAcquisitionSourceChanged = {},
            onPublicNoteChanged = {},
            onInternalNoteChanged = {},
            onAddInternalTag = {},
            onRemoveInternalTag = {}
        )
    }
}
