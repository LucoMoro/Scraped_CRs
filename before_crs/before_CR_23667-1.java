/*Cherrypick e2bdf98b from master to r12. do not merge.

Fix NPE on edit unknown AVD hardware property.

Change-Id:I93d2348622b8ccd320c76e40c508bf15ff96ff31*/
//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/HardwarePropertyChooser.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/HardwarePropertyChooser.java
//Synthetic comment -- index 5184442..ac2a38f 100644

//Synthetic comment -- @@ -17,6 +17,7 @@
package com.android.sdkuilib.internal.widgets;

import com.android.sdklib.internal.avd.HardwareProperties.HardwareProperty;
import com.android.sdkuilib.ui.GridDialog;

import org.eclipse.swt.SWT;
//Synthetic comment -- @@ -102,15 +103,22 @@
}

private void processSelection(String name, boolean pack) {
        mChosenProperty = mProperties.get(name);
        mTypeLabel.setText(mChosenProperty.getType().getValue());
        String desc = mChosenProperty.getDescription();
        if (desc != null) {
            mDescriptionLabel.setText(mChosenProperty.getDescription());
        } else {
            mDescriptionLabel.setText("N/A");
}

if (pack) {
getShell().pack();
}







