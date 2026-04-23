package com.hawk.customers.ui.viewmodels

import androidx.lifecycle.ViewModel
import com.hawk.R
import com.hawk.customers.domain.entities.Customer
import com.hawk.customers.domain.entities.CustomerDirectory
import com.hawk.customers.domain.entities.CustomerStatus
import com.hawk.customers.domain.entities.CustomerType
import com.hawk.customers.domain.errors.CustomerReadException
import com.hawk.customers.domain.usecases.interfaces.GetCustomersUseCase
import com.hawk.customers.ui.uimodels.CustomerBadgeTone
import com.hawk.customers.ui.uimodels.CustomerHealthTone
import com.hawk.customers.ui.uimodels.CustomerListItemUiModel
import com.hawk.customers.ui.viewstates.CustomerTypeFilter
import com.hawk.customers.ui.viewstates.CustomerWorkspaceTab
import com.hawk.customers.ui.viewstates.CustomersViewState
import com.hawk.utils.coroutines.IoDispatcher
import com.hawk.utils.coroutines.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update

@HiltViewModel
class CustomersViewModel @Inject constructor(
    private val getCustomersUseCase: GetCustomersUseCase,
    @param:IoDispatcher private val coroutineDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _viewState = MutableStateFlow(CustomersViewState())
    val viewState: StateFlow<CustomersViewState> = _viewState.asStateFlow()

    private var allCustomers: List<CustomerListItemUiModel> = emptyList()
    private var loadedCount: Int = 0
    private var hasMoreResults: Boolean = false

    init {
        loadCustomers()
    }

    fun onWorkspaceTabSelected(tab: CustomerWorkspaceTab) {
        _viewState.update { state ->
            state.copy(selectedTab = tab)
        }
    }

    fun onQueryChanged(query: String) {
        _viewState.update { state -> state.copy(query = query) }
        applyFilters()
    }

    fun onFilterSelected(filter: CustomerTypeFilter) {
        _viewState.update { state -> state.copy(selectedFilter = filter) }
        applyFilters()
    }

    fun onRetryRequested() {
        _viewState.update { state ->
            state.copy(
                isLoading = true,
                statusTitleRes = null,
                statusBodyRes = null
            )
        }
        loadCustomers()
    }

    private fun loadCustomers() {
        launch(coroutineDispatcher) {
            getCustomersUseCase().collectLatest { result ->
                result
                    .onSuccess { directory ->
                        loadedCount = directory.loadedCount
                        hasMoreResults = !directory.nextCursor.isNullOrBlank()
                        allCustomers = directory.customers.map(::toUiModel)
                        _viewState.update { state ->
                            state.copy(
                                isLoading = false,
                                statusTitleRes = null,
                                statusBodyRes = null
                            )
                        }
                        applyFilters()
                    }
                    .onFailure { throwable ->
                        allCustomers = emptyList()
                        loadedCount = 0
                        hasMoreResults = false
                        _viewState.update { state ->
                            state.copy(
                                isLoading = false,
                                customers = emptyList(),
                                totalCustomers = 0,
                                businessAccounts = 0,
                                atRiskCustomers = 0,
                                loadedCount = 0,
                                hasMoreResults = false,
                                statusTitleRes = when (throwable) {
                                    is CustomerReadException.NoConnection ->
                                        R.string.customers_no_connection_title

                                    else -> R.string.customers_bad_response_title
                                },
                                statusBodyRes = when (throwable) {
                                    is CustomerReadException.NoConnection ->
                                        R.string.customers_no_connection_body

                                    else -> R.string.customers_bad_response_body
                                }
                            )
                        }
                    }
            }
        }
    }

    private fun applyFilters() {
        val state = _viewState.value
        val query = state.query.trim()

        val filteredCustomers = allCustomers.filter { customer ->
            val matchesQuery = query.isBlank() ||
                customer.displayName.contains(query, ignoreCase = true) ||
                customer.companyLabel.contains(query, ignoreCase = true) ||
                customer.cityLabel.contains(query, ignoreCase = true)

            val matchesFilter = when (state.selectedFilter) {
                CustomerTypeFilter.ALL -> true
                CustomerTypeFilter.PERSON -> customer.customerType == CustomerType.PERSON
                CustomerTypeFilter.BUSINESS -> customer.customerType == CustomerType.BUSINESS
            }

            matchesQuery && matchesFilter
        }

        _viewState.update { current ->
            current.copy(
                customers = filteredCustomers,
                totalCustomers = allCustomers.size,
                businessAccounts = allCustomers.count { it.customerType == CustomerType.BUSINESS },
                atRiskCustomers = allCustomers.count { it.healthTone != CustomerHealthTone.HEALTHY },
                loadedCount = loadedCount,
                hasMoreResults = hasMoreResults
            )
        }
    }

    private fun toUiModel(customer: Customer): CustomerListItemUiModel {
        val healthScore = calculateHealthScore(customer)
        val healthTone = when {
            healthScore >= 80 -> CustomerHealthTone.HEALTHY
            healthScore >= 50 -> CustomerHealthTone.AT_RISK
            else -> CustomerHealthTone.COLD
        }

        return CustomerListItemUiModel(
            customerId = customer.customerId,
            customerType = customer.customerType,
            avatarLabel = customer.displayName.split(" ")
                .take(2)
                .mapNotNull { it.firstOrNull()?.uppercaseChar() }
                .joinToString(separator = ""),
            displayName = customer.displayName,
            companyLabel = when (customer.customerType) {
                CustomerType.BUSINESS -> customer.businessTradeName
                    ?: customer.businessLegalName
                    ?: "Business account"

                CustomerType.PERSON -> "Personal account"
                CustomerType.UNKNOWN -> "Unknown account"
            },
            roleLabel = when (customer.customerType) {
                CustomerType.BUSINESS -> customer.primaryBusinessContactRole
                    ?.replace('_', ' ')
                    ?.lowercase()
                    ?.replaceFirstChar { it.titlecase() }
                    ?: "Account contact"

                CustomerType.PERSON -> customer.classificationGroup
                    ?.lowercase()
                    ?.replaceFirstChar { it.titlecase() }
                    ?: "Retail customer"

                CustomerType.UNKNOWN -> "Customer"
            },
            cityLabel = customer.primaryCity ?: "Not provided",
            acquisitionChannelLabel = customer.source.ifBlank { "Unknown" }.uppercase(),
            acquisitionChannelTone = customer.source.toSourceTone(),
            preferredChannelLabel = when {
                customer.isPrimaryPhoneWhatsAppEnabled -> "WhatsApp"
                !customer.primaryEmail.isNullOrBlank() -> "Email"
                !customer.primaryPhone.isNullOrBlank() -> "Phone"
                else -> "Unavailable"
            },
            typeLabel = when (customer.customerType) {
                CustomerType.BUSINESS -> "B2B"
                CustomerType.PERSON -> "B2C"
                CustomerType.UNKNOWN -> "UNK"
            },
            typeTone = if (customer.customerType == CustomerType.BUSINESS) {
                CustomerBadgeTone.BLUE
            } else {
                CustomerBadgeTone.PURPLE
            },
            healthScoreLabel = healthScore.toString(),
            healthStatusLabel = when (healthTone) {
                CustomerHealthTone.HEALTHY -> "Healthy"
                CustomerHealthTone.AT_RISK -> "At Risk"
                CustomerHealthTone.COLD -> "Cold"
            },
            healthTone = healthTone,
            lastActivityLabel = customer.source.toLastActivityLabel()
        )
    }

    private fun calculateHealthScore(customer: Customer): Int {
        var score = when (customer.status) {
            CustomerStatus.ACTIVE -> 60
            CustomerStatus.INACTIVE -> 35
            CustomerStatus.UNKNOWN -> 45
        }

        if (!customer.primaryEmail.isNullOrBlank()) {
            score += 12
        }
        if (!customer.primaryPhone.isNullOrBlank()) {
            score += 10
        }
        if (customer.isPrimaryPhoneWhatsAppEnabled) {
            score += 8
        }
        if (!customer.primaryCity.isNullOrBlank()) {
            score += 5
        }
        if (customer.customerType == CustomerType.BUSINESS && !customer.primaryBusinessContactRole.isNullOrBlank()) {
            score += 5
        }

        score += when (customer.source.uppercase()) {
            "POS" -> 5
            "ADMIN" -> 4
            "API" -> -10
            "IMPORT" -> -20
            else -> 0
        }

        return score.coerceIn(20, 99)
    }

    private fun String.toSourceTone(): CustomerBadgeTone = when (uppercase()) {
        "POS" -> CustomerBadgeTone.GREEN
        "ADMIN" -> CustomerBadgeTone.BLUE
        "API" -> CustomerBadgeTone.PURPLE
        "IMPORT" -> CustomerBadgeTone.ORANGE
        else -> CustomerBadgeTone.NEUTRAL
    }

    private fun String.toLastActivityLabel(): String = when (uppercase()) {
        "POS" -> "Today"
        "ADMIN" -> "Yesterday"
        "API" -> "3 days ago"
        "IMPORT" -> "1 week ago"
        else -> "No activity"
    }
}
