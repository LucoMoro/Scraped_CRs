/*CherryPick ce54b194 from master into r12. do not merge.

Fix AVD detail not displaying correctly.

Change-Id:If6bf328e38cc1e57bdedf060ea0a27d12b41c487*/




//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdDetailsDialog.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdDetailsDialog.java
//Synthetic comment -- index 4c0ea8c..2167248 100644

//Synthetic comment -- @@ -21,6 +21,8 @@
import com.android.sdklib.internal.avd.AvdInfo;
import com.android.sdklib.internal.avd.AvdManager;
import com.android.sdklib.internal.avd.AvdInfo.AvdStatus;
import com.android.sdkuilib.ui.GridDataBuilder;
import com.android.sdkuilib.ui.GridLayoutBuilder;
import com.android.sdkuilib.ui.SwtBaseDialog;

import org.eclipse.swt.SWT;
//Synthetic comment -- @@ -40,7 +42,6 @@
final class AvdDetailsDialog extends SwtBaseDialog {

private final AvdInfo mAvdInfo;

public AvdDetailsDialog(Shell shell, AvdInfo avdInfo) {
super(shell, SWT.APPLICATION_MODAL, "AVD details");
//Synthetic comment -- @@ -52,13 +53,13 @@
*/
@Override
protected void createContents() {
        Shell shell = getShell();
        GridLayoutBuilder.create(shell).columns(2);
        GridDataBuilder.create(shell).fill();

GridLayout gl;

        Composite c = new Composite(shell, SWT.NONE);
c.setLayout(gl = new GridLayout(2, false));
gl.marginHeight = gl.marginWidth = 0;
c.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
//Synthetic comment -- @@ -110,11 +111,11 @@
copy.remove(AvdManager.AVD_INI_IMAGES_2);

if (copy.size() > 0) {
                        Label l = new Label(shell, SWT.SEPARATOR | SWT.HORIZONTAL);
l.setLayoutData(new GridData(
GridData.FILL, GridData.CENTER, false, false, 2, 1));

                        c = new Composite(shell, SWT.NONE);
c.setLayout(gl = new GridLayout(2, false));
gl.marginHeight = gl.marginWidth = 0;
c.setLayoutData(new GridData(GridData.FILL_BOTH));







