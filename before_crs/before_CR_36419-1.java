/*SDK: Update about box for 2012.

SDK Bug: b/6043405

Change-Id:I2c189ef30d68738de8becff874343296ad9c412b*/
//Synthetic comment -- diff --git a/ddms/app/src/com/android/ddms/AboutDialog.java b/ddms/app/src/com/android/ddms/AboutDialog.java
//Synthetic comment -- index cdf4370..b3ddff7 100644

//Synthetic comment -- @@ -130,7 +130,8 @@
label.setText("Dalvik Debug Monitor");
}
label = new Label(textArea, SWT.NONE);
        label.setText("Copyright 2007-2011, The Android Open Source Project");
label = new Label(textArea, SWT.NONE);
label.setText("All Rights Reserved.");









//Synthetic comment -- diff --git a/hierarchyviewer2/app/src/com/android/hierarchyviewer/AboutDialog.java b/hierarchyviewer2/app/src/com/android/hierarchyviewer/AboutDialog.java
//Synthetic comment -- index 150c70a..9968788 100644

//Synthetic comment -- @@ -61,8 +61,8 @@
imageLabel.setImage(mAboutImage);

CLabel textLabel = new CLabel(control, SWT.NONE);
        textLabel
                .setText("Hierarchy Viewer\nCopyright 2010, The Android Open Source Project\nAll Rights Reserved.");
textLabel.setLayoutData(new GridData(GridData.BEGINNING, GridData.CENTER, true, true));
getShell().setText("About...");
getShell().setImage(mSmallImage);








//Synthetic comment -- diff --git a/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/AboutDialog.java b/sdkmanager/libs/sdkuilib/src/com/android/sdkuilib/internal/repository/AboutDialog.java
//Synthetic comment -- index 35e3420..fa3f688 100755

//Synthetic comment -- @@ -65,6 +65,7 @@
"Revision %1$s\n" +
"Add-on XML Schema #%2$d\n" +
"Repository XML Schema #%3$d\n" +
"Copyright (C) 2009-2012 The Android Open Source Project.",
getRevision(),
SdkAddonConstants.NS_LATEST_VERSION,







