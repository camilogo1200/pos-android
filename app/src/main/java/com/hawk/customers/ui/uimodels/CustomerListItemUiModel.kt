package com.hawk.customers.ui.uimodels

import com.hawk.customers.domain.entities.CustomerType

data class CustomerListItemUiModel(
    val customerId: String,
    val customerType: CustomerType,
    val avatarLabel: String,
    val displayName: String,
    val companyLabel: String,
    val roleLabel: String,
    val cityLabel: String,
    val acquisitionChannelLabel: String,
    val acquisitionChannelTone: CustomerBadgeTone,
    val preferredChannelLabel: String,
    val typeLabel: String,
    val typeTone: CustomerBadgeTone,
    val healthScoreLabel: String,
    val healthStatusLabel: String,
    val healthTone: CustomerHealthTone,
    val lastActivityLabel: String
)

enum class CustomerBadgeTone {
    BLUE,
    GREEN,
    ORANGE,
    PURPLE,
    RED,
    NEUTRAL
}

enum class CustomerHealthTone {
    HEALTHY,
    AT_RISK,
    COLD
}
