package com.accounting.ilab.model.enums;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum RoleTypes {

    ADMIN(1),
    USER(2);

    private final long id;

    public static RoleTypes getById(long id) {
        return Arrays.stream(values()).filter(status -> status.id == id)
                .findFirst()
                .orElse(null);
    }

}
