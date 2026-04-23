package com.hawk.common.environment

import com.hawk.BuildConfig

object AppEnvironment {
    val keycloakBaseUrl: String
        get() = BuildConfig.KEYCLOAK_BASE_URL

    val keycloakTokenPath: String
        get() = BuildConfig.KEYCLOAK_TOKEN_PATH

    val keycloakClientId: String
        get() = BuildConfig.KEYCLOAK_CLIENT_ID

    val keycloakGrantType: String
        get() = BuildConfig.KEYCLOAK_GRANT_TYPE

    val productsBaseUrl: String
        get() = BuildConfig.PRODUCTS_BASE_URL

    val productsListPath: String
        get() = BuildConfig.PRODUCTS_LIST_PATH

    val productsCreatePath: String
        get() = BuildConfig.PRODUCTS_CREATE_PATH

    val customersBaseUrl: String
        get() = BuildConfig.CUSTOMERS_BASE_URL

    val customersListPath: String
        get() = BuildConfig.CUSTOMERS_LIST_PATH

    val customersCreatePath: String
        get() = BuildConfig.CUSTOMERS_CREATE_PATH

    const val databaseName: String = "hawk.db"
}
