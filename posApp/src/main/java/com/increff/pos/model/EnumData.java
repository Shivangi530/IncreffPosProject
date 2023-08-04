package com.increff.pos.model;

public class EnumData {
    public static enum Status {
        CREATED,
        INVOICED,
        CANCELED
    }

    public static enum Role {
        OPERATOR,
        SUPERVISOR,
        ADMIN
    }
}
