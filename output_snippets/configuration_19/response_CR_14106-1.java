//<Beginning of snippet n. 0>
public static void RGBToHSV(int red, int green, int blue, float hsv[]) {
    if (red < 0 || red > 255 || green < 0 || green > 255 || blue < 0 || blue > 255) {
        throw new IllegalArgumentException("RGB values must be in the range [0..255]");
    }
    if (hsv.length < 3) {
        throw new IllegalArgumentException("HSV array must have at least 3 elements");
    }
    // Conversion logic...
}

public static void colorToHSV(int color, float hsv[]) {
    RGBToHSV((color >> 16) & 0xFF, (color >> 8) & 0xFF, color & 0xFF, hsv);
}

public static int HSVToColor(float hsv[]) {
    if (hsv.length < 3) {
        throw new IllegalArgumentException("HSV array must have at least 3 elements");
    }
    // Pinning logic for hsv...
    // Conversion logic...
}

public static int HSVToColor(int alpha, float hsv[]) {
    if (alpha < 0 || alpha > 255) {
        throw new IllegalArgumentException("Alpha value must be in the range [0..255]");
    }
    if (hsv.length < 3) {
        throw new IllegalArgumentException("HSV array must have at least 3 elements");
    }
    // Pinning logic for hsv...
    // Conversion logic...
}
//<End of snippet n. 0>