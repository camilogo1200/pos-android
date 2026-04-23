package com.hawk.customers.data.datasources.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "customers")
data class CustomerEntity(
    @PrimaryKey
    @ColumnInfo(name = "customer_id")
    val customerId: String,
    @ColumnInfo(name = "customer_code")
    val customerCode: String,
    @ColumnInfo(name = "customer_type")
    val customerType: String,
    @ColumnInfo(name = "status")
    val status: String,
    @ColumnInfo(name = "display_name")
    val displayName: String,
    @ColumnInfo(name = "source")
    val source: String,
    @ColumnInfo(name = "primary_email")
    val primaryEmail: String? = null,
    @ColumnInfo(name = "primary_phone")
    val primaryPhone: String? = null,
    @ColumnInfo(name = "is_primary_phone_whatsapp_enabled")
    val isPrimaryPhoneWhatsAppEnabled: Boolean = false,
    @ColumnInfo(name = "primary_city")
    val primaryCity: String? = null,
    @ColumnInfo(name = "business_trade_name")
    val businessTradeName: String? = null,
    @ColumnInfo(name = "business_legal_name")
    val businessLegalName: String? = null,
    @ColumnInfo(name = "primary_business_contact_role")
    val primaryBusinessContactRole: String? = null,
    @ColumnInfo(name = "classification_group")
    val classificationGroup: String? = null
)
