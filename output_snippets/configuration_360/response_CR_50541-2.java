//<Beginning of snippet n. 0>
import com.android.annotations.NonNull;
import com.android.annotations.Nullable;
import java.util.logging.Logger;

/** Miscellaneous utilities used by the Android SDK tools */
public class SdkUtils {
    private static final Logger logger = Logger.getLogger(SdkUtils.class.getName());

    public static String getScreenSize(@NonNull String dpiStr, @NonNull String sizeStr) {
        int dpi = 160; // default value
        float size = 4.0f; // default value
        boolean dpiParsed = false;
        boolean sizeParsed = false;

        try {
            dpi = Integer.parseInt(dpiStr);
            dpiParsed = dpi > 0;
        } catch (NumberFormatException e) {
            logger.warning("Invalid dpi input: " + dpiStr);
        }

        try {
            size = Float.parseFloat(sizeStr);
            sizeParsed = size > 0;
        } catch (NumberFormatException e) {
            logger.warning("Invalid size input: " + sizeStr);
        }

        if (!dpiParsed || !sizeParsed) {
            dpi = 160;
            size = 4.0f;
        }

        double screenSize = dpi * Math.sqrt(size);
        return String.format("%.1f", screenSize);
    }
}
//<End of snippet n. 0>

//<Beginning of snippet n. 1>
import junit.framework.TestCase;

@SuppressWarnings("javadoc")
public class SdkUtilsTest extends TestCase {
    public void testEndsWithIgnoreCase() {
        // Your test code here.
    }

    public void testGetScreenSize() {
        assertEquals("8.0", SdkUtils.getScreenSize("160", "4.0"));
        assertEquals("8.0", SdkUtils.getScreenSize("invalid", "4.0"));
        assertEquals("3.0", SdkUtils.getScreenSize("160", "1.5"));
        assertEquals("4.0", SdkUtils.getScreenSize("invalid", "invalid"));
        assertEquals("8.0", SdkUtils.getScreenSize("0", "4.0")); // Added test for 0 dpi
        assertEquals("4.0", SdkUtils.getScreenSize("160", "0")); // Added test for 0 size
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
import java.util.logging.Logger;

public class ScreenEmulator {
    private String dpiStr;
    private String sizeStr;
    private static final Logger logger = Logger.getLogger(ScreenEmulator.class.getName());

    public String getScale() {
        int dpi = 160; // default value
        float size = 4.0f; // default value
        boolean dpiParsed = false;
        boolean sizeParsed = false;

        try {
            dpi = Integer.parseInt(dpiStr);
            dpiParsed = dpi > 0;
        } catch (NumberFormatException e) {
            logger.warning("Invalid dpi input: " + dpiStr);
        }

        try {
            size = Float.parseFloat(sizeStr);
            sizeParsed = size > 0;
        } catch (NumberFormatException e) {
            logger.warning("Invalid size input: " + sizeStr);
        }

        if (!dpiParsed || !sizeParsed) {
            dpi = 160;
            size = 4.0f;
        }

        float scale = dpi / size;

        if (scale == 0.f) {
            return "default";  //$NON-NLS-1$
        } else {
            return String.format("%.2f", scale);  //$NON-NLS-1$
        }
    }
}
//<End of snippet n. 2>