//<Beginning of snippet n. 0>
/**
 * These component values should be [0..255], but there is no
 * range check performed, so if they are out of range, the
 * returned color is undefined.
 *
 * @param red   Red component [0..255] of the color
 * @param green Green component [0..255] of the color
 * @param blue  Blue component [0..255] of the color
 * @param hsv   3 element array which holds the resulting HSV components.
 * @hide Pending API council
 *     hsv[0] is Hue [0 .. 360)
 *     hsv[1] is Saturation [0...1]
 *     hsv[2] is Value [0...1]
 */
public static void RGBToHSV(int red, int green, int blue, float hsv[]) {
    if (hsv.length < 3) {
        return; // Ensure hsv has at least 3 elements
    }
    // Range check and conversion logic here
}

/**
 * @param color the argb color to convert. The alpha component is ignored.
 * @param hsv   3 element array which holds the resulting HSV components.
 */
public static void colorToHSV(int color, float hsv[]) {
    RGBToHSV((color >> 16) & 0xFF, (color >> 8) & 0xFF, color & 0xFF, hsv);
}

/**
 * If hsv values are out of range, they are pinned.
 *
 * @param hsv  3 element array which holds the input HSV components.
 * @return the resulting argb color
 */
public static int HSVToColor(float hsv[]) {
    // Ensure hsv has three valid elements within the expected ranges
    // Conversion logic here
}

/**
 * @param alpha the alpha component of the returned argb color.
 * @param hsv   3 element array which holds the input HSV components.
 * @return the resulting argb color
 */
public static int HSVToColor(int alpha, float hsv[]) {
    // Ensure hsv has three valid elements within the expected ranges
    // Conversion logic here
}
//<End of snippet n. 0>