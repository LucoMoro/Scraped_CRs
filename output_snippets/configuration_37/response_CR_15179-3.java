//<Beginning of snippet n. 0>


import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import java.text.NumberFormat;
import java.text.ParseException;

private float mXDpi = Float.NaN;
private float mYDpi = Float.NaN;

public ConfigEditDialog(Shell parentShell, FolderConfiguration config) {
    super(parentShell, 1, false);
    String value = config.getXDpiValue(); // Assume the method to retrieve value

    if (value == null || value.isEmpty()) {
        mXDpi = Float.NaN;
    } else {
        try {
            mXDpi = NumberFormat.getInstance().parse(value).floatValue();
        } catch (ParseException e) {
            mXDpi = Float.NaN; // or handle accordingly
        }
    }

    value = config.getYDpiValue(); // Assume the method to retrieve value
    if (value == null || value.isEmpty()) {
        mYDpi = Float.NaN;
    } else {
        try {
            mYDpi = NumberFormat.getInstance().parse(value).floatValue();
        } catch (ParseException e) {
            mYDpi = Float.NaN; // or handle accordingly
        }
    }
}
//<End of snippet n. 0>

//<Beginning of snippet n. 1>


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.text.NumberFormat;


/**
scale = Math.round(scale * 100);
scale /=  100.f;
list.add("-scale");                       //$NON-NLS-1$
list.add(NumberFormat.getInstance().format(scale));   //$NON-NLS-1$
// convert the list into an array for the call to exec.
//<End of snippet n. 1>