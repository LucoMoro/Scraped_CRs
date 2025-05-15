
//<Beginning of snippet n. 0>


import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.regex.Pattern;

/**
private float mXDpi = Float.NaN;
private float mYDpi = Float.NaN;

    private final DecimalFormat mDecimalFormat = new DecimalFormat();


public ConfigEditDialog(Shell parentShell, FolderConfiguration config) {
super(parentShell, 1, false);
if (value.length() == 0) {
mXDpi = Float.NaN;
} else {
                    try {
                        mXDpi = mDecimalFormat.parse(value).floatValue();
                    } catch (ParseException exception) {
                        mXDpi = Float.NaN;
                    }
}
}
});
if (value.length() == 0) {
mYDpi = Float.NaN;
} else {
                    try {
                        mYDpi = mDecimalFormat.parse(value).floatValue();
                    } catch (ParseException exception) {
                        mYDpi = Float.NaN;
                    }
}
}
});

//<End of snippet n. 0>










//<Beginning of snippet n. 1>


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Formatter;
import java.util.Locale;


/**
scale = Math.round(scale * 100);
scale /=  100.f;
list.add("-scale");                       //$NON-NLS-1$
                // because the emulator expects English decimal values, don't use String.format
                // but a Formatter.
                Formatter formatter = new Formatter(Locale.US);
                formatter.format("%.2f", scale);   //$NON-NLS-1$
                list.add(formatter.toString());
}

// convert the list into an array for the call to exec.

//<End of snippet n. 1>








