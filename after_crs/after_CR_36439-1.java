/*Tweak DDMS monitor dialog

Change-Id:Iab558863997bb8c5f72c8ef9349719ca003c1724*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.ddms/src/com/android/ide/eclipse/ddms/LogCatMonitorDialog.java b/eclipse/plugins/com.android.ide.eclipse.ddms/src/com/android/ide/eclipse/ddms/LogCatMonitorDialog.java
//Synthetic comment -- index 44959e9..6194a0d 100644

//Synthetic comment -- @@ -20,7 +20,6 @@

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
//Synthetic comment -- @@ -36,8 +35,8 @@
public class LogCatMonitorDialog extends TitleAreaDialog {
private static final String TITLE = "Auto Monitor Logcat";
private static final String DEFAULT_MESSAGE =
            "Would you like ADT to automatically monitor logcat \n" +
            "output for messages from applications in the workspace?";

private boolean mShouldMonitor = true;

//Synthetic comment -- @@ -59,22 +58,25 @@
}

@Override
    protected Control createDialogArea(Composite parent) {
setTitle(TITLE);
setMessage(DEFAULT_MESSAGE);

        parent = (Composite) super.createDialogArea(parent);
Composite c = new Composite(parent, SWT.BORDER);
c.setLayout(new GridLayout(2, false));
        GridData gd_c = new GridData(GridData.FILL_BOTH);
        gd_c.grabExcessVerticalSpace = false;
        gd_c.grabExcessHorizontalSpace = false;
        c.setLayoutData(gd_c);

final Button disableButton = new Button(c, SWT.RADIO);
        disableButton.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));
disableButton.setText("No, do not monitor logcat output.");

final Button enableButton = new Button(c, SWT.RADIO);
        enableButton.setText("Yes, monitor logcat and display logcat view if there are\n" +
                "messages with priority higher than:");
enableButton.setSelection(true);

final Combo levelCombo = new Combo(c, SWT.READ_ONLY | SWT.DROP_DOWN);







