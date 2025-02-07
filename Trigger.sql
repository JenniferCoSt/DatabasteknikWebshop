use webshop;

delimiter //
CREATE TRIGGER save_out_of_stock_products after update on inventory for each row
BEGIN 
	IF new.quantity = 0 then
		INSERT INTO  outofstock (productTypeId) values (old.productTypeId);
	END IF;
END//
delimiter ;
