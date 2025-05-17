//<Beginning of snippet n. 0>

import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

private float mXDpi = Float.NaN;
private float mYDpi = Float.NaN;

public ConfigEditDialog(Shell parentShell, FolderConfiguration config, String value) { // value passed as parameter
    super(parentShell, 1, false);
    float parsedDpi = parseDpi(value);
    mXDpi = parsedDpi;
    mYDpi = parsedDpi;
}

private float parseDpi(String value) {
    if (value == null || value.trim().isEmpty()) {
        return Float.NaN;
    }
    try {
        NumberFormat format = NumberFormat.getInstance(Locale.getDefault());
        return format.parse(value).floatValue();
    } catch (ParseException e) { // Only handle ParseException
        // meaningful logging or user feedback can be added here
        return Float.NaN;
    }
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