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

2. Optionally, a `.jar`  has already been generated containing the recent most changes.
3. To run the jar, install java (recommended JVM=17) and use
```commandline
$ java -jar artifacts/couponsystem-0.0.1-SNAPSHOT.jar --server.port=8081

```
4. The service will run on port 8081. If this port is closed or already in use on your system, please change the `server.port` value.

## Testing
A postman collection has been provided which contains the necessary API contracts with sample requests. This JSON
can be imported in Postman and can be used to test the APIs.

Preferred sequence
1. Add products to the catalog.
2. Add coupons in the DB.
3. Check applicable coupons.
4. Apply coupon.

## Extensibility
New coupons can be easily added by extending the `AbstractCoupon` class and annotating the subclass with the specific coupon type.

## Assumptions
1. The product catalog/inventory assumes there is an infinite quantity of the products present in the catalog. 
2. Updates to the product catalog like removal of products does not happen when coupons are applied to carts. (This can be circumvented by using transactions or concurrency control mechanisms)
3. The Prouct wise coupon expects the item to exist in the product catalog to make the coupon valid.
4The BxGy coupon implemented works in the following ways:
   - First, it checks whether coupon is valid or not. This implies whether all the products mentioned in the buyProducts and getProducts lists exist in the product catalog.
   - Second, it checks coupon applicability, factoring in frequency of each buyProduct independently to calculate coupon multiplicity which is upper bounded by repetition limit.
   - Third, for the discount, it selects the item from `getProducts` list which has the highest quantity in the cart.
   - If no such item exists in the cart, it will select the item with the highest price by considering the price mentioned in the product catalog.
   - The final discount takes in multiplicity, as in suppose, multiplicity is 2 and the product selected for discount from the getProduct list has quantity 4, then in total 2*4 i.e. 8 such items will be added to cart.

## Possible improvements
1. In bxgy coupon, there can be several cases where in a certain combination of getProducts can be provided instead of just selecting a single product from the list for free discount.
2. I many places, the code reuses entities for request and response since they have common parameters. To improve readability, these can be separated out into separate DTOs.
3. In a real word scenario, the quantity of items is maintained in an inventory DB and these shall be factored in when checking or applying coupons.

