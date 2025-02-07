use webshop;

select * from PurchaseOrder;
select * from OrderDetails;

set @feedback = '';

call addToCart(3, null, 3, @feedback);
call addToCart(3, null, 7, @feedback);

select @feedback;

select * from inventory;

call addToCart(2, null, 9, @feedback);
call addToCart(4, null, 11, @feedback);

select * from outOfStock;

select * from shoes;

