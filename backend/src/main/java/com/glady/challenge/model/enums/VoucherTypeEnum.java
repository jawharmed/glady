package com.glady.challenge.model.enums;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public enum VoucherTypeEnum {

    MEAL("Meal"),
    GIFT("Gift");

    final String type;
    private static final Map<String, VoucherTypeEnum> BY_LABEL = new HashMap<>();

    VoucherTypeEnum(String type) {
        this.type = type;
    }

    public static VoucherTypeEnum valueOfType(String type) {
        return BY_LABEL.get(type);
    }

    static {
        for (VoucherTypeEnum v : values()) {
            BY_LABEL.put(v.type, v);
        }
    }
}
