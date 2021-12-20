package com.lampmobile.app;

/**
 * Created by root on 31.10.17.
 */
public class ColorMatrix {
    public static int[] hsvToRgb(float hue, float saturation, float value) {

        int h = (int)(hue * 6);
        float f = hue * 6 - h;
        float p = value * (1 - saturation);
        float q = value * (1 - f * saturation);
        float t = value * (1 - (1 - f) * saturation);

        switch (h) {
            case 0: return rgbToString(value, t, p);
            case 1: return rgbToString(q, value, p);
            case 2: return rgbToString(p, value, t);
            case 3: return rgbToString(p, q, value);
            case 4: return rgbToString(t, p, value);
            case 5: return rgbToString(value, p, q);
            default: throw new RuntimeException("Something went wrong when converting from HSV to RGB. Input was " + hue + ", " + saturation + ", " + value);
        }
    }

    public static int[] rgbToString(float r, float g, float b) {
        int[] ret = new int[3];
        ret[0] = (int)(r * 255);
        ret[1] = (int)(g * 255);
        ret[2] = (int)(b * 255);
        return ret;
    }
}
