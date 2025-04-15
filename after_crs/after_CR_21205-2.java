/*Add validation messages to the DDMS edit filter dialog.

Fixing issue 3584.http://code.google.com/p/android/issues/detail?id=3584Change-Id:I689c0036f36678c92c08d05a6690610f95233c66*/




//Synthetic comment -- diff --git a/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/EditFilterDialog.java b/ddms/libs/ddmuilib/src/com/android/ddmuilib/logcat/EditFilterDialog.java
//Synthetic comment -- index 8bbc7c5..6cd44d0 100644

//Synthetic comment -- @@ -43,7 +43,10 @@
public class EditFilterDialog extends Dialog {

private static final int DLG_WIDTH = 400;
    private static final int DLG_HEIGHT = 260;

    private static final String IMAGE_WARNING = "warning.png"; //$NON-NLS-1$
    private static final String IMAGE_EMPTY = "empty.png"; //$NON-NLS-1$

private Shell mParent;

//Synthetic comment -- @@ -68,6 +71,8 @@

private Button mOkButton;

    private Label mNameWarning;
    private Label mTagWarning;
private Label mPidWarning;

public EditFilterDialog(Shell parent) {
//Synthetic comment -- @@ -151,7 +156,7 @@
// top part with the filter name
Composite nameComposite = new Composite(mShell, SWT.NONE);
nameComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
        nameComposite.setLayout(new GridLayout(3, false));

Label l = new Label(nameComposite, SWT.NONE);
l.setText("Filter Name:");
//Synthetic comment -- @@ -172,6 +177,10 @@
}
});

        mNameWarning = new Label(nameComposite, SWT.NONE);
        mNameWarning.setImage(ImageLoader.getDdmUiLibLoader().loadImage(IMAGE_EMPTY,
                mShell.getDisplay()));

// separator
l = new Label(mShell, SWT.SEPARATOR | SWT.HORIZONTAL);
l.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
//Synthetic comment -- @@ -192,9 +201,8 @@
tagText.setText(mTag);
}
}

        tagText.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
tagText.addModifyListener(new ModifyListener() {
public void modifyText(ModifyEvent e) {
mTag = tagText.getText().trim();
//Synthetic comment -- @@ -202,6 +210,10 @@
}
});

        mTagWarning = new Label(main, SWT.NONE);
        mTagWarning.setImage(ImageLoader.getDdmUiLibLoader().loadImage(IMAGE_EMPTY,
                mShell.getDisplay()));

l = new Label(main, SWT.NONE);
l.setText("by pid:");

//Synthetic comment -- @@ -223,14 +235,14 @@
});

mPidWarning = new Label(main, SWT.NONE);
        mPidWarning.setImage(ImageLoader.getDdmUiLibLoader().loadImage(IMAGE_EMPTY,
mShell.getDisplay()));

l = new Label(main, SWT.NONE);
l.setText("by Log level:");

final Combo logCombo = new Combo(main, SWT.DROP_DOWN | SWT.READ_ONLY);
        GridData gd = new GridData(GridData.FILL_HORIZONTAL);
gd.horizontalSpan = 2;
logCombo.setLayoutData(gd);

//Synthetic comment -- @@ -324,26 +336,58 @@
*/
private void validate() {

        boolean result = true;

// then we check it only contains digits.
if (mPid != null) {
if (mPid.matches("[0-9]*") == false) { //$NON-NLS-1$
mPidWarning.setImage(ImageLoader.getDdmUiLibLoader().loadImage(
                        IMAGE_WARNING,
mShell.getDisplay()));
                mPidWarning.setToolTipText("PID must be a number"); //$NON-NLS-1$
                result = false;
} else {
mPidWarning.setImage(ImageLoader.getDdmUiLibLoader().loadImage(
                        IMAGE_EMPTY,
mShell.getDisplay()));
                mPidWarning.setToolTipText(null);
}
}

        // then we check it not contains character | or :
        if (mTag != null) {
            if (mTag.matches(".*[:|].*") == true) { //$NON-NLS-1$
                mTagWarning.setImage(ImageLoader.getDdmUiLibLoader().loadImage(
                        IMAGE_WARNING,
                        mShell.getDisplay()));
                mTagWarning.setToolTipText("Tag cannot contain | or :"); //$NON-NLS-1$
                result = false;
            } else {
                mTagWarning.setImage(ImageLoader.getDdmUiLibLoader().loadImage(
                        IMAGE_EMPTY,
                        mShell.getDisplay()));
                mTagWarning.setToolTipText(null);
            }
}

        // then we check it not contains character | or :
        if (mName != null && mName.length() > 0) {
            if (mName.matches(".*[:|].*") == true) { //$NON-NLS-1$
                mNameWarning.setImage(ImageLoader.getDdmUiLibLoader().loadImage(
                        IMAGE_WARNING,
                        mShell.getDisplay()));
                mNameWarning.setToolTipText("Name cannot contain | or :"); //$NON-NLS-1$
                result = false;
            } else {
                mNameWarning.setImage(ImageLoader.getDdmUiLibLoader().loadImage(
                        IMAGE_EMPTY,
                        mShell.getDisplay()));
                mNameWarning.setToolTipText(null);
            }
        } else {
            result = false;
        }

        mOkButton.setEnabled(result);
}
}







