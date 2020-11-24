package com.kay.security.validationcode.image;


import com.kay.security.validationcode.VerificationCode;
import java.awt.image.BufferedImage;

/**
 * @author LiuKay
 * @since 2019/12/5
 */
public class ImageCode extends VerificationCode {

    private BufferedImage image;

    public ImageCode(BufferedImage image, String code, int expiredSeconds) {
        super(code, expiredSeconds);
        this.image = image;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }
}
