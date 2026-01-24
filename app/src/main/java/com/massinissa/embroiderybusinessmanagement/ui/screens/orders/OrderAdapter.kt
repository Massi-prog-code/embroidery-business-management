package com.massinissa.embroiderybusinessmanagement.ui.screens.orders

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.massinissa.embroiderybusinessmanagement.data.database.entities.Order
import com.massinissa.embroiderybusinessmanagement.databinding.ItemOrderBinding
import com.massinissa.embroiderybusinessmanagement.utils.DateFormatter
import com.massinissa.embroiderybusinessmanagement.utils.PaymentStatusHelper
import com.massinissa.embroiderybusinessmanagement.utils.PriceCalculator

class OrderAdapter(
    private val onOrderClick: (Order) -> Unit,
    private val onPaymentClick: (Order) -> Unit
) : ListAdapter<Order, OrderAdapter.OrderViewHolder>(OrderDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val binding = ItemOrderBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return OrderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class OrderViewHolder(
        private val binding: ItemOrderBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(order: Order) {
            binding.apply {
                // Order ID and Status
                tvOrderId.text = order.orderId
                tvOrderStatus.text = order.status.name

                // Payment Status Badge
                val statusIcon = PaymentStatusHelper.getStatusIcon(order.paymentStatus)
                val statusColor = PaymentStatusHelper.getStatusColor(order.paymentStatus)
                tvPaymentStatus.text = "$statusIcon ${order.paymentStatus.displayName}"
                tvPaymentStatus.setTextColor(statusColor)

                // Dates
                tvOrderDate.text = "Order: ${DateFormatter.formatDate(order.orderDate)}"
                order.dueDate?.let { dueDate ->
                    val daysRemaining = DateFormatter.getDaysRemaining(dueDate)
                    tvDueDate.text = when {
                        daysRemaining < 0 -> "⚠️ Overdue"
                        daysRemaining == 0L -> "⏰ Due today"
                        daysRemaining <= 3 -> "⏰ Due in $daysRemaining days"
                        else -> "Due: ${DateFormatter.formatDate(dueDate)}"
                    }

                    // Color code based on urgency
                    tvDueDate.setTextColor(when {
                        daysRemaining < 0 -> Color.RED
                        daysRemaining <= 3 -> Color.parseColor("#FF9800")
                        else -> Color.GRAY
                    })
                }

                // Pricing
                tvTotalPrice.text = PriceCalculator.formatCurrency(order.totalPrice, "DZD")
                tvPaidAmount.text = PriceCalculator.formatCurrency(order.totalPaid, "DZD")
                tvRemainingBalance.text = PriceCalculator.formatCurrency(order.remainingBalance, "DZD")

                // Progress bar
                val percentage = PriceCalculator.calculatePaymentPercentage(
                    order.totalPrice,
                    order.totalPaid
                )
                progressPayment.progress = percentage
                tvPaymentPercentage.text = "$percentage%"

                // Stitch count
                tvStitchCount.text = "${order.stitchCount} stitches"

                // Click listeners
                root.setOnClickListener { onOrderClick(order) }
                btnAddPayment.setOnClickListener { onPaymentClick(order) }
            }
        }
    }

    class OrderDiffCallback : DiffUtil.ItemCallback<Order>() {
        override fun areItemsTheSame(oldItem: Order, newItem: Order): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Order, newItem: Order): Boolean {
            return oldItem == newItem
        }
    }
}
