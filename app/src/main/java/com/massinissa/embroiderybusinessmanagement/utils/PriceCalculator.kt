// FILE: utils/PriceCalculator.kt
package com.massinissa.embroiderybusinessmanagement.utils

import com.massinissa.embroiderybusinessmanagement.data.database.entities.PaymentStatus

object PriceCalculator {

    /**
     * Calculate total price for an order
     */
    fun calculateTotalPrice(
        basePrice: Double,
        stitchCount: Int,
        stitchRate: Double,
        materialCost: Double,
        additionalCost: Double
    ): Double {
        val stitchCost = (stitchCount.toDouble() / 1000.0) * stitchRate
        return basePrice + stitchCost + materialCost + additionalCost
    }

    /**
     * Calculate remaining balance
     */
    fun calculateBalance(totalPrice: Double, amountPaid: Double): Double {
        return (totalPrice - amountPaid).coerceAtLeast(0.0)
    }

    /**
     * Calculate payment percentage (0-100)
     */
    fun calculatePaymentPercentage(totalPrice: Double, amountPaid: Double): Int {
        if (totalPrice <= 0) return 0
        val percentage = (amountPaid / totalPrice * 100).toInt()
        return percentage.coerceIn(0, 100)
    }

    /**
     * Determine payment status based on amounts
     */
    fun determinePaymentStatus(
        totalPrice: Double,
        amountPaid: Double,
        isPending: Boolean = false
    ): PaymentStatus {
        return when {
            amountPaid <= 0.0 -> PaymentStatus.UNPAID
            isPending -> PaymentStatus.PENDING
            amountPaid >= totalPrice -> PaymentStatus.PAID
            else -> PaymentStatus.PARTIALLY_PAID
        }
    }

    /**
     * Format currency for display (Algerian Dinar)
     */
    fun formatCurrency(amount: Double, currencySymbol: String = "DZD"): String {
        return String.format("%s %.2f", currencySymbol, amount)
    }
}
