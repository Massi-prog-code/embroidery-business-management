package com.massinissa.embroiderybusinessmanagement.data.models

import com.massinissa.embroiderybusinessmanagement.data.database.entities.Payment
import com.massinissa.embroiderybusinessmanagement.data.database.entities.PaymentStatus

/**
 * Payment summary for display
 */
data class PaymentSummary(
    val totalPrice: Double,
    val totalPaid: Double,
    val remainingBalance: Double,
    val paymentStatus: PaymentStatus,
    val paymentPercentage: Int,
    val payments: List<Payment>
) {
    val isFullyPaid: Boolean get() = remainingBalance <= 0.0
    val hasPayments: Boolean get() = payments.isNotEmpty()
}