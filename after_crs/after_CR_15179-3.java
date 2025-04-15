/*Fix some handling of decimal values in the UI to handle non-en locales.

Change-Id:I3f0a7e1152b8c29d02b57c41a80d431ff99e6c2b*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigEditDialog.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigEditDialog.java
//Synthetic comment -- index 5c9ceef..5570e3b 100644

//Synthetic comment -- @@ -29,6 +29,8 @@
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.regex.Pattern;

/**
//Synthetic comment -- @@ -53,6 +55,8 @@
private float mXDpi = Float.NaN;
private float mYDpi = Float.NaN;

    private final DecimalFormat mDecimalFormat = new DecimalFormat();


public ConfigEditDialog(Shell parentShell, FolderConfiguration config) {
super(parentShell, 1, false);
//Synthetic comment -- @@ -150,7 +154,11 @@
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
//Synthetic comment -- @@ -170,7 +178,11 @@
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








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdSelector.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdSelector.java
//Synthetic comment -- index fe686dc..8dcb533 100644

//Synthetic comment -- @@ -61,6 +61,8 @@
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Formatter;
import java.util.Locale;


/**
//Synthetic comment -- @@ -1011,7 +1013,11 @@
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







