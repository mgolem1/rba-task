package com.rba.common.exceptions;

public enum AppError {

    JSON_PARSE_ERROR("JSON parse error"),
    BAD_REQUEST("Bad request!"),
    UNRECOGNIZED_EXCEPTION("Unrecognized exception!"),
    USER_ALREADY_EXISTS("User already exists!"),
    USER_NOT_FOUND("User not found!"),
    SENDING_DATA_TO_CARD_API_FAILED("Sending data to card api failed!");

    private final String desc; // error description

    AppError(String desc) {
        this.desc = desc;
    }

    public String desc() {
        return this.desc;
    }
}
