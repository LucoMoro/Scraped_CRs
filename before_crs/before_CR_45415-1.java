/*One more device menu tweak

(cherry picked from commit 14e60b9717d70165308cc8a328a92e1f5d94ad3a)

Change-Id:I1ecabaa6fbc0fdb3ba331e08f44747ecd7780d03*/
//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/ui/DeviceManagerPage.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/ui/DeviceManagerPage.java
//Synthetic comment -- index ad0ee6f..e5d94fb 100755

//Synthetic comment -- @@ -240,13 +240,15 @@
new Label(legend, SWT.NONE).setText("A user-created device definition.");
new Label(legend, SWT.NONE).setImage(mGenericImage);
new Label(legend, SWT.NONE).setText("A generic device definition.");
        new Label(legend, SWT.NONE).setImage(mOtherImage);
Label l = new Label(legend, SWT.NONE);
l.setText("A manufacturer-specific device definition.");
GridData gd;
l.setLayoutData(gd = new GridData(GridData.FILL_HORIZONTAL));
gd.horizontalSpan = 3;


// create the table columns
final TableColumn column0 = new TableColumn(mTable, SWT.NONE);







