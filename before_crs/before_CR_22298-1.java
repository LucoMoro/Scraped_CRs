/*Add menu to SdkMan2. Experimental. Do not submit.

Change-Id:I404d9ddca03c866ba5d5ea14085363060b768796*/
//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterWindowImpl2.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/UpdaterWindowImpl2.java
//Synthetic comment -- index 83a86b2..3830eb7 100755

//Synthetic comment -- @@ -20,7 +20,6 @@
import com.android.sdklib.ISdkLog;
import com.android.sdklib.SdkConstants;
import com.android.sdkuilib.internal.repository.icons.ImageFactory;
import com.android.sdkuilib.internal.tasks.ProgressTaskFactory;
import com.android.sdkuilib.internal.tasks.ProgressView;
import com.android.sdkuilib.internal.tasks.ProgressViewFactory;
import com.android.sdkuilib.repository.ISdkChangeListener;
//Synthetic comment -- @@ -42,6 +41,8 @@
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;

//Synthetic comment -- @@ -72,6 +73,9 @@
private Label mStatusText;
private ImgDisabledButton mButtonStop;
private ToggleButton mButtonDetails;

/**
* Creates a new window. Caller must call open(), which will block.
//Synthetic comment -- @@ -169,6 +173,62 @@
onToggleDetails();
}
});
}

private Image getImage(String filename) {







