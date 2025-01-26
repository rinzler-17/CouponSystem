# CouponSystem
An app to manage and apply couponEntities on an online shopping cart.

# Components
This repository contains implementation of a RESTful API server written in Spring. It uses H2 in-memory database underneath.

## Building and running the project

## Testing
A postman collection has been provided which contains the API contracts with sample request and response. This JSON
can be imported in Postman and can be used to test the APIs.

Preferred sequence
1. Add products in the inventory.
2. Add couponEntities in the DB.
3. Check applicable couponEntities.
4. Apply couponEntity.

## Extensibility
New couponEntities can be easily added by extending the AbstractCoupon class and annotating the subclass with the specific couponEntity type.

## Cases:
1. In bxgy couponEntity, there can be case where getProduct includes a product not present in the cart. This can be addressed by
   creating an inventory of products in the DB, mapping productID to its corresponding price.
2. Also if addressing above, the cart shall only contain productId and quantity of products since prices will be fetched from inventory.
3. The cart may not contain aggregated products correctly, like
   items: [
       {productId: 1, quantity: 2},
       {productId: 2, quantity: 3}
   ]

## Possible improvements
1. In many places, the code reuses entities for request and response since they have common parameters. But to improve readability, these can be separated out into separate DTOs.
