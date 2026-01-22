package com.massinissa.embroiderybusinessmanagement.data.database.entities



enum class PaymentStatus(val displayName: String, val colorCode: String) {
    UNPAID("Unpaid", "#F44336"),
    PENDING("Pending Payment", "#FF9800"),
    PARTIALLY_PAID("Partially Paid", "#2196F3"),
    PAID("Paid", "#4CAF50");

    companion object {
        fun fromString(value: String): PaymentStatus {
            return values().find { it.name == value } ?: UNPAID
        }
    }
}