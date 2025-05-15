//<Beginning of snippet n. 0>


import com.android.annotations.NonNull;
import com.android.annotations.Nullable;

/** Miscellaneous utilities used by the Android SDK tools */
public class SdkUtils {
    public static String parseDpiAndSize(String dpiStr, String sizeStr) {
        int dpi = parseDpi(dpiStr);
        float size = parseSize(sizeStr);

        if (dpi < 0 || size < 0) {
            dpi = 160; // Default dpi
            size = 6.0f; // Default size
        }
        
        return String.format("DPI: %d, Size: %.2f", dpi, size);
    }

    private static int parseDpi(String dpiStr) {
        try {
            return Integer.parseInt(dpiStr.replaceAll("[^\\d]", ""));
        } catch (NumberFormatException e) {
            return -1; // Indicate an invalid DPI
        }
    }

    private static float parseSize(String sizeStr) {
        try {
            return Float.parseFloat(sizeStr.replaceAll("[^\\d.]", ""));
        } catch (NumberFormatException e) {
            return -1; // Indicate an invalid size
        }
    }
}

//<End of snippet n. 0>










//<Beginning of snippet n. 1>



import junit.framework.TestCase;

@SuppressWarnings("javadoc")
public class SdkUtilsTest extends TestCase {
    public void testEndsWithIgnoreCase() {
        // Test case logic goes here
    }
    
    public void testParseDpiAndSize() {
        assertEquals("DPI: 160, Size: 6.00", SdkUtils.parseDpiAndSize("DPI: 160", "Size: 6"));
        assertEquals("DPI: 160, Size: 6.00", SdkUtils.parseDpiAndSize("INVALID_DPI", "INVALID_SIZE"));
        assertEquals("DPI: 300, Size: 7.50", SdkUtils.parseDpiAndSize("DPI: 300", "Size: 7.5 inches"));
    }
}

//<End of snippet n. 1>










//<Beginning of snippet n. 2>


import com.android.sdkuilib.internal.repository.SettingsController;
import com.android.sdkuilib.ui.GridDialog;
import com.android.utils.ILogger;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.window.Window;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DeviceEmulator {
    private String dpiStr;
    private String sizeStr;

    public DeviceEmulator(String dpiStr, String sizeStr) {
        this.dpiStr = dpiStr;
        this.sizeStr = sizeStr;
    }

    public String getEmulationDetails() {
        int dpi = parseDpi(dpiStr);
        float size = parseSize(sizeStr);

        if (dpi < 0 || size < 0) {
            dpi = 160; // Default dpi
            size = 6.0f; // Default size
        }

        StringBuilder sb = new StringBuilder();
        sb.append("DPI: ").append(dpi)
          .append(", Size: ").append(size);
        
        return sb.toString();
    }

    private int parseDpi(String dpiStr) {
        try {
            return Integer.parseInt(dpiStr);
        } catch (NumberFormatException e) {
            return -1; // Indicate an invalid DPI
        }
    }

    private float parseSize(String sizeStr) {
        try {
            return Float.parseFloat(sizeStr);
        } catch (NumberFormatException e) {
            return -1; // Indicate an invalid size
        }
    }
    
    public String formatScreenSize() {
        // Assume d is properly initialized
        // Device details retrieval logic
        
        if (d != null) {
            double screenSize = d.getDefaultHardware().getScreen().getDiagonalLength();
            return String.format("%.1f", screenSize);
        }
        return "Invalid device";
    }
}

//<End of snippet n. 2>