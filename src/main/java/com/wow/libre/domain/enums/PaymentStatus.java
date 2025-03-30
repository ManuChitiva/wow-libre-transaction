package com.wow.libre.domain.enums;

import lombok.*;

import java.util.*;

@Getter
@AllArgsConstructor
public enum PaymentStatus {
    APPROVED("APPROVED"),
    REJECTED("REJECTED"),
    ERROR("ERROR");

    private final String type;

    public static PaymentStatus getType(String type) {
        return Arrays.stream(values())
                .filter(transaction -> transaction.type.equalsIgnoreCase(type))
                .findFirst()
                .orElse(ERROR);
    }
}
