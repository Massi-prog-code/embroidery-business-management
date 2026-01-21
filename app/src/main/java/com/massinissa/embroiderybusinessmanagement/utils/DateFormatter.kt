// ==========================================
// FILE: utils/DateFormatter.kt
// ==========================================

package com.massinissa.embroiderybusinessmanagement.utils

import java.text.SimpleDateFormat
import java.util.*

object DateFormatter {

    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    private val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    private val dateTimeFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())

    fun formatDate(timestamp: Long): String {
        return dateFormat.format(Date(timestamp))
    }

    fun formatTime(timestamp: Long): String {
        return timeFormat.format(Date(timestamp))
    }

    fun formatDateTime(timestamp: Long): String {
        return dateTimeFormat.format(Date(timestamp))
    }

    fun getDaysRemaining(dueDate: Long): Long {
        val now = System.currentTimeMillis()
        val diff = dueDate - now
        return diff / (1000 * 60 * 60 * 24)
    }

    fun isOverdue(dueDate: Long?): Boolean {
        if (dueDate == null) return false
        return dueDate < System.currentTimeMillis()
    }
}