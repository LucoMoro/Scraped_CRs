/*Fix some handling of decimal values in the UI to handle non-en locales.

Change-Id:I3f0a7e1152b8c29d02b57c41a80d431ff99e6c2b*/




//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdSelector.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdSelector.java
//Synthetic comment -- index fe686dc..18e5661 100644

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
                // because the emulator expect english decimal values, don't use String.format
                // but a Formatter.
                Formatter formatter = new Formatter(Locale.US);
                formatter.format("%.2f", scale);   //$NON-NLS-1$
                list.add(formatter.toString());
}

// convert the list into an array for the call to exec.







