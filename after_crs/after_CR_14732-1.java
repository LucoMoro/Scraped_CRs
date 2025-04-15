/*Fix a typo.

Change-Id:I699ee939436964e0c88b7b8423b5ed6fef84add1*/




//Synthetic comment -- diff --git a/ddms/app/src/com/android/ddms/UIThread.java b/ddms/app/src/com/android/ddms/UIThread.java
//Synthetic comment -- index 7940c74..1c33613 100644

//Synthetic comment -- @@ -770,7 +770,7 @@

// create Device menu items
final MenuItem screenShotItem = new MenuItem(deviceMenu, SWT.NONE);
        screenShotItem.setText("&Screen capture...\tCtrl-S");
screenShotItem.setAccelerator('S' | SWT.CONTROL);
screenShotItem.addSelectionListener(new SelectionAdapter() {
@Override







