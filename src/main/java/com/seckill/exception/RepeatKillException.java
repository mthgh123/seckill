package com.seckill.exception;

/**
 * 重复秒杀异常，如一个用户秒杀多次（运行期异常）
 */

public class RepeatKillException extends SeckillException {

    public RepeatKillException(String message) {
        super(message);
    }

    public RepeatKillException(String message, Throwable cause) {
        super(message, cause);
    }
}
