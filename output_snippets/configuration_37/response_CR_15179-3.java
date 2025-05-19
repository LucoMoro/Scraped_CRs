//<Beginning of snippet n. 0>


import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

private float mXDpi = Float.NaN;
private float mYDpi = Float.NaN;

public ConfigEditDialog(Shell parentShell, FolderConfiguration config, String xDpiValue, String yDpiValue) {
    super(parentShell, 1, false);
    NumberFormat numberFormat = NumberFormat.getInstance(Locale.getDefault());

    mXDpi = parseDpiValue(xDpiValue, numberFormat);
    mYDpi = parseDpiValue(yDpiValue, numberFormat);
}

private float parseDpiValue(String dpiValue, NumberFormat numberFormat) {
    if (dpiValue == null || dpiValue.trim().isEmpty()) {
        logUserFeedback("DPI value cannot be null or empty.");
        return Float.NaN;
    }
    
    try {
        float value = numberFormat.parse(dpiValue).floatValue();
        if (value <= 0) {
            logUserFeedback("DPI value must be positive.");
            return Float.NaN;
        }
        if (value > 10000) { // Example threshold for an extremely large DPI
            logUserFeedback("DPI value is too large.");
            return Float.NaN;
        }
        return value;
    } catch (ParseException e) {
        logUserFeedback("Invalid format for DPI value.");
        return Float.NaN;
    }
}

private void logUserFeedback(String message) {
    // Implement logging or user notification mechanism here
    System.out.println(message); // Simple logging implementation for feedback
}

//<End of snippet n. 0>








//<Beginning of snippet n. 1>


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

scale = Math.round(scale * 100);
scale /=  100.f;
list.add("-scale");                       //$NON-NLS-1$
list.add(String.format(Locale.getDefault(), "%.2f", scale));   //$NON-NLS-1$

// convert the list into an array for the call to exec.

//<End of snippet n. 1>