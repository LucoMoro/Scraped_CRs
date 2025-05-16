//<Beginning of snippet n. 0>

import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

private float mXDpi = Float.NaN;
private float mYDpi = Float.NaN;

public ConfigEditDialog(Shell parentShell, FolderConfiguration config, String valueX, String valueY) {
    super(parentShell, 1, false);
    NumberFormat numberFormat = NumberFormat.getInstance(Locale.getDefault());
    try {
        if (valueX.length() == 0) {
            mXDpi = Float.NaN;
        } else {
            mXDpi = numberFormat.parse(valueX).floatValue();
        }
    } catch (ParseException e) {
        mXDpi = Float.NaN;
    }
    
    try {
        if (valueY.length() == 0) {
            mYDpi = Float.NaN;
        } else {
            mYDpi = numberFormat.parse(valueY).floatValue();
        }
    } catch (ParseException e) {
        mYDpi = Float.NaN;
    }
}

//<End of snippet n. 0>

//<Beginning of snippet n. 1>

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.text.NumberFormat;

NumberFormat format = NumberFormat.getInstance(Locale.getDefault());
scale = Math.round(scale * 100);
scale /=  100.f;
list.add("-scale");                       //$NON-NLS-1$
list.add(format.format(scale));   //$NON-NLS-1$

// convert the list into an array for the call to exec.

//<End of snippet n. 1>