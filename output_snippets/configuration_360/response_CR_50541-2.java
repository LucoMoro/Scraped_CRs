//<Beginning of snippet n. 0>
import com.android.annotations.NonNull;
import com.android.annotations.Nullable;

/** Miscellaneous utilities used by the Android SDK tools */
public class SdkUtils {
    public static String parseDpiAndSize(@Nullable String dpiStr, @Nullable String sizeStr) {
        int dpi = 96; // default dpi
        float size = 5.0f; // default size

        if (dpiStr != null && !dpiStr.isEmpty()) {
            try {
                dpi = Integer.parseInt(dpiStr);
            } catch (NumberFormatException e) {
                // Log or handle the error
            }
        }

        if (sizeStr != null && !sizeStr.isEmpty()) {
            try {
                size = Float.parseFloat(sizeStr);
            } catch (NumberFormatException e) {
                // Log or handle the error
            }
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
        // Test implementation goes here
    }

    public void testParseDpiAndSize() {
        assertEquals("DPI: 96, Size: 5.0", SdkUtils.parseDpiAndSize(null, null));
        assertEquals("DPI: 150, Size: 7.5", SdkUtils.parseDpiAndSize("150", "7.5"));
        assertEquals("DPI: 96, Size: 5.0", SdkUtils.parseDpiAndSize("invalid", null));
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

public class EmulatorSettings {
    private String dpiStr;
    private String sizeStr;
    private Object mScaleField;

    public void setDpiStr(String dpiStr) {
        this.dpiStr = dpiStr;
    }

    public void setSizeStr(String sizeStr) {
        this.sizeStr = sizeStr;
    }

    public void updateScaleField(float scale) {
        if (scale == 0.f) {
            mScaleField.setText("default");
        } else {
            mScaleField.setText(String.format("%.2f", scale));
        }
    }

    public String calculateScreenSize() {
        int dpi = 96; // default dpi
        float size = 5.0f; // default size

        try {
            if (dpiStr != null && !dpiStr.isEmpty()) {
                dpi = Integer.parseInt(dpiStr);
            }
            if (sizeStr != null && !sizeStr.isEmpty()) {
                size = Float.parseFloat(sizeStr);
            }
        } catch (NumberFormatException e) {
            // Log or handle the error
        }

        // Assuming some method to get the diagonal length
        double screenSize = getDefaultHardware().getScreen().getDiagonalLength(); // Assume this method exists
        return String.format("%.1f", screenSize);
    }

    private Object getDefaultHardware() {
        // Implementation goes here
        return null;
    }
}
//<End of snippet n. 2>