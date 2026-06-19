package com.rba.rest.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.rba.common.exceptions.AppError;
import com.rba.common.exceptions.AppException;
import com.rba.common.utils.JsonSimpleHelper;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseMessage<T> {
    private String status = "OK";

    private String errorCode;

    private String errorMessage;

    private T payload;

    public ResponseMessage() {
    }

    public ResponseMessage(String status, AppError error) {
        this(status, (error != null ? error.name() : null), (error != null ? error.desc() : null));
    }

    public ResponseMessage(String status, AppError error, String errorMessage) {
        this(status, (error != null ? error.name() : null), errorMessage);
    }

    public ResponseMessage(AppError error, T payload) {
        this.status = ("ERROR");
        this.errorMessage = error.desc();
        if (payload != null) this.payload = payload;
    }

    public ResponseMessage(T payload) {
        this("OK", null, null, payload);
    }

    public ResponseMessage(String status, String errorCode, String message) {
        this(status, errorCode, message, null);
    }

    public ResponseMessage(String status, String errorCode, String message, T payload) {
        this.status = status;
        this.errorCode = errorCode;
        this.errorMessage = message;
        this.payload = payload;
    }

    public static ResponseMessage createNewMsg(Object payload) {
        ResponseMessage msg = new ResponseMessage();
        msg.setPayload(payload);
        return msg;
    }

    public static ResponseMessage mapStringToResponseMessage(String messageString) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(messageString, ResponseMessage.class);
    }

    public static ResponseMessage createErrorMsg(AppError error) {
        ResponseMessage msg = new ResponseMessage(error);
        return msg;
    }

    public static String getMsgAsJson(ResponseMessage msg) throws AppException {
        if (msg.payload != null) {
            Object branch = msg.payload;
            msg.payload = null;
            return JsonSimpleHelper.combineAndSerialise(msg, "payload", branch);
        } else
            return JsonSimpleHelper.serialise(msg);
    }

    public static String packageAndJsoniseError(AppError error) throws AppException {
        ResponseMessage replyObject = new ResponseMessage("ERROR", error);
        return ResponseMessage.getMsgAsJson(replyObject);
    }

    public static String packageAndJsoniseError(AppError error, String message) throws AppException {
        ResponseMessage replyObject = new ResponseMessage("ERROR", error, message);
        return ResponseMessage.getMsgAsJson(replyObject);
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String error) {
        this.errorCode = error;
    }

    public void setLemonError(AppError lemonError) {
        errorCode = (lemonError != null ? lemonError.name() : null);
        errorMessage = (lemonError != null ? lemonError.desc() : null);
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String message) {
        this.errorMessage = message;
    }

    public T getPayload() {
        return payload;
    }

    public void setPayload(T payload) {
        this.payload = payload;
    }
}
