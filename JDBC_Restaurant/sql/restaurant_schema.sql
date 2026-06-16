-- ==========================================
-- RESTAURANT MANAGEMENT SYSTEM - PRO SCHEMA
-- ==========================================

DROP DATABASE IF EXISTS restaurantmanage;
CREATE DATABASE restaurantmanage CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE restaurantmanage;

-- 1. staff
CREATE TABLE staff (
    staffID     INT AUTO_INCREMENT PRIMARY KEY,
    staffName   VARCHAR(100) NOT NULL,
    phone       VARCHAR(15),
    salary      DECIMAL(12,2) NOT NULL DEFAULT 0,
    workYears   INT NOT NULL DEFAULT 0,
    job         VARCHAR(50) NOT NULL,        -- 'Chef', 'Waiter', 'Cashier', 'Manager'
    status      ENUM('active','inactive') DEFAULT 'active',
    createdAt   TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 2. account (FK → staff)
CREATE TABLE account (
    accountID   INT AUTO_INCREMENT PRIMARY KEY,
    username    VARCHAR(50) NOT NULL UNIQUE,
    password    VARCHAR(128) NOT NULL,       -- SHA-256 hash
    salt        VARCHAR(64) NOT NULL,        -- Random salt
    role        ENUM('manager','staff') NOT NULL DEFAULT 'staff',
    staffID     INT,
    status      ENUM('active','inactive') DEFAULT 'active',
    FOREIGN KEY (staffID) REFERENCES staff(staffID) ON DELETE SET NULL
);

-- 3. customer
CREATE TABLE customer (
    customerID  INT AUTO_INCREMENT PRIMARY KEY,
    firstName   VARCHAR(50) NOT NULL,
    lastName    VARCHAR(50) NOT NULL,
    phone       VARCHAR(15) UNIQUE,
    email       VARCHAR(100),
    address     VARCHAR(255),
    totalVisits INT DEFAULT 0,
    createdAt   TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 4. item_type
CREATE TABLE item_type (
    typeID      INT AUTO_INCREMENT PRIMARY KEY,
    typeName    VARCHAR(100) NOT NULL UNIQUE,
    description VARCHAR(255)
);

-- 5. item (FK → item_type)
CREATE TABLE item (
    itemID      INT AUTO_INCREMENT PRIMARY KEY,
    itemName    VARCHAR(150) NOT NULL,
    itemPrice   DECIMAL(12,2) NOT NULL,
    itemDetail  TEXT,
    calories    INT DEFAULT 0,
    typeID      INT,
    status      ENUM('available','unavailable') DEFAULT 'available',
    FOREIGN KEY (typeID) REFERENCES item_type(typeID) ON DELETE SET NULL
);

-- 6. drink
CREATE TABLE drink (
    drinkID     INT AUTO_INCREMENT PRIMARY KEY,
    drinkName   VARCHAR(150) NOT NULL,
    drinkPrice  DECIMAL(12,2) NOT NULL,
    category    VARCHAR(50),                 -- 'Coffee', 'Tea', 'Juice', 'Soft drink'
    status      ENUM('available','unavailable') DEFAULT 'available'
);

-- 7. tabletop
CREATE TABLE tabletop (
    tableID     INT AUTO_INCREMENT PRIMARY KEY,
    tableCode   VARCHAR(10) NOT NULL UNIQUE, 
    capacity    INT NOT NULL DEFAULT 4,
    status      ENUM('empty','reserved','occupied') DEFAULT 'empty',
    floor       INT DEFAULT 1
);

-- 8. reserve (FK → customer, tabletop)
CREATE TABLE reserve (
    reserveID   INT AUTO_INCREMENT PRIMARY KEY,
    customerID  INT,
    tableID     INT,
    reserveTime DATETIME NOT NULL,
    guestCount  INT NOT NULL DEFAULT 1,
    note        TEXT,
    status      ENUM('pending','confirmed','cancelled','completed') DEFAULT 'pending',
    createdAt   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (customerID) REFERENCES customer(customerID) ON DELETE CASCADE,
    FOREIGN KEY (tableID) REFERENCES tabletop(tableID) ON DELETE SET NULL
);

-- 9. order (FK → tabletop, customer)
CREATE TABLE `order` (
    orderID     INT AUTO_INCREMENT PRIMARY KEY,
    tableID     INT,
    customerID  INT,
    totalPrice  DECIMAL(12,2) DEFAULT 0,
    status      ENUM('new','processing','completed','cancelled') DEFAULT 'new',
    note        TEXT,
    createdAt   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (tableID) REFERENCES tabletop(tableID) ON DELETE SET NULL,
    FOREIGN KEY (customerID) REFERENCES customer(customerID) ON DELETE SET NULL
);

-- 10. order_item (FK → order, item)
CREATE TABLE order_item (
    orderID     INT,
    itemID      INT,
    quantity    INT NOT NULL DEFAULT 1,
    unitPrice   DECIMAL(12,2) NOT NULL,
    PRIMARY KEY (orderID, itemID),
    FOREIGN KEY (orderID) REFERENCES `order`(orderID) ON DELETE CASCADE,
    FOREIGN KEY (itemID) REFERENCES item(itemID) ON DELETE CASCADE
);

-- 11. transaction (FK → order, customer)
CREATE TABLE transaction (
    transactionID INT AUTO_INCREMENT PRIMARY KEY,
    orderID       INT,
    customerID    INT,
    amount        DECIMAL(12,2) NOT NULL,
    paymentMethod ENUM('cash','card','transfer') DEFAULT 'cash',
    status        ENUM('pending','completed','refunded') DEFAULT 'pending',
    note          TEXT,
    createdAt     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (orderID) REFERENCES `order`(orderID) ON DELETE SET NULL,
    FOREIGN KEY (customerID) REFERENCES customer(customerID) ON DELETE SET NULL
);

-- ==========================================
-- SAMPLE DATA
-- ==========================================

-- Staff
INSERT INTO staff (staffName, phone, salary, workYears, job) VALUES 
('Nguyễn Văn Admin', '0901234567', 15000000, 5, 'Manager'),
('Trần Thị Phục Vụ', '0912345678', 8000000, 2, 'Waiter'),
('Lê Hoàng Đầu Bếp', '0923456789', 12000000, 4, 'Chef'),
('Phạm Thu Thu Ngân', '0934567890', 9000000, 3, 'Cashier');

-- Account (admin: admin123, staff1: staff123)
-- hash for admin123 with salt YWRtaW5zYWx0MTIzNDU2 is jGl25bVBBBW96Qi9Te4V37Fnqchz/Eu4qB9vKrRIqRg=
INSERT INTO account (username, password, salt, role, staffID) 
VALUES ('admin', 'jGl25bVBBBW96Qi9Te4V37Fnqchz/Eu4qB9vKrRIqRg=', 'YWRtaW5zYWx0MTIzNDU2', 'manager', 1);

-- hash for staff123 with salt c3RhZmZzYWx0Nzg5MDEy is xMpCOKC5I4INzFCab3WEmw==
INSERT INTO account (username, password, salt, role, staffID) 
VALUES ('staff1', 'xMpCOKC5I4INzFCab3WEmw==', 'c3RhZmZzYWx0Nzg5MDEy', 'staff', 2);

-- Customer
INSERT INTO customer (firstName, lastName, phone, email, totalVisits) VALUES 
('Nguyễn', 'An', '0987654321', 'an.nguyen@email.com', 5),
('Trần', 'Bình', '0976543210', 'binh.tran@email.com', 2);

-- Item Type
INSERT INTO item_type (typeName, description) VALUES 
('Khai vị', 'Món ăn nhẹ bắt đầu bữa ăn'),
('Món chính', 'Món ăn chính đầy đủ dinh dưỡng'),
('Tráng miệng', 'Món ngọt sau bữa ăn');

-- Item
INSERT INTO item (itemName, itemPrice, itemDetail, calories, typeID) VALUES 
('Gỏi cuốn tôm thịt', 45000, 'Gỏi cuốn tươi với tôm, thịt, rau sống', 150, 1),
('Súp cua trứng cút', 55000, 'Súp cua với nấm tuyết và trứng cút', 200, 1),
('Phở bò đặc biệt', 75000, 'Phở bò tái nạm gầu gân', 450, 2),
('Cơm chiên hải sản', 65000, 'Cơm chiên tôm, mực, chả lụa', 520, 2),
('Chè ba màu', 30000, 'Chè đậu xanh, đậu đỏ, sương sa', 280, 3),
('Bánh flan', 25000, 'Bánh flan caramel mềm mịn', 200, 3);

-- Drink
INSERT INTO drink (drinkName, drinkPrice, category) VALUES 
('Cà phê sữa đá', 29000, 'Coffee'),
('Trà đào cam sả', 39000, 'Tea'),
('Sinh tố bơ', 35000, 'Juice'),
('Coca Cola', 20000, 'Soft drink');

-- Tabletop
INSERT INTO tabletop (tableCode, capacity, floor) VALUES 
('T01', 2, 1), ('T02', 4, 1), ('T03', 4, 1), ('T04', 6, 1), ('T05', 8, 1),
('T06', 2, 2), ('T07', 4, 2), ('T08', 6, 2), ('T09', 10, 2), ('T10', 12, 2);
