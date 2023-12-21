package com.glady.challenge.service.common;

public final class ErrorMessage {

    public ErrorMessage() {
        throw new IllegalStateException("Utility class.");
    }

    public static final String ENTITY_NAME_ALREADY_EXIST_BUT_DELETED = "%s with the same name %s exists but has already been deleted on %s.";
    public static final String ENTITY_ID_NAME_ALREADY_EXIST = "%s with (ID, NAME) [%s, %s] already exists.";
    public static final String ENTITY_ID_ALREADY_EXIST = "%s with ID %s already exists.";

    public static final String ENTITY_ID_NOT_FOUND = "%s with ID %s not found.";
    public static final String ENTITY_ID_NAME_NOT_FOUND = "%s with (ID, NAME) [%s, %s] not found.";

    public static final String COMPANY_BALANCE_INSUFFICIENT = "Company %s balance is insufficient.";

    public static final String VOUCHER_CODE_EXIST = "The voucher with code %s already exists.";
}
