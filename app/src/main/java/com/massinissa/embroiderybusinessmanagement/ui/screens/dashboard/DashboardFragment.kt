package com.massinissa.embroiderybusinessmanagement.ui.screens.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.massinissa.embroiderybusinessmanagement.EmbroideryApp
import com.massinissa.embroiderybusinessmanagement.R
import com.massinissa.embroiderybusinessmanagement.databinding.FragmentDashboardBinding
import com.massinissa.embroiderybusinessmanagement.utils.DateFormatter
import com.massinissa.embroiderybusinessmanagement.utils.PriceCalculator
import com.massinissa.embroiderybusinessmanagement.viewmodel.OrderViewModel
import com.massinissa.embroiderybusinessmanagement.viewmodel.OrderViewModelFactory
import java.util.Calendar

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    private val viewModel: OrderViewModel by activityViewModels {
        OrderViewModelFactory((requireActivity().application as EmbroideryApp).repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupClickListeners()
        observeData()
    }

    private fun setupClickListeners() {
        // New Order button
        binding.btnNewOrder.setOnClickListener {
            // Navigate to create order screen
            // findNavController().navigate(R.id.action_dashboard_to_createOrder)
        }

        // View all pending orders
        binding.cardPendingOrders.setOnClickListener {
            // Navigate to orders list
          //   findNavController().navigate(R.id.action_dashboard_to_orders)
        }

        // View statistics
        binding.cardStatistics.setOnClickListener {
            // Navigate to statistics
            // findNavController().navigate(R.id.action_dashboard_to_statistics)
        }
    }

    private fun observeData() {
        // Observe pending orders count
        viewModel.pendingOrdersCount.observe(viewLifecycleOwner) { count ->
            binding.tvPendingOrdersCount.text = count?.toString() ?: "0"
        }

        // Observe pending orders for quick view
        viewModel.pendingOrders.observe(viewLifecycleOwner) { orders ->
            binding.tvTotalOrders.text = orders.size.toString()

            // Count urgent orders (due in 3 days or less)
            val urgentCount = orders.count { order ->
                order.dueDate?.let { dueDate ->
                    val daysRemaining = DateFormatter.getDaysRemaining(dueDate)
                    daysRemaining in 0..3
                } ?: false
            }
            binding.tvUrgentOrders.text = urgentCount.toString()
        }

        // Observe unpaid balance
        viewModel.totalUnpaidBalance.observe(viewLifecycleOwner) { balance ->
            val formatted = PriceCalculator.formatCurrency(balance ?: 0.0, "DZD")
            binding.tvUnpaidBalance.text = formatted

            // Show warning if balance is high
            if ((balance ?: 0.0) > 5000.0) {
                binding.tvUnpaidWarning.visibility = View.VISIBLE
            } else {
                binding.tvUnpaidWarning.visibility = View.GONE
            }
        }

        // Calculate this month's revenue
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_MONTH, 1)
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        val monthStart = calendar.timeInMillis
        val monthEnd = System.currentTimeMillis()

        viewModel.getTotalRevenue(monthStart, monthEnd).observe(viewLifecycleOwner) { revenue ->
            val formatted = PriceCalculator.formatCurrency(revenue ?: 0.0, "DZD")
            binding.tvMonthlyRevenue.text = formatted
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}



