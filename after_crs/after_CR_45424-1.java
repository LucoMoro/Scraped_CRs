/*One more device menu tweak

Change-Id:I95c2c0cbfbb937cb99623207d21a056f695d973d*/




//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/ui/DeviceManagerPage.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/ui/DeviceManagerPage.java
//Synthetic comment -- index ad0ee6f..e5d94fb 100755

//Synthetic comment -- @@ -240,13 +240,15 @@
new Label(legend, SWT.NONE).setText("A user-created device definition.");
new Label(legend, SWT.NONE).setImage(mGenericImage);
new Label(legend, SWT.NONE).setText("A generic device definition.");
        Label icon = new Label(legend, SWT.NONE);
        icon.setImage(mOtherImage);
Label l = new Label(legend, SWT.NONE);
l.setText("A manufacturer-specific device definition.");
GridData gd;
l.setLayoutData(gd = new GridData(GridData.FILL_HORIZONTAL));
gd.horizontalSpan = 3;
        icon.setVisible(false);
        l.setVisible(false);

// create the table columns
final TableColumn column0 = new TableColumn(mTable, SWT.NONE);







