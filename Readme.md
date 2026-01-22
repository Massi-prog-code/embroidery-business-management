# 🧵 Embroidery Business Manager

[![Android](https://img.shields.io/badge/Platform-Android-green.svg)](https://developer.android.com)
[![Kotlin](https://img.shields.io/badge/Language-Kotlin-blue.svg)](https://kotlinlang.org)
[![License](https://img.shields.io/badge/License-MIT-red.svg)](LICENSE)
[![API](https://img.shields.io/badge/API-24%2B-brightgreen.svg)](https://android-arsenal.com/api?level=24)

A modern Android application designed for seamstresses and embroidery workers to digitally manage their business operations, track orders, calculate pricing automatically, and monitor payments with ease.

<p align="center">
  <img src="screenshots/app_demo.gif" alt="App Demo" width="250"/>
  <img src="screenshots/order_list.png" alt="Order List" width="250"/>
  <img src="screenshots/payment_tracking.png" alt="Payment Tracking" width="250"/>
</p>

---

## 📋 Table of Contents

- [Features](#-features)
- [Screenshots](#-screenshots)
- [Tech Stack](#-tech-stack)
- [Architecture](#-architecture)
- [Installation](#-installation)
- [Usage](#-usage)
- [DST File Support](#-dst-file-support)
- [Payment System](#-payment-system)
- [Database Schema](#-database-schema)
- [Contributing](#-contributing)
- [Roadmap](#-roadmap)
- [License](#-license)
- [Acknowledgments](#-acknowledgments)

---

## 🎯 Features

### 📦 Order Management
- ✅ Create and track embroidery/sewing orders
- ✅ Auto-generated unique order IDs (`ORD-YYYYMMDD-XXX`)
- ✅ Order status tracking (Pending, In Progress, Completed, Cancelled)
- ✅ Due date management with visual indicators
- ✅ Quick search and filter capabilities

### 👥 Client Management
- ✅ Store client information (name, phone, address, notes)
- ✅ View complete order history per client
- ✅ Quick search by name or phone number
- ✅ One-tap call functionality

### 🎨 Design Handling
- ✅ Support for image files (PNG, JPG)
- ✅ **DST embroidery file parser** - automatically reads stitch count
- ✅ **DST to image preview** - generates visual previews
- ✅ Design preview gallery
- ✅ Stitch count extraction

### 💰 Smart Price Calculator
- ✅ **Automatic price calculation** based on:
    - Base sewing/embroidery price
    - Stitch count (extracted from DST files)
    - Price per 1,000 stitches
    - Material costs (fabric, thread, accessories)
    - Additional charges (rush orders, modifications)
- ✅ Configurable pricing in settings
- ✅ Price breakdown display

### 💳 Advanced Payment Tracking
- ✅ **4 Payment Status States:**
    - 🔴 **Unpaid** - No payment received
    - 🟡 **Pending Payment** - Payment being processed
    - 🔵 **Partially Paid** - Installment payments
    - 🟢 **Paid** - Fully paid
- ✅ Multiple payment entries per order
- ✅ Payment history timeline
- ✅ Balance calculation
- ✅ Payment progress indicators
- ✅ Support for multiple payment methods (Cash, Bank Transfer, Check, Mobile Payment)

### 📊 Statistics & Analytics
- ✅ Revenue tracking (daily, monthly, yearly)
- ✅ Order completion metrics
- ✅ Payment collection analysis
- ✅ Total unpaid balance overview
- ✅ Visual charts and graphs
- ✅ Client analytics

### 🔧 Additional Features
- ✅ Offline-first architecture
- ✅ Data backup and restore
- ✅ Simple, senior-friendly UI
- ✅ Large buttons and clear text
- ✅ Dark mode support (optional)

---

## 📸 Screenshots

<p align="center">
  <img src="screenshots/dashboard.png" alt="Dashboard" width="200"/>
  <img src="screenshots/create_order.png" alt="Create Order" width="200"/>
  <img src="screenshots/order_details.png" alt="Order Details" width="200"/>
  <img src="screenshots/payment_screen.png" alt="Payment Screen" width="200"/>
</p>

---

## 🛠 Tech Stack

### Core Technologies
- **Language:** Kotlin
- **Minimum SDK:** API 24 (Android 7.0)
- **Target SDK:** API 34 (Android 14)

### Architecture & Libraries
- **Architecture:** MVVM (Model-View-ViewModel)
- **Database:** Room (SQLite)
- **Async:** Kotlin Coroutines + Flow
- **Lifecycle:** AndroidX Lifecycle components
- **UI:** ViewBinding / XML Layouts
- **Charts:** MPAndroidChart
- **Image Loading:** Coil
- **Navigation:** AndroidX Navigation Component

### Key Dependencies
```gradle
// Room Database
implementation "androidx.room:room-runtime:2.6.1"
implementation "androidx.room:room-ktx:2.6.1"
ksp "androidx.room:room-compiler:2.6.1"

// ViewModel & LiveData
implementation "androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0"
implementation "androidx.lifecycle:lifecycle-livedata-ktx:2.7.0"

// Coroutines
implementation "org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3"

// Charts
implementation "com.github.PhilJay:MPAndroidChart:v3.1.0"

// Image Loading
implementation "io.coil-kt:coil:2.5.0"
```

---

## 🏗 Architecture

This app follows the **MVVM (Model-View-ViewModel)** architecture pattern with a clean separation of concerns:

```
┌─────────────────────────────────────────────────────┐
│                    Presentation Layer                │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐ │
│  │  Activity/  │  │  ViewModel  │  │   LiveData  │ │
│  │  Fragment   │◄─┤             │◄─┤             │ │
│  └─────────────┘  └─────────────┘  └─────────────┘ │
└───────────────────────────┬─────────────────────────┘
                            │
┌───────────────────────────▼─────────────────────────┐
│                    Domain Layer                      │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐ │
│  │ Repository  │  │   Use Cases │  │   Models    │ │
│  └─────────────┘  └─────────────┘  └─────────────┘ │
└───────────────────────────┬─────────────────────────┘
                            │
┌───────────────────────────▼─────────────────────────┐
│                     Data Layer                       │
│  ┌─────────────┐  ┌─────────────┐  ┌─────────────┐ │
│  │  Room DAO   │  │   Entities  │  │   Utility   │ │
│  └─────────────┘  └─────────────┘  └─────────────┘ │
└─────────────────────────────────────────────────────┘
```

### Project Structure
```
app/
├── data/
│   ├── database/
│   │   ├── AppDatabase.kt
│   │   ├── entities/
│   │   │   ├── Client.kt
│   │   │   ├── Order.kt
│   │   │   ├── Payment.kt
│   │   │   └── PaymentStatus.kt
│   │   └── dao/
│   │       ├── ClientDao.kt
│   │       ├── OrderDao.kt
│   │       └── PaymentDao.kt
│   ├── repository/
│   │   └── OrderRepository.kt
│   └── models/
│       ├── OrderWithDetails.kt
│       └── PaymentSummary.kt
├── ui/
│   ├── screens/
│   │   ├── dashboard/
│   │   ├── orders/
│   │   ├── clients/
│   │   ├── statistics/
│   │   └── settings/
│   ├── adapters/
│   └── components/
├── viewmodel/
│   ├── OrderViewModel.kt
│   ├── PaymentViewModel.kt
│   └── StatisticsViewModel.kt
├── utils/
│   ├── DSTParser.kt
│   ├── PriceCalculator.kt
│   ├── OrderIdGenerator.kt
│   └── PaymentStatusHelper.kt
└── MainActivity.kt
```

---

## 📥 Installation

### Prerequisites
- Android Studio Arctic Fox or later
- JDK 17 or higher
- Android SDK (API 24+)

### Steps

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/embroidery-business-manager.git
   cd embroidery-business-manager
   ```

2. **Open in Android Studio**
   ```
   File → Open → Select the project directory
   ```

3. **Sync Gradle**
   ```
   The IDE will automatically prompt to sync Gradle files
   Or: File → Sync Project with Gradle Files
   ```

4. **Build the project**
   ```
   Build → Make Project (Ctrl+F9)
   ```

5. **Run on device/emulator**
   ```
   Run → Run 'app' (Shift+F10)
   ```

### Building APK
```bash
# Debug APK
./gradlew assembleDebug

# Release APK (requires signing configuration)
./gradlew assembleRelease
```

---

## 🚀 Usage

### Creating Your First Order

1. **Launch the app** and tap **"New Order"**
2. **Select or add a client** (name and phone required)
3. **Upload a design:**
    - Take a photo
    - Choose from gallery
    - Upload DST file (stitch count auto-extracted)
4. **Enter pricing details:**
    - Base price (or use default)
    - Material costs
    - Additional charges
    - *Total calculated automatically*
5. **Set due date** (optional)
6. **Tap "Save Order"**

### Recording Payments

1. **Open an order** from the pending list
2. **Tap "Add Payment"**
3. **Enter amount received**
4. **Select payment method** (Cash, Bank Transfer, etc.)
5. **Add notes** (optional, e.g., "First installment")
6. **Tap "Save"**
    - Payment status updates automatically
    - Progress bar shows completion percentage

### Viewing Statistics

1. **Navigate to Dashboard/Statistics**
2. **Select time period** (Daily, Monthly, Yearly)
3. **View insights:**
    - Total revenue
    - Orders completed
    - Unpaid balance
    - Payment collection rate
    - Popular designs

---

## 🎨 DST File Support

### What is DST?

DST (Tajima) is the most common embroidery machine file format. This app includes a **custom DST parser** that:

- ✅ Reads binary DST files
- ✅ Extracts total stitch count
- ✅ Parses stitch coordinates
- ✅ Generates visual preview images
- ✅ Handles color changes and jump stitches

### How It Works

```kotlin
// Parse DST file
val dstFile = File(filePath)
val dstData = DSTParser.parseDSTFile(dstFile)

// Get stitch count
val stitchCount = dstData.stitchCount  // e.g., 15,420

// Generate preview
val preview = DSTParser.generatePreview(dstData, width = 512, height = 512)

// Use in order
val order = Order(
    stitchCount = stitchCount,
    designPath = filePath,
    designType = DesignType.DST_FILE
)
```

### Supported Features
- ✅ Stitch count extraction (accurate to 1 stitch)
- ✅ Design bounds calculation
- ✅ Preview rendering (black and white)
- ✅ Jump stitch detection
- ⚠️ Color information (basic support)

### Limitations
- Preview is simplified (not full embroidery rendering)
- Colors shown as single color (black)
- Large files (100k+ stitches) may take longer to parse

---

## 💳 Payment System

### Payment Status States

| Status | Icon | Description | Use Case |
|--------|------|-------------|----------|
| **UNPAID** | 🔴 | No payment received | Order just created |
| **PENDING** | 🟡 | Payment processing | Bank transfer initiated |
| **PARTIALLY PAID** | 🔵 | Partial payment made | Installment payments |
| **PAID** | 🟢 | Fully paid | Payment complete |

### Automatic Calculations

The app automatically handles:

```kotlin
// When payment is added
Total Paid = Sum of all payments
Remaining Balance = Total Price - Total Paid
Payment Percentage = (Total Paid / Total Price) × 100

// Status auto-updates based on amount
if (paid == 0) → UNPAID
if (0 < paid < total) → PARTIALLY_PAID
if (paid >= total) → PAID
```

### Payment History

Every payment is recorded with:
- Amount
- Date & time
- Payment method
- Transaction reference (optional)
- Notes

---

## 🗄 Database Schema

### Tables

#### `orders`
```sql
CREATE TABLE orders (
    id INTEGER PRIMARY KEY,
    orderId TEXT UNIQUE,
    clientId INTEGER,
    orderDate INTEGER,
    dueDate INTEGER,
    completionDate INTEGER,
    status TEXT,
    paymentStatus TEXT,
    designPath TEXT,
    designType TEXT,
    stitchCount INTEGER,
    basePrice REAL,
    stitchRate REAL,
    materialCost REAL,
    additionalCost REAL,
    totalPrice REAL,
    totalPaid REAL,
    remainingBalance REAL,
    notes TEXT,
    createdAt INTEGER,
    updatedAt INTEGER,
    FOREIGN KEY(clientId) REFERENCES clients(id)
);
```

#### `clients`
```sql
CREATE TABLE clients (
    id INTEGER PRIMARY KEY,
    name TEXT NOT NULL,
    phone TEXT NOT NULL,
    address TEXT,
    email TEXT,
    notes TEXT,
    createdAt INTEGER
);
```

#### `payments`
```sql
CREATE TABLE payments (
    id INTEGER PRIMARY KEY,
    orderId INTEGER,
    amount REAL,
    paymentDate INTEGER,
    paymentMethod TEXT,
    transactionReference TEXT,
    notes TEXT,
    FOREIGN KEY(orderId) REFERENCES orders(id)
);
```

---

## 🤝 Contributing

Contributions are welcome! Here's how you can help:

### Reporting Bugs
1. Check if the issue already exists
2. Create a new issue with:
    - Clear title
    - Steps to reproduce
    - Expected vs actual behavior
    - Screenshots (if applicable)
    - Device/Android version

### Suggesting Features
1. Open an issue with the `enhancement` label
2. Describe the feature and use case
3. Explain why it would be beneficial

### Pull Requests
1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

### Code Style
- Follow Kotlin coding conventions
- Use meaningful variable names
- Add comments for complex logic
- Write unit tests for new features

---

## 🗺 Roadmap

### Version 1.0 (MVP) ✅
- [x] Order management
- [x] Client database
- [x] Payment tracking
- [x] DST file parser
- [x] Basic statistics

### Version 1.1 (Planned)
- [ ] PDF invoice generation
- [ ] WhatsApp integration for order sharing
- [ ] Payment reminders/notifications
- [ ] Backup to Google Drive
- [ ] Export data to Excel

### Version 1.2 (Future)
- [ ] Multi-language support (Arabic, French)
- [ ] Inventory management (thread, fabric stock)
- [ ] Employee/multi-user support
- [ ] Cloud sync across devices
- [ ] Advanced analytics dashboard
- [ ] Print receipt functionality

### Version 2.0 (Long-term)
- [ ] Web dashboard
- [ ] Customer portal (view order status)
- [ ] Integration with embroidery machines
- [ ] AI-powered design suggestions
- [ ] Accounting features (profit/loss)

---

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

```
MIT License

Copyright (c) 2024 [Your Name]

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software...
```

---

## 🙏 Acknowledgments

### Inspiration
This app was built to help my mom manage her embroidery business more efficiently, replacing manual paper-based order tracking with a modern digital solution.

### Libraries & Tools
- [Android Jetpack](https://developer.android.com/jetpack) - Modern Android development
- [Room Database](https://developer.android.com/training/data-storage/room) - Local data persistence
- [MPAndroidChart](https://github.com/PhilJay/MPAndroidChart) - Beautiful charts
- [Coil](https://coil-kt.github.io/coil/) - Image loading
- [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html) - Asynchronous programming

### Resources
- DST file format specification
- Android Material Design guidelines
- Community feedback from seamstresses and embroidery workers

---

## 📞 Contact & Support

- **Developer:** [Your Name]
- **Email:** your.email@example.com
- **GitHub:** [@yourusername](https://github.com/yourusername)
- **Issues:** [GitHub Issues](https://github.com/yourusername/embroidery-business-manager/issues)

### Getting Help
- 📖 Check the [Wiki](https://github.com/yourusername/embroidery-business-manager/wiki) for detailed guides
- 💬 Join our [Discussions](https://github.com/yourusername/embroidery-business-manager/discussions)
- 🐛 Report bugs in [Issues](https://github.com/yourusername/embroidery-business-manager/issues)

---

## ⭐ Show Your Support

If this app helped your business or you found it useful, please consider:
- ⭐ Starring the repository
- 🍴 Forking and contributing
- 📢 Sharing with others in the embroidery community
- 💖 Sponsoring the project

---

## 📊 Project Stats

![GitHub stars](https://img.shields.io/github/stars/yourusername/embroidery-business-manager?style=social)
![GitHub forks](https://img.shields.io/github/forks/yourusername/embroidery-business-manager?style=social)
![GitHub issues](https://img.shields.io/github/issues/yourusername/embroidery-business-manager)
![GitHub pull requests](https://img.shields.io/github/issues-pr/yourusername/embroidery-business-manager)
![GitHub last commit](https://img.shields.io/github/last-commit/yourusername/embroidery-business-manager)

---

<p align="center">
  <b>Built with ❤ for seamstresses and embroidery workers worldwide</b>
</p>

<p align="center">
  <sub>Transforming traditional craftsmanship with modern technology</sub>
</p>