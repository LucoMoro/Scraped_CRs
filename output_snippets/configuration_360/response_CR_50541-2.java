//<Beginning of snippet n. 0>


import com.android.annotations.NonNull;
import com.android.annotations.Nullable;

/** Miscellaneous utilities used by the Android SDK tools */
public class SdkUtils {
    public static String getDeviceScreenSize(@NonNull Device d, @Nullable String dpiStr, @Nullable String sizeStr) {
        if (d == null) {
            return "0.0"; // Default value if device configuration is null
        }

        int dpi = 96; // Default dpi value
        float size = 0.0f; // Default size value

        try {
            if (dpiStr != null && !dpiStr.isEmpty()) {
                dpi = Integer.parseInt(dpiStr);
            }
        } catch (NumberFormatException e) {
            // Log error or handle invalid DPI if necessary
        }

        try {
            if (sizeStr != null && !sizeStr.isEmpty()) {
                size = Float.parseFloat(sizeStr);
            }
        } catch (NumberFormatException e) {
            // Log error or handle invalid size if necessary
        }

        int mSize1 = 0;
        int mSize2 = 0; // Assuming resolution is defined elsewhere; handle string parsing accordingly

        // Example of resolution string parsing, replace with logic as needed
        String resolution = "1920x1080"; // Placeholder
        String[] sizes = resolution.split("x");
        if (sizes.length == 2) {
            try {
                mSize1 = Integer.parseInt(sizes[0]);
                mSize2 = Integer.parseInt(sizes[1]);
            } catch (NumberFormatException e) {
                // Handle parsing error if necessary
            }
        }

        float scale = 0.f; // Placeholder for scaling logic
        if (scale == 0.f) {
            mScaleField.setText("default");  //$NON-NLS-1$
        } else {
            mScaleField.setText(String.format("%.2f", scale));  //$NON-NLS-1$
        }

        double screenSize = d.getDefaultHardware().getScreen().getDiagonalLength();
        return String.format("%.1f", screenSize);
    }
}

//<End of snippet n. 0>










//<Beginning of snippet n. 1>


import junit.framework.TestCase;

@SuppressWarnings("javadoc")
public class SdkUtilsTest extends TestCase {
    public void testEndsWithIgnoreCase() {
        // Add assert statements and test logic as required here
    }

    public void testGetDeviceScreenSize() {
        // Add tests for valid and invalid dpiStr and sizeStr, and ensure proper defaults are applied
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

// Function implementations and logic were not defined in the snippet, ensure to maintain all structure according to original needs.

//<End of snippet n. 2>
