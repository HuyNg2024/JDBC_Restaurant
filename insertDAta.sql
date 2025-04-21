USE restaurantmanage;

-- Thêm dữ liệu vào Staff
INSERT INTO Staff (staffID, staffName, Salary, workYears, Job) VALUES
(1, 'Nguyen Van A', 1000.00, 2, 'Waiter'),
(2, 'Le Thi B', 1200.00, 3, 'Chef'),
(3, 'Tran Van C', 900.00, 1, 'Waiter');

-- Thêm dữ liệu vào Waiter
INSERT INTO Waiter (staffID) VALUES
(1),
(3);

-- Thêm dữ liệu vào Chef
INSERT INTO Chef (staffID, cookType) VALUES
(2, 'Asian');

-- Thêm dữ liệu vào Customer
INSERT INTO Customer (CustomerID, FirstName, LastName, Phone, StaffID) VALUES
(101, 'Pham', 'Duc', '0123456789', 1),
(102, 'Nguyen', 'Lan', '0987654321', 3);

-- Thêm dữ liệu vào Reserve
INSERT INTO Reserve (reserveID, reserveTime, CustomerName, tableType, tableID) VALUES
(201, '2025-04-15 18:30:00', 'Pham Duc', 'VIP', 301),
(202, '2025-04-15 19:00:00', 'Nguyen Lan', 'Standard', 302);

-- Thêm dữ liệu vào TableTop
INSERT INTO TableTop (tableID, Customer_ID, Table_Code, Status, Capacity, Current) VALUES
(301, 101, 'TBL-01', 'Occupied', 4, 2),
(302, 102, 'TBL-02', 'Available', 6, 0);

-- Thêm dữ liệu vào Item_Type
INSERT INTO Item_Type (TypeID, TypeName, staffID) VALUES
(1, 'Main Dish', 2),
(2, 'Drink', 2);

-- Thêm dữ liệu vào Item
INSERT INTO Item (ItemID, ItemName, staffID, ItemDetail, productMin, ItemPrice, Calories, ItemType) VALUES
(401, 'Grilled Chicken', 2, 'Spicy grilled chicken', 1, 120.00, 500, 1),
(402, 'Coca-Cola', 2, 'Cold drink', 1, 20.00, 150, 2);

-- Thêm dữ liệu vào Drink (mặc dù không liên kết khóa ngoại)
INSERT INTO Drink (DrinkID, DrinkName, DrinkPrice) VALUES
(501, 'Coca-Cola', 20.00),
(502, 'Pepsi', 18.00);

-- Thêm dữ liệu vào Order
INSERT INTO `Order` (OrderID, tableID, CustomerID, totalPrice, Content, TransactionID) VALUES
(601, 301, 101, 140.00, 'Grilled Chicken and Coca-Cola', NULL),
(602, 302, 102, 20.00, 'Only Coca-Cola', NULL);

-- Thêm dữ liệu vào OrderItem
INSERT INTO OrderItem (OrderID, ItemID, Quantity) VALUES
(601, 401, 1),
(601, 402, 1),
(602, 402, 1);

-- Thêm dữ liệu vào Transaction
INSERT INTO Transaction (TransactionID, Customer_ID, Type, Status, Create_At, OrderID, Content) VALUES
(701, 101, 'Card', 'Paid', '2025-04-15 19:10:00', 601, 'Payment for grilled chicken and coca'),
(702, 102, 'Cash', 'Pending', '2025-04-15 19:15:00', 602, 'Payment for coca only');

-- Cập nhật lại bảng Order với TransactionID (sau khi tạo Transaction để tránh lỗi vòng lặp)
UPDATE `Order` SET TransactionID = 701 WHERE OrderID = 601;
UPDATE `Order` SET TransactionID = 702 WHERE OrderID = 602;
