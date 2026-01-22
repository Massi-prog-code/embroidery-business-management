package com.massinissa.embroiderybusinessmanagement.viewmodel

import androidx.lifecycle.*
import com.massinissa.embroiderybusinessmanagement.data.database.entities.Order
import com.massinissa.embroiderybusinessmanagement.data.database.entities.OrderStatus
import com.massinissa.embroiderybusinessmanagement.data.database.entities.Payment
import com.massinissa.embroiderybusinessmanagement.data.database.entities.PaymentStatus
import com.massinissa.embroiderybusinessmanagement.data.repository.OrderRepository
import kotlinx.coroutines.launch

class OrderViewModel(private val repository: OrderRepository) : ViewModel() {

    // LiveData for orders
    val pendingOrders: LiveData<List<Order>> = repository.getPendingOrders()
    val completedOrders: LiveData<List<Order>> = repository.getCompletedOrders()

    // Current selected order
    private val _selectedOrderId = MutableLiveData<Long>()
    val selectedOrder: LiveData<Order> = _selectedOrderId.switchMap { orderId ->
        repository.getOrderById(orderId)
    }

    // Payments for selected order
    val selectedOrderPayments: LiveData<List<Payment>> = _selectedOrderId.switchMap { orderId ->
        repository.getPaymentsForOrder(orderId)
    }

    // Statistics
    val pendingOrdersCount: LiveData<Int> = repository.getPendingOrdersCount()
    val totalUnpaidBalance: LiveData<Double?> = repository.getTotalUnpaidBalance()

    // UI State
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    private val _successMessage = MutableLiveData<String?>()
    val successMessage: LiveData<String?> = _successMessage

    /**
     * Select an order to view details
     */
    fun selectOrder(orderId: Long) {
        _selectedOrderId.value = orderId
    }

    /**
     * Create new order
     */
    fun createOrder(order: Order, onSuccess: (Long) -> Unit) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val orderId = repository.createOrder(order)
                _successMessage.value = "Order created successfully"
                onSuccess(orderId)
            } catch (e: Exception) {
                _errorMessage.value = "Failed to create order: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Update existing order
     */
    fun updateOrder(order: Order) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                repository.updateOrder(order)
                _successMessage.value = "Order updated successfully"
            } catch (e: Exception) {
                _errorMessage.value = "Failed to update order: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Delete order
     */
    fun deleteOrder(order: Order) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                repository.deleteOrder(order)
                _successMessage.value = "Order deleted successfully"
            } catch (e: Exception) {
                _errorMessage.value = "Failed to delete order: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Mark order as completed
     */
    fun completeOrder(order: Order) {
        val completedOrder = order.copy(
            status = OrderStatus.COMPLETED,
            completionDate = System.currentTimeMillis(),
            updatedAt = System.currentTimeMillis()
        )
        updateOrder(completedOrder)
    }

    /**
     * Add payment to order
     */
    fun addPayment(payment: Payment, order: Order) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                repository.addPayment(payment, order)
                _successMessage.value = "Payment added successfully"
            } catch (e: Exception) {
                _errorMessage.value = "Failed to add payment: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Mark order as pending payment
     */
    fun markAsPendingPayment(order: Order) {
        viewModelScope.launch {
            try {
                repository.markOrderAsPendingPayment(order)
                _successMessage.value = "Order marked as pending payment"
            } catch (e: Exception) {
                _errorMessage.value = "Failed to update status: ${e.message}"
            }
        }
    }

    /**
     * Get orders by payment status
     */
    fun getOrdersByPaymentStatus(status: PaymentStatus): LiveData<List<Order>> {
        return repository.getOrdersByPaymentStatus(status)
    }

    /**
     * Clear messages
     */
    fun clearMessages() {
        _errorMessage.value = null
        _successMessage.value = null
    }
}
