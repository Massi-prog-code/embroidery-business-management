
package com.massinissa.embroiderybusinessmanagement

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.massinissa.embroiderybusinessmanagement.data.database.entities.*
import com.massinissa.embroiderybusinessmanagement.databinding.ActivityMainBinding
import com.massinissa.embroiderybusinessmanagement.ui.screens.dashboard.DashboardFragment
import com.massinissa.embroiderybusinessmanagement.ui.screens.orders.OrderListFragment
import com.massinissa.embroiderybusinessmanagement.utils.OrderIdGenerator
import com.massinissa.embroiderybusinessmanagement.utils.PriceCalculator
import com.massinissa.embroiderybusinessmanagement.viewmodel.OrderViewModel
import com.massinissa.embroiderybusinessmanagement.viewmodel.OrderViewModelFactory
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var orderViewModel: OrderViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize ViewModel
        setupViewModel()

        // Observe data
        observeData()

        // Setup navigation AFTER setting content view
        setupBottomNavigation()

        // Show dashboard by default (ALWAYS load on first create)
        if (savedInstanceState == null) {
            binding.bottomNavigation.selectedItemId = R.id.nav_dashboard
            loadDashboardFragment()
        }

        // Create sample data (ONLY FOR TESTING - Remove in production)
        // Delay to ensure database is ready
        binding.root.postDelayed({
            createSampleDataIfNeeded()
        }, 1000)
    }

    private fun setupViewModel() {
        val app = application as EmbroideryApp
        val factory = OrderViewModelFactory(app.repository)
        orderViewModel = ViewModelProvider(this, factory)[OrderViewModel::class.java]
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_dashboard -> {
                    loadDashboardFragment()
                    true
                }
                R.id.nav_orders -> {
                    loadOrdersFragment()
                    true
                }
                R.id.nav_clients -> {
                    // TODO: Implement ClientsFragment
                    Toast.makeText(this, "Clients screen - Coming soon!", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.nav_statistics -> {
                    // TODO: Implement StatisticsFragment
                    Toast.makeText(this, "Statistics screen - Coming soon!", Toast.LENGTH_SHORT).show()
                    true
                }
                else -> false
            }
        }
    }

    private fun loadDashboardFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, DashboardFragment())
            .commitNow() // Use commitNow to ensure immediate execution
    }

    private fun loadOrdersFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, OrderListFragment())
            .commitNow() // Use commitNow to ensure immediate execution
    }

    private fun observeData() {
        // Observe error messages
        orderViewModel.errorMessage.observe(this) { error ->
            error?.let {
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
                orderViewModel.clearMessages()
            }
        }

        // Observe success messages
        orderViewModel.successMessage.observe(this) { message ->
            message?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                orderViewModel.clearMessages()
            }
        }

        // Observe pending orders count (for notification badge)
        orderViewModel.pendingOrdersCount.observe(this) { count ->
            // You can add a badge to the bottom navigation here
            // Example: binding.bottomNavigation.getOrCreateBadge(R.id.nav_orders).number = count ?: 0
        }
    }

    /**
     * Create sample data for testing
     * IMPORTANT: Remove this in production or add a flag to disable it
     */
    private fun createSampleDataIfNeeded() {
        lifecycleScope.launch {
            try {
                val app = application as EmbroideryApp
                val repository = app.repository

                // Check if we already have data
                val existingOrders = repository.getPendingOrders()

                // Only create sample data if database is empty
                // This prevents creating duplicate data every time the app opens
                var shouldCreateSampleData = true

                existingOrders.observe(this@MainActivity) { orders ->
                    if (orders.isNotEmpty()) {
                        shouldCreateSampleData = false
                    }
                }

                // Wait a bit for the observer to check
                kotlinx.coroutines.delay(500)

                if (!shouldCreateSampleData) {
                    return@launch
                }

                // Create sample client 1
                val client1 = Client(
                    name = "Fatima Zerrouki",
                    phone = "0555-123-456",
                    address = "Bab Ezzouar, Algiers",
                    email = "fatima.z@email.dz",
                    notes = "Regular customer - Wedding dresses"
                )
                val clientId1 = repository.createClient(client1)

                // Create sample client 2
                val client2 = Client(
                    name = "Ahmed Benali",
                    phone = "0661-234-567",
                    address = "Hussein Dey, Algiers",
                    notes = "Wholesale orders"
                )
                val clientId2 = repository.createClient(client2)

                // Create sample client 3
                val client3 = Client(
                    name = "Sarah Mohammed",
                    phone = "0770-345-678",
                    address = "Birtouta, Algiers",
                    notes = "Custom embroidery designs"
                )
                val clientId3 = repository.createClient(client3)

                // Create sample order 1 - Unpaid
                val totalPrice1 = PriceCalculator.calculateTotalPrice(
                    basePrice = 500.0,
                    stitchCount = 15000,
                    stitchRate = 10.0,
                    materialCost = 200.0,
                    additionalCost = 0.0
                )

                val order1 = Order(
                    orderId = OrderIdGenerator.generateOrderId(),
                    clientId = clientId1,
                    orderDate = System.currentTimeMillis(),
                    dueDate = System.currentTimeMillis() + (7 * 24 * 60 * 60 * 1000), // 7 days
                    status = OrderStatus.PENDING,
                    paymentStatus = PaymentStatus.UNPAID,
                    designType = DesignType.IMAGE,
                    stitchCount = 15000,
                    basePrice = 500.0,
                    stitchRate = 10.0,
                    materialCost = 200.0,
                    additionalCost = 0.0,
                    totalPrice = totalPrice1,
                    totalPaid = 0.0,
                    remainingBalance = totalPrice1,
                    notes = "Wedding dress with floral embroidery"
                )
                repository.createOrder(order1)

                // Create sample order 2 - Partially Paid
                val totalPrice2 = PriceCalculator.calculateTotalPrice(
                    basePrice = 800.0,
                    stitchCount = 25000,
                    stitchRate = 12.0,
                    materialCost = 300.0,
                    additionalCost = 100.0
                )

                val order2 = Order(
                    orderId = OrderIdGenerator.generateOrderId(),
                    clientId = clientId2,
                    orderDate = System.currentTimeMillis() - (3 * 24 * 60 * 60 * 1000), // 3 days ago
                    dueDate = System.currentTimeMillis() + (5 * 24 * 60 * 60 * 1000), // 5 days
                    status = OrderStatus.IN_PROGRESS,
                    paymentStatus = PaymentStatus.PARTIALLY_PAID,
                    designType = DesignType.DST_FILE,
                    stitchCount = 25000,
                    basePrice = 800.0,
                    stitchRate = 12.0,
                    materialCost = 300.0,
                    additionalCost = 100.0,
                    totalPrice = totalPrice2,
                    totalPaid = 600.0,
                    remainingBalance = totalPrice2 - 600.0,
                    notes = "Traditional kaftan with gold thread"
                )
                val orderId2 = repository.createOrder(order2)

                // Add payment to order 2
                val payment1 = Payment(
                    orderId = orderId2,
                    amount = 600.0,
                    paymentDate = System.currentTimeMillis() - (2 * 24 * 60 * 60 * 1000), // 2 days ago
                    paymentMethod = PaymentMethod.CASH,
                    notes = "First installment"
                )
                repository.addPayment(payment1, order2)

                // Create sample order 3 - Paid and Completed
                val totalPrice3 = PriceCalculator.calculateTotalPrice(
                    basePrice = 600.0,
                    stitchCount = 18000,
                    stitchRate = 10.0,
                    materialCost = 250.0,
                    additionalCost = 50.0
                )

                val order3 = Order(
                    orderId = OrderIdGenerator.generateOrderId(),
                    clientId = clientId3,
                    orderDate = System.currentTimeMillis() - (10 * 24 * 60 * 60 * 1000), // 10 days ago
                    dueDate = System.currentTimeMillis() - (3 * 24 * 60 * 60 * 1000), // 3 days ago
                    completionDate = System.currentTimeMillis() - (1 * 24 * 60 * 60 * 1000), // 1 day ago
                    status = OrderStatus.COMPLETED,
                    paymentStatus = PaymentStatus.PAID,
                    designType = DesignType.DST_FILE,
                    stitchCount = 18000,
                    basePrice = 600.0,
                    stitchRate = 10.0,
                    materialCost = 250.0,
                    additionalCost = 50.0,
                    totalPrice = totalPrice3,
                    totalPaid = totalPrice3,
                    remainingBalance = 0.0,
                    notes = "Jacket with custom logo embroidery"
                )
                val orderId3 = repository.createOrder(order3)

                // Add payment to order 3
                val payment2 = Payment(
                    orderId = orderId3,
                    amount = totalPrice3,
                    paymentDate = System.currentTimeMillis() - (5 * 24 * 60 * 60 * 1000), // 5 days ago
                    paymentMethod = PaymentMethod.BANK_TRANSFER,
                    transactionReference = "TRX123456789",
                    notes = "Full payment via bank transfer"
                )
                repository.addPayment(payment2, order3)

                // Create sample order 4 - Pending Payment
                val totalPrice4 = PriceCalculator.calculateTotalPrice(
                    basePrice = 400.0,
                    stitchCount = 12000,
                    stitchRate = 10.0,
                    materialCost = 150.0,
                    additionalCost = 0.0
                )

                val order4 = Order(
                    orderId = OrderIdGenerator.generateOrderId(),
                    clientId = clientId1,
                    orderDate = System.currentTimeMillis() - (1 * 24 * 60 * 60 * 1000), // 1 day ago
                    dueDate = System.currentTimeMillis() + (10 * 24 * 60 * 60 * 1000), // 10 days
                    status = OrderStatus.PENDING,
                    paymentStatus = PaymentStatus.PENDING,
                    designType = DesignType.IMAGE,
                    stitchCount = 12000,
                    basePrice = 400.0,
                    stitchRate = 10.0,
                    materialCost = 150.0,
                    additionalCost = 0.0,
                    totalPrice = totalPrice4,
                    totalPaid = 0.0,
                    remainingBalance = totalPrice4,
                    notes = "Bank transfer initiated - waiting for confirmation"
                )
                repository.createOrder(order4)

                Toast.makeText(
                    this@MainActivity,
                    "Sample data created successfully! 4 orders and 3 clients added.",
                    Toast.LENGTH_LONG
                ).show()

            } catch (e: Exception) {
                Toast.makeText(
                    this@MainActivity,
                    "Error creating sample data: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}

// ==========================================
// FILE: activity_main.xml (UPDATED)
// Location: app/src/main/res/layout/
// ==========================================





