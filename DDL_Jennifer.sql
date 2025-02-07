drop DATABASE if EXISTS Webshop;
create DATABASE Webshop;
use Webshop;

create table Customer(
id int not null auto_increment PRIMARY key,
firstName VARCHAR(20) not null,
lastName VARCHAR(40) not null,
email VARCHAR(50) not null,
streetName VARCHAR(35) not null,
ZipCode VARCHAR(6) not null,
city VARCHAR(25) not null,
customerPassword VARCHAR(20) not null, 
created timestamp default current_timestamp,
lastUpdate timestamp default current_timestamp on update current_timestamp
);
 
 create table PurchaseOrder(
 orderNumber int not null auto_increment PRIMARY key,
 customerId int,
 orderDate date default (current_date) not null,
 isActive boolean not null,
 FOREIGN key (customerId) REFERENCES Customer(id) on delete set null
 );
 
 create table Brand(
 id int not null auto_increment PRIMARY key,
 brand VARCHAR(25) not null
 );

 create table Color(
 id int not null auto_increment PRIMARY key,
 color VARCHAR(15) not null
 );
 
  create table ProductSize(
 id int not null auto_increment PRIMARY key,
 ProductSize int not null
 );
 
 create table ProductType(
 id int not null auto_increment PRIMARY key,
 brandId int,
 model varchar(30),
 colorId int,
 ProductSizeId int, 
 price int,
 FOREIGN key (brandId) REFERENCES Brand(id) on delete set null,
 FOREIGN key (colorId) REFERENCES Color(id) on delete set null,
 FOREIGN key (ProductSizeId) REFERENCES ProductSize(id) on delete set null
 );
 
 create table OrderDetails(
 id int not null auto_increment PRIMARY key,
 purchaseOrderNumber int not null, 
 productTypeId int not null,
 amount int not null,
 FOREIGN key (purchaseOrderNumber) REFERENCES PurchaseOrder(orderNumber),
 FOREIGN key (productTypeId) REFERENCES ProductType(id)
 );
 
 create table Inventory(
 id int not null auto_increment primary key,
 productTypeId int not null,
 quantity int not null,
 FOREIGN key (productTypeId) REFERENCES ProductType(id)
 );
 
 create table Category(
 id int not null auto_increment PRIMARY key,
 categoryName VARCHAR(25) not null
 );
 
 create table ProductCategory(
 productTypeId int not null,
 categoryId int not null,
 primary key (productTypeId, categoryId),
 FOREIGN key (productTypeId) REFERENCES ProductType(id) on delete cascade,
 FOREIGN key (categoryId) REFERENCES Category(id) on delete cascade
 );
 
 create table OutOfStock(
 id int not null auto_increment PRIMARY key,
 productTypeId int not null,
 created timestamp default current_timestamp
 );
 
 insert into Customer(firstName, lastName, email, streetName, zipCode, city, customerPassword) values
 ('Amaya', 'Antilop', 'aman@djuris.se', 'Ågatan 3', '12345', 'Göteborg', 'Loopidoop'),
 ('Mila', 'Myrslok', 'mimy@djuris.se', 'Sjövägen 42', '98765', 'Stockholm', 'Koolslok'),
 ('Indira', 'Igelkott', 'inig@djuris.se', 'Havets allé 12', '50432', 'Västerås', 'Kottis'),
 ('Elma', 'Elefant', 'elel@djuris.se', 'Dammringen 8', '76503', 'Västerås', 'Fantis'),
 ('Tom', 'Tiger', 'toti@djuris.se', 'Lagungatan 58', '36902', 'Malmö', 'Rawr');
 
 insert into PurchaseOrder(CustomerId, orderDate, isActive) values
 (2, '2024-12-20', false),
 (5, '2024-12-28', false),
 (1, '2024-12-29', false),
 (3, '2025-01-07', false),
 (2, '2025-01-12', false),
 (3, '2025-01-15', false),
 (4, '2025-01-18', false);
 
 insert into Brand(brand) values
 ('Ecco'),
 ('Dr Martens'),
 ('Nike'),
 ('Adidas'),
 ('Crocs');
 
 insert into Color(color) values
 ('White'),
 ('Blue'),
 ('Black'),
 ('Red'),
 ('Green');
 
 insert into ProductSize(productSize) values
 (35),
 (36),
 (37),
 (38),
 (39),
 (40),
 (41),
 (42),
 (43),
 (44),
 (45),
 (46);
 
 insert into ProductType(brandId, model, colorId, productSizeId, price) values
 (1, 'OFFROAD', 3, 4, 300),
 (1, 'OFFROAD', 3, 5, 300),
 (1, 'COZMO', 2, 3, 350),
 (1, 'COZMO', 1, 4, 350),
 (2, '1460 SMOOTH', 3, 2, 1000),
 (2, '1460 SMOOTH', 3, 3, 1000),
 (2, '1460 SMOOTH', 3, 4, 1000),
 (3, 'PEGASUS', 1, 3, 899),
 (3, 'PEGASUS', 1, 4, 899),
 (5, 'CROCS CLOG', 2, 9, 300),
 (4, 'RUNFALCON', 4, 1, 600),
 (4, 'RUNFALCON', 4, 8, 600);
 
 insert into OrderDetails(purchaseOrderNumber, productTypeId, amount) values
 (1, 5, 1),
 (1, 6, 1),
 (1, 1, 1),
 (2, 3, 1),
 (2, 10, 2),
 (3, 11, 1),
 (4, 1, 1),
 (5, 1, 2),
 (5, 3, 1), 
 (6, 11, 3),
 (7, 1, 1),
 (7, 8, 3);
 
 insert into Inventory(productTypeId, quantity) values
 (1, 230),
 (2, 187),
 (3, 342),
 (4, 295),
 (5, 163),
 (6, 249),
 (7, 152),
 (8, 381),
 (9, 203),
 (10, 119),
 (11, 277);
 
 insert into Category(categoryName) values
 ('Sandals'),
 ('Sports shoes'),
 ('Vegan shoes'),
 ('Boots'),
 ('Running shoes');
 
 insert into ProductCategory(productTypeId, categoryId) values
 (1, 1),
 (1, 3),
 (2, 1),
 (2, 3),
 (3, 1),
 (3, 3),
 (4, 1),
 (4, 3),
 (5, 3),
 (5, 4),
 (6, 3),
 (6, 4),
 (7, 3),
 (7, 4),
 (8, 2),
 (8, 5),
 (9, 2),
 (9, 5),
 (10, 1),
 (10, 3),
 (11, 2),
 (11, 5),
 (12, 2),
 (12, 5);
 
 create index IX_lastname on Customer(lastname);
 
 select * from inventory;
 