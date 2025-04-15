/*Fix in PreferencePage.

When the user clicked on the "Restored Defaults" button,
the "ADBHOST value:" field could still be enabled while
the "Use ADBHOST" was disabled.
This commit fixes the problem.

Change-Id:Id5948fed655a7034a65cb96b509e1ad80226a93b*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.ddms/src/com/android/ide/eclipse/ddms/preferences/PreferencePage.java b/eclipse/plugins/com.android.ide.eclipse.ddms/src/com/android/ide/eclipse/ddms/preferences/PreferencePage.java
//Synthetic comment -- index 47445f8..cd3727e 100644

//Synthetic comment -- @@ -114,4 +114,10 @@
mAdbHostValue.setEnabled(mUseAdbHost.getBooleanValue(), getFieldEditorParent());
}
}
}







