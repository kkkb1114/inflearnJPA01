package com.example.inflearnJPA01.domain.exception;

/**
 * RuntimeException: 이름 그대로 실행 중에 발생하며 시스템 환경 또는 인춧 값이 잘못된 경우, 혹은 의도적으로 프로그래머가 잡아내기 위한
 *                   조건드에 부합할 때 발생(throw)되게 만든다.
 */
public class NotEnoughStockException extends RuntimeException{
    public NotEnoughStockException() {
        super();
    }

    public NotEnoughStockException(String message) {
        super(message);
    }

    public NotEnoughStockException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotEnoughStockException(Throwable cause) {
        super(cause);
    }

    protected NotEnoughStockException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
