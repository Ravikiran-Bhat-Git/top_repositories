package com.redcare.enums;

import lombok.Getter;

@Getter
public enum Order {
    ASC("asc"), DESC("desc");

    private String name;

    Order(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name();
    }
}
