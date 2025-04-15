/*42863: Fix size parsing in the Avd Start Dialog

This fixes
42863: Bad default value for Screen Size when starting the emulator
prevents the emulator from starting

Change-Id:Ibfa137887c1b7e31a5173ff709add455070d0b25*/




//Synthetic comment -- diff --git a/sdkuilib/src/main/java/com/android/sdkuilib/internal/widgets/AvdStartDialog.java b/sdkuilib/src/main/java/com/android/sdkuilib/internal/widgets/AvdStartDialog.java
//Synthetic comment -- index 925b0cd..0ec26ff 100644

//Synthetic comment -- @@ -23,6 +23,7 @@
import com.android.sdkuilib.internal.repository.SettingsController;
import com.android.sdkuilib.ui.GridDialog;
import com.android.utils.ILogger;
import com.android.utils.SdkUtils;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.window.Window;
//Synthetic comment -- @@ -48,7 +49,9 @@
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
//Synthetic comment -- @@ -383,7 +386,16 @@
}

int dpi = Integer.parseInt(dpiStr);

        // The size number is formatted using String.format (locale formatting)
        float size;
        try {
            size = (float) SdkUtils.parseLocalizedDouble(sizeStr);
        } catch (ParseException e) {
            setScale(0);
            return;
        }

/*
* We are trying to emulate the following device:
* resolution: 'mSize1'x'mSize2'
//Synthetic comment -- @@ -411,7 +423,7 @@
if (scale == 0.f) {
mScaleField.setText("default");  //$NON-NLS-1$
} else {
            mScaleField.setText(String.format(Locale.getDefault(), "%.2f", scale));  //$NON-NLS-1$
}
}

//Synthetic comment -- @@ -453,7 +465,7 @@
if (d != null) {
double screenSize =
d.getDefaultHardware().getScreen().getDiagonalLength();
                    return String.format(Locale.getDefault(), "%.1f", screenSize);
}
}
}







