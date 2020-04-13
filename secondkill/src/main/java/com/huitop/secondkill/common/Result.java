package com.huitop.secondkill.common;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class Result<R> {
    private boolean success;

    private int code;

    private String message;

    private R data;

    public boolean isSuccess() {
        return success;
    }

    public static <R> Result<R> ofSuccess(R data) {
        return new Result<R>()
                .setSuccess(true)
                .setMessage("success")
                .setData(data);
    }

    public static <R> Result<R> ofSuccessMsg(String msg) {
        return new Result<R>()
                .setSuccess(true)
                .setMessage(msg);
    }

    public static <R> Result<R> ofFail(String msg) {
        return new Result<R>()
                .setSuccess(false)
                .setCode(500)
                .setMessage(msg);
    }

    public static <R> Result<R> ofFail(int code, String msg) {
        return new Result<R>()
                .setSuccess(false)
                .setCode(code)
                .setMessage(msg);
    }

    public static <R> Result<R> ofThrowable(int code, Throwable throwable) {
        return new Result<R>()
                .setSuccess(false)
                .setCode(code)
                .setMessage(throwable.getClass().getName() + ", " + throwable.getMessage());
    }

    /**
     * 只获取异常message
     *
     * @param code
     * @param throwable
     * @param <R>
     * @return
     */
    public static <R> Result<R> ofThrowableMsg(int code, Throwable throwable) {
        return new Result<R>()
                .setSuccess(false)
                .setCode(code)
                .setMessage(throwable.getMessage());
    }

    @Override
    public String toString() {
        return "Result{" +
                "success=" + success +
                ", code=" + code +
                ", msg='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}
