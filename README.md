# CouponSystem
An app to manage and apply coupons on an online shopping cart.

## Components
This repository contains implementation of a RESTful service written in Spring. It uses H2 in-memory database underneath.

## Building and running the project
1. Please use the gradle wrapper CLI to build and run the project.
```commandline
$ ./gradlew clean build
$ ./gradlew bootRun
```
2. The service runs on port 8081. If this port is already in use on your system, please change the `server.port` value in application.properties and rebuild and run the service.

## Testing
A postman collection has been provided which contains the API contracts with sample requests. This JSON
can be imported in Postman and can be used to test the APIs.

Preferred sequence
1. Add products in the inventory.
2. Add coupons in the DB.
3. Check applicable coupons.
4. Apply coupon.

## Extensibility
New coupons can be easily added by extending the AbstractCoupon class and annotating the subclass with the specific coupon type.

## Cases:
1. In bxgy coupon, there can be case where getProduct includes a product not present in the cart. This can be addressed by
   creating an inventory of products in the DB, mapping productID to its corresponding price.
2. Also if addressing above, the cart shall only contain productId and quantity of products since prices will be fetched from inventory.
3. The cart may not contain aggregated products correctly, like
   items: [
       {productId: 1, quantity: 2},
       {productId: 2, quantity: 3}
   ]

## Possible improvements
1. In many places, the code reuses entities for request and response since they have common parameters. To improve readability, these can be separated out into separate DTOs.
