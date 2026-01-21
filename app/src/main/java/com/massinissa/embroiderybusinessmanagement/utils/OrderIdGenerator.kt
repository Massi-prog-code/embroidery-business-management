package com.massinissa.embroiderybusinessmanagement.utils

import java.text.SimpleDateFormat
import java.util.*

object OrderIdGenerator {

    private val dateFormat = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
    private var dailyCounter = 1
    private var lastDate = ""

    /**
     * Generate unique order ID in format: ORD-YYYYMMDD-XXX
     */
    @Synchronized
    fun generateOrderId(): String {
        val currentDate = dateFormat.format(Date())

        // Reset counter if new day
        if (currentDate != lastDate) {
            dailyCounter = 1
            lastDate = currentDate
        }

        val orderId = "ORD-$currentDate-${String.format("%03d", dailyCounter)}"
        dailyCounter++

        return orderId
    }
}
