
package com.massinissa.embroiderybusinessmanagement.ui.screens.orders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayout
import com.massinissa.embroiderybusinessmanagement.EmbroideryApp
import com.massinissa.embroiderybusinessmanagement.databinding.FragmentOrderListBinding
import com.massinissa.embroiderybusinessmanagement.viewmodel.OrderViewModel
import com.massinissa.embroiderybusinessmanagement.viewmodel.OrderViewModelFactory

class OrderListFragment : Fragment() {

    private var _binding: FragmentOrderListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: OrderViewModel by activityViewModels {
        OrderViewModelFactory((requireActivity().application as EmbroideryApp).repository)
    }

    private lateinit var orderAdapter: OrderAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrderListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupTabs()
        setupFab()
        observeData()
    }

    private fun setupRecyclerView() {
        orderAdapter = OrderAdapter(
            onOrderClick = { order ->
                // Navigate to order details
                viewModel.selectOrder(order.id)
                // findNavController().navigate(R.id.action_orders_to_orderDetails)
            },
            onPaymentClick = { order ->
                // Show payment dialog
                showPaymentDialog(order)
            }
        )

        binding.recyclerViewOrders.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = orderAdapter
        }
    }

    private fun setupTabs() {
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> observePendingOrders()
                    1 -> observeCompletedOrders()
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }

    private fun setupFab() {
        binding.fabNewOrder.setOnClickListener {
            // Navigate to create order screen
            // findNavController().navigate(R.id.action_orders_to_createOrder)
        }
    }

    private fun observeData() {
        // Start with pending orders
        observePendingOrders()
    }

    private fun observePendingOrders() {
        viewModel.pendingOrders.observe(viewLifecycleOwner) { orders ->
            orderAdapter.submitList(orders)

            // Show/hide empty state
            if (orders.isEmpty()) {
                binding.tvEmptyState.visibility = View.VISIBLE
                binding.recyclerViewOrders.visibility = View.GONE
            } else {
                binding.tvEmptyState.visibility = View.GONE
                binding.recyclerViewOrders.visibility = View.VISIBLE
            }
        }
    }

    private fun observeCompletedOrders() {
        viewModel.completedOrders.observe(viewLifecycleOwner) { orders ->
            orderAdapter.submitList(orders)

            if (orders.isEmpty()) {
                binding.tvEmptyState.visibility = View.VISIBLE
                binding.recyclerViewOrders.visibility = View.GONE
            } else {
                binding.tvEmptyState.visibility = View.GONE
                binding.recyclerViewOrders.visibility = View.VISIBLE
            }
        }
    }

    private fun showPaymentDialog(order: com.massinissa.embroiderybusinessmanagement.data.database.entities.Order) {
        // TODO: Show payment dialog
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}