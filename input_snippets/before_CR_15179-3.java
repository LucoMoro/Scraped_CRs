
//<Beginning of snippet n. 0>


import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import java.util.regex.Pattern;

/**
private float mXDpi = Float.NaN;
private float mYDpi = Float.NaN;


public ConfigEditDialog(Shell parentShell, FolderConfiguration config) {
super(parentShell, 1, false);
if (value.length() == 0) {
mXDpi = Float.NaN;
} else {
                    mXDpi = Float.parseFloat(value);
}
}
});
if (value.length() == 0) {
mYDpi = Float.NaN;
} else {
                    mYDpi = Float.parseFloat(value);
}
}
});

//<End of snippet n. 0>










//<Beginning of snippet n. 1>


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;


/**
scale = Math.round(scale * 100);
scale /=  100.f;
list.add("-scale");                       //$NON-NLS-1$
                list.add(String.format("%.2f", scale));   //$NON-NLS-1$
}

// convert the list into an array for the call to exec.

//<End of snippet n. 1>








