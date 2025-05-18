//<Beginning of snippet n. 0>


import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
private float mXDpi = Float.NaN;
private float mYDpi = Float.NaN;
private static final Logger LOGGER = Logger.getLogger(ConfigEditDialog.class.getName());

public ConfigEditDialog(Shell parentShell, FolderConfiguration config, Locale locale, String valueX, String valueY) {
    super(parentShell, 1, false);
    mXDpi = parseValue(valueX, locale);
    mYDpi = parseValue(valueY, locale);
}

private float parseValue(String value, Locale locale) {
    if (value == null || value.isEmpty()) {
        LOGGER.log(Level.WARNING, "Empty value provided for parsing.");
        return Float.NaN; 
    }
    try {
        NumberFormat format = NumberFormat.getInstance(locale);
        Number number = format.parse(value);
        if (number.floatValue() < 0) {
            LOGGER.log(Level.WARNING, "Negative value parsed: " + value);
            return Float.NaN; // Handle negative value
        }
        return number.floatValue();
    } catch (ParseException | NumberFormatException e) {
        LOGGER.log(Level.SEVERE, "Parsing error for value " + value, e);
        return Float.NaN; 
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