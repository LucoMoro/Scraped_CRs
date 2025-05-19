//<Beginning of snippet n. 0>
import com.android.annotations.NonNull;
import com.android.annotations.Nullable;

/** Miscellaneous utilities used by the Android SDK tools */
public class SdkUtils {
    public static String getDeviceDetails(String dpiStr, String sizeStr, float scale) {
        int dpi;
        float size;

        if (isValidDpi(dpiStr)) {
            dpi = Integer.parseInt(dpiStr);
        } else {
            dpi = 160; // Default DPI
            notifyUser("Invalid DPI value. Setting to default of 160.");
        }

        if (isValidSize(sizeStr)) {
            size = Float.parseFloat(sizeStr);
        } else {
            size = 5.0f; // Default size
            notifyUser("Invalid size value. Setting to default of 5.0f.");
        }

        StringBuilder sb = new StringBuilder();
        sb.append("DPI: ").append(dpi).append("\n");
        sb.append("Size: ").append(size).append("\n");

        if (scale == 0.f) {
            sb.append("Scale: default\n");
        } else {
            sb.append(String.format("Scale: %.2f\n", scale));
        }

        return sb.toString();
    }

    private static boolean isValidDpi(String value) {
        try {
            int intValue = Integer.parseInt(value);
            return intValue > 0 && intValue <= 600; // Arbitrary upper limit for DPI.
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static boolean isValidSize(String value) {
        try {
            float floatValue = Float.parseFloat(value);
            return floatValue > 0 && floatValue <= 20.0; // Arbitrary upper limit for size.
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static void notifyUser(String message) {
        // Implement user feedback mechanism here (e.g., logger, UI alert)
        System.err.println(message);
    }
}
//<End of snippet n. 0>

//<Beginning of snippet n. 1>
import junit.framework.TestCase;

@SuppressWarnings("javadoc")
public class SdkUtilsTest extends TestCase {
    public void testEndsWithIgnoreCase() {
        String str = "Sample";
        String suffix = "sample";
        assertTrue(str.toLowerCase().endsWith(suffix.toLowerCase()));
    }

    public void testGetDeviceDetailsWithInvalidDPI() {
        String result = SdkUtils.getDeviceDetails("invalid", "5.0", 1.0f);
        assertTrue(result.contains("DPI: 160"));
    }

    public void testGetDeviceDetailsWithInvalidSize() {
        String result = SdkUtils.getDeviceDetails("160", "invalid", 1.0f);
        assertTrue(result.contains("Size: 5.0"));
    }

    public void testGetDeviceDetailsWithNegativeDPI() {
        String result = SdkUtils.getDeviceDetails("-160", "5.0", 1.0f);
        assertTrue(result.contains("DPI: 160"));
    }

    public void testGetDeviceDetailsWithNegativeSize() {
        String result = SdkUtils.getDeviceDetails("160", "-5.0", 1.0f);
        assertTrue(result.contains("Size: 5.0"));
    }

    public void testGetDeviceDetailsWithExcessiveDPI() {
        String result = SdkUtils.getDeviceDetails("1000", "5.0", 1.0f);
        assertTrue(result.contains("DPI: 160"));
    }

    public void testGetDeviceDetailsWithExcessiveSize() {
        String result = SdkUtils.getDeviceDetails("160", "25.0", 1.0f);
        assertTrue(result.contains("Size: 5.0"));
    }
}
//<End of snippet n. 1>

//<Beginning of snippet n. 2>
import com.android.sdkuilib.internal.repository.SettingsController;
import com.android.sdkuilib.ui.GridDialog;
import com.android.utils.ILogger;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.window.Window;

public class DeviceEmulator {
    public String getDeviceInfo(String dpiStr, String sizeStr, float scale) {
        int dpi;
        float size;

        if (isValidDpi(dpiStr)) {
            dpi = Integer.parseInt(dpiStr);
        } else {
            dpi = 160; // Default DPI
            notifyUser("Invalid DPI value. Setting to default of 160.");
        }

        if (isValidSize(sizeStr)) {
            size = Float.parseFloat(sizeStr);
        } else {
            size = 5.0f; // Default size
            notifyUser("Invalid size value. Setting to default of 5.0f.");
        }

        if (scale == 0.f) {
            return "Scale: default";
        } else {
            return String.format("Scale: %.2f", scale);
        }
    }

    private boolean isValidDpi(String value) {
        try {
            int intValue = Integer.parseInt(value);
            return intValue > 0 && intValue <= 600; // Arbitrary upper limit for DPI.
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isValidSize(String value) {
        try {
            float floatValue = Float.parseFloat(value);
            return floatValue > 0 && floatValue <= 20.0; // Arbitrary upper limit for size.
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void notifyUser(String message) {
        // Implement user feedback mechanism here (e.g., logger, UI alert)
        System.err.println(message);
    }

    public String getScreenSize(Device d) {
        if (d != null) {
            double screenSize = d.getDefaultHardware().getScreen().getDiagonalLength();
            return String.format("%.1f", screenSize);
        }
        return "Screen size info not available.";
    }
}
//<End of snippet n. 2>