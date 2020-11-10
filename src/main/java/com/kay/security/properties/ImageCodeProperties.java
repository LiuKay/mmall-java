package com.kay.security.properties;

/**
 * Default configuration for verification code in image
 *
 * @author LiuKay
 * @since 2019/12/6
 */
public class ImageCodeProperties {

    private int width = 64;

    private int height = 32;

    private int length = 4;

    // second
    private int expire = 3600;

    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getExpire() {
        return expire;
    }

    public void setExpire(int expire) {
        this.expire = expire;
    }
}
