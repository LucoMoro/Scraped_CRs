/*SDK Manager dialog to manager user add-on sites.

Not ready for review.

Change-Id:I36209964b8a59c6f8b987032e175849a35cf467a*/
//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/AddonSitesDialog.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/AddonSitesDialog.java
new file mode 100755
//Synthetic comment -- index 0000000..3b06c68

//Synthetic comment -- @@ -0,0 +1,231 @@








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/PackagesPage.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/PackagesPage.java
//Synthetic comment -- index 51d20f7..88398b4 100755

//Synthetic comment -- @@ -95,6 +95,10 @@
private Color mColorUpdate;
private Color mColorNew;
private Font mTreeFontItalic;

public PackagesPage(Composite parent, UpdaterData updaterData) {
super(parent, SWT.BORDER);
//Synthetic comment -- @@ -188,61 +192,89 @@
mCheckSortApi.setText("API level");
mCheckSortApi.setSelection(true);

                Label expandPlaceholder = new Label(mGroupOptions, SWT.NONE);
                expandPlaceholder.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

                Label label3 = new Label(mGroupOptions, SWT.NONE);
                label3.setText("Show:");

                        mCheckFilterNew = new Button(mGroupOptions, SWT.CHECK);
                        mCheckFilterNew.setToolTipText("Show Updates and New");
                        mCheckFilterNew.addSelectionListener(new SelectionAdapter() {
                            @Override
                            public void widgetSelected(SelectionEvent e) {
                                sortPackages();
                            }
                        });
                        mCheckFilterNew.setImage(getImage("reject_icon16.png"));
                        mCheckFilterNew.setSelection(true);
                        mCheckFilterNew.setText("Updates/New");

                        mCheckFilterInstalled = new Button(mGroupOptions, SWT.CHECK);
                        mCheckFilterInstalled.setToolTipText("Show Installed");
                        mCheckFilterInstalled.addSelectionListener(new SelectionAdapter() {
                            @Override
                            public void widgetSelected(SelectionEvent e) {
                                sortPackages();
                            }
                        });
                        mCheckFilterInstalled.setImage(getImage("accept_icon16.png"));  //$NON-NLS-1$
                        mCheckFilterInstalled.setSelection(true);
                        mCheckFilterInstalled.setText("Installed");

                mCheckFilterObsolete = new Button(mGroupOptions, SWT.CHECK);
                mCheckFilterObsolete.setToolTipText("Show Obsolete");
                mCheckFilterObsolete.addSelectionListener(new SelectionAdapter() {
                    @Override
                    public void widgetSelected(SelectionEvent e) {
                        sortPackages();
                    }
                });
                mCheckFilterObsolete.setImage(getImage("nopkg_icon16.png"));  //$NON-NLS-1$
                mCheckFilterObsolete.setSelection(false);
                mCheckFilterObsolete.setText("Obsolete");

mGroupButtons = new Composite(parent, SWT.NONE);
mGroupButtons.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
false, 1, 1));
        mGroupButtons.setLayout(new GridLayout(3, false));

mButtonDelete = new Button(mGroupButtons, SWT.NONE);
        mButtonDelete.setText("Delete");

Label label4 = new Label(mGroupButtons, SWT.NONE);
label4.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

mButtonInstall = new Button(mGroupButtons, SWT.NONE);
        mButtonInstall.setText("Install");
}

private Image getImage(String filename) {
//Synthetic comment -- @@ -806,4 +838,14 @@
// --- End of hiding from SWT Designer ---
//$hide<<$

}







