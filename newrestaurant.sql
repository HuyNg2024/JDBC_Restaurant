USE restaurantmanage;

-- Create the Staff table
CREATE TABLE Staff (
    staffID INT AUTO_INCREMENT PRIMARY KEY,
    staffName VARCHAR(100) NOT NULL,
    Salary DECIMAL(10, 2),
    workYears INT,
    Job VARCHAR(50)
);

-- Create the Waiter table that inherits from Staff
CREATE TABLE Waiter (
    staffID INT PRIMARY KEY,
    FOREIGN KEY (staffID) REFERENCES Staff(staffID)
);

-- Create the Chef table that inherits from Staff
CREATE TABLE Chef (
    staffID INT PRIMARY KEY,
    cookType VARCHAR(50),
    FOREIGN KEY (staffID) REFERENCES Staff(staffID)
);

-- Create the Customer table
CREATE TABLE Customer (
    CustomerID INT AUTO_INCREMENT PRIMARY KEY,
    FirstName VARCHAR(50) NOT NULL,
    LastName VARCHAR(50) NOT NULL,
    Phone VARCHAR(20),
    StaffID INT,
    FOREIGN KEY (StaffID) REFERENCES Staff(staffID)
);

-- Create the Reservation table
CREATE TABLE Reserve (
    reserveID INT AUTO_INCREMENT PRIMARY KEY,
    reserveTime DATETIME NOT NULL,
    CustomerName VARCHAR(100) NOT NULL,
    tableType VARCHAR(50),
    tableID INT
);

-- Create the TableTop table
CREATE TABLE TableTop (
    tableID INT AUTO_INCREMENT PRIMARY KEY,
    Customer_ID INT,
    Table_Code VARCHAR(20),
    Status VARCHAR(20),
    Capacity INT,
    Current INT,
    FOREIGN KEY (Customer_ID) REFERENCES Customer(CustomerID)
);

-- Create the Item_Type table
CREATE TABLE Item_Type (
    TypeID INT AUTO_INCREMENT PRIMARY KEY,
    TypeName VARCHAR(50) NOT NULL,
    staffID INT,
    FOREIGN KEY (staffID) REFERENCES Staff(staffID)
);

-- Create the Item table
CREATE TABLE Item (
    ItemID INT AUTO_INCREMENT PRIMARY KEY,
    ItemName VARCHAR(100) NOT NULL,
    staffID INT,
    ItemDetail TEXT,
    productMin INT,
    ItemPrice DECIMAL(10, 2) NOT NULL,
    Calories INT,
    ItemType INT,
    FOREIGN KEY (staffID) REFERENCES Staff(staffID),
    FOREIGN KEY (ItemType) REFERENCES Item_Type(TypeID)
);

-- Create the Drink table that inherits from Item
CREATE TABLE Drink (
    DrinkID INT AUTO_INCREMENT PRIMARY KEY,
    DrinkName VARCHAR(100) NOT NULL,
    DrinkPrice DECIMAL(10, 2) NOT NULL
);

-- Create the Order table
CREATE TABLE `Order` (
    OrderID INT AUTO_INCREMENT PRIMARY KEY,
    tableID INT,
    CustomerID INT,
    totalPrice DECIMAL(10, 2),
    Content TEXT,
    TransactionID INT,
    FOREIGN KEY (tableID) REFERENCES TableTop(tableID),
    FOREIGN KEY (CustomerID) REFERENCES Customer(CustomerID)
);

-- Create OrderItem junction table (implied from the diagram)
CREATE TABLE OrderItem (
    OrderID INT,
    ItemID INT,
    Quantity INT NOT NULL,
    PRIMARY KEY (OrderID, ItemID),
    FOREIGN KEY (OrderID) REFERENCES `Order`(OrderID),
    FOREIGN KEY (ItemID) REFERENCES Item(ItemID)
);

-- Create the Transaction table
CREATE TABLE Transaction (
    TransactionID INT AUTO_INCREMENT PRIMARY KEY,
    Customer_ID INT,
    Type VARCHAR(50),
    Status VARCHAR(50),
    Create_At DATETIME,
    OrderID INT,
    Content TEXT,
    FOREIGN KEY (Customer_ID) REFERENCES Customer(CustomerID),
    FOREIGN KEY (OrderID) REFERENCES `Order`(OrderID)
);

CREATE TABLE Account (
    accountID INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    role ENUM('manager', 'staff') NOT NULL,
    staffID INT,
    FOREIGN KEY (staffID) REFERENCES Staff(staffID)
);

-- Add foreign key constraint from Order to Transaction (circular reference)
ALTER TABLE `Order`
ADD CONSTRAINT fk_order_transaction
FOREIGN KEY (TransactionID) REFERENCES Transaction(TransactionID);
