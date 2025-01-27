package com.monk.couponsystem.exceptions;

public class NotFoundException extends RuntimeException {
    String entity;
    String id;
    public NotFoundException(String entity, String id) {
        this.id = id;
        this.entity = entity;
    }
    public String getMessage() {
        return entity + " with ID " + id + " not found";
    }
}
