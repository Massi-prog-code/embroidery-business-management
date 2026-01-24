
package com.massinissa.embroiderybusinessmanagement.ui.screens.orders

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.massinissa.embroiderybusinessmanagement.data.database.entities.Payment
import com.massinissa.embroiderybusinessmanagement.databinding.ItemPaymentHistoryBinding
import com.massinissa.embroiderybusinessmanagement.utils.DateFormatter
import com.massinissa.embroiderybusinessmanagement.utils.PriceCalculator

class PaymentHistoryAdapter :
    ListAdapter<Payment, PaymentHistoryAdapter.PaymentViewHolder>(PaymentDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentViewHolder {
        val binding = ItemPaymentHistoryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PaymentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PaymentViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class PaymentViewHolder(
        private val binding: ItemPaymentHistoryBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(payment: Payment) {
            binding.apply {
                tvPaymentDate.text = DateFormatter.formatDateTime(payment.paymentDate)
                tvPaymentMethod.text = payment.paymentMethod.displayName
                tvPaymentAmount.text = "+ ${PriceCalculator.formatCurrency(payment.amount, "DZD")}"

                payment.notes?.let { notes ->
                    tvPaymentNotes.text = notes
                    tvPaymentNotes.visibility = android.view.View.VISIBLE
                } ?: run {
                    tvPaymentNotes.visibility = android.view.View.GONE
                }

                payment.transactionReference?.let { ref ->
                    tvTransactionRef.text = "Ref: $ref"
                    tvTransactionRef.visibility = android.view.View.VISIBLE
                } ?: run {
                    tvTransactionRef.visibility = android.view.View.GONE
                }
            }
        }
    }

    class PaymentDiffCallback : DiffUtil.ItemCallback<Payment>() {
        override fun areItemsTheSame(oldItem: Payment, newItem: Payment): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Payment, newItem: Payment): Boolean {
            return oldItem == newItem
        }
    }
}
