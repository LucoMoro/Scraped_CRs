/*Merge AdtUpdateDialog CL on top of latest SdkMan2

This is a merge/conflict resolution to correctly
merge the recent AdtUpdateDialog CL on top of the
recent SdkMan2 changes.

Change-Id:If3461381bfae3d820465a51107b1bcca0896f611*/




//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/AdtUpdateDialog.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/AdtUpdateDialog.java
//Synthetic comment -- index 911b3a5..0b4879b 100755

//Synthetic comment -- @@ -150,7 +150,10 @@
protected void postCreate() {
ProgressViewFactory factory = new ProgressViewFactory();
factory.setProgressView(new ProgressView(
                mStatusText,
                mProgressBar,
                null /*buttonStop*/,
                mUpdaterData.getSdkLog()));
mUpdaterData.setTaskFactory(factory);

setupSources();








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterWindowImpl2.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterWindowImpl2.java
//Synthetic comment -- index 8f256e9..dda5b36 100755

//Synthetic comment -- @@ -24,6 +24,8 @@
import com.android.sdkuilib.internal.repository.icons.ImageFactory;
import com.android.sdkuilib.internal.tasks.ProgressView;
import com.android.sdkuilib.internal.tasks.ProgressViewFactory;
import com.android.sdkuilib.internal.widgets.ImgDisabledButton;
import com.android.sdkuilib.internal.widgets.ToggleButton;
import com.android.sdkuilib.repository.ISdkChangeListener;
import com.android.sdkuilib.repository.UpdaterWindow.InvocationContext;
import com.android.sdkuilib.ui.GridDataBuilder;
//Synthetic comment -- @@ -32,12 +34,8 @@
import com.android.util.Pair;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
//Synthetic comment -- @@ -513,126 +511,6 @@
// -----

/**
* Dialog used to display either the About page or the Settings (aka Options) page
* with a "close" button.
*/







