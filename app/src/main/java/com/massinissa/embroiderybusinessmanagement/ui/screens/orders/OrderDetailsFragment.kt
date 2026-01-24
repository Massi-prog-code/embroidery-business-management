
package com.massinissa.embroiderybusinessmanagement.ui.screens.orders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.massinissa.embroiderybusinessmanagement.EmbroideryApp
import com.massinissa.embroiderybusinessmanagement.databinding.FragmentOrderDetailsBinding
import com.massinissa.embroiderybusinessmanagement.utils.DateFormatter
import com.massinissa.embroiderybusinessmanagement.utils.PaymentStatusHelper
import com.massinissa.embroiderybusinessmanagement.utils.PriceCalculator
import com.massinissa.embroiderybusinessmanagement.viewmodel.OrderViewModel
import com.massinissa.embroiderybusinessmanagement.viewmodel.OrderViewModelFactory

class OrderDetailsFragment : Fragment() {

    private var _binding: FragmentOrderDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: OrderViewModel by activityViewModels {
        OrderViewModelFactory((requireActivity().application as EmbroideryApp).repository)
    }

    private lateinit var paymentHistoryAdapter: PaymentHistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrderDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupPaymentHistory()
        setupClickListeners()
        observeData()
    }

    private fun setupPaymentHistory() {
        paymentHistoryAdapter = PaymentHistoryAdapter()

        binding.recyclerViewPaymentHistory.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = paymentHistoryAdapter
        }
    }

    private fun setupClickListeners() {
        binding.btnAddPayment.setOnClickListener {
            // Show add payment dialog
        }

        binding.btnMarkCompleted.setOnClickListener {
            viewModel.selectedOrder.value?.let { order ->
                viewModel.completeOrder(order)
            }
        }

        binding.btnEditOrder.setOnClickListener {
            // Navigate to edit order
        }
    }

    private fun observeData() {
        // Observe selected order
        viewModel.selectedOrder.observe(viewLifecycleOwner) { order ->
            order?.let {
                binding.apply {
                    // Header
                    tvOrderId.text = it.orderId

                    // Payment Status
                    val statusIcon = PaymentStatusHelper.getStatusIcon(it.paymentStatus)
                    tvPaymentStatus.text = "$statusIcon ${it.paymentStatus.displayName}"
                    tvPaymentStatus.setTextColor(PaymentStatusHelper.getStatusColor(it.paymentStatus))

                    tvStatusMessage.text = PaymentStatusHelper.getStatusMessage(it.paymentStatus)

                    // Dates
                    tvOrderDate.text = DateFormatter.formatDate(it.orderDate)
                    it.dueDate?.let { dueDate ->
                        tvDueDate.text = DateFormatter.formatDate(dueDate)
                    }

                    // Order Status
                    tvOrderStatus.text = it.status.name

                    // Design Info
                    tvDesignType.text = it.designType.name
                    tvStitchCount.text = "${it.stitchCount} stitches"

                    // Pricing Breakdown
                    tvBasePrice.text = PriceCalculator.formatCurrency(it.basePrice, "DZD")
                    tvStitchCost.text = PriceCalculator.formatCurrency(
                        (it.stitchCount / 1000.0) * it.stitchRate, "DZD"
                    )
                    tvMaterialCost.text = PriceCalculator.formatCurrency(it.materialCost, "DZD")
                    tvAdditionalCost.text = PriceCalculator.formatCurrency(it.additionalCost, "DZD")

                    // Payment Summary
                    tvTotalPrice.text = PriceCalculator.formatCurrency(it.totalPrice, "DZD")
                    tvTotalPaid.text = PriceCalculator.formatCurrency(it.totalPaid, "DZD")
                    tvRemainingBalance.text = PriceCalculator.formatCurrency(it.remainingBalance, "DZD")

                    // Progress
                    val percentage = PriceCalculator.calculatePaymentPercentage(
                        it.totalPrice,
                        it.totalPaid
                    )
                    progressPayment.progress = percentage
                    tvPaymentPercentage.text = "$percentage%"

                    // Notes
                    it.notes?.let { notes ->
                        tvNotes.text = notes
                        tvNotes.visibility = View.VISIBLE
                    } ?: run {
                        tvNotes.visibility = View.GONE
                    }
                }
            }
        }

        // Observe payments
        viewModel.selectedOrderPayments.observe(viewLifecycleOwner) { payments ->
            paymentHistoryAdapter.submitList(payments)

            if (payments.isEmpty()) {
                binding.tvNoPayments.visibility = View.VISIBLE
                binding.recyclerViewPaymentHistory.visibility = View.GONE
            } else {
                binding.tvNoPayments.visibility = View.GONE
                binding.recyclerViewPaymentHistory.visibility = View.VISIBLE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
