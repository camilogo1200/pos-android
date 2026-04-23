CREATE TABLE IF NOT EXISTS customers (
    customer_id TEXT NOT NULL,
    customer_code TEXT NOT NULL,
    customer_type TEXT NOT NULL,
    status TEXT NOT NULL,
    display_name TEXT NOT NULL,
    source TEXT NOT NULL,
    primary_email TEXT,
    primary_phone TEXT,
    is_primary_phone_whatsapp_enabled INTEGER NOT NULL,
    primary_city TEXT,
    business_trade_name TEXT,
    business_legal_name TEXT,
    primary_business_contact_role TEXT,
    classification_group TEXT,
    PRIMARY KEY(customer_id)
)
