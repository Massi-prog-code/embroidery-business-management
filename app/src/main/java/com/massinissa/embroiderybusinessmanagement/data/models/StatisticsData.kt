package com.massinissa.embroiderybusinessmanagement.data.models

data class StatisticsData(
    val totalOrders: Int = 0,
    val completedOrders: Int = 0,
    val pendingOrders: Int = 0,
    val totalRevenue: Double = 0.0,
    val totalPaid: Double = 0.0,
    val totalUnpaid: Double = 0.0,
    val averageOrderValue: Double = 0.0,
    val collectionRate: Double = 0.0
)