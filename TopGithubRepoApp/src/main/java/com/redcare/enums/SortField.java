package com.redcare.enums;

import lombok.Getter;

@Getter
public enum SortField {
    STARS("STARS"),
    FORKS("forks"),
    HELP_WANTED_ISSUES("help-wanted-issues"),
    UPDATED("updated");

    private String name;
    SortField(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name();
    }
}
