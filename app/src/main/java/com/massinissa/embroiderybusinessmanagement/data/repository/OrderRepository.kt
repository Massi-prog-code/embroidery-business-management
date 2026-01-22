// FILE: data/repository/OrderRepository.kt
package com.massinissa.embroiderybusinessmanagement.data.repository

import androidx.lifecycle.LiveData
import com.massinissa.embroiderybusinessmanagement.data.database.dao.*
import com.massinissa.embroiderybusinessmanagement.data.database.entities.*
import com.massinissa.embroiderybusinessmanagement.data.models.OrderWithDetails
import com.massinissa.embroiderybusinessmanagement.data.models.PaymentSummary
import com.massinissa.embroiderybusinessmanagement.utils.PriceCalculator

class OrderRepository(
    private val orderDao: OrderDao,
    private val clientDao: ClientDao,
    private val paymentDao: PaymentDao
) {

    // ========== ORDER OPERATIONS ==========

    fun getPendingOrders(): LiveData<List<Order>> {
        return orderDao.getOrdersByStatus(
            listOf(OrderStatus.PENDING, OrderStatus.IN_PROGRESS)
        )
    }

    fun getCompletedOrders(): LiveData<List<Order>> {
        return orderDao.getCompletedOrders()
    }

    fun getOrderById(orderId: Long): LiveData<Order> {
        return orderDao.getOrderById(orderId)
    }

    suspend fun createOrder(order: Order): Long {
        return orderDao.insertOrder(order)
    }

    suspend fun updateOrder(order: Order) {
        orderDao.updateOrder(order)
    }

    suspend fun deleteOrder(order: Order) {
        orderDao.deleteOrder(order)
    }

    // ========== CLIENT OPERATIONS ==========

    fun getAllClients(): LiveData<List<Client>> {
        return clientDao.getAllClients()
    }

    fun getClientById(clientId: Long): LiveData<Client> {
        return clientDao.getClientById(clientId)
    }

    fun searchClients(query: String): LiveData<List<Client>> {
        return clientDao.searchClients(query)
    }

    suspend fun createClient(client: Client): Long {
        return clientDao.insertClient(client)
    }

    suspend fun updateClient(client: Client) {
        clientDao.updateClient(client)
    }

    // ========== PAYMENT OPERATIONS ==========

    fun getPaymentsForOrder(orderId: Long): LiveData<List<Payment>> {
        return paymentDao.getPaymentsByOrder(orderId)
    }

    fun getTotalPaidForOrder(orderId: Long): LiveData<Double?> {
        return paymentDao.getTotalPaidForOrder(orderId)
    }

    /**
     * Add payment and update order payment status
     */
    suspend fun addPayment(payment: Payment, order: Order) {
        // Insert payment
        paymentDao.insertPayment(payment)

        // Update order totals
        val newTotalPaid = order.totalPaid + payment.amount
        val newBalance = PriceCalculator.calculateBalance(order.totalPrice, newTotalPaid)
        val newStatus = PriceCalculator.determinePaymentStatus(order.totalPrice, newTotalPaid)

        val updatedOrder = order.copy(
            totalPaid = newTotalPaid,
            remainingBalance = newBalance,
            paymentStatus = newStatus,
            updatedAt = System.currentTimeMillis()
        )

        orderDao.updateOrder(updatedOrder)
    }

    /**
     * Create payment summary for an order
     */
    fun createPaymentSummary(order: Order, payments: List<Payment>): PaymentSummary {
        return PaymentSummary(
            totalPrice = order.totalPrice,
            totalPaid = order.totalPaid,
            remainingBalance = order.remainingBalance,
            paymentStatus = order.paymentStatus,
            paymentPercentage = PriceCalculator.calculatePaymentPercentage(
                order.totalPrice,
                order.totalPaid
            ),
            payments = payments
        )
    }

    /**
     * Mark order as pending payment
     */
    suspend fun markOrderAsPendingPayment(order: Order) {
        val updatedOrder = order.copy(
            paymentStatus = PaymentStatus.PENDING,
            updatedAt = System.currentTimeMillis()
        )
        orderDao.updateOrder(updatedOrder)
    }

    // ========== STATISTICS ==========

    fun getTotalRevenue(startDate: Long, endDate: Long): LiveData<Double?> {
        return orderDao.getTotalRevenueInRange(startDate, endDate)
    }

    fun getPendingOrdersCount(): LiveData<Int> {
        return orderDao.getPendingOrdersCount()
    }

    fun getTotalUnpaidBalance(): LiveData<Double?> {
        return orderDao.getTotalUnpaidBalance()
    }

    fun getOrdersByPaymentStatus(status: PaymentStatus): LiveData<List<Order>> {
        return orderDao.getOrdersByPaymentStatus(status)
    }
}
