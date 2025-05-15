//<Beginning of snippet n. 0>
import com.android.annotations.NonNull;
import com.android.annotations.Nullable;

/** Miscellaneous utilities used by the Android SDK tools */
public class SdkUtils {
    // Add utility methods and logic as needed
    public static String getScreenSize(String dpiStr, String sizeStr) {
        int dpi = 0;
        float size = 0f;

        try {
            dpi = Integer.parseInt(dpiStr);
            size = Float.parseFloat(sizeStr);
        } catch (NumberFormatException e) {
            System.err.println("Error parsing DPI or size: " + e.getMessage());
            dpi = 160; // Default DPI
            size = 4.0f; // Default size
        }

        // Ensure valid values for dpi and size
        if (dpi <= 0 || size <= 0) {
            throw new IllegalArgumentException("DPI and size must be positive numbers.");
        }
        // Calculate and return screen information
        double screenSize = Math.sqrt(Math.pow(size * dpi / 160, 2));
        return String.format("%.1f", screenSize);
    }
}
//<End of snippet n. 0>

//<Beginning of snippet n. 1>
import junit.framework.TestCase;

@SuppressWarnings("javadoc")
public class SdkUtilsTest extends TestCase {
    public void testGetScreenSize() {
        assertEquals("5.0", SdkUtils.getScreenSize("160", "5.0"));
        assertEquals("6.2", SdkUtils.getScreenSize("240", "6.0"));
        try {
            SdkUtils.getScreenSize("invalid", "5.0");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
        try {
            SdkUtils.getScreenSize("160", "-5.0");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
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

public class ScreenSizeHandler {
    public String getScreenSize(String dpiStr, String sizeStr, float scale) {
        int dpi = 0;
        float size = 0f;

        try {
            dpi = Integer.parseInt(dpiStr);
            size = Float.parseFloat(sizeStr);
        } catch (NumberFormatException e) {
            System.err.println("Error parsing DPI or size: " + e.getMessage());
            dpi = 160;  // Default DPI
            size = 4.0f; // Default size
        }

        if (dpi <= 0 || size <= 0) {
            throw new IllegalArgumentException("DPI and size must be positive numbers.");
        }

        double screenSize = Math.sqrt(Math.pow(size * dpi / 160, 2));

        if (scale == 0.f) {
            return "default";  //$NON-NLS-1$
        } else {
            return String.format("%.2f", scale);  //$NON-NLS-1$
        }
    }
}
//<End of snippet n. 2>