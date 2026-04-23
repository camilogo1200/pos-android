package com.hawk.customers.ui.viewstates

import androidx.annotation.StringRes
import com.hawk.customers.ui.uimodels.CustomerListItemUiModel

data class CustomersViewState(
    val isLoading: Boolean = true,
    val selectedTab: CustomerWorkspaceTab = CustomerWorkspaceTab.CONTACTS,
    val query: String = "",
    val selectedFilter: CustomerTypeFilter = CustomerTypeFilter.ALL,
    val customers: List<CustomerListItemUiModel> = emptyList(),
    val totalCustomers: Int = 0,
    val businessAccounts: Int = 0,
    val atRiskCustomers: Int = 0,
    val loadedCount: Int = 0,
    val hasMoreResults: Boolean = false,
    @param:StringRes val statusTitleRes: Int? = null,
    @param:StringRes val statusBodyRes: Int? = null
)

enum class CustomerWorkspaceTab {
    CONTACTS,
    PIPELINE,
    ACTIVITIES,
    WORKFLOWS
}

enum class CustomerTypeFilter {
    ALL,
    PERSON,
    BUSINESS
}
