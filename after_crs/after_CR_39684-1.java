/*ndk: Allow empty path to NDK

There is no way to get out of the NDK preference page unless
a valid NDK is provided since Eclipse always pops up a message
saying that "the provided path is invalid".

This CL allows empty path to be a valid path and disables error
checking in such a case.

Change-Id:I4e5d5b975bd4629920f1b2750adec521c3a054a4*/




//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.ndk/src/com/android/ide/eclipse/ndk/internal/preferences/NdkPreferencePage.java b/eclipse/plugins/com.android.ide.eclipse.ndk/src/com/android/ide/eclipse/ndk/internal/preferences/NdkPreferencePage.java
//Synthetic comment -- index e66eb40..e1ab247 100644

//Synthetic comment -- @@ -48,7 +48,7 @@
private static class NdkDirectoryFieldEditor extends DirectoryFieldEditor {
public NdkDirectoryFieldEditor(String name, String labelText, Composite parent) {
super(name, labelText, parent);
            setEmptyStringAllowed(true);
}

@Override
//Synthetic comment -- @@ -59,7 +59,7 @@
}

String dirname = getTextControl().getText().trim();
            if (!dirname.isEmpty() && !NdkManager.isValidNdkLocation(dirname)) {
setErrorMessage(Messages.NDKPreferencePage_not_a_valid_NDK_directory);
return false;
}







