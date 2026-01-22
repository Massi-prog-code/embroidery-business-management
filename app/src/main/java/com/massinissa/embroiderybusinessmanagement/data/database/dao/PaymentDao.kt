package com.massinissa.embroiderybusinessmanagement.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.massinissa.embroiderybusinessmanagement.data.database.entities.Payment

@Dao
interface PaymentDao {

    @Query("SELECT * FROM payments WHERE orderId = :orderId ORDER BY paymentDate DESC")
    fun getPaymentsByOrder(orderId: Long): LiveData<List<Payment>>

    @Query("SELECT SUM(amount) FROM payments WHERE orderId = :orderId")
    fun getTotalPaidForOrder(orderId: Long): LiveData<Double?>

    @Query("SELECT * FROM payments WHERE paymentDate >= :startDate AND paymentDate <= :endDate ORDER BY paymentDate DESC")
    fun getPaymentsInRange(startDate: Long, endDate: Long): LiveData<List<Payment>>

    @Insert
    suspend fun insertPayment(payment: Payment): Long

    @Update
    suspend fun updatePayment(payment: Payment)

    @Delete
    suspend fun deletePayment(payment: Payment)
}