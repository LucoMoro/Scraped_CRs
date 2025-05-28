//<Beginning of snippet n. 0>
public static void RGBToHSV(int red, int green, int blue, float hsv[]) {
    if (hsv.length < 3) {
        throw new IllegalArgumentException("hsv array must have at least 3 elements.");
    }
    if (red < 0 || red > 255 || green < 0 || green > 255 || blue < 0 || blue > 255) {
        throw new IllegalArgumentException("RGB components must be in the range [0, 255].");
    }
    // Conversion logic here...
}

public static void colorToHSV(int color, float hsv[]) {
    RGBToHSV((color >> 16) & 0xFF, (color >> 8) & 0xFF, color & 0xFF, hsv);
}

public static int HSVToColor(float hsv[]) {
    if (hsv.length < 3) {
        throw new IllegalArgumentException("hsv array must have at least 3 elements.");
    }
    if (hsv[0] < 0 || hsv[0] >= 360 || hsv[1] < 0 || hsv[1] > 1 || hsv[2] < 0 || hsv[2] > 1) {
        throw new IllegalArgumentException("HSV values are out of range.");
    }
    // Conversion logic here...
}

public static int HSVToColor(int alpha, float hsv[]) {
    if (hsv.length < 3) {
        throw new IllegalArgumentException("hsv array must have at least 3 elements.");
    }
    if (hsv[0] < 0 || hsv[0] >= 360 || hsv[1] < 0 || hsv[1] > 1 || hsv[2] < 0 || hsv[2] > 1) {
        throw new IllegalArgumentException("HSV values are out of range.");
    }
    // Conversion logic here...
}
//<End of snippet n. 0>