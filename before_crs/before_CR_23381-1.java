/*Typo.

Fixed a typo. "Diplay" should read "Display".

Change-Id:I8b20e1c76e686e9ab5fa8ba9f8cee9a2ad1cd2a7*/
//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdSelector.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/widgets/AvdSelector.java
//Synthetic comment -- index ae499e9..bbf17fa 100644

//Synthetic comment -- @@ -298,7 +298,7 @@
mDetailsButton = new Button(buttons, SWT.PUSH | SWT.FLAT);
mDetailsButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
mDetailsButton.setText("Details...");
        mDetailsButton.setToolTipText("Diplays details of the selected AVD.");
mDetailsButton.addSelectionListener(new SelectionAdapter() {
@Override
public void widgetSelected(SelectionEvent arg0) {







