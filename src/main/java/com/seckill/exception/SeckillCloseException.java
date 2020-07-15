package com.seckill.exception;

import com.seckill.dto.SeckillExecution;

/**
 * 秒杀关闭异常，比如说我们的秒杀关闭了，但是有相关操作还要进行秒杀操作，那么这时候弹出对应异常
 */
public class SeckillCloseException extends SeckillException {

    public SeckillCloseException(String message) {
        super(message);
    }

    public SeckillCloseException(String message, Throwable cause) {
        super(message, cause);
    }
}
