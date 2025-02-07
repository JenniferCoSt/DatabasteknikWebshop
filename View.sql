use webshop;
drop view if exists shoes;

create view shoes as 
select productType.id, brand.brand as Brand, productType.model as Model, color.color as Color, productsize.productsize as Size, productType.price from productType
inner join brand on productType.brandId = brand.id
inner join color on productType.colorId = color.id
inner join productsize on productType.productsizeId = productsize.id
order by productType.id;