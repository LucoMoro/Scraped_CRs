//<Beginning of snippet n. 0>
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

/**
private float mXDpi = Float.NaN;
private float mYDpi = Float.NaN;

public ConfigEditDialog(Shell parentShell, FolderConfiguration config, String value) {
    super(parentShell, 1, false);
    NumberFormat numberFormat = NumberFormat.getInstance(Locale.getDefault());
    try {
        if (value.isEmpty()) {
            mXDpi = Float.NaN;
        } else {
            mXDpi = numberFormat.parse(value).floatValue();
        }
    } catch (ParseException e) {
        // Handle invalid input appropriately (e.g., log an error or show a message to the user)
        mXDpi = Float.NaN;
    }

    try {
        if (value.isEmpty()) {
            mYDpi = Float.NaN;
        } else {
            mYDpi = numberFormat.parse(value).floatValue();
        }
    } catch (ParseException e) {
        // Handle invalid input appropriately (e.g., log an error or show a message to the user)
        mYDpi = Float.NaN;
    }
}
//<End of snippet n. 0>

//<Beginning of snippet n. 1>
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.text.NumberFormat;
import java.util.Locale;

/**
scale = Math.round(scale * 100);
scale /=  100.f;
list.add("-scale");                       //$NON-NLS-1$
NumberFormat numberFormat = NumberFormat.getInstance(Locale.getDefault());
list.add(numberFormat.format(scale));   //$NON-NLS-1$
// convert the list into an array for the call to exec.
//<End of snippet n. 1>