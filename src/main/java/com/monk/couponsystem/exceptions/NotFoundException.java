package com.monk.couponsystem.exceptions;


import lombok.AllArgsConstructor;

/*
This exception is thrown whenever the service layer runs into a NotFound error
when interacting with the DB.
 */
@AllArgsConstructor
public class NotFoundException extends RuntimeException {
    String entity;
    String id;
    public String getMessage() {
        return entity + " with ID " + id + " not found";
    }
}
