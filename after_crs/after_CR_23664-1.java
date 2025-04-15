/*Fix NPE on edit unknown AVD hardware property.

Change-Id:Ie79eb0bbfef17dd114b2002bbf5414d375eeb499*/




//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/HardwarePropertyChooser.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/HardwarePropertyChooser.java
//Synthetic comment -- index 5184442..ac2a38f 100644

//Synthetic comment -- @@ -17,6 +17,7 @@
package com.android.sdkuilib.internal.widgets;

import com.android.sdklib.internal.avd.HardwareProperties.HardwareProperty;
import com.android.sdklib.internal.avd.HardwareProperties.ValueType;
import com.android.sdkuilib.ui.GridDialog;

import org.eclipse.swt.SWT;
//Synthetic comment -- @@ -102,15 +103,22 @@
}

private void processSelection(String name, boolean pack) {
        mChosenProperty = name == null ? null : mProperties.get(name);

        String type = "Unknown";
        String desc = "Unknown";

        if (mChosenProperty != null) {
            desc = mChosenProperty.getDescription();
            ValueType vt = mChosenProperty.getType();
            if (vt != null) {
                type = vt.getValue();
            }
}

        mTypeLabel.setText(type);
        mDescriptionLabel.setText(desc);

if (pack) {
getShell().pack();
}







