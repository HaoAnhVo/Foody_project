package com.codegym.foody.model.enumable;

public enum Role {
    ADMIN("Quản trị viên"),
    MERCHANT("Nhà cung cấp"),
    CUSTOMER("Khách hàng");

    private final String vietnameseName;

    Role(String vietnameseName) {
        this.vietnameseName = vietnameseName;
    }

    public String getVietnameseName() {
        return vietnameseName;
    }
}
