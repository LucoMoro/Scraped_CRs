/*gltrace: Improve font width detection.

Set a floor value for font width when used to determine how wide
widgets should be.

Change-Id:I3b7f1e75051032e9e234c92360903c161d233832*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/SwtUtils.java b/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/SwtUtils.java
//Synthetic comment -- index 750d36a..a7c960d 100644

//Synthetic comment -- @@ -20,10 +20,14 @@
import org.eclipse.swt.widgets.Control;

public class SwtUtils {
    public static int getFontWidth(Control c) {
GC gc = new GC(c);
int avgCharWidth = gc.getFontMetrics().getAverageCharWidth();
gc.dispose();
        return avgCharWidth;
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/editors/GLFunctionTraceViewer.java b/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/editors/GLFunctionTraceViewer.java
//Synthetic comment -- index db69ffe..3364c5b 100644

//Synthetic comment -- @@ -268,7 +268,7 @@
mFrameSelectionSpinner = new Spinner(c, SWT.BORDER);
gd = new GridData();
// width to hold atleast 6 digits
        gd.widthHint = SwtUtils.getFontWidth(mFrameSelectionSpinner) * 6;
mFrameSelectionSpinner.setLayoutData(gd);

mFrameSelectionSpinner.setMinimum(1);







