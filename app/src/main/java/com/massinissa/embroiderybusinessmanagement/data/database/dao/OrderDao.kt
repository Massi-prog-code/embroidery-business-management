package com.massinissa.embroiderybusinessmanagement.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.massinissa.embroiderybusinessmanagement.data.database.entities.Order
import com.massinissa.embroiderybusinessmanagement.data.database.entities.OrderStatus
import com.massinissa.embroiderybusinessmanagement.data.database.entities.PaymentStatus

@Dao
interface OrderDao {

    @Query("SELECT * FROM orders WHERE status IN (:statuses) ORDER BY dueDate ASC")
    fun getOrdersByStatus(statuses: List<OrderStatus>): LiveData<List<Order>>

    @Query("SELECT * FROM orders WHERE status = 'COMPLETED' ORDER BY completionDate DESC")
    fun getCompletedOrders(): LiveData<List<Order>>

    @Query("SELECT * FROM orders WHERE id = :orderId")
    fun getOrderById(orderId: Long): LiveData<Order>

    @Query("SELECT * FROM orders WHERE clientId = :clientId ORDER BY orderDate DESC")
    fun getOrdersByClient(clientId: Long): LiveData<List<Order>>

    @Insert
    suspend fun insertOrder(order: Order): Long

    @Update
    suspend fun updateOrder(order: Order)

    @Delete
    suspend fun deleteOrder(order: Order)

    @Query("SELECT COUNT(*) FROM orders WHERE status != 'COMPLETED'")
    fun getPendingOrdersCount(): LiveData<Int>

    @Query("SELECT SUM(totalPrice) FROM orders WHERE status = 'COMPLETED' AND completionDate >= :startDate AND completionDate <= :endDate")
    fun getTotalRevenueInRange(startDate: Long, endDate: Long): LiveData<Double?>

    @Query("SELECT SUM(remainingBalance) FROM orders WHERE status != 'CANCELLED' AND paymentStatus != 'PAID'")
    fun getTotalUnpaidBalance(): LiveData<Double?>

    @Query("SELECT * FROM orders WHERE paymentStatus = :status ORDER BY dueDate ASC")
    fun getOrdersByPaymentStatus(status: PaymentStatus): LiveData<List<Order>>
}