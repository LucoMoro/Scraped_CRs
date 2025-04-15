/*Fix AVD detail not displaying correctly.

Change-Id:I4ecf1c25c9f9defe28b8896332301c15b343c056*/
//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdDetailsDialog.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdDetailsDialog.java
//Synthetic comment -- index 4c0ea8c..2167248 100644

//Synthetic comment -- @@ -21,6 +21,8 @@
import com.android.sdklib.internal.avd.AvdInfo;
import com.android.sdklib.internal.avd.AvdManager;
import com.android.sdklib.internal.avd.AvdInfo.AvdStatus;
import com.android.sdkuilib.ui.SwtBaseDialog;

import org.eclipse.swt.SWT;
//Synthetic comment -- @@ -40,7 +42,6 @@
final class AvdDetailsDialog extends SwtBaseDialog {

private final AvdInfo mAvdInfo;
    private Composite mRootComposite;

public AvdDetailsDialog(Shell shell, AvdInfo avdInfo) {
super(shell, SWT.APPLICATION_MODAL, "AVD details");
//Synthetic comment -- @@ -52,13 +53,13 @@
*/
@Override
protected void createContents() {
        mRootComposite = new Composite(getShell(), SWT.NONE);
        mRootComposite.setLayout(new GridLayout(2, false));
        mRootComposite.setLayoutData(new GridData(GridData.FILL_BOTH));

GridLayout gl;

        Composite c = new Composite(mRootComposite, SWT.NONE);
c.setLayout(gl = new GridLayout(2, false));
gl.marginHeight = gl.marginWidth = 0;
c.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
//Synthetic comment -- @@ -110,11 +111,11 @@
copy.remove(AvdManager.AVD_INI_IMAGES_2);

if (copy.size() > 0) {
                        Label l = new Label(mRootComposite, SWT.SEPARATOR | SWT.HORIZONTAL);
l.setLayoutData(new GridData(
GridData.FILL, GridData.CENTER, false, false, 2, 1));

                        c = new Composite(mRootComposite, SWT.NONE);
c.setLayout(gl = new GridLayout(2, false));
gl.marginHeight = gl.marginWidth = 0;
c.setLayoutData(new GridData(GridData.FILL_BOTH));







