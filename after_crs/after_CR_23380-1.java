/*LogLevelFilter button will be uncheckable in LogCat.

Change-Id:I0f364fe9ede856b04d03898cf6ef9d902eef71c2*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.ddms/src/com/android/ide/eclipse/ddms/views/LogCatView.java b/eclipse/plugins/com.android.ide.eclipse.ddms/src/com/android/ide/eclipse/ddms/views/LogCatView.java
//Synthetic comment -- index 37e265a..167c0ff 100644

//Synthetic comment -- @@ -316,10 +316,16 @@
for (int i = 0; i < mLogLevelActions.length; i++) {
Action a = mLogLevelActions[i];
if (a == this) {
                            boolean check = false;
                            int level = -1;
                            if (a.isChecked()) {
                                check = true;
                                level = i + 2;
                            }
                            a.setChecked(check);

// set the log level
                            mLogPanel.setCurrentFilterLogLevel(level);
} else {
a.setChecked(false);
}







