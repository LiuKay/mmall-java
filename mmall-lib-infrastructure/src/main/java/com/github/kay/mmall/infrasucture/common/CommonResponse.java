package com.github.kay.mmall.infrasucture.common;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.function.Consumer;

@Slf4j
@UtilityClass
public class CommonResponse {

    public static ResponseEntity<CodedMessage> send(HttpStatus status, String message) {
        Integer code = status.is2xxSuccessful() ? CodedMessage.CODE_SUCCESS : CodedMessage.CODE_DEFAULT_FAILURE;
        return ResponseEntity.status(status)
                             .contentType(MediaType.APPLICATION_JSON)
                             .body(new CodedMessage(code, message));
    }

    public static ResponseEntity<CodedMessage> failure(String message) {
        return send(HttpStatus.INTERNAL_SERVER_ERROR, message);
    }

    public static ResponseEntity<CodedMessage> success(String message) {
        return send(HttpStatus.OK, message);
    }

    public static ResponseEntity<CodedMessage> success() {
        return send(HttpStatus.OK, "success");
    }

    public static ResponseEntity<CodedMessage> op(Runnable executor) {
        return op(executor, e -> log.error(e.getMessage(), e));
    }

    public static ResponseEntity<CodedMessage> op(Runnable executor, Consumer<Exception> exceptionConsumer) {
        try {
            executor.run();
            return CommonResponse.success();
        } catch (Exception e) {
            exceptionConsumer.accept(e);
            throw e;
        }
    }
}
