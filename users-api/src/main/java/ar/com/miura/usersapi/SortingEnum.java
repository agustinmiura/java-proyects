package ar.com.miura.usersapi;

import lombok.val;

public enum SortingEnum {
    DESC("desc"),ASC("asc");

    private String value;

    private SortingEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
