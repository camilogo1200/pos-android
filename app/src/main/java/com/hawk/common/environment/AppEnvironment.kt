package com.hawk.common.environment

import com.hawk.BuildConfig

object AppEnvironment {
    val keycloakBaseUrl: String
        get() = BuildConfig.KEYCLOAK_BASE_URL

    val keycloakTokenPath: String
        get() = BuildConfig.KEYCLOAK_TOKEN_PATH

    val keycloakTokenUrl: String
        get() = "${BuildConfig.KEYCLOAK_BASE_URL}${BuildConfig.KEYCLOAK_TOKEN_PATH}"

    val keycloakClientId: String
        get() = BuildConfig.KEYCLOAK_CLIENT_ID

    val keycloakGrantType: String
        get() = BuildConfig.KEYCLOAK_GRANT_TYPE

    val productsBaseUrl: String
        get() = BuildConfig.PRODUCTS_BASE_URL

    val productsListPath: String
        get() = BuildConfig.PRODUCTS_LIST_PATH

    val productsListUrl: String
        get() = "${BuildConfig.PRODUCTS_BASE_URL}${BuildConfig.PRODUCTS_LIST_PATH}"

    const val formContentType: String = "application/x-www-form-urlencoded"
    const val databaseName: String = "hawk.db"
}
