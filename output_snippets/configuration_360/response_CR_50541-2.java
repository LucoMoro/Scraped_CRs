//<Beginning of snippet n. 0>


import com.android.annotations.NonNull;
import com.android.annotations.Nullable;

/** Miscellaneous utilities used by the Android SDK tools */
public class SdkUtils {
    public static String parseDpiAndSize(@NonNull String dpiStr, @NonNull String sizeStr) {
        int dpi = 160; // Default value
        float size = 8.0f; // Default value
        boolean defaultDpi = false;
        boolean defaultSize = false;

        if (dpiStr == null || dpiStr.isEmpty()) {
            throw new IllegalArgumentException("DPI cannot be null or empty");
        }

        try {
            dpi = Integer.parseInt(dpiStr);
            if (dpi < 0) {
                throw new IllegalArgumentException("DPI must be a positive number");
            }
        } catch (NumberFormatException e) {
            defaultDpi = true;
        }

        if (sizeStr == null || sizeStr.isEmpty()) {
            throw new IllegalArgumentException("Size cannot be null or empty");
        }

        try {
            size = Float.parseFloat(sizeStr);
            if (size < 0) {
                throw new IllegalArgumentException("Size must be a positive number");
            }
        } catch (NumberFormatException e) {
            defaultSize = true;
        }

        if (defaultDpi || defaultSize) {
            System.out.println("Using default values: DPI = 160, Size = 8.0");
        }

        return String.format("DPI: %d, Size: %.1f", dpi, size);
    }
}

//<End of snippet n. 0>










//<Beginning of snippet n. 1>


import junit.framework.TestCase;

@SuppressWarnings("javadoc")
public class SdkUtilsTest extends TestCase {
    public void testEndsWithIgnoreCase() {
        assertEquals("DPI: 160, Size: 8.0", SdkUtils.parseDpiAndSize("", ""));
        assertEquals("DPI: 160, Size: 8.0", SdkUtils.parseDpiAndSize(null, null));
        assertEquals("DPI: 240, Size: 10.0", SdkUtils.parseDpiAndSize("240", "10.0"));
        assertEquals("DPI: 160, Size: 8.0", SdkUtils.parseDpiAndSize("abc", "10.0"));
        assertEquals("DPI: 160, Size: 8.0", SdkUtils.parseDpiAndSize("240", "xyz"));
        assertEquals("DPI: 160, Size: 8.0", SdkUtils.parseDpiAndSize("-1", "10.0"));
        assertEquals("DPI: 160, Size: 8.0", SdkUtils.parseDpiAndSize("240", "-5"));
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
    public String emulateDevice(String dpiStr, String sizeStr) {
        int dpi = 160; // Default value
        float size = 8.0f; // Default value
        boolean defaultDpi = false;
        boolean defaultSize = false;

        if (dpiStr == null || dpiStr.isEmpty()) {
            throw new IllegalArgumentException("DPI cannot be null or empty");
        }

        try {
            dpi = Integer.parseInt(dpiStr);
            if (dpi < 0) {
                throw new IllegalArgumentException("DPI must be a positive number");
            }
        } catch (NumberFormatException e) {
            defaultDpi = true;
        }

        if (sizeStr == null || sizeStr.isEmpty()) {
            throw new IllegalArgumentException("Size cannot be null or empty");
        }

        try {
            size = Float.parseFloat(sizeStr);
            if (size < 0) {
                throw new IllegalArgumentException("Size must be a positive number");
            }
        } catch (NumberFormatException e) {
            defaultSize = true;
        }

        if (defaultDpi || defaultSize) {
            System.out.println("Using default values: DPI = 160, Size = 8.0");
        }

        float scale = calculateScale(dpi, size);
        if (scale == 0.f) {
            mScaleField.setText("default");  //$NON-NLS-1$
        } else {
            mScaleField.setText(String.format("%.2f", scale));  //$NON-NLS-1$
        }

        if (d != null) {
            double screenSize = d.getDefaultHardware().getScreen().getDiagonalLength();
            return String.format("%.1f", screenSize);
        }

        return "Invalid device configuration.";
    }

    private float calculateScale(int dpi, float size) {
        return size / (dpi / 160.0f);
    }
}

//<End of snippet n. 2>