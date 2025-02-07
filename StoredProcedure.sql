USE webshop;
DROP procedure if exists AddToCart;

delimiter //
CREATE PROCEDURE AddToCart(IN customer int, IN orderId int, IN productId int)
BEGIN

	DECLARE activeOrder int default -1; 
 
	IF EXISTS (select * from Inventory where productTypeId = productId and quantity = 0) then
		SIGNAL SQLSTATE '45000' set message_text = 'Produkten finns ej i lager';
	END IF;

	IF orderId IS NOT NULL then
		IF EXISTS (select * from PurchaseOrder where orderNumber = orderId and customerId = customer and isActive = true) then
			IF EXISTS (select * from OrderDetails where purchaseOrderNumber = orderId and productTypeId = productId) then
					UPDATE OrderDetails
					SET amount = amount + 1 where purchaseOrderNumber = orderId and productTypeId = productId;
				ELSE
					INSERT INTO OrderDetails(purchaseOrderNumber, productTypeId, amount) values (orderId, productId, 1);
            END IF;
			UPDATE Inventory
			SET quantity = quantity - 1 where productTypeId = productId;
		ELSEIF EXISTS (select * from PurchaseOrder where orderNumber = orderId and customerId != customer) then
			SIGNAL SQLSTATE '45000' set message_text = 'Felaktigt beställningsnummer';
		ELSEIF EXISTS (select * from PurchaseOrder where orderNumber = orderId and customerId = customer and isActive = false) then
			SIGNAL SQLSTATE '45000' set message_text = 'Denna order är ej aktiv';
		ELSE 
			INSERT INTO PurchaseOrder(orderNumber, customerId, isActive) values (orderId, customer, true);
            INSERT INTO OrderDetails(purchaseOrderNumber, productTypeId, amount) values (orderId, productId, 1);
			UPDATE Inventory
			SET quantity = quantity - 1 where productTypeId = productId;
        END IF;
	ELSE
		IF EXISTS (select * from PurchaseOrder where customerId = customer and isActive = true) then
			SELECT orderNumber from PurchaseOrder where customerId = customer and isActive = true into activeOrder;
			IF EXISTS (select * from OrderDetails where purchaseOrderNumber = activeOrder and productTypeId = productId) then
				UPDATE OrderDetails
				SET amount = amount + 1 where purchaseOrderNumber = activeOrder and productTypeId = productId;
			ELSE
				INSERT INTO OrderDetails(purchaseOrderNumber, productTypeId, amount) values (activeOrder, productId, 1);
            END IF;
		ELSE
			INSERT INTO PurchaseOrder(customerId, isActive) values (customer, true);
			INSERT INTO OrderDetails(purchaseOrderNumber, productTypeId, amount) values (last_insert_id(), productId, 1);
        END IF;
		UPDATE Inventory
		SET quantity = quantity - 1 where productTypeId = productId;
	END IF;
END //

delimiter ;
