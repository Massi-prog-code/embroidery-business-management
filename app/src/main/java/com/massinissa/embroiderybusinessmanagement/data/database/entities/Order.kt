package com.massinissa.embroiderybusinessmanagement.data.database.entities


import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "orders",
    foreignKeys = [
        ForeignKey(
            entity = Client::class,
            parentColumns = ["id"],
            childColumns = ["clientId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("clientId")]
)
data class Order(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val orderId: String,
    val clientId: Long,
    val orderDate: Long,
    val dueDate: Long? = null,
    val completionDate: Long? = null,
    val status: OrderStatus,
    val paymentStatus: PaymentStatus = PaymentStatus.UNPAID,
    val designPath: String? = null,
    val designType: DesignType,
    val stitchCount: Int = 0,
    val basePrice: Double = 0.0,
    val stitchRate: Double = 0.0,
    val materialCost: Double = 0.0,
    val additionalCost: Double = 0.0,
    val totalPrice: Double = 0.0,
    val totalPaid: Double = 0.0,
    val remainingBalance: Double = 0.0,
    val notes: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

enum class OrderStatus {
    PENDING,
    IN_PROGRESS,
    COMPLETED,
    CANCELLED
}

enum class DesignType {
    IMAGE,
    DST_FILE
}