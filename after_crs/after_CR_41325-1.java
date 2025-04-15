/*gltrace: Improve font width detection.

Set a floor value for font width when used to determine how wide
widgets should be.

Change-Id:I3b7f1e75051032e9e234c92360903c161d233832*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/SwtUtils.java b/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/SwtUtils.java
//Synthetic comment -- index 750d36a..a7c960d 100644

//Synthetic comment -- @@ -20,10 +20,14 @@
import org.eclipse.swt.widgets.Control;

public class SwtUtils {
    /** Minimum Character width in pixels. */
    private static final int MIN_CHAR_WIDTH = 10;

    /** Returns the font width if it is greater than {@link #MIN_CHAR_WIDTH}. */
    public static int getApproximateFontWidth(Control c) {
GC gc = new GC(c);
int avgCharWidth = gc.getFontMetrics().getAverageCharWidth();
gc.dispose();
        return Math.max(avgCharWidth, MIN_CHAR_WIDTH);
}
}








//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/editors/GLFunctionTraceViewer.java b/eclipse/plugins/com.android.ide.eclipse.gldebugger/src/com/android/ide/eclipse/gltrace/editors/GLFunctionTraceViewer.java
//Synthetic comment -- index db69ffe..3364c5b 100644

//Synthetic comment -- @@ -268,7 +268,7 @@
mFrameSelectionSpinner = new Spinner(c, SWT.BORDER);
gd = new GridData();
// width to hold atleast 6 digits
        gd.widthHint = SwtUtils.getApproximateFontWidth(mFrameSelectionSpinner) * 6;
mFrameSelectionSpinner.setLayoutData(gd);

mFrameSelectionSpinner.setMinimum(1);







