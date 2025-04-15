/*SDK Manager: addon schema version number in about box.

Change-Id:I2a54180f46e52cba931154ae57826c6df22b6a07*/
//Synthetic comment -- diff --git a/sdkmanager/app/src/com/android/sdkmanager/internal/repository/AboutPage.java b/sdkmanager/app/src/com/android/sdkmanager/internal/repository/AboutPage.java
//Synthetic comment -- index 114aa22..cb2f981 100755

//Synthetic comment -- @@ -19,6 +19,7 @@

import com.android.sdklib.SdkConstants;
import com.android.sdklib.internal.repository.Package;
import com.android.sdklib.repository.SdkRepoConstants;
import com.android.sdkmanager.*;
import org.eclipse.swt.SWT;
//Synthetic comment -- @@ -67,9 +68,11 @@
mLabel.setText(String.format(
"Android SDK Updater.\n" +
"Revision %1$s\n" +
                "Repository XML Schema #%2$d\n" +
"Copyright (C) 2009-2010 The Android Open Source Project.",
getRevision(),
SdkRepoConstants.NS_LATEST_VERSION));
}








