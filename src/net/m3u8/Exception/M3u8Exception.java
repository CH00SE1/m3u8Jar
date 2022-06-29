package net.m3u8.Exception;

/**
 * @author liushaoxiong
 * @date 2022-06-16
 */
public class M3u8Exception extends RuntimeException {
    public M3u8Exception() {
        super();
    }

    public M3u8Exception(String message) {
        super(message);
    }

    public M3u8Exception(String message, Throwable cause) {
        super(message, cause);
    }
}
