
package com.massinissa.embroiderybusinessmanagement

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.massinissa.embroiderybusinessmanagement.data.database.entities.*
import com.massinissa.embroiderybusinessmanagement.utils.OrderIdGenerator
import com.massinissa.embroiderybusinessmanagement.utils.PriceCalculator
import com.massinissa.embroiderybusinessmanagement.viewmodel.OrderViewModel
import com.massinissa.embroiderybusinessmanagement.viewmodel.OrderViewModelFactory
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var orderViewModel: OrderViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupViewModel()
        setupToolbar()
        observeData()

        // FOR TESTING: Create sample data
        createSampleData()
    }

    private fun setupViewModel() {
        val app = application as EmbroideryApp
        val factory = OrderViewModelFactory(app.repository)
        orderViewModel = ViewModelProvider(this, factory)[OrderViewModel::class.java]
    }

    private fun setupToolbar() {
        supportActionBar?.apply {
            title = getString(R.string.app_name)
            elevation = 4f
        }
    }

    private fun observeData() {
        orderViewModel.pendingOrders.observe(this) { orders ->
            Toast.makeText(
                this,
                "You have ${orders.size} pending orders",
                Toast.LENGTH_SHORT
            ).show()
        }

        orderViewModel.errorMessage.observe(this) { error ->
            error?.let {
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
                orderViewModel.clearMessages()
            }
        }

        orderViewModel.successMessage.observe(this) { message ->
            message?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                orderViewModel.clearMessages()
            }
        }
    }

    /**
     * Create sample data for testing (REMOVE IN PRODUCTION)
     */
    private fun createSampleData() {
        lifecycleScope.launch {
            try {
                val app = application as EmbroideryApp
                val repository = app.repository

                // Create a test client
                val client = Client(
                    name = "Test Client",
                    phone = "0555-123-456",
                    address = "Bab Ezzouar, Algiers",
                    notes = "Test customer for development"
                )

                val clientId = repository.createClient(client)

                // Create a test order
                val totalPrice = PriceCalculator.calculateTotalPrice(
                    basePrice = 500.0,
                    stitchCount = 15000,
                    stitchRate = 10.0,
                    materialCost = 200.0,
                    additionalCost = 0.0
                )

                val order = Order(
                    orderId = OrderIdGenerator.generateOrderId(),
                    clientId = clientId,
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
                    totalPrice = totalPrice,
                    totalPaid = 0.0,
                    remainingBalance = totalPrice,
                    notes = "Test order for wedding dress embroidery"
                )

                orderViewModel.createOrder(order) { orderId ->
                    Toast.makeText(
                        this@MainActivity,
                        "Sample order created! ID: ${order.orderId}",
                        Toast.LENGTH_LONG
                    ).show()
                }

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
