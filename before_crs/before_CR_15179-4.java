/*Fix some handling of decimal values in the UI to handle non-en locales.

Change-Id:I3f0a7e1152b8c29d02b57c41a80d431ff99e6c2b*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigEditDialog.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/editors/layout/configuration/ConfigEditDialog.java
//Synthetic comment -- index 5c9ceef..96859a5 100644

//Synthetic comment -- @@ -29,6 +29,10 @@
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import java.util.regex.Pattern;

/**
//Synthetic comment -- @@ -36,8 +40,18 @@
*/
public class ConfigEditDialog extends GridDialog {

    private static final Pattern FLOAT_PATTERN = Pattern.compile("\\d*(\\.\\d?)?");


private final FolderConfiguration mConfig = new FolderConfiguration();

//Synthetic comment -- @@ -53,6 +67,8 @@
private float mXDpi = Float.NaN;
private float mYDpi = Float.NaN;


public ConfigEditDialog(Shell parentShell, FolderConfiguration config) {
super(parentShell, 1, false);
//Synthetic comment -- @@ -150,7 +166,11 @@
if (value.length() == 0) {
mXDpi = Float.NaN;
} else {
                    mXDpi = Float.parseFloat(value);
}
}
});
//Synthetic comment -- @@ -170,7 +190,11 @@
if (value.length() == 0) {
mYDpi = Float.NaN;
} else {
                    mYDpi = Float.parseFloat(value);
}
}
});








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdSelector.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdSelector.java
//Synthetic comment -- index fe686dc..8dcb533 100644

//Synthetic comment -- @@ -61,6 +61,8 @@
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;


/**
//Synthetic comment -- @@ -1011,7 +1013,11 @@
scale = Math.round(scale * 100);
scale /=  100.f;
list.add("-scale");                       //$NON-NLS-1$
                list.add(String.format("%.2f", scale));   //$NON-NLS-1$
}

// convert the list into an array for the call to exec.







