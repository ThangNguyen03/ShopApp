Create database shopapp;
use shopapp;
CREATE TABLE users (
    id INT PRIMARY KEY AUTO_INCREMENT,
    fullname VARCHAR(100) DEFAULT '',
    phone_number VARCHAR(10) NOT NULL,
    address VARCHAR(200) DEFAULT '',
    password VARCHAR(100) NOT NULL DEFAULT '',
    created_at DATETIME,
    updated_at DATETIME,
    is_active TINYINT(1) DEFAULT 1,
    date_of_birth DATE,
    facebook_account_id INT DEFAULT 0
);
alter table users add column role_id int;
create table tokens(
  id int primary key auto_increment,
    token varchar(255) unique not null,
    expiration_date datetime,
    revoked tinyint(1) not null,
    expired tinyint(1) not null,
    user_id int,
    foreign key (user_id) references users(id)
);
-- Ho tro dang nhap tu google, facebook
create table social_accounts(
  id int primary key auto_increment,
    provider varchar(20) not null comment 'Ten nha social network',
    provider_id varchar(50) not null,
    email varchar(150) not null comment 'Email tai khoan',
    name varchar(100) not null comment 'Ten nguoi dung',
    user_id int,
    foreign key (user_id) references users(id)
);
create table categories(
  id int primary key auto_increment,
    name varchar(100) not null comment 'Ten danh muc: dien tu, gia dung'
);

-- Bang chua san pham
create table product(
id int primary key auto_increment,
name varchar(350) not null,
price float not null check(price >=0), 
thumbnail varchar(300) default '' comment 'Duong dan anh',
description longtext,
 created_at DATETIME,
updated_at DATETIME,
 category_id int,
foreign key (category_id) references categories(id)
);
create table roles(
id int primary key auto_increment,
name varchar(20) not null
);
alter table users add foreign key (role_id) references roles(id);
CREATE TABLE orders (
    id INT PRIMARY KEY AUTO_INCREMENT,
    user_id INT,
    FOREIGN KEY (user_id) REFERENCES users(id),
    fullname VARCHAR(100) DEFAULT '',
    email VARCHAR(140) DEFAULT '',
    phone_number VARCHAR(20) NOT NULL,
    address VARCHAR(200) NOT NULL,
    note VARCHAR(100) DEFAULT '',
    order_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(20),
    total_money FLOAT CHECK(total_money >= 0)
);
ALTER TABLE orders ADD COLUMN shipping_method VARCHAR(100);
ALTER TABLE orders ADD COLUMN shipping_address VARCHAR(200);
ALTER TABLE orders ADD COLUMN shipping_date DATE;
ALTER TABLE orders ADD COLUMN tracking_number VARCHAR(100);
ALTER TABLE orders ADD COLUMN payment_method VARCHAR(100);

-- Xóa 1 đơn hàng => xóa mềm => thêm trường active
ALTER TABLE orders ADD COLUMN active TINYINT(1);

-- Trạng thái đơn hàng chỉ được phép nhận "một số giá trị cụ thể"
ALTER TABLE orders
MODIFY COLUMN status ENUM('pending', 'processing', 'shipped', 'delivered', 'cancelled')
COMMENT 'Trạng thái đơn hàng';
CREATE TABLE order_details (
    id INT PRIMARY KEY AUTO_INCREMENT,
    order_id INT,
    FOREIGN KEY (order_id) REFERENCES orders(id),
    product_id INT,
    FOREIGN KEY (product_id) REFERENCES product(id),
    price FLOAT CHECK(price >= 0),
    number_of_products INT CHECK(number_of_products > 0),
    total_money FLOAT CHECK(total_money >= 0),
    color VARCHAR(20) DEFAULT ''
);
