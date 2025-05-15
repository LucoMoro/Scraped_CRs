//<Beginning of snippet n. 0>


import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.regex.Pattern;

/**
private float mXDpi = Float.NaN;
private float mYDpi = Float.NaN;


public ConfigEditDialog(Shell parentShell, FolderConfiguration config) {
    super(parentShell, 1, false);
    Locale locale = Locale.getDefault();
    NumberFormat numberFormat = NumberFormat.getInstance(locale);
    if (value.length() == 0) {
        mXDpi = Float.NaN;
    } else {
        try {
            mXDpi = numberFormat.parse(value).floatValue();
        } catch (Exception e) {
            mXDpi = Float.NaN; // Handle the exception or log it as needed
        }
    }
}
if (value.length() == 0) {
    mYDpi = Float.NaN;
} else {
    try {
        mYDpi = numberFormat.parse(value).floatValue();
    } catch (Exception e) {
        mYDpi = Float.NaN; // Handle the exception or log it as needed
    }
}

//<End of snippet n. 0>










//<Beginning of snippet n. 1>


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

/**
// scale calculation
scale = Math.round(scale * 100);
scale /=  100.f;
Locale locale = Locale.getDefault();
NumberFormat numberFormat = NumberFormat.getInstance(locale);
list.add("-scale");                       //$NON-NLS-1$
list.add(numberFormat.format(scale));   //$NON-NLS-1$
// convert the list into an array for the call to exec.
//<End of snippet n. 1>