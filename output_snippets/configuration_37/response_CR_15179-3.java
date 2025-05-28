//<Beginning of snippet n. 0>


import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

/**
private float mXDpi = Float.NaN;
private float mYDpi = Float.NaN;

public ConfigEditDialog(Shell parentShell, FolderConfiguration config, Locale locale, String valueX, String valueY) {
    super(parentShell, 1, false);
    mXDpi = parseValue(valueX, locale);
    mYDpi = parseValue(valueY, locale);
}

private float parseValue(String value, Locale locale) {
    if (value == null || value.length() == 0) {
        return Float.NaN; // Handle empty value
    }
    try {
        return NumberFormat.getInstance(locale).parse(value).floatValue();
    } catch (ParseException | NumberFormatException e) {
        return Float.NaN; // Handle parse failure
    }
}

//<End of snippet n. 0>


//<Beginning of snippet n. 1>


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

/**
scale = Math.round(scale * 100);
scale /= 100.f;
list.add("-scale");                       //$NON-NLS-1$
list.add(String.format(locale, "%.2f", scale));   //$NON-NLS-1$
// convert the list into an array for the call to exec.

//<End of snippet n. 1>