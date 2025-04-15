/*Fix validation where selecting same build and min sdk version

Fix bug where selecting the same min sdk version and build target
version would result in a wizard complaint that the minSdkVersion
was higher than the build target API level. This happened because
the code passed the minSdk string to the AndroidVersion compare
method, which assumes that string is a codename; in the wizard
case it can be the string representation of the api level.

(cherry picked from commit 86b9ea11f0f65ac33c417712b2f50022c5b84977)

Change-Id:Ie6affec980dd1632e972e9e3a54dbb5f5ea45c73*/
//Synthetic comment -- diff --git a/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/NewProjectPage.java b/eclipse/plugins/com.android.ide.eclipse.adt/src/com/android/ide/eclipse/adt/internal/wizards/templates/NewProjectPage.java
//Synthetic comment -- index 83326a4..d4aff90 100644

//Synthetic comment -- @@ -611,7 +611,8 @@
"Preview platforms require the min SDK version to match their codenames.");
}
} else if (mValues.target.getVersion().compareTo(
                            mValues.minSdkLevel, mValues.minSdk) < 0) {
status = new Status(IStatus.WARNING, AdtPlugin.PLUGIN_ID,
"The minimum SDK version is higher than the build target version");
}







