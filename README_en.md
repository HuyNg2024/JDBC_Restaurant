<h1 align="center">
  🍽️ Premium Restaurant POS System
</h1>

*Read this in other languages: [English](README_en.md), [Tiếng Việt](README.md).*

<p align="center">
  <img src="https://img.shields.io/badge/Java-11%2B-blue?style=for-the-badge&logo=java" alt="Java Version">
  <img src="https://img.shields.io/badge/MySQL-8.0-orange?style=for-the-badge&logo=mysql" alt="MySQL">
  <img src="https://img.shields.io/badge/Swing-GUI-brightgreen?style=for-the-badge" alt="Java Swing">
  <img src="https://img.shields.io/badge/Architecture-3--Tier-purple?style=for-the-badge" alt="3-Tier Architecture">
  <img src="https://img.shields.io/badge/License-MIT-black?style=for-the-badge" alt="License">
</p>

<p align="center">
  A professional Restaurant Management System (Point of Sale - POS) built with <b>Java Swing</b> and <b>MySQL</b>. It strictly follows the <b>3-Tier Architecture</b>, ensuring high security and features a modern Dark Mode (Catppuccin Mocha) UI.
</p>

---

## 🌟 Key Features

### 💻 For Managers
- **Statistical Dashboard**: Integrated self-drawn (Java2D Bar Chart) revenue chart, visualizing real-time data.
- **Staff & Account Management**: Role-based access control, secure password hashing (SHA-256 + Salt).
- **Table & Reservation Management**: Track table status (Empty, In-use, Reserved).
- **Menu Management**: Smartly integrates 3 modules (Foods, Drinks, Categories) into a single interface.
- **Export Reports (CSV/Excel)**: Export business data perfectly without font encoding issues (UTF-8 BOM).

### 🛒 For Cashiers (Staff / POS)
- **Touch-friendly POS Interface**: Quickly select tables and add/remove items with simple mouse clicks.
- **Smart Calculations**: Supports custom Discount (%) and Tax (VAT) adjustments on the fly.
- **Auto Invoice Printing**: Automatically generates a beautiful HTML invoice and invokes the Print command (`Ctrl+P`) via browser immediately after payment.

---

## 🏗️ System Architecture

This project implements the standard **3-Tier Architecture**:
1. **GUI (Presentation Layer)**: The user interface, integrated with custom-designed components (RoundedButton, StyledTable, SearchBar, ToastNotification).
2. **BLL (Business Logic Layer)**: Handles business logic and data validation before persistence.
3. **DAL (Data Access Layer) & DTO**: Communicates directly with MySQL using the GenericDAO pattern and `PreparedStatement` (100% SQL Injection prevention).

---

## 🎨 UI/UX Design

The system completely eliminates the boring standard Java Swing look by:
- Applying the modern and eye-friendly **Catppuccin Mocha Dark Theme**.
- Custom-building all UI Components for rounded corners and hover highlight effects.
- Replacing annoying alert dialogs (`JOptionPane`) with a smooth sliding **Toast Notification** system.

---

## 🚀 Installation & Setup

### 1. Environment Requirements
- JDK 11 or higher (Java 17 or 21 recommended).
- Database Manager: MySQL Server (Recommend using [XAMPP](https://www.apachefriends.org/) or MySQL Workbench).
- IDE: Apache NetBeans, IntelliJ IDEA, or Eclipse.

### 2. Database Setup
1. Open MySQL / phpMyAdmin.
2. Import the `sql/restaurant_schema.sql` file provided in the project directory.
3. The script will automatically create the `restaurantmanage` database along with all dummy data.

### 3. Application Configuration
Open the Database configuration file (`src/config/AppConfig.java`) and update it according to your local machine:
```java
public static final String DB_URL = "jdbc:mysql://localhost:3306/restaurantmanage";
public static final String DB_USER = "root";
public static final String DB_PASS = "12345"; // Change password if needed
```

### 4. Running the App
- Open the project with NetBeans.
- Run the `src/GUI/Login.java` file (press `Shift + F6`).
- Login using the default Administrator account:
  - **Username:** `admin`
  - **Password:** `admin123`

---

## 📄 License
This project is distributed under the **MIT License**. You are free to copy, modify, and use it for personal projects, academic assignments, or commercial purposes.
