/*Fixing issue 3584 in Logcat DDMS.http://code.google.com/p/android/issues/detail?id=3584For solving the issue, this patch add input restriction
that prevent inputting separator characters to Edit Filter Dialog.

Change-Id:I689c0036f36678c92c08d05a6690610f95233c66*/
//Synthetic comment -- diff --git a/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/EditFilterDialog.java b/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/EditFilterDialog.java
//Synthetic comment -- index 8bbc7c5..723d275 100644

//Synthetic comment -- @@ -43,7 +43,10 @@
public class EditFilterDialog extends Dialog {

private static final int DLG_WIDTH = 400;
    private static final int DLG_HEIGHT = 250;

private Shell mParent;

//Synthetic comment -- @@ -68,6 +71,8 @@

private Button mOkButton;

private Label mPidWarning;

public EditFilterDialog(Shell parent) {
//Synthetic comment -- @@ -151,7 +156,7 @@
// top part with the filter name
Composite nameComposite = new Composite(mShell, SWT.NONE);
nameComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
        nameComposite.setLayout(new GridLayout(2, false));

Label l = new Label(nameComposite, SWT.NONE);
l.setText("Filter Name:");
//Synthetic comment -- @@ -172,6 +177,10 @@
}
});

// separator
l = new Label(mShell, SWT.SEPARATOR | SWT.HORIZONTAL);
l.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
//Synthetic comment -- @@ -192,9 +201,8 @@
tagText.setText(mTag);
}
}
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
        gd.horizontalSpan = 2;
        tagText.setLayoutData(gd);
tagText.addModifyListener(new ModifyListener() {
public void modifyText(ModifyEvent e) {
mTag = tagText.getText().trim();
//Synthetic comment -- @@ -202,6 +210,10 @@
}
});

l = new Label(main, SWT.NONE);
l.setText("by pid:");

//Synthetic comment -- @@ -223,14 +235,14 @@
});

mPidWarning = new Label(main, SWT.NONE);
        mPidWarning.setImage(ImageLoader.getDdmUiLibLoader().loadImage("empty.png", //$NON-NLS-1$
mShell.getDisplay()));

l = new Label(main, SWT.NONE);
l.setText("by Log level:");

final Combo logCombo = new Combo(main, SWT.DROP_DOWN | SWT.READ_ONLY);
        gd = new GridData(GridData.FILL_HORIZONTAL);
gd.horizontalSpan = 2;
logCombo.setLayoutData(gd);

//Synthetic comment -- @@ -324,26 +336,62 @@
*/
private void validate() {

// then we check it only contains digits.
if (mPid != null) {
if (mPid.matches("[0-9]*") == false) { //$NON-NLS-1$
mOkButton.setEnabled(false);
mPidWarning.setImage(ImageLoader.getDdmUiLibLoader().loadImage(
                        "warning.png", //$NON-NLS-1$
mShell.getDisplay()));
                return;
} else {
mPidWarning.setImage(ImageLoader.getDdmUiLibLoader().loadImage(
                        "empty.png", //$NON-NLS-1$
mShell.getDisplay()));
}
}

        if (mName == null || mName.length() == 0) {
            mOkButton.setEnabled(false);
            return;
}

        mOkButton.setEnabled(true);
}
}







