/*42863: Fix size parsing in the Avd Start Dialog

This fixes
42863: Bad default value for Screen Size when starting the emulator
prevents the emulator from starting

Change-Id:I3a6e2acd14c796b57e1dfdcfeb3664251dd2da9c*/
//Synthetic comment -- diff --git a/common/src/main/java/com/android/utils/SdkUtils.java b/common/src/main/java/com/android/utils/SdkUtils.java
//Synthetic comment -- index 160f95d..d610527 100644

//Synthetic comment -- @@ -19,6 +19,9 @@
import com.android.annotations.NonNull;
import com.android.annotations.Nullable;

/** Miscellaneous utilities used by the Android SDK tools */
public class SdkUtils {
/**
//Synthetic comment -- @@ -213,4 +216,77 @@
return sb.toString();
}

}








//Synthetic comment -- diff --git a/common/src/test/java/com/android/utils/SdkUtilsTest.java b/common/src/test/java/com/android/utils/SdkUtilsTest.java
//Synthetic comment -- index 030e1b7..b250972 100644

//Synthetic comment -- @@ -18,6 +18,9 @@

import junit.framework.TestCase;

@SuppressWarnings("javadoc")
public class SdkUtilsTest extends TestCase {
public void testEndsWithIgnoreCase() {
//Synthetic comment -- @@ -129,5 +132,88 @@
wrapped);
}


}








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdStartDialog.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdStartDialog.java
//Synthetic comment -- index 925b0cd..0ec26ff 100644

//Synthetic comment -- @@ -23,6 +23,7 @@
import com.android.sdkuilib.internal.repository.SettingsController;
import com.android.sdkuilib.ui.GridDialog;
import com.android.utils.ILogger;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.window.Window;
//Synthetic comment -- @@ -48,7 +49,9 @@
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
//Synthetic comment -- @@ -383,7 +386,16 @@
}

int dpi = Integer.parseInt(dpiStr);
        float size = Float.parseFloat(sizeStr);
/*
* We are trying to emulate the following device:
* resolution: 'mSize1'x'mSize2'
//Synthetic comment -- @@ -411,7 +423,7 @@
if (scale == 0.f) {
mScaleField.setText("default");  //$NON-NLS-1$
} else {
            mScaleField.setText(String.format("%.2f", scale));  //$NON-NLS-1$
}
}

//Synthetic comment -- @@ -453,7 +465,7 @@
if (d != null) {
double screenSize =
d.getDefaultHardware().getScreen().getDiagonalLength();
                    return String.format("%.1f", screenSize);
}
}
}







