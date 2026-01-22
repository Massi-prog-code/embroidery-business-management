package com.massinissa.embroiderybusinessmanagement.data.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "payments",
    foreignKeys = [
        ForeignKey(
            entity = Order::class,
            parentColumns = ["id"],
            childColumns = ["orderId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("orderId")]
)
data class Payment(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val orderId: Long,
    val amount: Double,
    val paymentDate: Long,
    val paymentMethod: PaymentMethod = PaymentMethod.CASH,
    val transactionReference: String? = null,
    val notes: String? = null
)

enum class PaymentMethod(val displayName: String) {
    CASH("Cash"),
    BANK_TRANSFER("Bank Transfer"),
    CHECK("Check"),
    MOBILE_PAYMENT("Mobile Payment"),
    CREDIT_CARD("Credit Card"),
    OTHER("Other");

    companion object {
        fun fromString(value: String): PaymentMethod {
            return values().find { it.name == value } ?: CASH
        }
    }
}