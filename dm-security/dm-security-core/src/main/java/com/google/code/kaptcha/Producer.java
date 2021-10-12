package com.google.code.kaptcha;

import java.awt.image.BufferedImage;

/**
 * Responsible for creating captcha image with a text drawn on it.
 */
public interface Producer {
    /**
     * Create an image which will have written a distorted text.
     *
     * @param text the distorted characters
     * @return image with the text
     */
    BufferedImage createImage(String text);

    /**
     * @return the text to be drawn
     */
    String createText();
}
