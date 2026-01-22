
// FILE: EmbroideryApp.kt
package com.massinissa.embroiderybusinessmanagement

import android.app.Application
import com.massinissa.embroiderybusinessmanagement.data.database.AppDatabase
import com.massinissa.embroiderybusinessmanagement.data.repository.OrderRepository

class EmbroideryApp : Application() {

    // Database instance
    val database by lazy { AppDatabase.getDatabase(this) }

    // Repository instance
    val repository by lazy {
        OrderRepository(
            database.orderDao(),
            database.clientDao(),
            database.paymentDao()
        )
    }

    override fun onCreate() {
        super.onCreate()
        // Initialize any app-wide configurations here
    }
}
