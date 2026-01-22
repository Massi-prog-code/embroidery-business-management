package com.massinissa.embroiderybusinessmanagement.utils

import android.graphics.Color
import com.massinissa.embroiderybusinessmanagement.data.database.entities.PaymentStatus

object PaymentStatusHelper {

    /**
     * Get color for payment status badge
     */
    fun getStatusColor(status: PaymentStatus): Int {
        return Color.parseColor(status.colorCode)
    }

    /**
     * Get icon/emoji for payment status
     */
    fun getStatusIcon(status: PaymentStatus): String {
        return when (status) {
            PaymentStatus.UNPAID -> "🔴"
            PaymentStatus.PENDING -> "🟡"
            PaymentStatus.PARTIALLY_PAID -> "🔵"
            PaymentStatus.PAID -> "🟢"
        }
    }

    /**
     * Get detailed status message
     */
    fun getStatusMessage(status: PaymentStatus): String {
        return when (status) {
            PaymentStatus.UNPAID -> "No payment has been received for this order"
            PaymentStatus.PENDING -> "Payment is being processed. Please check with bank or customer"
            PaymentStatus.PARTIALLY_PAID -> "Partial payment received. Balance is still outstanding"
            PaymentStatus.PAID -> "Payment completed. Full amount received"
        }
    }
}
