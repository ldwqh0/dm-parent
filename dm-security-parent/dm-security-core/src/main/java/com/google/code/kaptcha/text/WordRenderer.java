package com.google.code.kaptcha.text;

import java.awt.image.BufferedImage;

/**
 * {@link WordRenderer} is responsible for rendering words.
 */
public interface WordRenderer {
    BufferedImage renderWord(String word, int width, int height);
}
