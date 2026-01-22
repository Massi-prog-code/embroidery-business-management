package com.massinissa.embroiderybusinessmanagement.data.models

import androidx.room.Embedded
import androidx.room.Relation
import com.massinissa.embroiderybusinessmanagement.data.database.entities.Client
import com.massinissa.embroiderybusinessmanagement.data.database.entities.Order
import com.massinissa.embroiderybusinessmanagement.data.database.entities.Payment

/**
 * Order with complete client and payment information
 */
data class OrderWithDetails(
    @Embedded val order: Order,

    @Relation(
        parentColumn = "clientId",
        entityColumn = "id"
    )
    val client: Client,

    @Relation(
        parentColumn = "id",
        entityColumn = "orderId"
    )
    val payments: List<Payment>
)