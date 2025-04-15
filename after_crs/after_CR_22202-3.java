/*Drop-Down list for example projects looks strange on Linux (Project Wizard)

Seehttp://code.google.com/p/android/issues/detail?id=15529Change-Id:If69ae23c3949a6c871c4d6dd451011e7c0ba1656*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newproject/NewProjectCreationPage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/newproject/NewProjectCreationPage.java
//Synthetic comment -- index 195c41f..9eb572e 100644

//Synthetic comment -- @@ -22,8 +22,8 @@

package com.android.ide.eclipse.adt.internal.wizards.newproject;

import com.android.ide.eclipse.adt.AdtConstants;
import com.android.ide.eclipse.adt.AdtPlugin;
import com.android.ide.eclipse.adt.internal.project.AndroidManifestHelper;
import com.android.ide.eclipse.adt.internal.sdk.Sdk;
import com.android.ide.eclipse.adt.internal.sdk.Sdk.ITargetChangeListener;
//Synthetic comment -- @@ -31,8 +31,8 @@
import com.android.sdklib.IAndroidTarget;
import com.android.sdklib.SdkConstants;
import com.android.sdklib.internal.project.ProjectProperties;
import com.android.sdklib.internal.project.ProjectProperties.PropertyType;
import com.android.sdklib.internal.project.ProjectPropertiesWorkingCopy;
import com.android.sdklib.xml.AndroidManifest;
import com.android.sdklib.xml.ManifestData;
import com.android.sdklib.xml.ManifestData.Activity;
//Synthetic comment -- @@ -77,6 +77,9 @@
import java.io.FileFilter;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;

/**
//Synthetic comment -- @@ -532,7 +535,11 @@

new Label(samples_group, SWT.NONE).setText("Samples:");

        if (Platform.getWS().equals(Platform.WS_GTK)) {
            mSamplesCombo = new Combo(samples_group, SWT.SIMPLE | SWT.READ_ONLY);
        } else {
            mSamplesCombo = new Combo(samples_group, SWT.DROP_DOWN | SWT.READ_ONLY);
        }
mSamplesCombo.setEnabled(false);
mSamplesCombo.select(0);
mSamplesCombo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
//Synthetic comment -- @@ -1201,6 +1208,8 @@
mSamplesCombo.add("This target has no samples. Please select another target.");
mSamplesCombo.select(0);
return;
            } else {
                Collections.sort(mSamplesPaths);
}

// Recompute the description of each sample (the relative path
//Synthetic comment -- @@ -1208,6 +1217,7 @@
int selIndex = 0;
int i = 0;
int n = samplesRootPath.length();
            Set<String> paths = new TreeSet<String>();
for (String path : mSamplesPaths) {
if (path.length() > n) {
path = path.substring(n);
//Synthetic comment -- @@ -1224,10 +1234,10 @@
selIndex = i;
}

                paths.add(path);
i++;
}
            mSamplesCombo.setItems(paths.toArray(new String[0]));
mSamplesCombo.select(selIndex);

} else {







