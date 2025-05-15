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
 */
public static void RGBToHSV(int red, int green, int blue, float hsv[]) {
    if (hsv.length < 3) {
        return;
    }
    red = Math.max(0, Math.min(255, red));
    green = Math.max(0, Math.min(255, green));
    blue = Math.max(0, Math.min(255, blue));
    float r = red / 255f;
    float g = green / 255f;
    float b = blue / 255f;
    float max = Math.max(r, Math.max(g, b));
    float min = Math.min(r, Math.min(g, b));
    float h, s, v = max;
    float d = max - min;
    s = max == 0 ? 0 : d / max;
    if (max == min) {
        h = 0; // achromatic
    } else {
        if (max == r) {
            h = (g - b) / d + (g < b ? 6 : 0);
        } else if (max == g) {
            h = (b - r) / d + 2;
        } else {
            h = (r - g) / d + 4;
        }
        h /= 6;
    }
    hsv[0] = h * 360; // Hue [0 .. 360)
    hsv[1] = s;       // Saturation [0...1]
    hsv[2] = v;       // Value [0...1]
}

/**
 * If hsv values are out of range, they are pinned.
 *
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
    if (hsv.length < 3) {
        return 0; // Invalid input
    }
    float h = hsv[0] / 360; // Hue [0 .. 1]
    float s = hsv[1];       // Saturation [0...1]
    float v = hsv[2];       // Value [0...1]
    int r, g, b;

    if (s == 0) {
        r = g = b = (int) (v * 255);
    } else {
        int i = (int) Math.floor(h * 6);
        float f = h * 6 - i;
        int p = (int) (v * (1 - s) * 255);
        int q = (int) (v * (1 - f * s) * 255);
        int t = (int) (v * (1 - (1 - f) * s) * 255);
        i = i % 6;
        switch (i) {
            case 0:
                r = (int) (v * 255);
                g = t;
                b = p;
                break;
            case 1:
                r = q;
                g = (int) (v * 255);
                b = p;
                break;
            case 2:
                r = p;
                g = (int) (v * 255);
                b = t;
                break;
            case 3:
                r = p;
                g = q;
                b = (int) (v * 255);
                break;
            case 4:
                r = t;
                g = p;
                b = (int) (v * 255);
                break;
            case 5:
                r = (int) (v * 255);
                g = p;
                b = q;
                break;
            default:
                r = g = b = 0; // Fallback for safety
        }
    }
    return (0xFF << 24) | (r << 16) | (g << 8) | b; // Alpha set to 255
}

/**
 * If hsv values are out of range, they are pinned.
 *
 * @param alpha the alpha component of the returned argb color.
 * @param hsv   3 element array which holds the input HSV components.
 * @return the resulting argb color
 */
public static int HSVToColor(int alpha, float hsv[]) {
    int color = HSVToColor(hsv);
    return (alpha << 24) | (color & 0x00FFFFFF); // Preserve RGB, set alpha
}

//<End of snippet n. 0>